package com.noteit.auth.authorization;

import com.noteit.auth.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "PasswordResetToken")
@Data
@NoArgsConstructor
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 60 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Timestamp expiryTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private Users user;

    public PasswordResetToken(Users user) {
        this.user = user;
        this.refresh();
    }

    public void refresh() {
        this.token = UUID.randomUUID().toString();
        this.expiryTime = new Timestamp(System.currentTimeMillis() + EXPIRATION);
    }
}
