package de.sprachfaul.evemanager.server.cache;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.sprachfaul.evemanager.server.model.CombinedCharacter;
import de.sprachfaul.evemanager.server.model.InvTypes;
import net.troja.eve.esi.JSON;
import net.troja.eve.esi.auth.CharacterInfo;

@Component
public class CacheDAO {

	private JSON json;
	public static final File CACHE_PATH = new File("target", "cache");
	
	public CacheDAO() {
		json = new JSON();
		// Wir können die Scopes nicht serialisieren..
        json.getContext(Object.class).disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
	}
	
	public List<CombinedCharacter> readAllCombinedCharacters() throws IOException {
		FilenameFilter cachedFilesFilter = new SuffixFileFilter("CombinedCharacter.json");
		List<CombinedCharacter> results = new ArrayList<>();
		File[] cachedFiles = CACHE_PATH.listFiles(cachedFilesFilter);
		for (File file : cachedFiles) {
	        CombinedCharacter readValue = json.getContext(CharacterInfo.class).readerFor(CombinedCharacter.class).readValue(file);
	        results.add(readValue);
		}
		
		return results;
	}

	public void clearCache() {
		CACHE_PATH.delete();
	}

	public void setupCache() {
		CACHE_PATH.mkdirs();
	}

	public void store(String characterName, CombinedCharacter combinedCharacter) throws IOException {
		// Wir können die Scopes nicht serialisieren - deswegen löschen wir diese
		combinedCharacter.getCharacterInfo().setScopes(null);

		File cacheFile = new File(CACHE_PATH, characterName + ".CombinedCharacter.json");
		json.getContext(CombinedCharacter.class).writer().writeValue(cacheFile, combinedCharacter);
	}

	public String getCacheBasePath() {
		return CACHE_PATH.toString();
	}

	public void storeTypIdList(List<Integer> typeIdList) throws IOException {
		File cacheFile = new File(CACHE_PATH, "usedtypeids.json");
		json.getContext(Integer.class).writer().writeValue(cacheFile, typeIdList);
	}

	public List<Integer> readTypIdList() throws IOException {
		File cacheFile = new File(CACHE_PATH, "usedtypeids.json");
		Integer[] readValue = json.getContext(Integer.class).readerFor(Integer[].class).readValue(cacheFile);
		return Arrays.asList(readValue);
	}

	public void storeUsedInvTypes(Iterable<InvTypes> findAllById) throws IOException {
		File cacheFile = new File(CACHE_PATH, "usedtypes.json");
		json.getContext(InvTypes.class).writer().writeValue(cacheFile, findAllById);
	}

	public List<InvTypes> readUsedInvTypes() throws IOException {
		File cacheFile = new File(CACHE_PATH, "usedtypes.json");
		InvTypes[] readValue = json.getContext(InvTypes.class).readerFor(InvTypes[].class).readValue(cacheFile);
		return Arrays.asList(readValue);
	}
}
