package test;

import org.junit.Before;

/**
 * Hier stehen viele Sicherheitsrelevante Dinge
 * 
 * Step 1 - register Client at https://developers.eveonline.com/applications/create (you need a eve online account).
 * Step 2 - execute net.troja.eve.esi.api.auth.SsoAuthTest with your Alt to get a Refresh Token. paste the refreshtoken to ANY_REFRESH_TOKEN (or create Name Refresh Tokens and add them to ALL_REFRESH_TOKENS).
 * Step 3 - have fun :)
 */
public interface ISecrets {
	
	public static final String DATASOURCE = "tranquility";
	public static final String SSO_CLIENT_ID = "";
	public static final String SSO_CLIENT_SECRET = "";
	public static final String SSO_CALLBACK_URL = "http://localhost:3000/eve-sso-callback";

	// die Tokens werden von net.troja.eve.esi.api.auth.SsoAuthTest erstellt.
	
	// Ein einzelner Refreshtoken
	public static final String ANY_REFRESH_TOKEN = null;
	
	// Liste aller Refreshtokens
	public static String[] ALL_REFRESH_TOKENS = {ANY_REFRESH_TOKEN};
	
	@Before
	default public void setup() {
		System.setProperty("SSO_CLIENT_ID", SSO_CLIENT_ID);
		System.setProperty("SSO_CLIENT_SECRET", SSO_CLIENT_SECRET);
		System.setProperty("SSO_CALLBACK_URL", SSO_CALLBACK_URL);
	}
}
