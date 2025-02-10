package com.example.clush; // 패키지 선언

import org.springframework.data.jpa.repository.JpaRepository; // JPA Repository를 위한 import

// User 엔티티에 대한 데이터 액세스를 처리하는 인터페이스
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 사용자 이름으로 사용자 존재 여부를 확인하는 메서드
    boolean existsByUsername(String username);
    
    // 고유번호 존재 여부 확인 메서드 추가
    boolean existsByUniqueNumber(String uniqueNumber); 

    // 사용자 이름으로 사용자 정보를 찾는 메서드
    User findByUsername(String username);
    
}