package org.example.quizapponlinebe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.LOCAL;
    public enum Provider {
        LOCAL,GOOGLE
    }
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    public enum Role{
        ADMIN,USER
    }
    private Boolean status = false;
    @Column(name = "is_delete")
    private Boolean isDelete = false;
    @Column(name = "create_at")
    private LocalDate createAt = LocalDate.now();
    private String name;
    private String avatar;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
}

