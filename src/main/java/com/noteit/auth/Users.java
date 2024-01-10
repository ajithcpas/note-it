package com.noteit.auth;

import com.noteit.auth.authentication.AuthUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private boolean enabled;

    public Users(String username, String password) {
        this.username = username;
        this.setPassword(password);
        this.enabled = true;
    }

    public void setPassword(String password) {
        this.password = AuthUtil.getEncoder().encode(password);
    }
}
