package com.ahmeds.superdrive.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private Integer noteid;
    private String notetitle;
    private String notedescription;
    private Integer userid;
    private User user; // reference to the User object
}