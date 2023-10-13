package com.oscat.cinema.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.MovieStills;

@Repository
public interface MovieStillsRepository extends JpaRepository<MovieStills, UUID> {
}
