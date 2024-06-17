package com.example.store.mapper.impl;

import com.example.store.dto.ReviewResponseDTO;
import com.example.store.entity.Reviews;
import com.example.store.mapper.IReviewMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewMapperImpl implements IReviewMapper {
    @Override
    public ReviewResponseDTO toDTO(Reviews reviews) {
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setId(reviews.getId());
        reviewResponseDTO.setContentReviews(reviews.getContent());
        reviewResponseDTO.setUsername(reviews.getUser().getUsername());
        reviewResponseDTO.setName(reviews.getUser().getFullName());
        reviewResponseDTO.setCreatedDate(reviews.getCreatedDate());
        reviewResponseDTO.setRate(reviews.getRate());
        reviewResponseDTO.setUrlImage(reviews.getImage());

        return reviewResponseDTO;
    }

    @Override
    public List<ReviewResponseDTO> toDTOs(List<Reviews> reviewsList) {
        List<ReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();
        for (Reviews reviews : reviewsList) {
            reviewResponseDTOList.add(toDTO(reviews));
        }
        return reviewResponseDTOList;
    }
}
