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

    @GetMapping
    public List<ListItem> getListItems() {
        return listRepository.findAll();
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
