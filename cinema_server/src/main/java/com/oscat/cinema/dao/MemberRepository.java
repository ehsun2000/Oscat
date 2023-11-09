package com.oscat.cinema.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.oscat.cinema.entity.Member;
import com.oscat.cinema.entity.TransOrder;

public interface MemberRepository extends JpaRepository<Member, UUID> {

	@Query(value="from Member where email =:email")
	Member findByEmail(@Param("email") String email);
	
	@Query(value = "SELECT o From TransOrder o "
			+ "WHERE o.member.memberId = :mid "
			+ "ORDER BY o.bookingDateAndTime ASC")
	List<TransOrder> findOrdersByMid(@Param("mid") UUID mid);
	
}
