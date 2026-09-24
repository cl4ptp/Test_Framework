package utilities;

import static java.lang.Boolean.parseBoolean;

/**
 * Global constant storage.
 * <p></p>
 * Constants from this class are related to:
 * <p> - sandbox information (e.g. name) </p>
 * <p> - access credentials (e.g. username/password for SF) </p>
 * <p> - global toggles/keys for turning on/off test features (e.g. turn on/off dynamic account generation) </p>
 * <p> - URLs for direct access to main SF pages (e.g. login, logout, Visualforce, etc...)</p>
 * <p> etc... </p>
 */
public class Constants {

    //  ### Sandbox/environment information ###
    /**
     * Name for Salesforce sandbox environment (e.g. "uat", "e2estaging", etc).
     */
    public static final String QA_SANDBOX_NAME = System.getProperty("sf.sandboxName");
    /**
     * An environment name used to get proper URLs to AGS and SCP.
     */
    public static final String BASE_ENV_NAME = System.getProperty("env.name");

    //  ### Credentials ###
    /**
     * Username to log in into Salesforce via web/API.
     */
    public static final String USER = System.getProperty("sf.username", "invalidUsername") + "." + QA_SANDBOX_NAME;
    /**
     * Password to log in into Salesforce via web/API.
     */
    public static final String PASSWORD = System.getProperty("sf.password", "invalidPassword");
    /**
     * Security token to log into Salesforce via API.
     */
    public static final String SECURITY_TOKEN = System.getProperty("sf.token", "invalidToken");

    //  ### URLs/links ###
    /**
     * Base URL for Salesforce sandbox environment.
     */
    public static final String BASE_URL = "https://" + QA_SANDBOX_NAME;
    /**
     * Base URL for Salesforce Visualforce pages.
     * Can be used to access custom VF pages, using parameters, and/or page names.
     */
    public static final String BASE_VF_URL = "https://" + QA_SANDBOX_NAME;
    /**
     * URL to log into Salesforce via Salesforce API.
     */
    public static final String LOGIN_URL = "test.salesforce.com";
    /**
     * URL to log into Salesforce directly using username/password as URL parameters (query).
     */
    public static final String LOGIN_WITH_PARAMETERS_URL = BASE_URL + "?un=" + USER + "&pw=" + PASSWORD;
    /**
     * URL to log out of the current Salesforce session.
     */
    public static final String LOGOUT_LINK = BASE_URL + "/secur/logout.jsp";
    /**
     * Label of the default application.
     */
    public static final String DEFAULT_APP_LABEL = "Sales";

    //  ### Toggles ###
    /**
     * Is 'Generate Accounts Dynamically' via AGS/NGBS API enabled or not (default: disabled).
     * Should be controlled via system property/environment runtime variable.
     */
    public static final boolean IS_GENERATE_ACCOUNTS = parseBoolean(System.getProperty("generateAccounts", "false"));
}