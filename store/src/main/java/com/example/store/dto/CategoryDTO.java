package com.example.store.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String urlImage;
    private String identity;
    private boolean status;

    public CategoryDTO(String name, String description, String urlImage, boolean status) {
        this.name = name;
        this.description = description;
        this.urlImage = urlImage;
        this.status = status;
    }

    public CategoryDTO(String name, String description, String urlImage, String identity) {
        this.name = name;
        this.description = description;
        this.urlImage = urlImage;
        this.identity = identity;
    }
}
