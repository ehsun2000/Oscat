package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MemberAdminRepository;
import com.oscat.cinema.entity.Member;

import jakarta.transaction.Transactional;

@Service
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

    // 根據name查找會員
    public Optional<Member> findMemberByName(String name) {
    	return memberRepository.findMemberByNameLike(name);    
    }
    
    // 根據email查找會員
    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }   
    
    // 根據id查找會員
    public Optional<Member> findMemberById(UUID memberId) {
		return memberRepository.findById(memberId);
    }
    
    // 關鍵字搜尋 byEmail
    public List<Member> findKeywordByEmail(String email){
    	return memberRepository.findMemberByEmailLike(email);
    }
    

    // 獲取所有會員列表
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 根據email刪除會員
    public String deleteMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            memberRepository.deleteByEmail(email);
            return "刪除成功";
        } else {
            return "找不到該會員";
        }
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

}
