package com.demotake5.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tank {

    @Id
    private Long id; // Remove @GeneratedValue to allow manual ID assignment

    private Integer capacity; // Use Integer instead of int to allow null values

    @OneToMany(mappedBy = "tank", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Fish> fishes = new ArrayList<>(); // One-to-Many relationship with Fish

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(List<Fish> fishes) {
        this.fishes = fishes;
    }

    // Helper method to manage the bidirectional relationship
    public void addFish(Fish fish) {
        fishes.add(fish);
        fish.setTank(this);
    }

    // Helper method to manage the bidirectional relationship
    public void removeFish(Fish fish) {
        fishes.remove(fish);
        fish.setTank(null);
    }

    // Override toString for better logging
    @Override
    public String toString() {
        return "Tank{" +
                "id=" + id +
                ", capacity=" + capacity +
                '}';
    }
}