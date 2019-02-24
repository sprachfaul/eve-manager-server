package test.cache;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.SerializationFeature;

import de.sprachfaul.evemanager.server.cache.CacheDAO;
import de.sprachfaul.evemanager.server.model.CombinedCharacter;
import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.JSON;
import net.troja.eve.esi.api.CharacterApi;
import net.troja.eve.esi.api.IndustryApi;
import net.troja.eve.esi.api.PlanetaryInteractionApi;
import net.troja.eve.esi.api.SsoApi;
import net.troja.eve.esi.auth.CharacterInfo;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.model.CharacterIndustryJobsResponse;
import net.troja.eve.esi.model.CharacterPlanetResponse;
import net.troja.eve.esi.model.CharacterPlanetsResponse;
import net.troja.eve.esi.model.CharacterPortraitResponse;
import test.ISecrets;

/**
 * Befüllt den Cache (JSON Dateien) frisch aus der Eve API.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FillCacheFromEveAPI implements ISecrets {

	private static final String NO_IFNONEMATCH = null;
	private static final String NO_TOKEN = null;
	
	@Test
//	@Ignore("SHA - wollen wir erstmal nicht")
	public void rebuildCache() throws Exception {

		CacheDAO cacheDAO = new CacheDAO();
		cacheDAO.clearCache();
		cacheDAO.setupCache();
		
        System.out.println("Reading " + ALL_REFRESH_TOKENS.length + " characters from api and store them to " + cacheDAO.getCacheBasePath());
        for (String refreshToken : ALL_REFRESH_TOKENS) {
        	if (refreshToken == null) {
        		System.out.println("A refreshtoken is null - change that in your ISecrets !");
        		continue;
        	}
        	int retryCount = 3;
        	boolean success = false;
        	while (retryCount > 0 && ! success) {
	        	try {
		        	CombinedCharacter combinedCharacter = readFromAPI(refreshToken);
		        	String characterName = combinedCharacter.getCharacterInfo().getCharacterName();
		        	System.out.println(" + " + characterName + " read from api. Store it to cache.");
		        	cacheDAO.store(characterName, combinedCharacter);
		        	success = true;
	        	} catch (Exception e) {
	        		System.out.printf(" ! Error (%s) during retrieving data - try again %s times.\n", e.toString(), retryCount);
	        		--retryCount;
	        	}
        	}
        }
	}
	
	private void addUsedTypeIds(CombinedCharacter combinedCharacter, List<Integer> typeIdList) {
		combinedCharacter.getIndustryJobs().stream().forEach(job -> {
			addToListIfNotNull(job.getBlueprintTypeId(), typeIdList);
			addToListIfNotNull(job.getProductTypeId(), typeIdList);
		});
		
		combinedCharacter.getCombinedPlanets().stream().forEach(combinedPlanet -> {
			combinedPlanet.getPlanetLayout().getPins().forEach(pin -> {
				addToListIfNotNull(pin.getTypeId(), typeIdList);
				if (pin.getExtractorDetails()!= null)
					addToListIfNotNull(pin.getExtractorDetails().getProductTypeId(), typeIdList);
				if (pin.getContents() != null)
					pin.getContents().forEach(content -> {
						addToListIfNotNull(content.getTypeId(), typeIdList);
					});
			});
		});
	}

	private void addToListIfNotNull(Integer typeId, List<Integer> typeIdList) {
		if (typeId != null)
			if (!typeIdList.contains(typeId))
				typeIdList.add(typeId);
	}

	/**
	 * Die Daten von der API muessen auch wieder gelesen werden koennen..
	 */
	@Test
	public void zCheckReadCache() throws Exception {
		List<Integer> typeIdList = new ArrayList<>();
		CacheDAO cacheDAO = new CacheDAO();
		List<CombinedCharacter> characters = cacheDAO.readAllCombinedCharacters();
		for (CombinedCharacter readValue : characters) {
        	addUsedTypeIds(readValue, typeIdList);
	        System.out.printf("%s has %s planets and %s jobs.\n", readValue.getCharacterInfo().getCharacterName(), readValue.getCombinedPlanets().size(), readValue.getIndustryJobs().size());
		}
        cacheDAO.storeTypIdList(typeIdList);
	}
	
	public CombinedCharacter readFromAPI(String refreshToken) throws ApiException {
        final ApiClient client = new ApiClient();
        final OAuth auth = (OAuth) client.getAuthentication("evesso");
        auth.setClientId(SSO_CLIENT_ID);
        auth.setClientSecret(SSO_CLIENT_SECRET);
        auth.setRefreshToken(refreshToken);
        
        final SsoApi api = new SsoApi(client);
        final CharacterInfo info = api.getCharacterInfo();
        int characterId = info.getCharacterId();
        CombinedCharacter combinedCharacter = new CombinedCharacter();
        combinedCharacter.setCharacterInfo(info);
        
        final CharacterApi charApi = new CharacterApi(client);
        CharacterPortraitResponse charactersPortrait = charApi.getCharactersCharacterIdPortrait(characterId, DATASOURCE, NO_IFNONEMATCH);
        combinedCharacter.setCharactersPortrait(charactersPortrait);
        
        final IndustryApi industryApi = new IndustryApi(client);
        List<CharacterIndustryJobsResponse> industryJobs = industryApi.getCharactersCharacterIdIndustryJobs(characterId, DATASOURCE, NO_IFNONEMATCH, false, NO_TOKEN);
        combinedCharacter.addIndustryJobs(industryJobs);
        
        final PlanetaryInteractionApi piApi = new PlanetaryInteractionApi(client);
        List<CharacterPlanetsResponse> planets = piApi.getCharactersCharacterIdPlanets(characterId, DATASOURCE, NO_IFNONEMATCH, NO_TOKEN);
        
        planets.forEach(planet -> {
        	StringBuffer b = new StringBuffer();
        	b.append(planet.getPlanetType()).append(" - ");
        	Integer planetId = planet.getPlanetId();
			try {
				CharacterPlanetResponse planetLayout = piApi.getCharactersCharacterIdPlanetsPlanetId(characterId, planetId, DATASOURCE, NO_IFNONEMATCH, NO_TOKEN);
				combinedCharacter.addCombinedPlanet(planet, planetLayout);
			} catch (ApiException e) {
				throw new RuntimeException(e);
			}
        });
        return combinedCharacter;
	}
}
