package com.oscat.cinema.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	List<Member> findMemberByEmailLike(String email);

	Page<Member> findAll(Pageable pageable);

	// 計算男女會員數量
	@Query("select count(m) from Member m where m.gender = :gender")
	long countGender(@Param("gender") String gender);

	// 計算指定日期內的會員數量
	@Query("select count(m) from Member m where m.joinDate between :startDate and :endDate")
	long countJoinDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	// 計算生日月份人數
	@Query("select month(m.birthDate) as month, count(m) as count from Member m group by month(m.birthDate)")
	List<Object[]> countBirthDate();
}