package utilities.salesforce;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.tooling.ToolingConnection;
import com.sforce.ws.ConnectionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static java.util.stream.Collectors.toList;

/**
 * Utility class providing additional processing for Salesforce API operations.
 * Such operations might need some additional logging and exception handling.
 * <p></p>
 * For example, if we update some SObjects, and operation partially fails,
 * then it's good to know what errors happened, and what SObjects were problematic.
 * This information might be useful for debugging, and the aftermath "housekeeping" activities.
 */
public class SalesforceUtils {

    /**
     * Create new enterprise SObjects in the Salesforce database
     * (any standard/custom objects that exist in the Enterprise WSDL).
     *
     * @param enterpriseConnection Enterprise connection with Salesforce via SOAP API
     * @param objects              enterprise SObjects to be created in the database
     * @return list of SObject IDs for every created SObject in the database
     * @throws ConnectionException in case of errors while accessing API
     */
    public static List<String> create(EnterpriseConnection enterpriseConnection,
                                      SObject... objects)
            throws ConnectionException {
        var saveResults = enterpriseConnection.create(objects);
        var successIdList = new ArrayList<String>();
        var errorMessages = new ArrayList<String>();

        for (var saveResult : saveResults) {
            if (saveResult.getSuccess()) {
                successIdList.add(saveResult.getId());
            } else {
                var errors = saveResult.getErrors();

                if (errors.length != 0) {
                    for (var error : errors) {
                        errorMessages.add(error.toString());
                    }
                }
            }
        }

        if (!successIdList.isEmpty()) {
            step("Successfully created SObject(s) with IDs: " + successIdList);
        }

        if (!errorMessages.isEmpty()) {
            throw new RuntimeException("Failed to insert SObject(s)! \n" +
                    "Errors: " + errorMessages);
        }

        return successIdList;
    }

    /**
     * Create new tooling SObjects in the Salesforce database
     * (any standard objects and metadata that exist in the Tooling WSDL).
     *
     * @param toolingConnection Tooling connection with Salesforce via SOAP API
     * @param objects           tooling SObjects to be created in the database
     * @return list of SObject IDs for every created SObject in the database
     * @throws ConnectionException in case of errors while accessing API
     */
    public static List<String> create(ToolingConnection toolingConnection,
                                      com.sforce.soap.tooling.sobject.SObject... objects)
            throws ConnectionException {
        var saveResults = toolingConnection.create(objects);
        var successIdList = new ArrayList<String>();
        var errorMessages = new ArrayList<String>();

        for (var saveResult : saveResults) {
            if (saveResult.getSuccess()) {
                successIdList.add(saveResult.getId());
            } else {
                var errors = saveResult.getErrors();

                if (errors.length != 0) {
                    for (var error : errors) {
                        errorMessages.add(error.toString());
                    }
                }
            }
        }

        if (!successIdList.isEmpty()) {
            step("Successfully created SObject(s) with ID(s): " + successIdList);
        }

        if (!errorMessages.isEmpty()) {
            throw new RuntimeException("Failed to insert SObject(s)! \n" +
                    "Errors: " + errorMessages);
        }

        return successIdList;
    }

    /**
     * Update the state of the provided SObjects in the Salesforce database.
     *
     * @param enterpriseConnection Enterprise connection with Salesforce via SOAP API
     * @param objects              SObjects to be updated in the database
     * @return list of SObject IDs for every updated SObject in the database
     * @throws ConnectionException in case of errors while accessing API
     */
    public static List<String> update(EnterpriseConnection enterpriseConnection, SObject... objects)
            throws ConnectionException {
        var saveResults = enterpriseConnection.update(objects);
        var successIdList = new ArrayList<String>();
        var errorMessages = new ArrayList<String>();

        for (var saveResult : saveResults) {
            if (saveResult.getSuccess()) {
                successIdList.add(saveResult.getId());
            } else {
                var errors = saveResult.getErrors();

                if (errors.length != 0) {
                    for (var error : errors) {
                        errorMessages.add(error.toString());
                    }
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            var failedIds = Arrays.stream(objects)
                    .map(SObject::getId)
                    .collect(toList());
            failedIds.removeAll(successIdList);

            throw new RuntimeException("Failed to update SObject(s) with id(s): " + failedIds + "\n" +
                    "Errors: " + errorMessages);
        }

        return successIdList;
    }

    /**
     * Delete SObjects with the provided IDs from the Salesforce database.
     *
     * @param enterpriseConnection Enterprise connection with Salesforce via SOAP API
     * @param ids                  SObjects' IDs to be deleted in the database
     * @return list of SObject IDs for every deleted SObject in the database
     * @throws ConnectionException in case of errors while accessing API
     */
    public static List<String> delete(EnterpriseConnection enterpriseConnection, String... ids)
            throws ConnectionException {
        var deleteResults = enterpriseConnection.delete(ids);
        var successIdList = new ArrayList<String>();
        var errorMessages = new ArrayList<String>();

        for (var deleteResult : deleteResults) {
            if (deleteResult.getSuccess()) {
                successIdList.add(deleteResult.getId());
            } else {
                var errors = deleteResult.getErrors();

                if (errors.length != 0) {
                    for (var error : errors) {
                        errorMessages.add(error.toString());
                    }
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            var failedIds = new ArrayList<>(Arrays.asList(ids));
            failedIds.removeAll(successIdList);

            throw new RuntimeException("Failed to delete SObject(s) with id(s): " + failedIds + "\n" +
                    "Errors: " + errorMessages);
        }

        return successIdList;
    }
}
