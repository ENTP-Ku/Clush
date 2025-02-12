package com.example.clush;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/list")
@CrossOrigin(origins = "http://localhost:3000")
public class ListController {

    private final ListRepository listRepository;

    public ListController(ListRepository listRepository) {
        this.listRepository = listRepository;
    }
    


    // username을 기준으로 할 일 목록을 가져오는 API
    @GetMapping
    public List<ListItem> getListItems(@RequestParam String username) {
        return listRepository.findByUsername(username);
    }

    @PostMapping("/add")
    public ListItem addTask(@RequestBody ListItem newItem) {
        return listRepository.save(newItem);
    }

    @PostMapping("/update")
    public void updateTask(@RequestBody ListItem item) {
        listRepository.save(item);
    }

    @PutMapping("/edit")
    public void editListItem(@RequestBody ListItem item) {
        listRepository.save(item);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteListItem(@PathVariable Integer id) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // 현재 로그인한 사용자의 username

        // ID로 할 일 항목 조회
        Optional<ListItem> listItemOptional = listRepository.findById(id);
        if (listItemOptional.isPresent()) {
            ListItem listItem = listItemOptional.get();

            // 할 일 항목이 현재 로그인한 사용자에게 속하는지 확인
            if (!listItem.getUsername().equals(username)) {
                return ResponseEntity.status(403).body("이 작업을 삭제할 권한이 없습니다.");
            }

            // 할 일 삭제
            listRepository.deleteById(id);
            return ResponseEntity.ok("작업이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(404).body("작업을 찾을 수 없습니다.");
        }
    }

    
    // 공유된 할 일만 가져오는 엔드포인트
    @GetMapping("/shared")
    public ResponseEntity<List<ListItem>> getSharedTasks() {
        List<ListItem> sharedTasks = listRepository.findBySharedTrue(); // shared가 true인 할 일만 가져옴
        return ResponseEntity.ok(sharedTasks);
    }


}

