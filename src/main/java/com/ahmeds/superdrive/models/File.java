package com.ahmeds.superdrive.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private int fileId;
    private String filename;
    private String contentType;
    private String fileSize;
    private int userid;
    private byte[] fileData;
    private User user; // reference to the User object
}