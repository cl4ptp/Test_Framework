package utilities;

import static utilities.Constants.QA_SANDBOX_NAME;
import static java.lang.Thread.sleep;

/**
 * Additional utility class with useful methods for tests execution.
 */
public class ServiceUtils {
    private static final long DELAY = 2_000L;

    /**
     * Adds a short time delay in the execution.
     * <p>
     * Currently, this is a lame workaround for inserting objects in SFDC on E2E Staging environment
     * (or other not-so-fast environment).
     * Most of the time it's too busy to be able to add some sObjects in time.
     * <p>
     * Note: conditional logic here helps NOT to delay tests in other environments.
     *
     * @throws Exception in case of invalid delay value or interruption of the current thread
     */
    public static void shortDelay() throws Exception {
        if (QA_SANDBOX_NAME.equalsIgnoreCase("E2ESTAGING")) {
            sleep(DELAY);
        }
    }
}
