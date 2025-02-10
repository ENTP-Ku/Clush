package com.example.clush;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
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
    public void deleteListItem(@PathVariable Integer id) {
        listRepository.deleteById(id);
    }
    
    // 공유된 할 일만 가져오는 엔드포인트
    @GetMapping("/shared")
    public ResponseEntity<List<ListItem>> getSharedTasks() {
        List<ListItem> sharedTasks = listRepository.findBySharedTrue(); // shared가 true인 할 일만 가져옴
        return ResponseEntity.ok(sharedTasks);
    }


}

