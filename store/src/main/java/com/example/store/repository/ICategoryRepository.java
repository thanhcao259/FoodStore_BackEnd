package com.example.store.repository;

import com.example.store.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select c from Category c where c.status = ?2 and c.id =?1")
    Optional<Category> findByIdAndStatus(Long id, boolean status);

    @Query(value = "select c from Category c where c.status=true")
    List<Category> findAllByStatusIsTrue();


}
