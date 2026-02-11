package com.persproj.zones.controller;


import com.persproj.zones.model.Perimeter;
import com.persproj.zones.service.PerimeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MapController {

    @Autowired
    private PerimeterService perimeterService;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    // Serve the main page
    @GetMapping("/")
    public String index() {
        return "map";
    }

    // Verify admin password
    @PostMapping("/api/admin/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> verifyAdmin(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", adminPassword.equals(password));
        return ResponseEntity.ok(response);
    }

    // Get all perimeters
    @GetMapping("/api/perimeters")
    @ResponseBody
    public ResponseEntity<List<Perimeter>> getAllPerimeters() {
        return ResponseEntity.ok(perimeterService.getAllPerimeters());
    }

    // Create perimeter
    @PostMapping("/api/perimeters")
    @ResponseBody
    public ResponseEntity<Perimeter> createPerimeter(@RequestBody Perimeter perimeter) {
        Perimeter created = perimeterService.createPerimeter(perimeter);
        return ResponseEntity.ok(created);
    }

    // Update perimeter
    @PutMapping("/api/perimeters/{id}")
    @ResponseBody
    public ResponseEntity<Perimeter> updatePerimeter(@PathVariable Long id, @RequestBody Perimeter perimeter) {
        Perimeter updated = perimeterService.updatePerimeter(id, perimeter);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete perimeter
    @DeleteMapping("/api/perimeters/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletePerimeter(@PathVariable Long id) {
        boolean deleted = perimeterService.deletePerimeter(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Find perimeter for a location
    @GetMapping("/api/locate")
    @ResponseBody
    public ResponseEntity<Map<String, String>> locatePoint(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        String perimeterName = perimeterService.findPerimeterForLocation(latitude, longitude);
        Map<String, String> response = new HashMap<>();
        response.put("perimeter", perimeterName);
        return ResponseEntity.ok(response);
    }
}