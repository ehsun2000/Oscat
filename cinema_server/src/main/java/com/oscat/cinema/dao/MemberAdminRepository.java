package com.oscat.cinema.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Member;

@Repository
public interface MemberAdminRepository extends JpaRepository<Member, UUID> {

	 Optional<Member> findByEmail(String email);
	  
	 public String deleteByEmail(String email);
	 
}
