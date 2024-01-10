package com.noteit.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "Authorities")
@Data
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private Users user;

    @NotBlank
    @Column(nullable = false)
    private String authority;
}
