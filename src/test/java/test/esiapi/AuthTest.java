package test.esiapi;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assume.assumeThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.SerializationFeature;

import de.sprachfaul.evemanager.server.model.CombinedCharacter;
import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.JSON;
import net.troja.eve.esi.api.CharacterApi;
import net.troja.eve.esi.api.IndustryApi;
import net.troja.eve.esi.api.MarketApi;
import net.troja.eve.esi.api.PlanetaryInteractionApi;
import net.troja.eve.esi.api.SsoApi;
import net.troja.eve.esi.auth.CharacterInfo;
import net.troja.eve.esi.auth.OAuth;
import net.troja.eve.esi.model.CharacterIndustryJobsResponse;
import net.troja.eve.esi.model.CharacterPlanetResponse;
import net.troja.eve.esi.model.CharacterPlanetsResponse;
import net.troja.eve.esi.model.CharacterPortraitResponse;
import net.troja.eve.esi.model.MarketOrdersResponse;
import test.ISecrets;

/**
 * Auth Test..
 */
public class AuthTest implements ISecrets {
	
	private static final String NO_IFNONEMATCH = null;
	public static String REFRESH_TOKEN = ISecrets.ANY_REFRESH_TOKEN;
	
	@Test
	public void testName() throws Exception {

		assumeThat("no alt configured", ISecrets.ANY_REFRESH_TOKEN, not(nullValue()));
		
        final ApiClient client = new ApiClient();
        final OAuth auth = (OAuth) client.getAuthentication("evesso");
        auth.setClientId(SSO_CLIENT_ID);
        auth.setClientSecret(SSO_CLIENT_SECRET);
        auth.setRefreshToken(REFRESH_TOKEN);
        
        final SsoApi api = new SsoApi(client);
        final CharacterInfo info = api.getCharacterInfo();

        String characterName = info.getCharacterName();
        int characterId = info.getCharacterId();
        System.out.printf("Hello %s - you have id %s\n", characterName, characterId);
        
        final CharacterApi charApi = new CharacterApi(client);
        CharacterPortraitResponse charactersPortrait = charApi.getCharactersCharacterIdPortrait(characterId, DATASOURCE, NO_IFNONEMATCH);
        System.out.println(charactersPortrait);
        
//        List<MarketOrdersResponse> marketsRegionIdOrders = marketApi.getMarketsRegionIdOrders("all", 10000002, DATASOURCE, null, 1, 34);
//        marketsRegionIdOrders.stream().forEach(item -> System.out.println(item));
	}
}
