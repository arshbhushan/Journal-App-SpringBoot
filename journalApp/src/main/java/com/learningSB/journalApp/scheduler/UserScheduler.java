package com.learningSB.journalApp.scheduler;

import com.learningSB.journalApp.cache.AppCache;
import com.learningSB.journalApp.entity.JournalEntry;
import com.learningSB.journalApp.entity.User;
import com.learningSB.journalApp.repository.UserRepository;
import com.learningSB.journalApp.repository.UserRepositoryImpl;
import com.learningSB.journalApp.service.EmailService;
import com.learningSB.journalApp.service.SentimentAnalysisServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail(){
        List<User>users= userRepository.getUserForSA();
        for(User user:users){
            List<JournalEntry>journalEntries= user.getJournalEntries();
            List<String>filteredEntries= journalEntries.stream()
                    .filter(x->x.getDate()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x->x.getContent())
                    .collect(Collectors.toList());
            String entry= String.join(" ",filteredEntries);
            String sentiment = sentimentAnalysisServce.getSentimentAnalysis(entry);
            emailService.sendEmail(user.getEmail(),"sentiment for last 7 days",sentiment);

        }

    }
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }
}
