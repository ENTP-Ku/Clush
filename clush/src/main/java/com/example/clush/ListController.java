package com.example.clush;

import org.springframework.http.ResponseEntity;
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

//    @GetMapping
//    public List<ListItem> getListItems() {
//        return listRepository.findAll();
//    }
    
    // username을 기준으로 할 일 목록을 가져오는 API
    @GetMapping
    public List<ListItem> getListItems(@RequestParam String username) {
        // username에 해당하는 할 일 목록만 가져오기
        return listRepository.findByUsername(username);
    }
    
    @PostMapping("/add")
    public ListItem addTask(@RequestBody ListItem newItem) {
        return listRepository.save(newItem);
    }


    @PostMapping("/update")
    public void updateChecked(@RequestBody ListItem item) {
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
    
    // 할 일 공유 여부 업데이트
    @PutMapping("/update/shared/{id}")
    public ResponseEntity<ListItem> updateShareStatus(@PathVariable Integer id, @RequestBody boolean isShared) {
        Optional<ListItem> optionalListItem = listRepository.findById(id);
        if (optionalListItem.isPresent()) {
            ListItem listItem = optionalListItem.get();
            listItem.setShared(isShared); // 공유 여부 업데이트
            listRepository.save(listItem);
            return ResponseEntity.ok(listItem); // 업데이트된 할 일 반환
        }
        return ResponseEntity.notFound().build(); // 할 일이 존재하지 않으면 404 응답
    }

    // 공유된 할 일 목록만 조회
    @GetMapping("/shared")
    public ResponseEntity<List<ListItem>> getSharedTasks() {
        List<ListItem> sharedTasks = listRepository.findByIsSharedTrue();
        return ResponseEntity.ok(sharedTasks); // 공유된 할 일 목록 반환
    }

    
    
}
