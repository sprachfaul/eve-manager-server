package de.sprachfaul.evemanager.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.troja.eve.esi.model.CharacterPlanetResponse;
import net.troja.eve.esi.model.CharacterPlanetsResponse;

/**
 * Ein Planet besteht aus Planet + Layout
 */
public class CombinedPlanet {

	/** Info Zum Planeten */
	@JsonProperty("PlanetInfo")
	private CharacterPlanetsResponse planet;

	@JsonProperty("PlanetLayout")
	private CharacterPlanetResponse planetLayout;

	public CombinedPlanet() {
	}

	public CombinedPlanet(CharacterPlanetsResponse planet, CharacterPlanetResponse planetLayout) {
		this.planet = planet;
		this.planetLayout = planetLayout;
	}

	public CharacterPlanetsResponse getPlanet() {
		return planet;
	}

	public void setPlanet(CharacterPlanetsResponse planet) {
		this.planet = planet;
	}

	public CharacterPlanetResponse getPlanetLayout() {
		return planetLayout;
	}

	public void setPlanetLayout(CharacterPlanetResponse planetLayout) {
		this.planetLayout = planetLayout;
	}
}
