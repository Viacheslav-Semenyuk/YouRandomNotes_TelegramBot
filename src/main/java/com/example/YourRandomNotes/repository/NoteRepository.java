package com.example.YourRandomNotes.repository;

import com.example.YourRandomNotes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    void deleteByNoteMessageId(Integer noteMessageId);
}
