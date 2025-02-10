package com.example.clush;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<ListItem, Integer> {
    List<ListItem> findByUsername(String username);
    List<ListItem> findByIsSharedTrue();

}
