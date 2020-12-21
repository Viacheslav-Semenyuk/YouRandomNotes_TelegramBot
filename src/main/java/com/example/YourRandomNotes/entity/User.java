package com.example.YourRandomNotes.entity;

import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "MessageBuilder", toBuilder = true)
@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String chatId;

    @Column
    private Boolean state;
}
