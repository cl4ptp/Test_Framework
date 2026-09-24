package utilities.ngbs;

import model.ngbs.dto.account.*;
import model.ngbs.dto.contracts.ContractNgbsDTO;
import model.ngbs.dto.discounts.DiscountNgbsDTO;
import model.ngbs.dto.discounts.PromotionNgbsDTO;
import model.ngbs.dto.license.BillingInfoLicenseDTO;
import model.ngbs.dto.packages.PackageNgbsDTO;
import utilities.JsonUtils;
import utilities.RestApiClient;
import io.qameta.allure.Step;
import org.json.JSONObject;

import java.util.List;

import static utilities.RestApiAuthentication.usingBasicAuthentication;
import static utilities.StringHelper.EMPTY_STRING;
import static utilities.ngbs.NGBSRestApiHelper.*;
import static io.qameta.allure.Allure.step;

/**
 * Class for handling calls to NGBS API.
 * <br/>
 * Useful for getting the data from NGBS for entities like accounts, packages, contracts, discounts, etc...
 */
public class NGBSRestApiClient {
    private static final RestApiClient CLIENT = new RestApiClient(
            usingBasicAuthentication(NGBS_API_USERNAME, NGBS_API_PASSWORD),
            "Unable to get a response from NGBS! Details: "
    );

    /**
     * Get all the discounts on the NGBS account.
     *
     * @param billingId ID for the NGBS account (e.g. "235714001")
     * @param packageId ID for the package on the account in NGBS (e.g. "235798001")
     * @return list of all the active discounts on the NGBS account
     */
    @Step("Get discounts on the NGBS account")
    public static List<DiscountNgbsDTO> getDiscountsFromNGBS(String billingId, String packageId) {
        var url = getDiscountTemplateGroupURL(billingId, packageId);

        return CLIENT.getAsList(url, DiscountNgbsDTO.class);
    }

    /**
     * Create a new discount on the NGBS account.
     *
     * @param billingId      ID for the NGBS account (e.g. "235714001")
     * @param packageId      ID for the package on the account in NGBS (e.g. "235798001")
     * @param discountObject discount object with the data for creating a new discount
     * @return response from the NGBS mapped to the NGBS Discount object
     */
    @Step("Create a discount on the NGBS account")
    public static DiscountNgbsDTO createDiscountInNGBS(String billingId, String packageId, DiscountNgbsDTO discountObject) {
        var url = getDiscountTemplateGroupURL(billingId, packageId);
        return CLIENT.post(url, discountObject, DiscountNgbsDTO.class);
    }

    /**
     * Delete one of the existing discounts on the NGBS account.
     *
     * @param billingId  ID for the NGBS account (e.g. "235714001")
     * @param packageId  ID for the package on the account in NGBS (e.g. "235798001")
     * @param discountId ID for the discount template in NGBS (e.g. "4730001")
     */
    @Step("Delete a discount on the NGBS account")
    public static void deleteDiscountFromNGBS(String billingId, String packageId, String discountId) {
        var url = getDiscountTemplateGroupURL(billingId, packageId) + "/" + discountId;
        CLIENT.delete(url);
    }

    /**
     * Search promotions in NGBS by promo code.
     *
     * @param promoCode promo code used to find a promotion
     *                  (e.g. "QA-AUTO-POLYCOM-PHONE-USD")
     * @return collection of all Promotions with the matching promo code
     */
    @Step("Search promotions in NGBS by promo code")
    public static PromotionNgbsDTO[] searchPromotionsByPromoCodeInNGBS(String promoCode) {
        var url = getPromotionsSearchURL();
        var jsonBody = getPromoSearchByPromoCodeJsonBody(promoCode);
        var response = CLIENT.post(url, jsonBody);
        return JsonUtils.readJson(response, PromotionNgbsDTO[].class);
    }

    /**
     * Get promotion's details from NGBS by its ID.
     *
     * @param promotionId promotion Id used to get promotion's details
     *                    (e.g. 18866001)
     * @return promotion object with all the available details on it from NGBS
     */
    @Step("Get promotion's details from NGBS by ID")
    public static PromotionNgbsDTO getPromotionDetailsFromNGBS(String promotionId) {
        var url = getPromotionsURL() + "/" + promotionId;
        return CLIENT.get(url, PromotionNgbsDTO.class);
    }

    /**
     * Create a new Promotion in NGBS.
     * <br/>
     * Note: the promotion has Status='Actual' when created via this method.
     *
     * @param promoObject promotion object with the data for creating a new promo code
     * @return details of a new promotion object
     */
    @Step("Create a promotion in NGBS")
    public static PromotionNgbsDTO createPromotionInNGBS(PromotionNgbsDTO promoObject) {
        var url = getPromotionsURL();
        var createdPromotionResponse = CLIENT.post(url, promoObject, PromotionNgbsDTO.class);

        //  promo is created with Status='Draft' and can only be changed to Status='Actual' with additional request
        var urlStatusActual = getPromotionsStatusActualURL(createdPromotionResponse.id);
        CLIENT.post(urlStatusActual);

        return createdPromotionResponse;
    }

    /**
     * Get payment method type on the NGBS account.
     *
     * @param billingId ID for the NGBS account (e.g. "235714001").
     * @return payment method type object with the current payment method type
     * on the NGBS account (e.g. "CreditCard", "Invoice") + all the former types as well
     */
    public static PaymentMethodTypeDTO getPaymentMethodTypeFromNGBS(String billingId) {
        var url = getAccountPaymentMethodTypeURL(billingId);

        return CLIENT.get(url, PaymentMethodTypeDTO.class);
    }

    /**
     * Get the list of all the contracts on the NGBS account (active, terminated, etc...).
     *
     * @param billingId ID for the NGBS account (e.g. "235714001")
     * @param packageId ID for the package on the account in NGBS (e.g. "235798001")
     * @return list of the contracts mapped to the NGBS Contract objects
     */
    @Step("Get all the contracts on the NGBS account")
    public static List<ContractNgbsDTO> getContractsInNGBS(String billingId, String packageId) {
        var url = getContractsURL(billingId, packageId);
        return CLIENT.getAsList(url, ContractNgbsDTO.class);
    }

    /**
     * Create a new contract on the NGBS account.
     *
     * @param billingId      ID for the NGBS account (e.g. "235714001")
     * @param packageId      ID for the package on the account in NGBS (e.g. "235798001")
     * @param contractObject contract data mapped to the NGBS Contract object
     * @return response from the NGBS mapped to the NGBS Contract object
     */
    public static ContractNgbsDTO createContractInNGBS(String billingId, String packageId,
                                                       ContractNgbsDTO contractObject) {
        return updateContractInNGBS(billingId, packageId, EMPTY_STRING, contractObject);
    }

    /**
     * Update an existing contract on the NGBS account.
     *
     * @param billingId      ID for the NGBS account (e.g. "235714001")
     * @param packageId      ID for the package on the account in NGBS (e.g. "235798001")
     * @param contractId     ID for the contract in NGBS (e.g. "488001")
     * @param contractObject contract data mapped to the NGBS Contract object
     * @return response from the NGBS mapped to the NGBS Contract object
     */
    public static ContractNgbsDTO updateContractInNGBS(String billingId, String packageId, String contractId,
                                                       ContractNgbsDTO contractObject) {
        var isCreate = contractId.isBlank();
        var stepAction = isCreate ? "Create" : "Update";
        var url = isCreate ?
                getContractsURL(billingId, packageId) :
                getContractsURL(billingId, packageId) + "/" + contractId;

        return step(stepAction + " a contract on the NGBS account", () ->
                CLIENT.put(url, contractObject, ContractNgbsDTO.class)
        );
    }

    /**
     * Terminate an existing contract on the NGBS account.
     * <p></p>
     * Note: the contract won't be deleted from the account.
     * It just changes its status from "ACTIVE" to "TERMINATED".
     *
     * @param billingId  ID for the NGBS account (e.g. "235714001")
     * @param packageId  ID for the package on the account in NGBS (e.g. "235798001")
     * @param contractId ID for the contract in NGBS (e.g. "488001")
     */
    @Step("Terminate a contract on the NGBS account")
    public static void terminateContractInNGBS(String billingId, String packageId, String contractId) {
        var url = getContractsURL(billingId, packageId) + "/" + contractId;
        CLIENT.delete(url);
    }

    /**
     * Get the information about the package.
     *
     * @param packageId      ID of the package in NGBS
     *                       (e.g. "18" for RingCentral MVP Standard US).
     *                       Not to be mistaken for the long package ID on the specific NGBS account!
     * @param packageVersion version of the package in NGBS
     *                       (e.g. "3" as default for RingCentral MVP Standard US package).
     * @return response from the NGBS mapped to the NGBS Package object
     */
    @Step("Get a package summary from NGBS")
    public static PackageNgbsDTO getPackageSummary(String packageId, String packageVersion) {
        var url = getPackageSummaryURL(packageId, packageVersion);
        return CLIENT.get(url, PackageNgbsDTO.class);
    }

    /**
     * Get the Account information from NGBS.
     *
     * @param billingId ID for the NGBS account (e.g. "235714001")
     * @return response from the NGBS mapped to the NGBS Account object
     */
    @Step("Get the Account information from NGBS")
    public static AccountNgbsDTO getAccountInNGBS(String billingId) {
        var url = getAccountURL(billingId);
        var accountData = CLIENT.get(url);
        return JsonUtils.readJson(accountData, AccountNgbsDTO.class);
    }

    /**
     * Get a Free Service Credit on the NGBS account.
     *
     * @param billingId ID for the NGBS account (e.g. "235714001")
     * @return response from the NGBS mapped to the NGBS FSC object
     */
    @Step("Get a Free Service Credit on the NGBS account")
    public static AccountNgbsDTO.FreeServiceCreditDTO getFreeServiceCredit(String billingId) {
        var url = getAccountURL(billingId);
        var responseAccount = CLIENT.get(url, AccountNgbsDTO.class);
        return responseAccount.freeServiceCredit;
    }

    /**
     * Update a Free Service Credit on the NGBS account.
     *
     * @param billingId     ID for the NGBS account (e.g. "235714001")
     * @param serviceCredit Free Service Credit data mapped to the NGBS FSC object
     */
    @Step("Update a Free Service Credit on the NGBS account")
    public static void updateFreeServiceCredit(String billingId, FreeServiceCreditUpdateDTO serviceCredit) {
        var url = getFreeServiceCreditUpdateURL(billingId);
        CLIENT.put(url, serviceCredit);
    }

    /**
     * Search for an Account in NGBS using the last name of its Contact (from SFDC).
     *
     * @param contactLastName last name for the account's contact (e.g. "Newton")
     * @return first found account object with useful information (full name, company name, status, etc...)
     */
    @Step("Search account in NGBS using its Contact's Last Name")
    public static AccountNgbsDTO searchAccountInNGBS(String contactLastName) {
        var url = getSearchAccountURL();
        var jsonBody = getAccountSearchByContactLastNameJsonBody(contactLastName);
        var response = CLIENT.post(url, jsonBody);
        var account = new JSONObject(response).getJSONArray("result").get(0).toString();
        return JsonUtils.readJson(account, AccountNgbsDTO.class);
    }

    /**
     * Get information about licenses on account.
     *
     * @param billingID ID for the NGBS account (e.g. "235714001")
     * @param packageID ID for the package on the account in NGBS (e.g. "235798001")
     * @return response from the NGBS contains information about licenses on account
     */
    @Step("Get the licenses from the billing info summary on the NGBS account")
    public static BillingInfoLicenseDTO[] getBillingInfoSummaryLicenses(String billingID, String packageID) {
        var url = getBillingInfoSummaryLicensesURL(billingID, packageID);
        var licenses = CLIENT.get(url);
        return JsonUtils.readJson(licenses, BillingInfoLicenseDTO[].class);
    }
}