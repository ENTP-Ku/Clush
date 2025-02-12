package com.example.clush;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // JwtUtil 주입

    @Autowired
    private PasswordEncoder passwordEncoder;  // PasswordEncoder 주입

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {    	
        // 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);  // 해시된 비밀번호로 설정

        // 사용자 등록
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser); 
    }

    @PostMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestBody User user) {
        boolean exists = userService.usernameExists(user.getUsername()); 
        return ResponseEntity.ok(exists); 
    }

    @PostMapping("/check-unique-number")
    public ResponseEntity<Boolean> checkUniqueNumber(@RequestBody User user) {
        System.out.println("고유번호 중복확인 엔드포인트에 접근 성공!!!");
        boolean exists = userService.uniqueNumberExists(user.getUniqueNumber()); 
        return ResponseEntity.ok(exists); 
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        // DB에서 사용자 정보 가져오기 (Optional<User> 반환)
        Optional<User> loggedInUserOptional = userService.findByUsername(user.getUsername());

        if (loggedInUserOptional.isPresent()) {
            User loggedInUser = loggedInUserOptional.get();

            // 비밀번호가 일치하면 JWT 생성
            if (passwordEncoder.matches(user.getPassword(), loggedInUser.getPassword())) {
                String token = jwtUtil.generateToken(loggedInUser.getId(), loggedInUser.getUsername());

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", loggedInUser);

                return ResponseEntity.ok(response); 
            }
        }

        // 로그인 실패
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "로그인 실패"); 
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); 
    }
}
