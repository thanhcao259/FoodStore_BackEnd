package com.example.store.repository;

import com.example.store.entity.ForgotPassword;
import com.example.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

//    @Query(value = "select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    @Query(value = "select fp from  ForgotPassword fp inner join User u on fp.user.id = u.id where u.username = ?2 and fp.otp = ?1 and fp.isActive = false")
    Optional<ForgotPassword> findByOtpAndUsername(Integer otp, String username);
}
