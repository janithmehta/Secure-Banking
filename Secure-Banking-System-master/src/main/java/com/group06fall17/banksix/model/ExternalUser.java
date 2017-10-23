package com.group06fall17.banksix.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.group06fall17.banksix.interceptor.ILogs;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import java.beans.Transient;
import java.sql.Blob;
import java.util.List;

/**
 * @author Saurabh
 *
 */

@Entity
@Table(name = "external_user")
@DynamicUpdate
@SelectBeforeUpdate 
public class ExternalUser implements ILogs{	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "userid", nullable = false)
	private int userid;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	/*@Column(name = "middlename")
	private String middlename;
	
	@Column(name = "lastname", nullable = false)
	private String lastname;*/
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email")
	private Users email;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	/*@Column(name = "addressline2")
	private String addressline2;
	
	@Column(name = "city", nullable = false)
	private String city;
	
	@Column(name = "state", nullable = false)
	private String state;
	
	@Column(name = "zipcode", nullable = false, columnDefinition = "char")	
	private String zipcode;*/
	
	@Column(name = "userType", nullable = false)
	private String userType;
	
	@Column(name = "publickey", nullable = false)
	private Blob publickey;
	
	@Column(name = "ssn", nullable = false)
	private String ssn;
	
	@Column(name = "organisationName")
	private String organisationName;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="userid")
     public List<BankAccount> account;
	
	public List<BankAccount> getAccount() {
		return account;
	}


	public void setAccount(List<BankAccount> account) {
		this.account = account;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
*/
	public Users getEmail() {
		return email;
	}

	public void setEmail(Users email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

/*	public String getAddressline2() {
		return addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
*/
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Blob getPublickey() {
		return publickey;
	}

	public void setPublickey(Blob publickey) {
		this.publickey = publickey;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String name) {
		this.organisationName = name;
	}
	
	@Transient
	@Override
	public Long getId() {
		return Long.valueOf(this.userid);
	}

	@Transient
	@Override
	public String getLogDetail() {
		StringBuilder logString = new StringBuilder();
		
		logString.append(" external_user ")
		.append(" userid :" ).append(userid)
		.append(" name : ").append(name)
		/*.append(" middlename : ").append(middlename)
		.append(" lastname : ").append(lastname)*/
		.append(" email : ").append(email.getUsername())
		.append(" address :").append(address)
		/*.append(" addressline2 : ").append(addressline2)
		.append(" city : ").append(city)
		.append(" state : ").append(state)
		.append(" zipcode :").append(zipcode)*/
		.append(" userType :").append(userType);

		return logString.toString();
	}
}
