package com.example.store.repository;

import com.example.store.entity.Reviews;
import com.example.store.entity.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStatusOrderRepository extends JpaRepository<StatusOrder, Long> {
}
