package com.learningSB.journalApp.service;

import com.learningSB.journalApp.entity.JournalEntry;
import com.learningSB.journalApp.entity.User;
import com.learningSB.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional  //this will make sure that ever line is executed like a single line of code as one and no line is left.
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user= userService.findByUsername(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error saving journal entry",e);
        }

    }
    public void saveEntry(JournalEntry journalEntry) {

        journalEntryRepository.save(journalEntry);
    }
public List<JournalEntry> getAll() {
    return journalEntryRepository.findAll();
}

public Optional<JournalEntry> findById(ObjectId id) {
    return journalEntryRepository.findById(id);
}

public void deleteById(ObjectId id, String userName) {
    User user= userService.findByUsername(userName);
    user.getJournalEntries().removeIf(x -> x.getId().equals(id));
    userService.saveEntry(user);
    journalEntryRepository.deleteById(id);
}
}
