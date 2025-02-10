package com.example.clush;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<ListItem, Integer> {
}
