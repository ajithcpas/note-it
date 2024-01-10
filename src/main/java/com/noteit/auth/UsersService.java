package com.noteit.auth;

import com.noteit.auth.authentication.AuthUtil;
import com.noteit.auth.authentication.AuthenticationControllerErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UsersService {
    private static final Logger logger = Logger.getLogger(UsersService.class.getName());

    @Autowired
    private UsersRepository usersRepository;

    public void addUser(Users user) throws Exception {
        if (isExists(user)) {
            throw new Exception(AuthenticationControllerErrorHandler.EMAIL_EXISTS);
        }
        usersRepository.save(user);
    }

    public void updateUser(Users user) throws Exception {
        if (!isExists(user)) {
            throw new Exception(AuthenticationControllerErrorHandler.EMAIL_NOT_EXISTS);
        }
        usersRepository.save(user);
    }

    public boolean isExists(Users user) {
        return usersRepository.findByUsername(user.getUsername()) != null;
    }

    public Users getUser(String username) throws Exception {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new Exception(AuthenticationControllerErrorHandler.EMAIL_NOT_EXISTS);
        }
        return user;
    }

    public Users getUser(Long userId) throws Exception {
        Optional<Users> user = usersRepository.findById(userId);
        if (!user.isPresent()) {
            throw new Exception(AuthenticationControllerErrorHandler.EMAIL_NOT_EXISTS);
        }
        return user.get();
    }

    public Users getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return getUser(authentication.getName());
        } catch (Exception ex) {
            logger.log(Level.INFO, "Unable to get logged-in user.", ex);
        }
        return null;
    }

    public boolean checkPassword(String password) {
        return AuthUtil.getEncoder().matches(password, getCurrentUser().getPassword());
    }

    public void changePassword(String newPassword) throws Exception {
        Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword(newPassword);
        updateUser(user);
    }
}
