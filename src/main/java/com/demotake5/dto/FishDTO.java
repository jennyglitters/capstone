package com.demotake5.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FishDTO {
    private Long id;

    @NotBlank(message = "Species cannot be blank")
    private String species;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Tank ID cannot be null")
    private Long tankId; // Ensure this field exists

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

    public Long getTankId() {
        return tankId;
    }

    public void setTankId(Long tankId) {
        this.tankId = tankId;
    }
}