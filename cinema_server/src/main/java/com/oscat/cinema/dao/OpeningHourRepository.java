package com.oscat.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.OpeningHour;

@Repository
public interface OpeningHourRepository extends JpaRepository<OpeningHour, Integer>{

}
