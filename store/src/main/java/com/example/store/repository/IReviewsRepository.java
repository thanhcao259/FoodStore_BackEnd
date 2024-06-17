package com.example.store.repository;

import com.example.store.entity.Product;
import com.example.store.entity.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReviewsRepository extends JpaRepository<Reviews, Long> {
    Optional<Reviews> findByIdAndUserId (Long id, Long UserId);
}
