package com.example.clush; // 패키지 선언

import java.util.HashMap; // HashMap 클래스를 사용하기 위한 import
import java.util.Map; // Map 인터페이스를 사용하기 위한 import

import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 import
import org.springframework.http.HttpStatus; // HTTP 상태 코드를 사용하기 위한 import
import org.springframework.http.ResponseEntity; // HTTP 응답을 표현하기 위한 import
import org.springframework.web.bind.annotation.*; // RESTful API 어노테이션을 위한 import

import io.jsonwebtoken.Jwts; // JWT 생성을 위한 import
import io.jsonwebtoken.SignatureAlgorithm; // JWT 서명을 위한 import

@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냄
@RequestMapping("/api/users") // 기본 URL 경로 설정
public class UserController {
    
    @Autowired // UserService 의존성을 자동으로 주입
    private UserService userService; // UserService 클래스의 인스턴스를 주입받아 사용자 관련 로직 처리

    // 사용자 등록 엔드포인트
    @PostMapping("/register") // POST 요청을 처리
    public ResponseEntity<User> registerUser(@RequestBody User user) {    	
        // 사용자 등록 서비스 호출
        User newUser = userService.registerUser(user); // 새 사용자 객체를 생성하여 등록
        
        // 등록된 사용자 정보를 포함한 200 OK 응답 반환
        return ResponseEntity.ok(newUser); 
    }
    
    // 사용자 ID 중복 체크 엔드포인트 추가
    @PostMapping("/check-username") // 사용자 ID 중복 체크 엔드포인트
    public ResponseEntity<Boolean> checkUsername(@RequestBody User user) {
        // User 엔티티에서 username 추출
        String username = user.getUsername(); 
        
        // 아이디 존재 여부 확인
        boolean exists = userService.usernameExists(username); 
        
        // 존재하면 true, 아니면 false 반환
        return ResponseEntity.ok(exists); 
    }

    // 고유번호 중복 체크 엔드포인트 추가
    @PostMapping("/check-unique-number") // 고유번호 중복 체크 엔드포인트
    public ResponseEntity<Boolean> checkUniqueNumber(@RequestBody User user) {
        // 고유번호 중복 확인 엔드포인트에 접근 성공 메시지 출력
        System.out.println("고유번호 중복확인 엔드포인트에 접근 성공!!!");
        
        // User 엔티티에서 uniqueNumber 추출하여 존재 여부 확인
        boolean exists = userService.uniqueNumberExists(user.getUniqueNumber()); 
        
        // 존재하면 true, 아니면 false 반환
        return ResponseEntity.ok(exists); 
    }

    // 사용자 로그인 엔드포인트
    @PostMapping("/login") // POST 요청을 처리
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        // 로그인 서비스 호출
        User loggedInUser = userService.login(user.getUsername(), user.getPassword()); 
        
        if (loggedInUser != null) { // 로그인 성공 시
            // JWT 생성
            String token = Jwts.builder()
                .setSubject(loggedInUser.getId().toString()) // 사용자 ID를 주제로 설정
                .claim("username", loggedInUser.getUsername()) // JWT에 username 추가
                .signWith(SignatureAlgorithm.HS256, "secretKey") // 비밀 키를 사용하여 서명
                .compact(); // JWT 생성 완료

            // 토큰과 사용자 정보를 포함한 응답 반환
            Map<String, Object> response = new HashMap<>(); // 응답을 저장할 Map 생성
            response.put("token", token); // 생성된 JWT 추가
            response.put("user", loggedInUser); // 사용자 정보 추가
            
            // 200 OK 응답 반환
            return ResponseEntity.ok(response); 
        } else { // 로그인 실패 시
            // 로그인 실패 메시지를 Map으로 래핑
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패"); // 실패 메시지 추가
            
            // 401 UNAUTHORIZED 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); 
        }
    }
}