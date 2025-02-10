package com.example.clush; // 패키지 선언


import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 import
import org.springframework.stereotype.Service; // 서비스 클래스를 나타내는 annotation

@Service // 이 클래스가 서비스 레이어의 컴포넌트임을 나타냄
public class UserService {
    
    @Autowired // UserRepository 의존성 주입
    private UserRepository userRepository;

    // 사용자 등록 메서드
    public User registerUser(User user) {
        return userRepository.save(user); // 사용자 정보를 저장하고 반환
    }
    
    // 주어진 사용자 이름이 이미 존재하는지 확인하는 메서드
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username); // Repository에서 존재 여부 확인
    }
    
    // 주어진 고유번호가 이미 존재하는지 확인하는 메서드
    public boolean uniqueNumberExists(String uniqueNumber) {
        System.out.println("고유번호 서비스 접근 성공"); // 접근 성공 메시지 출력
        return userRepository.existsByUniqueNumber(uniqueNumber); // Repository에서 존재 여부 확인
    }

    // 사용자 로그인 메서드
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username); // 사용자 이름으로 사용자 검색

        // 비밀번호 확인 로직 추가
        if (user != null && user.getPassword().equals(password)) {
            return user; // 비밀번호가 일치하면 사용자 객체 반환
        } else {
            return null; // 비밀번호가 일치하지 않으면 null 반환
        }
    }

    // 주어진 사용자 이름으로 사용자를 검색하는 메서드
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // 사용자 이름으로 사용자 검색
    }
}