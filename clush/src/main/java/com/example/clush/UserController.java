package com.example.clush;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.clush.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // 🔹 JwtUtil 주입

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {    	
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
        User loggedInUser = userService.login(user.getUsername(), user.getPassword()); 
        
        if (loggedInUser != null) {
            // 🔹 JwtUtil을 사용해 JWT 생성
            String token = jwtUtil.generateToken(loggedInUser.getId(), loggedInUser.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", loggedInUser);
            
            return ResponseEntity.ok(response); 
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패"); 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); 
        }
    }
}
