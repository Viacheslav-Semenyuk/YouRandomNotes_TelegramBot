package com.example.YourRandomNotes;

import com.example.YourRandomNotes.bot.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


@SpringBootApplication
@EnableJpaRepositories
public class YourRandomNotesTelegramBotApplication {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        SpringApplication.run(YourRandomNotesTelegramBotApplication.class, args);

    }

}
