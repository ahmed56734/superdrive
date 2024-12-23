package com.ahmeds.superdrive.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {
    String firstName;
    String lastName;
    String username;
    String password;
}
