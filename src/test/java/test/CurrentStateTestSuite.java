package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.cache.FillCacheFromEveAPI;
import test.database.InvTypesTest;
import test.logic.SortAltsByExtractorFinishingTest;

/**
 * Combi Suite, um die aktuellen Daten aus der API zu holen,
 * die InvTypes hinzuzufügen
 * und die aktuelle Liste der Aktivitäten aus stdout ausgeben.
 */
@RunWith(Suite.class)				
@Suite.SuiteClasses({				
  FillCacheFromEveAPI.class,
  InvTypesTest.class,
  SortAltsByExtractorFinishingTest.class,  			
})	
public class CurrentStateTestSuite {

}
