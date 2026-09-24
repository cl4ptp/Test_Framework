package model.ngbs.dto.contracts;

import model.DataModel;
import utilities.ngbs.ContractNgbsFactory;
import utilities.ngbs.NGBSRestApiClient;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data object with contract information for usage with NGBS API services.
 * <br/><br/>
 * Useful data structure for contract request objects and parsing responses
 * to/fro NGBS API contract service (see {@link NGBSRestApiClient} for a reference).
 * <br/><br/>
 * Use {@link ContractNgbsFactory} to create quick instances of this DTO.
 */
@JsonInclude(value = NON_NULL)
public class ContractNgbsDTO extends DataModel {
    //  Constants for 'startBillingCycleNumber'
    public static final int CONTRACT_ACTIVE = 0;
    public static final int CONTRACT_NOT_STARTED = -1;
    public static final int CONTRACT_TERMINATED = -2;

    public String id;
    public String createdAt;
    public String lastUpdated;
    public Integer startBillingCycleNumber;
    public String startDate;
    public String renewalDate;
    public String description;
    public Integer term;
    public Integer renewalTerm;
    public Boolean autoRenewal;
    public String packageVersion;
    public License[] licenses;

    /**
     * Inner data structure for Contract data object.
     * Represents data for specific contractual license.
     */
    @JsonInclude(value = NON_NULL)
    public static class License {
        public String id;
        public String catalogId;
        public Integer contractualQty;
    }
}
