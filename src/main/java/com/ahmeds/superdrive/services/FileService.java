package com.ahmeds.superdrive.services;

import com.ahmeds.superdrive.mappers.FileMapper;
import com.ahmeds.superdrive.models.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public boolean isFileNameAvailable(String filename, Integer userId) {
        return fileMapper.getFileCountByFilename(filename, userId) == 0;
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }

    public int uploadFile(MultipartFile file, Integer userId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        if (!isFileNameAvailable(file.getOriginalFilename(), userId)) {
            throw new IllegalArgumentException("A file with that name already exists");
        }

        File newFile = new File();
        newFile.setFilename(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        newFile.setUserid(userId);
        newFile.setFileData(file.getBytes());

        return fileMapper.insertFile(newFile);
    }
}
