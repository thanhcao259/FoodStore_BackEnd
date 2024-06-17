package com.example.store.repository;

import com.example.store.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
//    Page<Category> findAll(Pageable pageable);
//     findAll(Pageable pageable);
}
