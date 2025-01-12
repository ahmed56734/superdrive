package com.ahmeds.superdrive.services;

import com.ahmeds.superdrive.mappers.UserMapper;
import com.ahmeds.superdrive.models.SignupForm;
import com.ahmeds.superdrive.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public int createUser(SignupForm form) {
        HashService.HashingResult hashingResult = hashService.getHashedValue(form.getPassword());

        User user = new User();
        user.setUsername(form.getUsername());
        user.setSalt(hashingResult.salt());
        user.setPassword(hashingResult.hashedValue());
        user.setFirstname(form.getFirstName());
        user.setLastname(form.getLastName());

        return userMapper.insert(user);
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUserByName(username) == null;
    }

    public User getUser(String name) {
        return userMapper.getUserByName(name);
    }

    public int deleteTestUsers() {
        return userMapper.deleteTestUsers();
    }
}
