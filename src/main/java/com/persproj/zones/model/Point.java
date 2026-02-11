package com.persproj.zones.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "points")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "perimeter_id")
    @JsonBackReference
    private Perimeter perimeter;

    public Point() {}

    public Point(Double latitude, Double longitude, Integer orderIndex) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderIndex = orderIndex;
    }

}