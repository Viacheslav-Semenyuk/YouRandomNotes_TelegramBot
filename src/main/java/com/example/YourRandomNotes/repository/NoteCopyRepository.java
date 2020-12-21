package com.example.YourRandomNotes.repository;

import com.example.YourRandomNotes.entity.NoteCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteCopyRepository extends JpaRepository<NoteCopy, Long> {
    void deleteByNoteMessageId(Integer noteMessageId);
}
