package com.example.YourRandomNotes.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "MessageBuilder", toBuilder = true)
@Entity
@Table(name = "notes_copy")
public class NoteCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String note;

    @Column
    private Integer noteMessageId;
}
