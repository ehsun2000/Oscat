package com.oscat.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {

}
