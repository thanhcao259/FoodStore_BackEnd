package com.example.store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    private String image;
    @Column(name = "description", length = -1)
    private String description;
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_created", referencedColumnName = "id")
    private User userCreated;
    @ManyToOne
    @JoinColumn(name = "user_updated", referencedColumnName = "id")
    private User userUpdated;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    @Column(name = "identity")
    private String identity;

    @Column(name = "status")
    @ColumnDefault(value = "true")
    private boolean status;

    public boolean isStatus() {
        return status;
    }
}
