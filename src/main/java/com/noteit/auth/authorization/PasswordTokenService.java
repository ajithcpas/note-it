package com.noteit.auth.authorization;

import com.noteit.auth.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordTokenService
{
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public PasswordResetToken getResetToken(Users user)
    {
        PasswordResetToken token = passwordTokenRepository.findByUser(user);
        if (token == null)
        {
            token = new PasswordResetToken(user);
        }
        else
        {
            token.refresh();
        }
        return passwordTokenRepository.save(token);
    }

    public boolean isValid(Long userId, String token)
    {
        PasswordResetToken resetToken = passwordTokenRepository.findByToken(token);
        return (resetToken != null && resetToken.getUser().getId() == userId && resetToken.getExpiryTime().after(new Date()));
    }

    public void removeToken(Users user)
    {
        PasswordResetToken token = passwordTokenRepository.findByUser(user);
        passwordTokenRepository.delete(token);
    }
}
