package com.ahmeds.superdrive.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String salt;
    private String password;
    private String firstname;
    private String lastname;
}