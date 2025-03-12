package com.demotake5.controller;

import com.demotake5.dto.TankDTO;
import com.demotake5.entity.Tank;
import com.demotake5.exception.ValidationException;
import com.demotake5.service.TankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.demotake5.repository.TankRepository;
import java.util.List;

@Controller
@RequestMapping("/tank")
public class TankController {

    @Autowired
    private TankService tankService;

    @Autowired
    private TankRepository tankRepository; // Inject TankRepository

    // Unified page for listing, adding, and editing tanks
    @GetMapping
    public String tankPage(Model model) {
        List<Tank> tankList = tankService.getAllTanks(); // Fetch all tanks
        model.addAttribute("tankList", tankList); // Add tank list to the model
        model.addAttribute("tankDTO", new TankDTO()); // Add empty TankDTO for the form
        return "tank"; // Render the tank.html template
    }

    // Handle form submission for both add and edit
    @PostMapping("/save")
    public String saveTank(@Valid @ModelAttribute TankDTO tankDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tankList", tankService.getAllTanks());
            model.addAttribute("tankDTO", tankDTO);
            return "tank";
        }

        try {
            // Determine if this is a new tank or an update
            boolean isNewTank = !tankRepository.existsById(tankDTO.getId());
            tankService.saveOrUpdateTank(tankDTO, isNewTank); // Pass the isNewTank flag
            return "redirect:/tank"; // Redirect back to the tank page
        } catch (ValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tankList", tankService.getAllTanks());
            model.addAttribute("tankDTO", tankDTO);
            return "tank";
        } catch (RuntimeException e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            model.addAttribute("tankList", tankService.getAllTanks());
            model.addAttribute("tankDTO", tankDTO);
            return "tank";
        }
    }

    // Handle delete
    @GetMapping("/delete/{id}")
    public String deleteTank(@PathVariable Long id) {
        tankService.deleteTank(id);
        return "redirect:/tank"; // Redirect back to the tank page
    }

    // Handle edit (pre-fill the form)
    @GetMapping("/edit/{id}")
    public String editTank(@PathVariable Long id, Model model) {
        TankDTO tankDTO = tankService.getTankDTOById(id); // Convert Tank entity to DTO
        model.addAttribute("tankDTO", tankDTO); // Pass DTO to form
        model.addAttribute("tankList", tankService.getAllTanks()); // Ensure list is populated
        return "tank"; // Correct template name (tank.html)
    }
    // TankController.java
    @GetMapping("/count")
    @ResponseBody
    public long getActiveTankCount() {
        return tankRepository.count();
    }
}
