package com.example.store.repository;

import com.example.store.entity.Reviews;
import com.example.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername (String username);
//    @Query(value = "select u from User u inner join Identification i where u.identification.id = i.id and Identification.email = ?1")
    @Query(value = "select u from User u where u.email = ?1")
    Optional<User> findByEmail (String email);

    @Query(value = "select u from User u where u.email = ?1 and u.username = ?2")
    Optional<User> findByEmailAndUsername (String email, String username);

}
