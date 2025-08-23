package com.extra.proj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employee")

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Employee {

	@Id
	private int id;
	
	private String name, email;
	private int age;
}
