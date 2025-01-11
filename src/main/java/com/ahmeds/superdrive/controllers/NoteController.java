package com.ahmeds.superdrive.controllers;

import com.ahmeds.superdrive.models.Note;
import com.ahmeds.superdrive.models.User;
import com.ahmeds.superdrive.services.NoteService;
import com.ahmeds.superdrive.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    public String createNote(Authentication authentication, Note note, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(authentication.getName());
            note.setUserid(user.getUserId());
            noteService.createNote(note);
            redirectAttributes.addFlashAttribute("success", "Note added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        return "redirect:/home";
    }

    @PostMapping("/{noteId}")
    public String updateNote(@PathVariable Integer noteId, Authentication authentication, Note note, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(authentication.getName());
            note.setUserid(user.getUserId());
            note.setNoteid(noteId);
            noteService.updateNote(note);
            redirectAttributes.addFlashAttribute("success", "Note updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        return "redirect:/home";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("success", "Note deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting note.");
        }
        redirectAttributes.addFlashAttribute("activeTab", "notes");
        return "redirect:/home";
    }
}
