package com.example.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "available")
    private int available;

    @Column(name = "discount")
    private double discount;

    @Column(name = "price")
    private double price;

    @Column(name = "image")
    private String urlImage;

    @Column(name = "created_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private ZonedDateTime createdDate;

    @Column(name = "updated_date")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private ZonedDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_created", referencedColumnName = "id")
    private User userCreated;
    @ManyToOne
    @JoinColumn(name = "user_updated", referencedColumnName = "id")
    private User userUpdated;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "description", length = -1)
    private String description;

    @OneToMany(mappedBy = "product")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "product")
    private List<Reviews> reviews;

}
