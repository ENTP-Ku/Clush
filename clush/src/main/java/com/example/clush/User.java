package com.example.clush; // 패키지 선언

import jakarta.persistence.Entity; // JPA 엔티티 어노테이션을 위한 import
import jakarta.persistence.GeneratedValue; // ID 자동 생성을 위한 import
import jakarta.persistence.GenerationType; // 자동 생성 전략을 위한 import
import jakarta.persistence.Id; // ID 필드를 위한 import
import lombok.Data; // Lombok의 @Data 어노테이션을 위한 import

@Entity // 이 클래스가 JPA 엔티티임을 나타냄
@Data // Lombok이 getter, setter, toString, equals, hashCode 메소드를 자동으로 생성함
public class User {
    @Id // 이 필드가 엔티티의 ID임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID가 데이터베이스에서 자동 생성됨
    private Long id; // 사용자 ID

    private String username; // 사용자 이름
    private String password; // 사용자 비밀번호
    private String uniqueNumber; // 고유 번호 (예: 회원가입 시 사용)
}