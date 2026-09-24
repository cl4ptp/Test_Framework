package model.accountgeneration;

import model.DataModel;
import model.ngbs.testdata.Product;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data object for creating a new account in NGBS.
 * <br/><br/>
 * Useful data structure for parsing data from input parameters in CI/CD job
 * that is used to generate test accounts in NGBS (Existing Business Accounts).
 * Typically, user provides AGS scenario
 * and (optionally) contract and/or discount data for the account.
 */
@JsonInclude(value = NON_NULL)
public class CreateNgbsAccountsDTO extends DataModel {
    public String scenario;
    public Contract contract;
    public Product[] discounts;

    //  These variables only get a value after NGBS account is created to report on its data
    public String billingId;
    public String packageId;
    public String rcUserId;

    /**
     * Inner data structure for contract information on the NGBS account:
     * <p> - contract external ID (e.g. "Office", "EV_Standalone") </p>
     * <p> - contract product data (at least, should contain "quantity"/"existingQuantity" and "dataName") </p>
     */
    public static class Contract {
        public String contractExtId;
        public Product contractProduct;
    }
}
