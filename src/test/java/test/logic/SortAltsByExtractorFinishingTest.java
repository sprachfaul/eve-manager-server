package test.logic;

import static org.junit.Assert.*;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import de.sprachfaul.evemanager.server.cache.CacheDAO;
import de.sprachfaul.evemanager.server.logic.ExtractorYieldCalculator;
import de.sprachfaul.evemanager.server.model.CombinedCharacter;
import de.sprachfaul.evemanager.server.model.CombinedPlanet;
import net.troja.eve.esi.model.CharacterIndustryJobsResponse;
import net.troja.eve.esi.model.CharacterPlanetsResponse;
import net.troja.eve.esi.model.PlanetExtractorDetails;
import net.troja.eve.esi.model.PlanetPin;

/**
 * Zeigt die Character sortiert nach dem auslaufen des "jüngsten" Extractors an.
 */
public class SortAltsByExtractorFinishingTest {

	@Test
	public void checkIt() throws Exception {
		List<CombinedCharacter> characters = new CacheDAO().readAllCombinedCharacters();
		
		Map<CombinedCharacter, OffsetDateTime> characterLowestExpiryPi = new LinkedHashMap<>();
		
		Map<CombinedCharacter, OffsetDateTime> characterLowestExpiryIndustry = new LinkedHashMap<>();

		for (CombinedCharacter character : characters) {
			System.out.printf("\nCharacter: %s\n", character.getCharacterInfo().getCharacterName());
			OffsetDateTime lowestExpiry = OffsetDateTime.MAX;
			List<CombinedPlanet> combinedPlanets = character.getCombinedPlanets();
			for (CombinedPlanet combinedPlanet : combinedPlanets) {
				for (PlanetPin planetPin : combinedPlanet.getPlanetLayout().getPins()) {
					CharacterPlanetsResponse planet = combinedPlanet.getPlanet();
					PlanetExtractorDetails extractorDetails = planetPin.getExtractorDetails();
					if (extractorDetails != null) {
						int[] yield = ExtractorYieldCalculator.calculateExtractorYield(extractorDetails.getCycleTime(), extractorDetails.getQtyPerCycle(), 3 + 24*4);
						int sumYield = IntStream.of(yield).sum();
						OffsetDateTime expiryTime = planetPin.getExpiryTime();
						if (lowestExpiry.compareTo(expiryTime) > 0)
							lowestExpiry = expiryTime;
						System.out.printf("+ %s extractor for %s ends at %s and produce %s units\n", planet.getPlanetType(), extractorDetails.getProductTypeId(), expiryTime, sumYield);
					}
					if (planetPin.getFactoryDetails() != null)
						if (planetPin.getFactoryDetails().getSchematicId() == 86)
							System.out.println("*** Test Cultures ***");
				}
			}
			characterLowestExpiryPi.put(character, lowestExpiry);
			
			if (character.getIndustryJobs().size() > 0) {
				lowestExpiry = OffsetDateTime.MAX;
				for (CharacterIndustryJobsResponse job : character.getIndustryJobs()) {
					OffsetDateTime expiryTime = job.getEndDate();
					if (lowestExpiry.compareTo(expiryTime) > 0)
						lowestExpiry = expiryTime;
				};
				characterLowestExpiryIndustry.put(character, lowestExpiry);
			}
		}
		
		System.out.println("\nPI");
		characterLowestExpiryPi.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> System.out.printf("%s => %s\n", entry.getKey().getCharacterInfo().getCharacterName(), entry.getValue()));
		
		System.out.println("\nIndustry");
		characterLowestExpiryIndustry.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> System.out.printf("%s => %s\n", entry.getKey().getCharacterInfo().getCharacterName(), entry.getValue()));

	}
}
