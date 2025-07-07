package com.zidio.connect.entities;

import org.springframework.security.core.GrantedAuthority;

import com.zidio.connect.enums.AuthorityTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class UserAuthority implements GrantedAuthority {

	private static final long serialVersionUID = -6223080355793128944L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String authority;

	public UserAuthority(AuthorityTypeEnum authorityEnum) {
		this.authority = "ROLE_" + authorityEnum.toString().toUpperCase();
	}

	public UserAuthority(Long id, AuthorityTypeEnum authorityEnum) {
		this(authorityEnum);
		this.id = id;
	}

	public UserAuthority(String authority) {
		this.setAuthority(authority);
	}

	public UserAuthority(Long id, String authority) {
		this.id = id;
		this.setAuthority(authority);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setAuthority(String authority) {
		if (authority.isBlank() || authority.contains(" ")) {
			throw new IllegalArgumentException("Role cannot be null and cannot contains spaces.");
		}
		try {
			if (authority.startsWith("ROLE_")) {
				AuthorityTypeEnum.valueOf(authority.substring(5));
				this.authority = authority;
			} else {
				AuthorityTypeEnum.valueOf(authority); // throws an IllegalArgumentException if no such constant exists.
				this.authority = "ROLE_" + authority;
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid authority type: " + authority);
		}
	}

}