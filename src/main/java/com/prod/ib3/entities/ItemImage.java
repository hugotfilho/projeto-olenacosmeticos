package com.prod.ib3.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ItemImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Item item;

    @Column(columnDefinition = "bytea")
    private byte[] image;

    public ItemImage(byte[] image) {
        this.image = image;
    }

    public ItemImage() {
        //TODO Auto-generated constructor stub
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
