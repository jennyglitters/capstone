package com.demotake5.service;

import com.demotake5.dto.TankDTO;
import com.demotake5.entity.Fish;
import com.demotake5.entity.Tank;
import com.demotake5.exception.ResourceNotFoundException;
import com.demotake5.exception.ValidationException;
import com.demotake5.repository.FishRepository;
import com.demotake5.repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TankService {

    @Autowired
    private TankRepository tankRepository;

    @Autowired
    private FishRepository fishRepository;

    // Get all tanks as a list
    public List<Tank> getAllTanks() {
        return tankRepository.findAll();
    }

    // Save or update a tank
    public void saveOrUpdateTank(TankDTO tankDTO, boolean isNewTank) {
        validateTankDTO(tankDTO, isNewTank);
        Tank tank = convertToEntity(tankDTO);
        saveTank(tank);
    }

    // Save a new tank
    public Tank saveTank(Tank tank) {
        validateTank(tank);
        return tankRepository.save(tank);
    }

    // Delete a tank by ID
    public void deleteTank(Long id) {
        if (!tankRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tank not found with id: " + id);
        }
        tankRepository.deleteById(id);
    }

    // Get a tank by ID (returns `Tank` entity)
    public Tank getTankById(Long id) {
        return tankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tank not found with id: " + id));
    }

    // Get a tank by ID but convert it to a DTO
    public TankDTO getTankDTOById(Long id) {
        Tank tank = getTankById(id);
        return convertToDTO(tank);
    }

    // Update an existing tank using DTO
    public void updateTank(Long id, TankDTO tankDTO) {
        validateTankDTO(tankDTO, false);
        Tank existingTank = getTankById(id);
        existingTank.setCapacity(tankDTO.getCapacity());
        tankRepository.save(existingTank);
    }

    // Convert Tank entity to TankDTO
    public TankDTO convertToDTO(Tank tank) {
        if (tank == null) {
            return null;
        }
        TankDTO dto = new TankDTO();
        dto.setId(tank.getId());
        dto.setCapacity(tank.getCapacity());
        return dto;
    }

    // Convert TankDTO to Tank entity
    public Tank convertToEntity(TankDTO tankDTO) {
        if (tankDTO == null) {
            return null;
        }
        Tank tank = new Tank();
        tank.setId(tankDTO.getId());
        tank.setCapacity(tankDTO.getCapacity());
        return tank;
    }

    // Validate tank capacity before adding or editing fish
    public void validateTankCapacity(Long tankId, int newFishQuantity, Long existingFishId) {
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(() -> new ResourceNotFoundException("Tank not found with id: " + tankId));

        int currentFishQuantity = fishRepository.getTotalFishQuantityByTankId(tankId);

        // If editing, subtract the existing fish quantity from the current total
        if (existingFishId != null) {
            Fish existingFish = fishRepository.findById(existingFishId)
                    .orElseThrow(() -> new ResourceNotFoundException("Fish not found with id: " + existingFishId));
            currentFishQuantity -= existingFish.getQuantity();
        }

        // Check if the tank has enough space
        if (currentFishQuantity + newFishQuantity > tank.getCapacity()) {
            throw new ValidationException("Tank capacity exceeded. Available space: " + (tank.getCapacity() - currentFishQuantity));
        }
    }

    // Custom validation for Tank entity
    private void validateTank(Tank tank) {
        if (tank == null) {
            throw new ValidationException("Tank cannot be null");
        }
        if (tank.getCapacity() == null || tank.getCapacity() <= 0) {
            throw new ValidationException("Capacity must be greater than 0 and cannot be null");
        }
    }

    private void validateTankDTO(TankDTO tankDTO, boolean isNewTank) {
        if (tankDTO == null) {
            throw new ValidationException("TankDTO cannot be null");
        }
        if (tankDTO.getId() == null) {
            throw new ValidationException("Tank ID cannot be null");
        }
        if (tankDTO.getCapacity() == null || tankDTO.getCapacity() <= 0) {
            throw new ValidationException("Capacity must be greater than 0 and cannot be null");
        }

        if (isNewTank) {
            if (tankRepository.existsById(tankDTO.getId())) {
                throw new ValidationException("Tank ID already exists");
            }
        } else {
            if (!tankRepository.existsById(tankDTO.getId())) {
                throw new ValidationException("Tank ID does not exist");
            }
        }
    }
}