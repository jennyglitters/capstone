package com.demotake5.entity;

import jakarta.persistence.*;

@Entity
public class Fish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Keep auto-generated ID for Fish
    private Long id;

    private String species;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "tank_id", nullable = false)
    private Tank tank;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }
}