package com.example.YourRandomNotes.service;

import com.example.YourRandomNotes.entity.Note;

public interface NoteService {


    String getRandomNote();

    void save(Note note);

    Note setNote(String note, Integer noteMessageId);

    void delete(Integer noteMessageId);
}
