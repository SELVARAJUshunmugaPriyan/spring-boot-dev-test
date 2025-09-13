package com.api.gateway.api_gateway;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="employee")
public class ApiGatewayEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer employeeId;

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

    public ApiGatewayEntity() {}
    
    public ApiGatewayEntity(String name, String dept, String email, int age,
        int salary, Integer managerId, boolean sex) {
            this.employeeId = null;
            this.name = name;
            this.dept = dept;
            this.email= email;
            this.age = age;
            this.salary = salary;
            this.managerId = managerId;
            this.sex = sex;
        }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="managerId")
    @SuppressWarnings("unused")
    private ApiGatewayEntity manager;

    @OneToMany(mappedBy="manager", cascade=CascadeType.ALL, orphanRemoval=true)
    private Set<ApiGatewayEntity> directReports = new HashSet<>();

    public Set<ApiGatewayEntity> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(Set<ApiGatewayEntity> directReports) {
        this.directReports = directReports;
    }

     public void setManager(ApiGatewayEntity manager) {
        if (this.manager != null) {
            this.manager.getDirectReports().remove(this);
        }
        this.manager = manager;
        if (manager != null) {
            manager.getDirectReports().add(this);
        }
    }

    public void addDirectReport(ApiGatewayEntity employee) {
        directReports.add(employee);
        employee.setManager(this);
    }

    public void removeDirectReport(ApiGatewayEntity employee) {
        directReports.remove(employee);
        employee.setManager(null);
    }

    public boolean getSex() {
        return getGender().equals("Male");
    }

    private String name, dept, email, gender;
    private int age, salary;

    @Column(name="managerId", insertable=false, updatable=false)
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

    public Integer getmanagerId() {
        return managerId;
    }

    public void setmanagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return employeeId + " " + name + " " + dept + " " + email + " " + 
               gender + " " + age + " " + salary + " " + managerId;
    }
}
