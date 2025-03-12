package com.demotake5.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.demotake5.entity.Fish;
import com.demotake5.entity.Tank;
import com.demotake5.repository.FishRepository;
import com.demotake5.repository.TankRepository;

import java.util.List;
import java.util.Map;

@DataJpaTest
public class FishRepositoryTest {

    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private TankRepository tankRepository;

    @BeforeEach
    public void setup() {
        // Clear the database before each test
        fishRepository.deleteAll();
        tankRepository.deleteAll();
    }

    @Test
    public void testFindByTankId() {
        // Create a tank
        Tank tank = new Tank();
        tank.setId(1L);
        tank.setCapacity(100);
        tankRepository.save(tank);

        // Create a fish associated with the tank
        Fish fish = new Fish();
        fish.setSpecies("Tilapia");
        fish.setQuantity(10);
        fish.setTank(tank);
        fishRepository.save(fish);

        // Test the findByTankId method
        List<Fish> fishList = fishRepository.findByTankId(1L);
        assertEquals(1, fishList.size());
        assertEquals("Tilapia", fishList.get(0).getSpecies());
    }

    @Test
    public void testCountFishPerTank() {
        // Create a tank
        Tank tank = new Tank();
        tank.setId(1L);
        tank.setCapacity(100);
        tankRepository.save(tank);

        // Create a fish associated with the tank
        Fish fish = new Fish();
        fish.setSpecies("Tilapia");
        fish.setQuantity(10);
        fish.setTank(tank);
        fishRepository.save(fish);

        // Test the countFishPerTank method
        List<Map<String, Object>> result = fishRepository.countFishPerTank();
        assertEquals(1, result.size());

        // Check the tank ID and total fish count
        Map<String, Object> tankData = result.get(0);
        assertEquals(1L, tankData.get("tankId")); // tankId is expected to be a Long
        assertEquals(10L, tankData.get("totalFish")); // totalFish is expected to be a Long
    }
}