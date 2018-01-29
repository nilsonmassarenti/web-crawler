package com.nilsonmassarenti.api.webcrawler.entity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PersonInformation {
	private Person person;
	private Person spouse;
	private Boolean isSpouse;
	private List<String> children;
	private Boolean allChildrenMatch;

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

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}

	public Boolean getAllChildrenMatch() {
		return allChildrenMatch;
	}

	public void setAllChildrenMatch(Boolean allChildrenMatch) {
		this.allChildrenMatch = allChildrenMatch;
	}

	@JsonIgnore
	public JSONObject getJson() {

		JSONObject json = new JSONObject();

		
		try {
			JSONObject person = new JSONObject();
			person.put("name", this.person.getName());
			person.put("link", this.person.getLink());
			json.put("person", person);

			JSONObject spouse = new JSONObject();
			spouse.put("name", this.spouse.getName());
			spouse.put("link", this.spouse.getLink());
			json.put("spouse", spouse);

			json.put("isSpouse", this.isSpouse);
			json.put("children", this.children);
			json.put("allChildrenMatch", this.allChildrenMatch);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return json;
	}

}
