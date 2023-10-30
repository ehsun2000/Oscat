package com.oscat.cinema.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import com.oscat.cinema.dto.MemberAdminDTO;
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
		List<Member> genderMember = memberRepository.findAll();

		long totalGender = genderMember.size();
		// stream().filter() 過濾list內的資料，count()計算比數
		long maleCount = genderMember.stream().filter(member -> "man".equals(member.getGender())).count();
		long femaleCount = genderMember.stream().filter(member -> "female".equals(member.getGender())).count();
		long otherCount = genderMember.stream().filter(member -> "other".equals(member.getGender())).count();
		
		Double maleProportion = (double) maleCount/totalGender;
		Double femaleProportion = (double) femaleCount/totalGender;
		Double otherProportion = (double) otherCount/totalGender;

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
	    long youngAge = ageMember.stream()
	            .filter(member -> calculateAge(member.getBirthDate(), currentYear) < 18)
	            .count();
	    long teenagerAge = ageMember.stream()
	            .filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 18 && calculateAge(member.getBirthDate(), currentYear) <= 25)
	            .count();
	    long adultAge = ageMember.stream()
	            .filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 26 && calculateAge(member.getBirthDate(), currentYear) <= 35)
	            .count();
	    long middleageAge = ageMember.stream()
	            .filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 36 && calculateAge(member.getBirthDate(), currentYear) <= 50)
	            .count();
	    long elderlyAge = ageMember.stream()
	            .filter(member -> calculateAge(member.getBirthDate(), currentYear) >= 51 && calculateAge(member.getBirthDate(), currentYear) <= 65)
	            .count();
	    long oldAge = ageMember.stream()
	            .filter(member -> calculateAge(member.getBirthDate(), currentYear) > 65)
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
	//計算年齡差
	private int calculateAge(Date birthDate, int currentYear) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(birthDate);
	    int birthYear = calendar.get(Calendar.YEAR);
	    return currentYear - birthYear;
	}

	// 依加入時間區段(一周/一個月/半年)人數比
	public JoinDateProportionDTO calculateJoinDate() {
		List<Member> joinDateMember= memberRepository.findAll();
		
		Calendar calender1 = Calendar.getInstance();
		calender1.add(Calendar.DAY_OF_YEAR, -7);
		Date weekCount = calender1.getTime();
		
		Calendar calender2 = Calendar.getInstance();
		calender2.add(Calendar.MONTH, -1);
		Date mouthCount = calender2.getTime();
		
		Calendar calender3 = Calendar.getInstance();
		calender3.add(Calendar.MONTH, -6);
		Date yearCount = calender3.getTime();
		
		long totalNumber = joinDateMember.size();
		long weekCountNumber = joinDateMember.stream()
				.filter(member -> member.getJoinDate().after(weekCount) && member.getJoinDate().before(Calendar.getInstance().getTime())).count();
		long monthCountNumber = joinDateMember.stream()
				.filter(member -> member.getJoinDate().after(mouthCount) && member.getJoinDate().before(Calendar.getInstance().getTime())).count();
		long yearCountNumber = joinDateMember.stream()
				.filter(member -> member.getJoinDate().after(yearCount) && member.getJoinDate().before(Calendar.getInstance().getTime())).count();
		
		JoinDateProportionDTO joinDateProportion = new JoinDateProportionDTO();
		joinDateProportion.setTotalNumber(totalNumber);
		joinDateProportion.setWeekCountNumber(weekCountNumber);
		joinDateProportion.setMonthCountNumber(monthCountNumber);
		joinDateProportion.setYearCountNumber(yearCountNumber);;
		
		return joinDateProportion;
		
	}
}
