package com.oscat.cinema.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Member;

@Repository
public interface MemberAdminRepository extends JpaRepository<Member, UUID> {

	 Optional<Member> findByEmail(String email);
	  
	 public String deleteByEmail(String email);
	 
	 @Query(value = "from Member where memberName like %:n%")
	 Optional<Member> findMemberByNameLike(@Param("n") String name);
	 
	 
	 List <Member> findMemberByEmailLike(String email);
	 
}
