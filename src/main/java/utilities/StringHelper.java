package utilities;

import java.util.*;

/**
 * Utility class that provides tests (and other callers) with useful methods
 * for string transformations/formatting.
 * <p></p>
 * Additionally, class contains useful string constants.
 */
public class StringHelper {

    /**
     * Just an empty string value.
     * Explicit declaration is better than implicit one.
     */
    public static final String EMPTY_STRING = "";

    /**
     * Test string value.
     */
    public static final String TEST_STRING = "Test String value";

    /**
     * String value for percent symbol.
     */
    public static final String PERCENT = "%";

    /**
     * Get randomly generated email address with a common domain.
     *
     * @return random email with a common domain "example.com"
     * (e.g. "ae13320a-db2c-477d-997a-f6dbc8c1c93d@example.com")
     */
    public static String getRandomEmail() {
        return UUID.randomUUID() + "@example.com";
    }

    /**
     * Get randomly generated US-based 10-digit phone number
     * using a pattern "(617) NXX-XXXX".
     * <br/>
     * More info on valid values via this link:
     * <a href="https://en.wikipedia.org/wiki/North_American_Numbering_Plan#Modern_plan">LINK</a>.
     *
     * @return 10-digit random phone number with the US area code
     * (e.g. "(617) 248-3864", "(617) 365-0448", etc...)
     */
    public static String getRandomUSPhone() {
        var centralOfficeCode = new Random().nextInt(800) + 200;
        if ((centralOfficeCode - 11) % 100 == 0) {
            centralOfficeCode += 1;
        }

        var lineNumber = new Random().nextInt(10000);

        return String.format("(617) %3d-%04d", centralOfficeCode, lineNumber);
    }
}
