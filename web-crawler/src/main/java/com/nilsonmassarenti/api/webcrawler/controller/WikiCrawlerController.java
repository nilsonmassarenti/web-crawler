package com.nilsonmassarenti.api.webcrawler.controller;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nilsonmassarenti.api.webcrawler.AppStaticConfig;
import com.nilsonmassarenti.api.webcrawler.entity.Message;
import com.nilsonmassarenti.api.webcrawler.entity.Person;
import com.nilsonmassarenti.api.webcrawler.entity.PersonInformation;
import com.nilsonmassarenti.api.webcrawler.util.HttpRequest;

@RestController
@RequestMapping("/crawler/v1/wiki")
public class WikiCrawlerController {

	@Autowired
	private HttpRequest httpRequest;

	/**
	 * This method is a rest to get information from wikipedia
	 * 
	 * @param linkPerson
	 * @param language
	 * @return error or Person Information
	 */
	@RequestMapping(value = "/spouse/{linkPerson}/{language}", method = RequestMethod.GET)
	public ResponseEntity<?> getSpouse(@PathVariable("linkPerson") String linkPerson,
			@PathVariable("language") String language) {
		Message message = new Message();
		Person person = new Person();
		String link = "";
		switch (language) {
		case "en":
			link = AppStaticConfig.WIKIEN + "/wiki/" + linkPerson;
			break;
		default:
			message.setMessage("Language not found");
			break;
		}
		if (!link.equals("")) {
			String responseHtml = httpRequest.getHtml(link);
			if (responseHtml != null) {

				person.setName(getPersonDetail(responseHtml));
				if (person.getName() != null) {
					List<Person> children = getChildren(responseHtml);
					person.setChilden(children);
					Person spouse = getSpouseDetail(responseHtml);
					if (spouse != null) {
						person.setSpouse(spouse);
						String spouseHtml = httpRequest.getHtml(AppStaticConfig.WIKIEN + spouse.getLink());
						if (spouseHtml != null) {
							Person spousePerson = getSpouseDetail(responseHtml);
							if (spousePerson != null) {
								List<Person> childrenSpouse = getChildren(responseHtml);
								spouse.setChilden(childrenSpouse);
								spouse.setSpouse(spousePerson);
							} else {
								message.setMessage("Spouse not found person.");
							}
						} else {
							message.setMessage("Spouse link not found.");
						}
					} else {
						message.setMessage("Spouse not found.");
					}
				} else {
					message.setMessage("Person not found.");
				}

			} else {
				message.setMessage("Invalid Link");
			}
		}

		PersonInformation personInformation = new PersonInformation();
		if (message.getMessage() == null) {

			person.setLink("/wiki/" + linkPerson);
			if (person.getSpouse().getSpouse().getName().equals(person.getSpouse().getName())) {
				personInformation.setIsSpouse(true);
			} else {
				personInformation.setIsSpouse(true);
			}

			List<Person> sameChildren = new ArrayList<>();
			List<String> children = new ArrayList<>();

			if (person.getChilden() != null && person.getSpouse().getChilden() != null) {
				personInformation.setAllChildrenMatch(true);
				if (person.getChilden().size() != person.getSpouse().getChilden().size()) {
					personInformation.setAllChildrenMatch(false);
				} else {
					Boolean child;
					if (person.getChilden() != null) {
						for (Person personChildrenCompare : person.getChilden()) {
							child = false;
							for (Person spouseChildrenCompare : person.getSpouse().getChilden()) {
								if (personChildrenCompare.getName() != null
										&& spouseChildrenCompare.getName() != null) {
									if (personChildrenCompare.getName().equals(spouseChildrenCompare.getName())) {
										sameChildren.add(personChildrenCompare);
										child = true;
										children.add(personChildrenCompare.getName());
										break;
									}
								} else if (personChildrenCompare.getLink() != null
										&& spouseChildrenCompare.getLink() != null) {
									if (personChildrenCompare.getLink().equals(spouseChildrenCompare.getLink())) {
										sameChildren.add(personChildrenCompare);
										child = true;
										;
										children.add(personChildrenCompare.getName());
										break;
									}
								}
							}
							if (child == false) {
								personInformation.setAllChildrenMatch(false);
							}
						}
					}
				}
				personInformation.setChildren(children);
			}

			personInformation.setPerson(person);
			personInformation.setSpouse(person.getSpouse());

		}

		if (message.getMessage() == null) {
			return new ResponseEntity<String>(personInformation.getJson().toString(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Message>(message, HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * This method is responsible to get Person Details
	 * @param html
	 * @return String
	 */
	private String getPersonDetail(String html) {
		Document doc = Jsoup.parse(html);
		String name = doc.title();
		if (name == "") {
			return null;
		} else {
			return name.replace(" - Wikipedia", "");
		}
	}

	private Person getSpouseDetail(String html) {
		Integer initPos = html.indexOf("<tr>\n" + "<th scope=\"row\">Spouse(s)</th>");
		if (initPos == -1) {
			initPos = html.indexOf("<th scope=\"row\"><span class=\"nowrap\">Spouse(s)</span></th>");
		}
		if (initPos != -1) {
			Integer endPos = html.substring(initPos).indexOf("</tr>\n" + "<tr>");
			String responseSpouse = html.substring(initPos, initPos + endPos);
			// responseSpouse = responseSpouse.substring(responseSpouse.indexOf("<ul>"),
			// responseSpouse.indexOf("</ul>")+4);

			responseSpouse = responseSpouse.substring(responseSpouse.indexOf("<td>"),
					responseSpouse.indexOf("</td>") + 5);

			Document doc = Jsoup.parse(responseSpouse);
			Elements elements = doc.select("span");
			Person spouse = new Person();
			for (Element e : elements) {
				if (!e.toString().contains("<abbr title=\"divorced\"")
						&& !e.toString().contains("<abbr title=\"died\">")) {
					Document docE = Jsoup.parse(e.toString());
					Element link = docE.select("a").first();

					String name = link.attr("title");
					String linkSpouse = link.attr("href");
					spouse.setName(name);
					spouse.setLink(linkSpouse);
					break;
				}

			}

			if (spouse.getName() != null) {
				if (spouse.getName().equals("") || spouse.getLink().equals("")) {
					return null;
				} else {
					return spouse;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	/**
	 * This method is responsible to get Children from html generated by http request on wikipedia
	 * @param html
	 * @return List of persons
	 */
	private List<Person> getChildren(String html) {

		List<Person> listChildren = new ArrayList<Person>();

		Integer initPos = html.indexOf("<tr>\n" + "<th scope=\"row\">Children</th>");
		if (initPos != -1) {
			Integer endPos = html.substring(initPos).indexOf("</tr>\n" + "<tr>");
			String responseChildren = html.substring(initPos, initPos + endPos);
			responseChildren = responseChildren.substring(responseChildren.indexOf("<td>") + 4,
					responseChildren.indexOf("</td>"));
			String[] arrayChildren = responseChildren.split("<br />");

			for (String child : arrayChildren) {
				Person childPerson = new Person();
				if (child.indexOf("<a href=") >= 0) {
					Document doc = Jsoup.parse(child);
					Element link = doc.select("a").first();
					String name = link.attr("title");
					String linkChild = link.attr("href");
					childPerson.setName(name);
					childPerson.setLink(linkChild);

				} else {
					childPerson.setName(child);
				}
				listChildren.add(childPerson);
			}
		} else {
			return null;
		}
		return listChildren;
	}

}
