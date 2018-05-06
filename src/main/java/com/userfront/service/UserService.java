package com.userfront.service;

import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;

import java.util.Set;

public interface UserService {

    User findByUsername(String userName);

    User findByEmail(String email);

    boolean checkUserExists(String userName, String email);

    boolean checkUsernameExists(String userName);

    boolean checkEmailExists(String email);

    void save(User user);

    User createUser(User user, Set<UserRole> userRoleSet);

}
