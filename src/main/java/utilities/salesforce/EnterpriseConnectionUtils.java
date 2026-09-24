package utilities.salesforce;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static utilities.StringHelper.EMPTY_STRING;

/**
 * Utility class that encapsulates the logic for working with SFDC using Enterprise connection SOAP API.
 * There are many useful methods: querying Salesforce's DB; performing DML operations with the data
 * (create, update, delete), etc...
 * <p></p>
 * The class is designed using Singleton pattern
 * to be the "single point of contact" for users of Enterprise connection API.
 */
public class EnterpriseConnectionUtils {

    //  Single instance of the class
    private static final EnterpriseConnectionUtils INSTANCE = new EnterpriseConnectionUtils();

    //  Connection object for accessing SFDC via SOAP API
    private final EnterpriseConnection enterpriseConnection;

    /**
     * Class constructor.
     * Contains the logic to establish an Enterprise connection with SFDC via SOAP API
     * and obtain a corresponding connection object.
     */
    private EnterpriseConnectionUtils() {
        try {
            enterpriseConnection = ConnectionFactory.getDefaultEnterpriseConnection();
        } catch (ConnectionException e) {
            throw new RuntimeException("Unable to create an Enterprise Connection! Details: " + e, e);
        }
    }

    /**
     * Get the instance of EnterpriseConnectionUtils object.
     * Useful for classes that contain Enterprise API connection's operations logic.
     *
     * @return instance of EnterpriseConnectionUtils with the current session's data.
     */
    public static EnterpriseConnectionUtils getInstance() {
        return INSTANCE;
    }

    //  ### QUERY ###

    /**
     * Query the Salesforce database with the provided SOQL expression and
     * map the result to the provided SObject's type.
     *
     * <pre><code class='java'>
     * var connectionUtils = EnterpriseConnectionUtils.getInstance();
     * var contacts = connectionUtils.query(
     *         "SELECT Id, FirstName, LastName " +
     *         "FROM Contact " +
     *         "WHERE Email != null",
     *     Contact.class);
     * </code>
     *
     * Then, these 'contacts' might look like this:
     * - Contact{Id='0031k00000bhPQeAAM', FirstName='James', LastName='Bond'}
     * - Contact{Id='0031k00000bWaLcAAK', FirstName='Maria', LastName='Poppins'}
     * - etc...
     * </pre>
     *
     * <b> Note: all resulting objects will only contain values in the queried fields ("SELECT" part)!
     * Non-null (in the DB) fields that weren't queried will remain null on the Java object!
     * </b>
     *
     * @param queryString SOQL expression that queries one or several fields for any SObject
     *                    (e.g. <i>"SELECT Id, FirstName, LastName FROM Contact WHERE Email != null"</i>)
     * @param valueType   any valid standard or custom SObject type
     *                    (e.g. Account, Contact, Opportunity...)
     * @return list of SObjects that were found using the provided query
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public <T extends SObject> List<T> query(String queryString, Class<T> valueType) throws ConnectionException {
        var records = enterpriseConnection.query(queryString).getRecords();

        return Arrays.stream(records)
                .map(valueType::cast)
                .collect(Collectors.toList());

    }

    /**
     * Query the Salesforce database with the provided SOQL expression and
     * map the result to the provided SObject's type.
     * <p></p>
     * Note: the method returns only the single object!
     *
     * @param queryString SOQL expression that queries one or several fields for any SObject
     *                    (e.g. <i>"SELECT Id, FirstName, LastName FROM Contact WHERE Email != null LIMIT 1"</i>)
     * @param valueType   any valid standard or custom SObject type
     *                    (e.g. Account, Contact, Opportunity...)
     * @return single SObject that was found using the provided query
     * @throws ConnectionException in case of errors while accessing API
     * @throws RuntimeException    if query returns more than 1 record
     * @see EnterpriseConnectionUtils#query(String, Class)
     */
    @Step
    public <T extends SObject> T querySingleRecord(String queryString, Class<T> valueType) throws ConnectionException {
        var resultList = query(queryString, valueType);

        if (resultList.size() != 1) {
            throw new RuntimeException(
                    "Query supposed to return 1 record, but returned " + resultList.size() + " element(s). \n" +
                            "Query: " + queryString);
        } else {
            return resultList.get(0);
        }
    }

    //  ### CREATE ###

    /**
     * Insert the provided SObject(-s) into the Salesforce DB and return the list of its/their resulting ID(-s).
     * <p></p>
     * This method also assigns resulting IDs to their corresponding provided SObjects.
     *
     * @param objects array of SObjects (or a single SObject) to insert into the database
     * @return list of the IDs for all provided SObjects after inserting them into the database
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public List<String> insertAndGetIds(SObject... objects) throws ConnectionException {
        var insertResult = SalesforceUtils.create(enterpriseConnection, objects);

        for (int i = 0; i < insertResult.size(); i++) {
            objects[i].setId(insertResult.get(i));
        }

        return insertResult;
    }

    //  ### UPDATE ###

    /**
     * Update the provided SObjects in the Salesforce database.
     * <p></p>
     * Note: all SObjects should contain non-null corresponding ID (SObject.Id field),
     * i.e. exist in the database.
     * Of course, they should also carry the updated state
     * (e.g. new values on their fields).
     *
     * @param objects SObjects that needs to be updated.
     * @return list of SObjects' IDs that were successfully updated
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public List<String> update(SObject... objects) throws ConnectionException {
        return SalesforceUtils.update(enterpriseConnection, objects);
    }

    //  ### DELETE ###

    /**
     * Delete the provided SObject(-s) from the database.
     *
     * @param objects array of SObjects (or just one) that need to be deleted
     * @return list of SObjects' IDs that were successfully deleted
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public List<String> delete(SObject... objects) throws ConnectionException {
        var ids = Arrays.stream(objects)
                .map(SObject::getId)
                .toArray(String[]::new);

        return deleteByIds(ids);
    }

    /**
     * Delete the provided SObjects from the database.
     *
     * @param objects collection of SObjects that need to be deleted
     * @return list of SObjects' IDs that were successfully deleted
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public <T extends SObject> List<String> delete(Collection<T> objects) throws ConnectionException {
        var ids = objects.stream()
                .map(SObject::getId)
                .toArray(String[]::new);

        return deleteByIds(ids);
    }

    /**
     * Delete SObjects with the provided IDs from the database.
     *
     * @param ids IDs for SObjects that need to be deleted
     * @return list of SObjects' IDs that were successfully deleted
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public List<String> deleteByIds(Collection<String> ids) throws ConnectionException {
        return deleteByIds(ids.toArray(new String[0]));
    }

    /**
     * Delete SObjects with the provided IDs from the database.
     *
     * @param ids IDs for SObjects that need to be deleted
     * @return list of SObjects' IDs that were successfully deleted
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step
    public List<String> deleteByIds(String... ids) throws ConnectionException {
        return SalesforceUtils.delete(enterpriseConnection, ids);
    }

    //  ### ADDITIONAL METHODS ###

    /**
     * Get ID for a Record Type of the given SObject.
     * Useful when creating non-standard SObjects via API.
     *
     * @param sObjectName    API name for the given SObject
     *                       (e.g. "Account", "Contact", "Opportunity", "Approval__c"...)
     * @param recordTypeName name for the SObject's record type
     *                       (e.g. "Partner Leads" for Lead, "Invoicing Request" for Approval__c ...).
     *                       Totally depends on the actual org's configuration.
     * @return ID for the SObject's record type
     * @throws ConnectionException in case of errors while accessing API
     */
    public String getRecordTypeId(String sObjectName, String recordTypeName) throws ConnectionException {
        var describeSObjectResult = enterpriseConnection.describeSObject(sObjectName);
        var recordTypeInfo = Arrays.stream(describeSObjectResult.getRecordTypeInfos())
                .filter(s -> s.getName().equals(recordTypeName))
                .findAny();

        return recordTypeInfo.isPresent() ?
                recordTypeInfo.get().getRecordTypeId() :
                EMPTY_STRING;
    }
}
