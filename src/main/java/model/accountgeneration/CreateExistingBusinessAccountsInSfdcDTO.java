package model.accountgeneration;

import model.DataModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data object for creating an Existing Business account in SFDC via provided user's data.
 * <br/><br/>
 * Useful data structure for parsing data from input parameters in CI/CD job
 * that is used to generate EB accounts in SFDC.
 * Typically, user provides Account's name, billing address, contact's data (first name, last name...),
 * and NGBS-related data: AGS scenario and (optionally) contract and/or discount data for the account.
 */
@JsonInclude(value = NON_NULL)
public class CreateExistingBusinessAccountsInSfdcDTO extends DataModel {
    public String accountName;
    public String accountURL; // URL is populated only after the account is created
    public String serviceType;
    public BillingAddress billingAddress;
    public Contact contact;

    public CreateNgbsAccountsDTO ngbsAccountData;

    /**
     * Inner data structure for data object to create Existing Business Account in SFDC.
     * Represents the info about the Billing Address information on Account record: Country, City, Street, etc...
     */
    public static class BillingAddress {
        public String country;
        public String state;
        public String city;
        public String street;
        public String postalCode;
    }

    /**
     * Inner data structure for data object to create Existing Business Account in SFDC.
     * Represents the info about the Contact record: First Name, Last Name, Email, etc...
     */
    public static class Contact {
        public String firstName;
        public String lastName;
        public String email;
        public String phone;
    }
}
