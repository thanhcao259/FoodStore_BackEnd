package com.example.store.service;

import com.example.store.dto.ReviewRequestDTO;
import com.example.store.dto.ReviewResponseDTO;

import java.util.List;

public interface IReviewService {
    ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO);
    ReviewResponseDTO updateReview(String username, Long reviewID, ReviewRequestDTO reviewRequestDTO);
    boolean deleteReview(String username, Long reviewID);
    List<ReviewResponseDTO> getAllReviewsByProduct(Long productID);
}
