package com.example.store.service.implement;

import com.example.store.dto.ReviewRequestDTO;
import com.example.store.dto.ReviewResponseDTO;
import com.example.store.entity.Product;
import com.example.store.entity.Reviews;
import com.example.store.entity.User;
import com.example.store.exception.ProductNotFoundException;
import com.example.store.exception.ReviewNotFoundException;
import com.example.store.exception.UserNotFoundException;
import com.example.store.mapper.IReviewMapper;
import com.example.store.repository.IProductRepository;
import com.example.store.repository.IReviewsRepository;
import com.example.store.repository.IUserRepository;
import com.example.store.service.IReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements IReviewService {
    private final IReviewsRepository reviewsRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IReviewMapper reviewMapper;

    public ReviewServiceImpl(IReviewsRepository reviewsRepository, IUserRepository userRepository, IProductRepository productRepository, IReviewMapper reviewMapper) {
        this.reviewsRepository = reviewsRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional
    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO) {
        Optional<User> existedUser = userRepository.findByUsername(reviewRequestDTO.getUsername());
        if(existedUser.isEmpty()) {
            throw new UserNotFoundException("Not found user: ");
        } User user = existedUser.get();
        Optional<Product> existedProduct = productRepository.findById(reviewRequestDTO.getIdProduct());
        if(existedProduct.isEmpty()) {
            throw new ProductNotFoundException("Not found product: ");
        } Product product = existedProduct.get();
        Reviews reviews = new Reviews();
        reviews.setUser(user);
        reviews.setProduct(product);
        reviews.setCreatedDate(ZonedDateTime.now());
        reviews.setContent(reviewRequestDTO.getContentReviews());
        reviews.setRate(reviewRequestDTO.getRate());
        if(!reviewRequestDTO.getUrlImage().isEmpty()){
            reviews.setImage(reviewRequestDTO.getUrlImage());
        } reviewsRepository.save(reviews);
        return reviewMapper.toDTO(reviews);
    }

    @Transactional
    @Override
    public ReviewResponseDTO updateReview(String username, Long reviewID, ReviewRequestDTO reviewRequestDTO) {
        Optional<User> existedUser = userRepository.findByUsername(reviewRequestDTO.getUsername());
        if(existedUser.isEmpty()) {
            throw new UserNotFoundException("Not found user: ");
        } User user = existedUser.get();
        Optional<Reviews> existedReview = reviewsRepository.findById(reviewID);
        if(existedReview.isEmpty()) {
            throw new ReviewNotFoundException("Not found review: ");
        } Reviews review = existedReview.get();
        review.setUser(user);
        review.setRate(reviewRequestDTO.getRate());
        review.setContent(reviewRequestDTO.getContentReviews());
        if(!reviewRequestDTO.getUrlImage().isEmpty()){
            review.setImage(reviewRequestDTO.getUrlImage());
        } reviewsRepository.save(review);
        return reviewMapper.toDTO(review);
    }

    @Transactional
    @Override
    public boolean deleteReview(String username, Long reviewID) {
        Optional<User> existedUser = userRepository.findByUsername(username);
        if(existedUser.isEmpty()) {
            throw new UserNotFoundException("Not found user: ");
        } User user = existedUser.get();
        Optional<Reviews> existedReview = reviewsRepository.findById(reviewID);
        if(existedReview.isEmpty()) {
            throw new ReviewNotFoundException("Not found review: ");
        } Reviews review = existedReview.get();
        reviewsRepository.delete(review);
        return true;
    }

    @Transactional
    @Override
    public List<ReviewResponseDTO> getAllReviewsByProduct(Long productID) {
        Optional<Product> existedProduct = productRepository.findById(productID);
        if(existedProduct.isEmpty()) {
            throw new ProductNotFoundException("Not found product: ");
        } Product product = existedProduct.get();
        List<Reviews> reviewsList = product.getReviews();
        return reviewMapper.toDTOs(reviewsList);
    }
}
