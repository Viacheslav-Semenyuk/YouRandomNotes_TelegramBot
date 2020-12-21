package com.example.YourRandomNotes.repository;

import com.example.YourRandomNotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByChatId(String chatId);

    User findByChatId(String chatId);

}
