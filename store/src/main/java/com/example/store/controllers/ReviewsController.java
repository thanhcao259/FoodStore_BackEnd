package com.example.store.controllers;

import com.example.store.dto.ReviewRequestDTO;
import com.example.store.dto.ReviewResponseDTO;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.exception.ReviewNotFoundException;
import com.example.store.service.IReviewService;
import com.example.store.service.IUploadFileService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewsController {
    private final IReviewService reviewService;
    private final IUploadFileService uploadFileService;

    public ReviewsController(IReviewService reviewService, IUploadFileService uploadFileService) {
        this.reviewService = reviewService;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/reviews/{proId}")
    public ResponseEntity<?> getReviews(@PathVariable Long proId) {
        try {
            List<ReviewResponseDTO> list = reviewService.getAllReviewsByProduct(proId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/user/reviews/create-review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(Authentication auth,
                                          @RequestParam("prodId") Long proId,
                                          @RequestParam("rate") int rate,
                                          @RequestParam("contentReview") String contentReview,
                                          @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String username = auth.getName();
            String urlImg = "";
            if (image != null) {
                urlImg = uploadFileService.uploadFile(image);
            }
            ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
            reviewRequestDTO.setUsername(username);
            reviewRequestDTO.setUrlImage(urlImg);
            reviewRequestDTO.setRate(rate);
            reviewRequestDTO.setContentReviews(contentReview);
            reviewRequestDTO.setIdProduct(proId);
            ReviewResponseDTO responseDTO = reviewService.createReview(reviewRequestDTO);

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/user/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatedReview(Authentication auth,
                                           @PathVariable Long reviewId,
                                           @RequestParam("rate") int rate,
                                           @RequestParam("contentReview") String contentReview,
                                           @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String username = auth.getName();
            String urlImg = "";
            if (image != null) {
                urlImg = uploadFileService.uploadFile(image);
            }
            ReviewRequestDTO requestDTO = new ReviewRequestDTO(username, rate, contentReview, urlImg);
            ReviewResponseDTO responseDTO = reviewService.updateReview(username, reviewId, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/user/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(Authentication auth, @PathVariable Long reviewId) {
        try {
            String username = auth.getName();
            boolean isDeleted = reviewService.deleteReview(username, reviewId);
            return new ResponseEntity<>(isDeleted, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
