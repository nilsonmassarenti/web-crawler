package com.nilsonmassarenti.api.webcrawler.entity;

import java.util.List;

public class Person {
	private String link;
	private String name;
	private Person spouse;
	private List<Person> childen;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Person getSpouse() {
		return spouse;
	}
	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}
	public List<Person> getChilden() {
		return childen;
	}
	public void setChilden(List<Person> childen) {
		this.childen = childen;
	}
	
	
}
