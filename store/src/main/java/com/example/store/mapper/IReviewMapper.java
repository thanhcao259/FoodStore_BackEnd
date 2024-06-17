package com.example.store.mapper;

import com.example.store.dto.ReviewResponseDTO;
import com.example.store.entity.Reviews;

import java.util.List;

public interface IReviewMapper {
    ReviewResponseDTO toDTO (Reviews reviews);
    List<ReviewResponseDTO> toDTOs (List<Reviews> reviewsList);

}
