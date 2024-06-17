package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private Long idProduct;
    private String username;
    private int rate;
    private String contentReviews;
    private String urlImage;

    public ReviewRequestDTO(String username, int rate, String contentReviews, String urlImage) {
        this.username = username;
        this.rate = rate;
        this.contentReviews = contentReviews;
        this.urlImage = urlImage;
    }
}
