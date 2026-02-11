package com.persproj.zones.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "perimeters")
public class Perimeter {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @OneToMany(mappedBy = "perimeter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<Point> points = new ArrayList<>();


    public Perimeter() {}

    public Perimeter(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // Helper method to add point
    public void addPoint(Point point) {
        points.add(point);
        point.setPerimeter(this);
    }

}