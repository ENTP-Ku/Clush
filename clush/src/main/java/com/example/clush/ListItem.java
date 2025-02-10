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
    private String username;  // 사용자 이름 필드

    // 공유 여부 필드 추가
    @Column(nullable = false)
    private boolean shared;  // 공유 여부 필드
}
