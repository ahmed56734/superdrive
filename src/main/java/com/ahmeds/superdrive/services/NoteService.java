package com.ahmeds.superdrive.services;

import com.ahmeds.superdrive.mappers.NoteMapper;
import com.ahmeds.superdrive.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotesByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public int createNote(Note note) {
        if (note.getNotetitle().isEmpty() || note.getNotedescription().isEmpty()) {
            throw new IllegalArgumentException("Note title and description cannot be empty");
        }
        return noteMapper.insert(note);
    }

    public void updateNote(Note note) {
        if (note.getNotetitle().isEmpty() || note.getNotedescription().isEmpty()) {
            throw new IllegalArgumentException("Note title and description cannot be empty");
        }
        noteMapper.update(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.delete(noteId);
    }
}
