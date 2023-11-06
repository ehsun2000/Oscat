package com.oscat.cinema.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oscat.cinema.entity.Member;

public interface MemberRepository extends JpaRepository<Member, UUID> {

	@Query(value="from Member where email =:email")
	Member findByEmail(@Param("email") String email);

}
