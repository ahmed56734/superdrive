package com.ahmeds.superdrive.controllers;

import com.ahmeds.superdrive.models.Credential;
import com.ahmeds.superdrive.services.CredentialService;
import com.ahmeds.superdrive.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String saveCredential(Authentication authentication, @ModelAttribute Credential credential, RedirectAttributes redirectAttributes) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        credential.setUserId(userId);

        try {
            if (credential.getCredentialId() != null) {
                credentialService.updateCredential(credential);
                redirectAttributes.addFlashAttribute("success", "Credential updated successfully");
            } else {
                credentialService.addCredential(credential);
                redirectAttributes.addFlashAttribute("success", "Credential added successfully");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error processing credential: " + e.getMessage());
        }

        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("success", "Credential deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting credential: " + e.getMessage());
        }
        return "redirect:/home";
    }
}
