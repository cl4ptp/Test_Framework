package utilities.salesforce;

import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.soap.tooling.sobject.SObject;
import com.sforce.ws.ConnectionException;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Utility class that encapsulates the logic for working with SFDC using Tooling connection SOAP API.
 * The main usage for Tooling Connection is to invoke Apex scripts via API calls.
 * <p></p>
 * The class is designed using Singleton pattern
 * to be the "single point of contact" for users of Tooling connection API.
 */
public class ToolingConnectionUtils {

    //  Single instance of the class
    private static final ToolingConnectionUtils INSTANCE = new ToolingConnectionUtils();

    //  Connection object for accessing SFDC via SOAP API
    private final ToolingConnection toolingConnection;

    /**
     * Class constructor.
     * Contains the logic to establish an Enterprise connection with SFDC via SOAP API
     * and obtain a corresponding connection object.
     */
    private ToolingConnectionUtils() {
        try {
            toolingConnection = ConnectionFactory.getDefaultToolingConnection();
        } catch (ConnectionException e) {
            throw new RuntimeException("Unable to create tooling connection! Details: " + e.toString(), e);
        }
    }

    /**
     * Get the instance of ToolingConnectionUtils object.
     * Useful for classes that contain Tooling API connection's operations logic.
     *
     * @return instance of ToolingConnectionUtils with the current session's data.
     */
    public static ToolingConnectionUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Query the Salesforce database with the provided SOQL expression and
     * map the result to the provided tooling SObject's type.
     *
     * <pre><code class='java'>
     * var connectionUtils = ToolingConnectionUtils.getInstance();
     * var contacts = connectionUtils.query(
     *         "SELECT ID, EndpointUrl " +
     *         "FROM RemoteProxy " +
     *         "WHERE SiteName != null",
     *     RemoteProxy.class);
     * </code>
     *
     * Then, these 'RemoteProxy's' might look like this:
     * - RemoteProxy{Id='0031k00000bhPQeAAM', EndpointUrl='http://example.com'}
     * - RemoteProxy{Id='0031k00000bWaLcAAK', EndpointUrl='https://test.com'}
     * - etc...
     * </pre>
     *
     * <b> Note: all resulting objects will only contain values in the queried fields ("SELECT" part)!
     * Non-null (in the DB) fields that weren't queried will remain null on the Java object!
     * </b>
     *
     * @param queryString SOQL expression that queries one or several fields for any SObject
     *                    (e.g. <i>"SELECT ID, EndpointUrl FROM RemoteProxy WHERE SiteName != null"</i>)
     * @param valueType   any valid standard tooling SObject type
     *                    (e.g. RemoteProxy...)
     * @return list of tooling SObjects that were found using the provided query
     * @throws ConnectionException in case of errors while accessing API
     */
    public <T extends SObject> List<T> query(String queryString, Class<T> valueType) throws ConnectionException {
        var records = toolingConnection.query(queryString).getRecords();

        return Arrays.stream(records)
                .map(valueType::cast)
                .collect(toList());
    }
}
