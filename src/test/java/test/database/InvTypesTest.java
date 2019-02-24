package test.database;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Optional;

import javax.net.ssl.HandshakeCompletedListener;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import de.sprachfaul.evemanager.server.Application;
import de.sprachfaul.evemanager.server.cache.CacheDAO;
import de.sprachfaul.evemanager.server.model.InvTypes;
import de.sprachfaul.evemanager.server.repositories.InvTypesRepository;
import test.ISecrets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InvTypesTest {

	@Autowired
	InvTypesRepository invTypes;
	
	@Autowired
	CacheDAO cacheDAO;
	
	@Test
	public void emil() throws Exception {
		
		Optional<InvTypes> tritanium = invTypes.findById(34);
		System.out.println(tritanium.get().toString());
	}
	
	@Test
	public void fillUsedTypeCache() throws Exception {
		
		List<Integer> usedTypeIds = cacheDAO.readTypIdList();
		Iterable<InvTypes> findAllById = invTypes.findAllById(usedTypeIds);
		cacheDAO.storeUsedInvTypes(findAllById);
	}
	
	@Test
	public void zReadUsedTypeCache() throws Exception {
		assumeThat("no alt configured", ISecrets.ANY_REFRESH_TOKEN, not(nullValue()));
		List<InvTypes> readValue = cacheDAO.readUsedInvTypes();
		assertThat(readValue, hasSize(greaterThan(0)));
	}
}
