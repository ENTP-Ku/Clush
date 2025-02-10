package com.example.clush;

import jakarta.persistence.*;

@Entity
@Table(name = "list")
public class ListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean checked;

    // Getters and Setters
}
