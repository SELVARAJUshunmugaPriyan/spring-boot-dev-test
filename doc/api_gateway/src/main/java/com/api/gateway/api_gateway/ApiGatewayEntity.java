package com.api.gateway.api_gateway;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="employee")
public class ApiGatewayEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int employeeId;

    @Transient
    @SuppressWarnings("unused")
    private boolean sex;

    public void setSex(boolean sex) {
        this.sex = sex;
        if (sex)
            setGender("Male");
        else
            setGender("Female");
    }

    public boolean getSex() {
        return getGender().equals("Male");
    }

    private String name, dept, email, gender;
    private int age, salary;
    private Integer managerId;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String getGender() {
        return gender;
    }

    private void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return employeeId + " " + name + " " + dept + " " + email + " " + 
               gender + " " + age + " " + salary + " " + managerId;
    }
}
