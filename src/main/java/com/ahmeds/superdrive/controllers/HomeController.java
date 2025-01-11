package com.ahmeds.superdrive.controllers;

import com.ahmeds.superdrive.models.User;
import com.ahmeds.superdrive.services.CredentialService;
import com.ahmeds.superdrive.services.FileService;
import com.ahmeds.superdrive.services.NoteService;
import com.ahmeds.superdrive.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String homeView(Authentication authentication, Model model, @ModelAttribute("activeTab") String activeTab) {
        User user = userService.getUser(authentication.getName());
        model.addAttribute("files", fileService.getFilesByUserId(user.getUserid()));
        model.addAttribute("notes", noteService.getNotesByUserId(user.getUserid()));
        model.addAttribute("credentials", credentialService.getCredentialsByUser(user.getUserid()));

        // Set default tab if none specified
        model.addAttribute("activeTab", activeTab != null && !activeTab.isEmpty() ? activeTab : "files");
        
        return "home";
    }
}
