package com.noteit.auth;

import com.noteit.auth.authentication.AuthUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean enabled;

    public Users(String username, String password)
    {
        this.username = username;
        this.setPassword(password);
        this.enabled = true;
    }

    public void setPassword(String password)
    {
        this.password = AuthUtil.getEncoder().encode(password);
    }
}
