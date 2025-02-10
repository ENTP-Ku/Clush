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

    // username 필드 추가
    @Column(nullable = false)
    private String username;  // 이 부분을 추가
}
