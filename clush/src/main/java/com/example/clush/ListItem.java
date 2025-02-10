package com.example.clush;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "list")
@Data
public class ListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean checked;


}
