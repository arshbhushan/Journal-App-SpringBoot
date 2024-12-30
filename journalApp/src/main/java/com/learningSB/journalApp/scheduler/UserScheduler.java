package com.learningSB.journalApp.scheduler;

import com.learningSB.journalApp.cache.AppCache;
import com.learningSB.journalApp.emuns.Sentiment;
import com.learningSB.journalApp.entity.JournalEntry;
import com.learningSB.journalApp.entity.User;
import com.learningSB.journalApp.model.SentimentData;
import com.learningSB.journalApp.repository.UserRepository;
import com.learningSB.journalApp.repository.UserRepositoryImpl;
import com.learningSB.journalApp.service.EmailService;
import com.learningSB.journalApp.service.SentimentAnalysisServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisServce sentimentAnalysisServce;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail(){
        List<User>users= userRepository.getUserForSA();
        for(User user:users){
            List<JournalEntry>journalEntries= user.getJournalEntries();
            List<Sentiment>sentiments= journalEntries.stream()
                    .filter(x->x.getDate()
                    .isAfter(LocalDateTime.now()
                    .minus(7, ChronoUnit.DAYS)))
                    .map(x->x.getSentiment())
                    .collect(Collectors.toList());

            Map<Sentiment,Integer>sentimentCounts=new HashMap<>();
            for(Sentiment sentiment:sentiments){
                sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
            }
            Sentiment mostFrequentSentiment=null;
            int maxCount=0;
            for(Map.Entry<Sentiment,Integer>entry: sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount=entry.getValue();
                    mostFrequentSentiment=entry.getKey();
                }
            }
            if(mostFrequentSentiment!=null){
                SentimentData sentimentData= SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days "+ mostFrequentSentiment).build();
                kafkaTemplate.send("weekly-sentiments",sentimentData.getEmail(),sentimentData);
            }
        }

    }
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
