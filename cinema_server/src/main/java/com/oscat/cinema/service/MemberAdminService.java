package com.oscat.cinema.service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MemberAdminRepository;
import com.oscat.cinema.dto.AgeProportionDTO;
import com.oscat.cinema.dto.GenderProportionDTO;
import com.oscat.cinema.dto.JoinDateProportionDTO;
import com.oscat.cinema.entity.Member;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MemberAdminService {

	private final MemberAdminRepository memberRepository;

	@Autowired
	public MemberAdminService(MemberAdminRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	// 新增會員
	public Member addMember(Member member) {
		return memberRepository.save(member);
	}

	// 根據id查找會員
	public Optional<Member> findMemberById(UUID memberId) {
		return memberRepository.findById(memberId);
	}

	// 關鍵字搜尋 byEmail
	public List<Member> findKeywordByEmail(String email) {
		return memberRepository.findMemberByEmailLike(email);
	}

	// 分頁
	public Page<Member> findPage(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

	// 根據email查找會員
	public Optional<Member> findMemberByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	// 獲取所有會員列表 (照加入時間排序)
	public List<Member> getAllMembers() {
		Sort sort = Sort.by(Sort.Direction.DESC, "joinDate");
		// (頁面索引(第一頁), 每一頁的最大值)
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
		return memberRepository.findAll(pageable).getContent();
	}

	// 根據id刪除會員
	public String deleteMemberById(UUID memberId) {
		Optional<Member> optionalMember = memberRepository.findById(memberId);
		if (optionalMember.isPresent()) {
			memberRepository.deleteById(memberId);
			return "刪除成功";
		} else {
			return "找不到該會員";
		}
	}

	// 更新會員資料
	public Optional<Member> updateMember(Member newMember) {
		Optional<Member> existingMember = memberRepository.findById(newMember.getMemberId());
		if (existingMember.isPresent()) {
			Member memberToUpdate = existingMember.get();
			memberToUpdate.setMemberName(newMember.getMemberName());
			memberToUpdate.setEmail(newMember.getEmail());
			memberToUpdate.setPassword(newMember.getPassword());
			memberToUpdate.setPhone(newMember.getPhone());
			memberToUpdate.setGender(newMember.getGender());
			memberToUpdate.setBirthDate(newMember.getBirthDate());

			return Optional.of(memberRepository.save(memberToUpdate));
		} else {
			return Optional.empty();
		}
	}

	// 計算性別比
	public GenderProportionDTO calculateGender() {

		long totalGender = memberRepository.count();
		long maleCount = memberRepository.countGender("man");
		long femaleCount = memberRepository.countGender("female");
		long otherCount = memberRepository.countGender("other");

		Double maleProportion = (double) maleCount / totalGender;
		Double femaleProportion = (double) femaleCount / totalGender;
		Double otherProportion = (double) otherCount / totalGender;

		GenderProportionDTO genderproportion = new GenderProportionDTO();
		genderproportion.setTotalGender(totalGender);
		genderproportion.setMaleCount(maleCount);
		genderproportion.setFemaleCount(femaleCount);
		genderproportion.setOtherCount(otherCount);
		genderproportion.setMaleProportion(maleProportion);
		genderproportion.setFemaleProportion(femaleProportion);
		genderproportion.setOtherProportion(otherProportion);

		return genderproportion;
	}

	// 計算年齡區間比
	public AgeProportionDTO calculateAge() {
		List<Member> ageMember = memberRepository.findAll();

		int currentYear = LocalDate.now().getYear();

		long totalAge = ageMember.size();
		long youngAge = ageMember.stream().filter(member -> calculateAge(member.getBirthDate(), currentYear) < 18)
				.count();
		long teenagerAge = ageMember.stream().filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 18
				&& calculateAge(member.getBirthDate(), currentYear) <= 25).count();
		long adultAge = ageMember.stream().filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 26
				&& calculateAge(member.getBirthDate(), currentYear) <= 35).count();
		long middleageAge = ageMember.stream().filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 36
				&& calculateAge(member.getBirthDate(), currentYear) <= 50).count();
		long elderlyAge = ageMember.stream().filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 51
				&& calculateAge(member.getBirthDate(), currentYear) <= 65).count();
		long oldAge = ageMember.stream().filter(member -> calculateAge(member.getBirthDate(), currentYear) > 65)
				.count();

		// 年齡分段比例
		double youngAgeProportion = (double) youngAge / totalAge;
		double teenagerAgeProportion = (double) teenagerAge / totalAge;
		double adultAgeProportion = (double) adultAge / totalAge;
		double middleageAgeProportion = (double) middleageAge / totalAge;
		double elderlyAgeProportion = (double) elderlyAge / totalAge;
		double oldAgeProportion = (double) oldAge / totalAge;

		AgeProportionDTO ageProportion = new AgeProportionDTO();
		ageProportion.setTotalAge(totalAge);
		ageProportion.setYoungAge(youngAge);
		ageProportion.setTeenagerAge(teenagerAge);
		ageProportion.setAdultAge(adultAge);
		ageProportion.setMiddleageAge(middleageAge);
		ageProportion.setElderlyAge(elderlyAge);
		ageProportion.setOldAge(oldAge);
		ageProportion.setYoungAgeProportion(youngAgeProportion);
		ageProportion.setTeenagerAgeProportion(teenagerAgeProportion);
		ageProportion.setAdultAgeProportion(adultAgeProportion);
		ageProportion.setMiddleageAgeProportion(middleageAgeProportion);
		ageProportion.setElderlyAgeProportion(elderlyAgeProportion);
		ageProportion.setOldAgeProportion(oldAgeProportion);

		return ageProportion;
	}

	// 計算年齡差
	private int calculateAge(Date birthDate, int currentYear) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(birthDate);
		int birthYear = calendar.get(Calendar.YEAR);
		return currentYear - birthYear;
	}

	// 今、去年加入人數比(一個月/半年/截至目前)
	public JoinDateProportionDTO calculateJoinDate() {

		// 計算今年的上一月加入人數
		Calendar calendarThisYear = Calendar.getInstance();
		calendarThisYear.add(Calendar.MONTH, -1);
		Date lastMonthThisYear = calendarThisYear.getTime(); // 今天往前一個月
		calendarThisYear.add(Calendar.MONTH, -5);
		Date halfThisYear = calendarThisYear.getTime(); // 今天往前半年

		Calendar calendarThisFirstDay = Calendar.getInstance();
		calendarThisFirstDay.set(Calendar.DAY_OF_YEAR, 1);
		Date thisYear = calendarThisFirstDay.getTime(); // 今年第一天

		// 計算去年的上一月加入人數
		Calendar calendarLastYear = Calendar.getInstance();
		calendarLastYear.add(Calendar.YEAR, -1);
		Date lastYearToday = calendarLastYear.getTime(); // 去年的今天
		calendarLastYear.add(Calendar.MONTH, -1);
		Date lastMonthLastYear = calendarLastYear.getTime(); // 去年的今天往前一個月
		calendarLastYear.add(Calendar.MONTH, -5);
		Date halfLastYear = calendarLastYear.getTime(); // 去年的今天往前半年

		Calendar calendarLastFirstDay = Calendar.getInstance();
		calendarLastFirstDay.add(Calendar.YEAR, -1);
		calendarLastFirstDay.set(Calendar.DAY_OF_YEAR, 1);
		Date lastYear = calendarLastFirstDay.getTime(); // 去年第一天

		long thisYearMonthCount = memberRepository.countJoinDateBetween(lastMonthThisYear,
				Calendar.getInstance().getTime());
		long thisHalfYearCount = memberRepository.countJoinDateBetween(halfThisYear, Calendar.getInstance().getTime());
		long thisYearCount = memberRepository.countJoinDateBetween(thisYear, Calendar.getInstance().getTime());

		long lastYearMonthCount = memberRepository.countJoinDateBetween(lastMonthLastYear, lastYearToday);
		long lastHalfYearCount = memberRepository.countJoinDateBetween(halfLastYear, lastYearToday);
		long lastYearCount = memberRepository.countJoinDateBetween(lastYear, lastYearToday);

		JoinDateProportionDTO joinDateProportion = new JoinDateProportionDTO();
		joinDateProportion.setThisYearMonthCount(thisYearMonthCount);
		joinDateProportion.setThisHalfYearCount(thisHalfYearCount);
		joinDateProportion.setThisYearCount(thisYearCount);
		joinDateProportion.setLastYearMonthCount(lastYearMonthCount);
		joinDateProportion.setLastHalfYearCount(lastHalfYearCount);
		joinDateProportion.setLastYearCount(lastYearCount);
		return joinDateProportion;
	}

	public Map<Integer, Long> calculateBirthDate() {
		List<Object[]> result = memberRepository.countBirthDate();
		Map<Integer, Long> birthResult = new HashMap<>();
		
		for(Object[] obj:result) {
			Integer month = (Integer) obj[0];
			Long count = (Long) obj[1];
			
			birthResult.put(month, count);
		}
		
		return birthResult;
	}

}
