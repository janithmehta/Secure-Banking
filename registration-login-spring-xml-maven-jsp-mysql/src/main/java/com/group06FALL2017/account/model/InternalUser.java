package com.group06FALL2017.account.model;

import javax.persistence.*;
//import java.util.Set;

@Entity
@Table(name = "user_internal")
public class InternalUser {
    private String mId;
    private String mType;
    private String mDesignation;
//    private Set<User> users;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String UserType) {
        this.mType = UserType;
    }
    public String getDesignation() {
        return mDesignation;
    }

    public void setDesignation(String designation) {
        this.mDesignation = designation;
    }

    /*@ManyToMany(mappedBy = "UserType")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> UserTypes) {
        this.users = UserTypes;
    }
*/}
