package com.lagamo.UserAuth;

import jakarta.persistence.*;
import lombok.Data; // If using Lombok, otherwise generate Getters/Setters manually

@Entity
@Table(name = "users")
@Data // Generates Getters/Setters automatically
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fields from your ERD
    private String firstname;
    private String middlename;
    private String lastname;
    private String street;
    private String barangay;
    private String municipality;
    private String province;
    private String country;
    private String contactNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}