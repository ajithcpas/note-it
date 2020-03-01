package com.noteit.auth.authorization;

import com.noteit.auth.Users;

import org.springframework.data.repository.CrudRepository;

public interface PasswordTokenRepository extends CrudRepository<PasswordResetToken, Long>
{
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(Users user);
}
