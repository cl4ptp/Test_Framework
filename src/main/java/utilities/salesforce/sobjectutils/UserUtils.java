package utilities.salesforce.sobjectutils;

import utilities.salesforce.EnterpriseConnectionUtils;
import utilities.salesforce.sobjecthelper.EmployeeHelper;
import com.sforce.soap.enterprise.sobject.*;
import com.sforce.ws.ConnectionException;
import io.qameta.allure.Step;

import static java.util.stream.Collectors.toList;

/**
 * Utility class that provides {@link User} objects.
 * <p>
 * All methods also create an {@link Employee__c} record for a User record in SFDC, if it doesn't exist yet.
 * </p>
 */
public class UserUtils {
    /**
     * Enterprise connection utility to interact with SFDC API.
     */
    private static final EnterpriseConnectionUtils CONNECTION_UTILS = EnterpriseConnectionUtils.getInstance();

    /**
     * Get User SObject with a provided profile name.
     * Used in tests mainly to get target users for test scenarios
     * (e.g. Sales reps, Sales engineers, etc...).
     *
     * @param profileName name for user's profile (Profile.Name field)
     * @return Salesforce User with a provided profile
     * @throws ConnectionException in case of malformed query, DB or network errors.
     */
    @Step
    public static User getUserByProfile(String profileName) throws ConnectionException {
        var user = CONNECTION_UTILS.querySingleRecord(
                "SELECT Id, FirstName, LastName, Name, PID__c, Email, Phone " +
                        "FROM User " +
                        "WHERE IsActive = true " +
                        "AND Profile.Name = '" + profileName + "' " +
                        "AND Email LIKE '%.invalid' " + // to avoid using real Users
                        "LIMIT 1",
                User.class);

        getEmployeeRecord(user);

        return user;
    }

    /**
     * Get Employee record for the provided User.
     * This Employee record is necessary for non-admin SF users
     * to be able to create standard objects, like Opportunity.
     * <p></p>
     * Note: if an Employee record doesn't exist, then it's created using
     * provided User object.
     *
     * @param user Salesforce User to link to Employee record
     * @throws ConnectionException in case of malformed query, DB or network errors.
     */
    private static void getEmployeeRecord(User user) throws ConnectionException {
        var employeeList = CONNECTION_UTILS.query(
                "SELECT Id " +
                        "FROM Employee__c " +
                        "WHERE User__c = '" + user.getId() + "'",
                Employee__c.class);

        if (employeeList.isEmpty()) {
            var employee = new Employee__c();
            employee.setFirst_Name__c(user.getFirstName());
            employee.setLast_Name__c(user.getLastName());
            employee.setIs_Active__c(true);
            employee.setEmail__c(user.getEmail());
            employee.setSPID__c(user.getPID__c());
            employee.setUser__c(user.getId());
            EmployeeHelper.setDefaultFields(employee);

            CONNECTION_UTILS.insertAndGetIds(employee);
        }
    }
}
