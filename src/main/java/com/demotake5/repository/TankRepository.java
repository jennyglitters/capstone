package com.demotake5.repository;


import com.demotake5.entity.Tank;
import org.springframework.data.jpa.repository.JpaRepository;

// TankRepository.java
public interface TankRepository extends JpaRepository<Tank, Long> {
}