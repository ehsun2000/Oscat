package com.oscat.cinema.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "uniqueidentifier", name = "member_id")
	private UUID memberId;

	@Column(name = "member_name", nullable = false, length = 255)
	@NotBlank(message = "姓名不能為空、")
	@Pattern(regexp = "^[A-Za-z\\u4e00-\\u9fa5]+$")
	private String memberName;

	@Column(name = "email", unique = true, length = 255)
	@NotBlank(message = "信箱不能為空、")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "信箱格式不正確、")
	private String email;

	@Column(name = "password", nullable = false, length = 255)
	@NotBlank(message = "密碼不能為空、")
	@Size(min = 8, message = "密碼不可少於8位、")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "密碼須包含一英跟一數、")
	private String password;

	@Column(name = "phone", length = 20)
	@NotBlank(message = "手機不能為空、")
	@Size(min = 8, message = "手機不可少於9位、")
	@Pattern(regexp = "^(\\d{2,3}-?|\\(\\d{2,3}\\))\\d{3,4}-?\\d{4}|09\\d{2}(\\d{6}|-\\d{3}-\\d{3})$", message = "手機格式不正確、")
	private String phone;

	@Column(name = "gender", length = 50)
	@NotBlank(message = "性別不能為空、")
	@Pattern(regexp = "^(man|female|other)$", message = "性別格式不正確、")
	private String gender;

	@Column(name = "birth_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past(message = "生日不能為未來日期")
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@Column(name = "join_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinDate;

	@JsonManagedReference(value = "member-order")
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<TransOrder> transOrders;

	@PrePersist
	public void onCreate() {
		if (joinDate == null) {
			joinDate = new Date();
		}
	}

	public void setBirthDateFormat(String dateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date parseDate = dateFormat.parse(dateString);
		this.birthDate = parseDate;
	}

	public void setBirthDateFromString(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.birthDate = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			this.birthDate = null;
		}
	}

	public String parseToString(Date birthday) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(birthday);
	}
}
