package com.hellmanstudios.rentanything.entities;

import org.hibernate.validator.constraints.Range;

import com.hellmanstudios.rentanything.dto.ItemDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;
    
    @Pattern(regexp = "^$|^[\\w\\s-]+\\.(?i)(jpg|gif|png)$", message = "Invalid image URL")
    @Column(name = "image", nullable = true)
    private String image;
       

    @Range(min=0, max=9999) 
    @Column(name = "price")
    private double price = 0.0;

    @Column(name = "available")
    private boolean available = true;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    public Item() {
    }

    public Item(String name, String description, String image, double price, Category category) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String priceFormatted() {
        return String.format("%.2f", price);
    }

    public ItemDTO toDTO() {
        return new ItemDTO(id, name, description, price, image, available, category != null ? category.toDTO() : null);
    }
}
