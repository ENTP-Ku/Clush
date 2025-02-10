package com.example.clush;

import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}
