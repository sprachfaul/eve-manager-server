package de.sprachfaul.evemanager.server.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.troja.eve.esi.auth.CharacterInfo;
import net.troja.eve.esi.model.CharacterIndustryJobsResponse;
import net.troja.eve.esi.model.CharacterPlanetResponse;
import net.troja.eve.esi.model.CharacterPlanetsResponse;
import net.troja.eve.esi.model.CharacterPortraitResponse;

/**
 * Character mit Planeten
 */
public class CombinedCharacter {

	@JsonProperty("CharacterInfo")
	private CharacterInfo characterInfo;

	@JsonProperty("Planets")
	private List<CombinedPlanet> combinedPlanets = new ArrayList<>();
	
	@JsonProperty("IndustryJobs")
	private List<CharacterIndustryJobsResponse> industryJobs = new ArrayList<>();

	@JsonProperty("CharactersPortrait")
	private CharacterPortraitResponse charactersPortrait;

	public CharacterInfo getCharacterInfo() {
		return characterInfo;
	}

	public void setCharacterInfo(CharacterInfo characterInfo) {
		this.characterInfo = characterInfo;
	}

	public List<CombinedPlanet> getCombinedPlanets() {
		return combinedPlanets;
	}

	public void setCombinedPlanets(List<CombinedPlanet> combinedPlanets) {
		this.combinedPlanets = combinedPlanets;
	}
	
	public void addCombinedPlanet(CharacterPlanetsResponse planet, CharacterPlanetResponse planetLayout) {
		combinedPlanets.add(new CombinedPlanet(planet, planetLayout));
	}

	public List<CharacterIndustryJobsResponse> getIndustryJobs() {
		return industryJobs;
	}

	public void setIndustryJobs(List<CharacterIndustryJobsResponse> industryJobs) {
		this.industryJobs = industryJobs;
	}
	
	public void addIndustryJobs(List<CharacterIndustryJobsResponse> jobs) {
		this.industryJobs.addAll(jobs);
	}

	public void setCharactersPortrait(CharacterPortraitResponse charactersPortrait) {
		this.charactersPortrait = charactersPortrait;
	}

	public CharacterPortraitResponse getCharactersPortrait() {
		return charactersPortrait;
	}
}
