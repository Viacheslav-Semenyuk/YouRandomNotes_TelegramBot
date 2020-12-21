package com.example.YourRandomNotes.service;

import com.example.YourRandomNotes.entity.User;

public interface UserService {

    boolean existsByChatId(String chatId);

    User findByChatId(String chatId);

    void save(User user);

}
