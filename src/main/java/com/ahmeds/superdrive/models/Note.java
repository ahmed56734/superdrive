package com.ahmeds.superdrive.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private int noteid;
    private String notetitle;
    private String notedescription;
    private int userid;
    private User user; // reference to the User object
}