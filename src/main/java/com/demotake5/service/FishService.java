package com.demotake5.service;

import com.demotake5.dto.FishDTO;
import com.demotake5.entity.Fish;
import com.demotake5.entity.Tank;
import com.demotake5.exception.ResourceNotFoundException;
import com.demotake5.exception.ValidationException;
import com.demotake5.repository.FishRepository;
import com.demotake5.repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FishService {

    private final FishRepository fishRepository;
    private final TankRepository tankRepository;

    @Autowired
    public FishService(FishRepository fishRepository, TankRepository tankRepository) {
        this.fishRepository = fishRepository;
        this.tankRepository = tankRepository;
    }

    // Save or update fish with capacity validation
    public void saveOrUpdateFish(FishDTO fishDTO) {
        Tank tank = tankRepository.findById(fishDTO.getTankId())
                .orElseThrow(() -> new ResourceNotFoundException("Tank not found with id: " + fishDTO.getTankId()));

        // Validate tank capacity
        int currentFishQuantity = fishRepository.getTotalFishQuantityByTankId(fishDTO.getTankId());
        if (fishDTO.getId() != null) {
            Fish existingFish = fishRepository.findById(fishDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fish not found with id: " + fishDTO.getId()));
            currentFishQuantity -= existingFish.getQuantity();
        }

        if (currentFishQuantity + fishDTO.getQuantity() > tank.getCapacity()) {
            throw new ValidationException("Tank capacity exceeded. Available space: " + (tank.getCapacity() - currentFishQuantity));
        }

        Fish fish = convertToEntity(fishDTO);
        fishRepository.save(fish);
    }

    // Get all fish
    public List<Fish> getAllFish() {
        return fishRepository.findAll();
    }

    // Get a fish by ID
    public Fish getFishById(Long id) {
        return fishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fish not found with id: " + id));
    }

    // Delete a fish by ID
    public void deleteFish(Long id) {
        fishRepository.deleteById(id);
    }

    // Convert FishDTO to Fish entity
    public Fish convertToEntity(FishDTO fishDTO) {
        Fish fish = new Fish();
        fish.setId(fishDTO.getId());
        fish.setSpecies(fishDTO.getSpecies());
        fish.setQuantity(fishDTO.getQuantity());

        Tank tank = tankRepository.findById(fishDTO.getTankId())
                .orElseThrow(() -> new ResourceNotFoundException("Tank not found with id: " + fishDTO.getTankId()));
        fish.setTank(tank);

        return fish;
    }

    // Convert Fish entity to FishDTO
    public FishDTO convertToDTO(Fish fish) {
        FishDTO dto = new FishDTO();
        dto.setId(fish.getId());
        dto.setSpecies(fish.getSpecies());
        dto.setQuantity(fish.getQuantity());
        dto.setTankId(fish.getTank().getId());
        return dto;
    }

    // Count fish per tank
    public List<Map<String, Object>> countFishPerTank() {
        return fishRepository.countFishPerTank();
    }

    // FishService.java
    public int getTotalFishCount() {
        return fishRepository.findAll().stream()
                .mapToInt(Fish::getQuantity)
                .sum();
    }
}