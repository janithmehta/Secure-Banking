package com.group06FALL2017.account.model;


import javax.persistence.*;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    private Long mId;
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    /**
     * Active/Inactive user flag
     */
    private String status;
    private String address;
    /**
     * SQL DATE
     */
    private String dob;
    /**
     * User's phone
     * TODO Add validation for 10 numbers
     */
    private String phone;
    
//    private Set<ExternalUser> mUserTypes;
    /**
     * Use this object to store the InternalUser/ExternalUser objects
     */
    private Object obj;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }
    public String getDOB() {
        return dob;
    }
    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

//    @ManyToMany
//    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//    public Set<ExternalUser> getUserTypes() {
//        return mUserTypes;
//    }
//
//    public void setRoles(ExternalUser userType) {
//        this.mUserTypes.add(userType);
//    }
}
