package model.ngbs.dto.account;

import model.DataModel;

/**
 * Data object with account information for usage with NGBS API services.
 */
public class AccountNgbsDTO extends DataModel {
    public FreeServiceCreditDTO freeServiceCredit;
    public AccountPackageDTO[] packages;
    public String id;
    public Integer businessIdentityId;

    /**
     * Inner data structure that stores information about Free Service Credit
     * on account for usage with NGBS API services.
     */
    public static class FreeServiceCreditDTO {
        public double amount;
    }

    /**
     * Inner data structure that stores information about package
     * on NGBS account.
     */
    public static class AccountPackageDTO {
        public String id;
        public String catalogId;
        public String version;
        public String billingStartDate;
    }
}
