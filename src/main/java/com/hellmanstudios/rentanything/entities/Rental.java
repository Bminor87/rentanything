package com.hellmanstudios.rentanything.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "rental_start", nullable = true)
    private LocalDateTime rentalStart;

    @Column(name = "rental_end", nullable = true)
    private LocalDateTime rentalEnd;

    public Rental() {
    }

    public Rental(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public Rental(User user, Item item, LocalDateTime rentalStart, LocalDateTime rentalEnd) {
        this.user = user;
        this.item = item;
        this.rentalStart = rentalStart;
        this.rentalEnd = rentalEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDateTime getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(LocalDateTime rentalStart) {
        this.rentalStart = rentalStart;
    }

    public LocalDateTime getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(LocalDateTime rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public String getRentalStartFormatted() {
        if (rentalStart == null) {
            return "";
        }

        return rentalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getRentalEndFormatted() {
        if (rentalEnd == null) {
            return "";
        }

        return rentalEnd.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    
}
