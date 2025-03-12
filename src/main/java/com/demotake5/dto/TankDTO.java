package com.demotake5.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TankDTO {
    private Long id; // Allow manual ID assignment

    @NotNull(message = "Capacity cannot be null")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity; // Use Integer instead of int

    // Default constructor
    public TankDTO() {
    }

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

    // toString method for logging
    @Override
    public String toString() {
        return "TankDTO{" +
                "id=" + id +
                ", capacity=" + capacity +
                '}';
    }
}