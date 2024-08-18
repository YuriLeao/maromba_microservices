package com.api.maromba.user.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_authorization")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 2)
	private String id;
	@Column(nullable = false, unique = true, length = 30)
	private String description;

}
