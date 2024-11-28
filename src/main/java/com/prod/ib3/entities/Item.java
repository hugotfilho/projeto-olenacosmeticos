package com.prod.ib3.entities;

import java.time.LocalDate;
import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    //@Column
    //@ManyToOne
    //private ItemImage image;

    @Column 
    private String description;

    @Column(columnDefinition = "bytea")
    private byte[] image;

    @Column
    private String category;
    
    @Column 
    private Integer pricing;

    @Column
    @CreatedDate
    private LocalDate createdDate;

    public Item(String name, /*ItemImage image,*/ String description, String category, Integer pricing, LocalDate createdDate, byte[] image) {
        this.name = name;
        this.category = category;
        this.image = image;
        this.description = description;
        this.pricing = pricing;
        this.createdDate = createdDate;
    }

    public Item() {
        //TODO Auto-generated constructor stub
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPricing() {
        return pricing;
    }

    public void setPricing(Integer pricing) {
        this.pricing = pricing;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
}
