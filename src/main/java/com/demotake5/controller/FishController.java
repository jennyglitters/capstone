package com.demotake5.controller;

import com.demotake5.dto.FishDTO;
import com.demotake5.entity.Fish;
import com.demotake5.entity.Tank;
import com.demotake5.exception.ResourceNotFoundException;
import com.demotake5.exception.ValidationException;
import com.demotake5.repository.TankRepository;
import com.demotake5.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/fish")
public class FishController {

    private final FishService fishService;
    private final TankRepository tankRepository;

    @Autowired
    public FishController(FishService fishService, TankRepository tankRepository) {
        this.fishService = fishService;
        this.tankRepository = tankRepository;
    }

    // Unified page for listing, adding, and editing fish
    @GetMapping
    public String fishPage(Model model) {
        model.addAttribute("fishList", fishService.getAllFish());
        model.addAttribute("tankList", tankRepository.findAll());
        model.addAttribute("fishDTO", new FishDTO());
        return "fish"; // Renders fish.html
    }

    // Handle form submission for both add and edit
    @PostMapping("/save")
    public String saveFish(@ModelAttribute FishDTO fishDTO, Model model) {
        try {
            // Validate tankId
            if (fishDTO.getTankId() == null || !tankRepository.existsById(fishDTO.getTankId())) {
                throw new ValidationException("Invalid tank ID: " + fishDTO.getTankId());
            }

            // Use saveOrUpdateFish instead of saveFish
            fishService.saveOrUpdateFish(fishDTO);
            return "redirect:/fish";
        } catch (ValidationException | ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("fishList", fishService.getAllFish());
            model.addAttribute("tankList", tankRepository.findAll());
            model.addAttribute("fishDTO", fishDTO);
            return "fish";
        }
    }

    // Handle delete
    @GetMapping("/delete/{id}")
    public String deleteFish(@PathVariable Long id) {
        fishService.deleteFish(id);
        return "redirect:/fish";
    }

    // Handle edit (pre-fill the form)
    @GetMapping("/edit/{id}")
    public String editFish(@PathVariable Long id, Model model) {
        Fish fish = fishService.getFishById(id);
        model.addAttribute("fishDTO", fishService.convertToDTO(fish));
        model.addAttribute("fishList", fishService.getAllFish());
        model.addAttribute("tankList", tankRepository.findAll());
        return "fish";
    }

    // Count fish per tank
    @GetMapping("/count-per-tank")
    @ResponseBody
    public List<Map<String, Object>> getFishCountPerTank() {
        return fishService.countFishPerTank();
    }

    // FishController.java
    @GetMapping("/total")
    @ResponseBody
    public int getTotalFish() {
        return fishService.getTotalFishCount();
    }
}