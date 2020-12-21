package com.example.YourRandomNotes.service.impl;

import com.example.YourRandomNotes.entity.Note;
import com.example.YourRandomNotes.entity.NoteCopy;
import com.example.YourRandomNotes.repository.NoteCopyRepository;
import com.example.YourRandomNotes.repository.NoteRepository;
import com.example.YourRandomNotes.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteCopyRepository noteCopyRepository;

    public Note setNote(String note, Integer noteMessageId) {
        return Note.builder().note(note).noteMessageId(noteMessageId).build();
    }

    @Override
    @Transactional
    public void delete(Integer noteMessageId) {
        noteRepository.deleteByNoteMessageId(noteMessageId);
        noteCopyRepository.deleteByNoteMessageId(noteMessageId);
    }


    @Override
    @Transactional
    public String getRandomNote() {
        if (noteRepository.count() == 0) {

            List<Note> list1 = noteCopyRepository.findAll().stream().map(noteCopy -> {
                Note note = new Note();
                note.setNote(noteCopy.getNote());
                note.setNoteMessageId(noteCopy.getNoteMessageId());
                return note;
            }).collect(Collectors.toList());

            noteRepository.saveAll(list1);
            noteCopyRepository.deleteAll();
        }

        List<Note> list = noteRepository.findAll();
        Note note = list.get((int) (Math.random() * list.size()));

        NoteCopy noteCopy = new NoteCopy();
        noteCopy.setNote(note.getNote());
        noteCopyRepository.save(noteCopy);

        noteRepository.delete(note);

        log.info(note.getNote());
        return note.getNote();

    }

    @Override
    public void save(Note note) {
        noteRepository.save(note);
    }


}
