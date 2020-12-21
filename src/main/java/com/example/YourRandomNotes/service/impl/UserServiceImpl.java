package com.example.YourRandomNotes.service.impl;

import com.example.YourRandomNotes.entity.User;
import com.example.YourRandomNotes.repository.UserRepository;
import com.example.YourRandomNotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public boolean existsByChatId(String chatId) {
        return userRepository.existsByChatId(chatId);
    }

    @Override
    public User findByChatId(String chatId) {
        return userRepository.findByChatId(chatId);
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
