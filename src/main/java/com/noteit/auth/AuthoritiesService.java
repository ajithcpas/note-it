package com.noteit.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService
{
    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private UsersService usersService;

    public void addAuthority(Authorities authority) throws Exception
    {
        usersService.addUser(authority.getUser());
        authoritiesRepository.save(authority);
    }
}
