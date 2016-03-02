package cz.inited.ofy.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Entity
public class User {

	@Id
    String username;
    String password;
    
    String firstName;
    String lastName;
    Date dateOfBirth;
    String email;
    String phone;
    String friendPhone;
    
    String street;
    String houseNr;
    String city;
    String postcode;
    String county;
    String country;
    
    Date lastLogged;
    Date agreement1;
    Date agreement2;
    Date agreement3;

    String PNkey;
    Long credit;

    String ticket;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFriendPhone() {
		return friendPhone;
	}

	public void setFriendPhone(String friendPhone) {
		this.friendPhone = friendPhone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNr() {
		return houseNr;
	}

	public void setHouseNr(String houseNr) {
		this.houseNr = houseNr;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getLastLogged() {
		return lastLogged;
	}

	public void setLastLogged(Date lastLogged) {
		this.lastLogged = lastLogged;
	}

	public Date getAgreement1() {
		return agreement1;
	}

	public void setAgreement1(Date agreement1) {
		this.agreement1 = agreement1;
	}

	public Date getAgreement2() {
		return agreement2;
	}

	public void setAgreement2(Date agreement2) {
		this.agreement2 = agreement2;
	}

	public Date getAgreement3() {
		return agreement3;
	}

	public void setAgreement3(Date agreement3) {
		this.agreement3 = agreement3;
	}

	public String getPNkey() {
		return PNkey;
	}

	public void setPNkey(String pNkey) {
		PNkey = pNkey;
	}

	public Long getCredit() {
		return credit;
	}

	public void setCredit(Long credit) {
		this.credit = credit;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

    

}
