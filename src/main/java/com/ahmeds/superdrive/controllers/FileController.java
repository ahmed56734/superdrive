package com.ahmeds.superdrive.controllers;

import com.ahmeds.superdrive.models.File;
import com.ahmeds.superdrive.models.User;
import com.ahmeds.superdrive.services.FileService;
import com.ahmeds.superdrive.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUser(authentication.getName());
            fileService.uploadFile(file, user.getUserId());
            redirectAttributes.addFlashAttribute("success", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("success", "File deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting file.");
        }
        return "redirect:/home";
    }
}
