package com.nilsonmassarenti.api.webcrawler.entity;

import java.util.List;

public class PersonInformation {
	private Person person;
	private Person spouse;
	private Boolean isSpouse;
	private List<Person> sameChildren;
	private List<Person> personChildren;
	private List<Person> spouseChildren;
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Person getSpouse() {
		return spouse;
	}
	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}
	public Boolean getIsSpouse() {
		return isSpouse;
	}
	public void setIsSpouse(Boolean isSpouse) {
		this.isSpouse = isSpouse;
	}
	public List<Person> getSameChildren() {
		return sameChildren;
	}
	public void setSameChildren(List<Person> sameChildren) {
		this.sameChildren = sameChildren;
	}
	public List<Person> getPersonChildren() {
		return personChildren;
	}
	public void setPersonChildren(List<Person> personChildren) {
		this.personChildren = personChildren;
	}
	public List<Person> getSpouseChildren() {
		return spouseChildren;
	}
	public void setSpouseChildren(List<Person> spouseChildren) {
		this.spouseChildren = spouseChildren;
	}
	
}
