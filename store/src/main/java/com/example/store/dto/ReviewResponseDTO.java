package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private int rate;
    private String name;
    private String username;
    private String contentReviews;
    private String urlImage;
    private ZonedDateTime createdDate;
}
