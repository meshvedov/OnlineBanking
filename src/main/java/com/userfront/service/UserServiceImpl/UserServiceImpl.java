package com.userfront.service.UserServiceImpl;

import com.userfront.dao.RoleDao;
import com.userfront.dao.UserDao;
import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AccountService accountService;

    public void save(User user) {
        userDao.save(user);
    }

    public User findByUsername(String userName) {
        return userDao.findByUsername(userName);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean checkUserExists(String userName, String email) {
        if (checkUsernameExists(userName) || checkEmailExists(email)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmailExists(String email) {
        return null != findByEmail(email);
    }

    public boolean checkUsernameExists(String userName) {
        return null != findByUsername(userName);
    }

    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userDao.findByUsername(user.getUsername());
        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done.", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleDao.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);

            user.setPrimaryAccount(accountService.createPrimaryAccount());
            user.setSavingsAccount(accountService.createSavingsAccount());

            localUser = userDao.save(user);
        }

        return localUser;
    }
}
