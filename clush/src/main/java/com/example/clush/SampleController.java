package com.example.clush;  // 패키지 선언 (프로젝트 구조에 맞게 수정)

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Sample API", description = "Sample API for demonstration")
public class SampleController {

    @GetMapping("/hello")
    @Operation(summary = "Say hello", description = "Returns a greeting message")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public String sayHello(@Parameter(description = "Name to greet") @RequestParam String name) {
        return "Hello, " + name + "!";
    }
}