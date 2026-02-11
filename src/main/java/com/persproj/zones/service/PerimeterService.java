package com.persproj.zones.service;


import com.persproj.zones.model.Perimeter;
import com.persproj.zones.model.Point;
import com.persproj.zones.repository.PerimeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PerimeterService {

    @Autowired
    private PerimeterRepository perimeterRepository;

    // Generate random color for perimeter
    private String generateRandomColor() {
        Random random = new Random();
        String[] colors = {
            "#FF5733", "#33FF57", "#3357FF", "#FF33F5",
            "#F5FF33", "#33FFF5", "#FF8C33", "#8C33FF",
            "#FF3333", "#33FF33", "#3333FF", "#FFFF33"
        };
        return colors[random.nextInt(colors.length)];
    }

    public List<Perimeter> getAllPerimeters() {
        return perimeterRepository.findAll();
    }

    public Optional<Perimeter> getPerimeterById(Long id) {
        return perimeterRepository.findById(id);
    }

    @Transactional
    public Perimeter createPerimeter(Perimeter perimeter) {
        if (perimeter.getColor() == null || perimeter.getColor().isEmpty()) {
            perimeter.setColor(generateRandomColor());
        }

        // Set order indices for points
        for (int i = 0; i < perimeter.getPoints().size(); i++) {
            perimeter.getPoints().get(i).setOrderIndex(i);
            perimeter.getPoints().get(i).setPerimeter(perimeter);
        }

        return perimeterRepository.save(perimeter);
    }

    @Transactional
    public Perimeter updatePerimeter(Long id, Perimeter updatedPerimeter) {
        Optional<Perimeter> existingOpt = perimeterRepository.findById(id);
        if (existingOpt.isPresent()) {
            Perimeter existing = existingOpt.get();
            existing.setName(updatedPerimeter.getName());

            // Clear old points
            existing.getPoints().clear();

            // Add new points
            for (int i = 0; i < updatedPerimeter.getPoints().size(); i++) {
                Point point = updatedPerimeter.getPoints().get(i);
                point.setOrderIndex(i);
                existing.addPoint(point);
            }

            return perimeterRepository.save(existing);
        }
        return null;
    }

    @Transactional
    public boolean deletePerimeter(Long id) {
        if (perimeterRepository.existsById(id)) {
            perimeterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Point-in-polygon algorithm (Ray casting)
    public String findPerimeterForLocation(double latitude, double longitude) {
        List<Perimeter> perimeters = getAllPerimeters();

        for (Perimeter perimeter : perimeters) {
            if (isPointInPolygon(latitude, longitude, perimeter.getPoints())) {
                return perimeter.getName();
            }
        }

        return null;
    }

    private boolean isPointInPolygon(double lat, double lon, List<Point> polygon) {
        if (polygon.size() < 3) return false;

        boolean inside = false;
        int j = polygon.size() - 1;

        for (int i = 0; i < polygon.size(); i++) {
            double xi = polygon.get(i).getLatitude();
            double yi = polygon.get(i).getLongitude();
            double xj = polygon.get(j).getLatitude();
            double yj = polygon.get(j).getLongitude();

            boolean intersect = ((yi > lon) != (yj > lon)) &&
                (lat < (xj - xi) * (lon - yi) / (yj - yi) + xi);

            if (intersect) inside = !inside;
            j = i;
        }

        return inside;
    }
}