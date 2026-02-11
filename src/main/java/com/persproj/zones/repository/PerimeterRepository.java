package com.persproj.zones.repository;


import com.persproj.zones.model.Perimeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerimeterRepository extends JpaRepository<Perimeter, Long> {
}