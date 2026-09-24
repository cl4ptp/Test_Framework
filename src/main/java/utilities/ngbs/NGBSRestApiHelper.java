package utilities.ngbs;

import static java.lang.String.format;

/**
 * Helper class for {@link NGBSRestApiClient} class to store and form useful data.
 */
public class NGBSRestApiHelper {

    /**
     * Data object with values for connecting to NGBS API, accessing various REST services, etc...
     */
    private static final NGBSRestApiSettings NGBS_REST_API_SETTINGS = NGBSRestApiSettings.getInstance();

    /**
     * Username to access and send requests to NGBS via REST API.
     */
    public static final String NGBS_API_USERNAME = NGBS_REST_API_SETTINGS.getUsername();

    /**
     * Password to access and send requests to NGBS via REST API.
     */
    public static final String NGBS_API_PASSWORD = NGBS_REST_API_SETTINGS.getPassword();

    //  NGBS endpoints for different environments
    public static final String NGBS_REST_ENDPOINT_UAT = "http://185.23.251.124:8080";
    public static final String NGBS_REST_ENDPOINT_STAGING = "http://199.68.214.251:8080";
    public static final String NGBS_REST_ENDPOINT_DEV = "http://185.23.251.118:8080";
    public static final String NGBS_REST_ENDPOINT_PATCH = "http://biq01-t01-igl01.ngbs-biqasv7.svc.c01.k01.k8s.sv701.lab.nordigy.ru:8080";

    //  Payment method type
    private static final String PAYMENT_METHOD_TYPE = "/account/%s/paymentMethod/type";

    //  Packages
    private static final String PACKAGE_SUMMARY = "/packages/%s/%s/summary";

    //  Contracts
    private static final String CONTRACTS = "/account/%s/package/%s/contract";

    //  Discounts
    private static final String DISCOUNT_TEMPLATE_GROUP = "/account/%s/package/%s/discountTemplateGroup";

    //  Promotions
    private static final String DISCOUNT_GROUPS = "/discountGroups";
    private static final String DISCOUNT_GROUPS_SEARCH = DISCOUNT_GROUPS + "/search";
    private static final String DISCOUNT_GROUPS_STATUS_ACTUAL = DISCOUNT_GROUPS + "/%s/status?status=Actual";

    //  Accounts
    private static final String UPDATE_FREE_SERVICE_CREDIT = "/account/%s/freeServiceCredit";
    private static final String GET_ACCOUNT = "/account/%s";

    //  Search Account
    private static final String SEARCH_ACCOUNT = "/search/account";

    //  Licenses
    private static final String LICENSES_SUMMARY = "/account/%s/package/%s/billinginfo/summary/license";

    private static final String ACCOUNT_SEARCH_LAST_NAME_PAYLOAD = "{\"query\": {\"lastname\": \"%s\"}, \"limit\": 1}";
    private static final String PROMO_SEARCH_PROMO_CODE_PAYLOAD = "{\"promoCode\": \"%s\"}";

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/account/{accountId}/package/{packageId}/discountTemplateGroup</i>
     *
     * @param billingId Account ID for account from Billing
     * @param packageId Package ID for account from Billing
     * @return string representation for URL to get discounts on account
     */
    public static String getDiscountTemplateGroupURL(String billingId, String packageId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() +
                format(DISCOUNT_TEMPLATE_GROUP, billingId, packageId);
    }

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/catalog/v1.0/discountGroups</i>
     *
     * @return string representation for URL to get/create/edit/delete promotions
     */
    public static String getPromotionsURL() {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getCatalogPath() +
                DISCOUNT_GROUPS;
    }

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/catalog/v1.0/discountGroups/search</i>
     *
     * @return string representation for URL to search promotions (by promo code)
     */
    public static String getPromotionsSearchURL() {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getCatalogPath() +
                DISCOUNT_GROUPS_SEARCH;
    }

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/catalog/v1.0/discountGroups/{promotionId}/status?status=Actual</i>
     *
     * @param promotionId promotion Id used to get promotion's details (e.g. 18866001)
     * @return string representation for URL to change promotion's status to 'Actual'
     */
    public static String getPromotionsStatusActualURL(String promotionId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getCatalogPath() +
                format(DISCOUNT_GROUPS_STATUS_ACTUAL, promotionId);
    }

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/account/{accountId}/paymentMethod/type</i>
     *
     * @param billingId Account ID for account from Billing.
     * @return string representation for URL to get payment method type on account.
     */
    public static String getAccountPaymentMethodTypeURL(String billingId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() +
                format(PAYMENT_METHOD_TYPE, billingId);
    }

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/account/{accountId}/package/{packageId}/contract</i>
     *
     * @param billingId Account ID for account from Billing
     * @param packageId Package ID for account from Billing
     * @return string representation for URL to get contracts on account
     */
    public static String getContractsURL(String billingId, String packageId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() +
                format(CONTRACTS, billingId, packageId);
    }

    /**
     * Return string URL for request to
     * <i>{ngbs.api.endpoint}/restapi/catalog/v1.0/packages/%s/1/summary</i>
     * <p>
     * <b>Not to be confused with Package ID for account from Billing!</b>
     * </p>
     *
     * @param packageId      Package ID from billing catalog (e.g.
     *                       <p><b>packageId="18"</b> for "RingCentral MVP Standard" for "RingCentral" brand (US),</p>
     *                       <p><b>packageId="6"</b> for "RingCentral Meetings Free" for "RingCentral" brand (US),</p>
     *                       <p><b>packageId="84"</b> for "RingCentral MVP Standard" for "RingCentral UK" brand (UK))</p>
     * @param packageVersion Package Version from billing catalog (e.g. "1", "2", "3")
     * @return string representation for URL to get package summary
     */
    public static String getPackageSummaryURL(String packageId, String packageVersion) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getCatalogPath() +
                format(PACKAGE_SUMMARY, packageId, packageVersion);
    }

    /**
     * Return string url for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/account/%s/freeServiceCredit</i>
     *
     * @param billingId Account ID for account from Billing
     * @return string representation for URL to update Free Service Credit
     */
    public static String getFreeServiceCreditUpdateURL(String billingId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() +
                format(UPDATE_FREE_SERVICE_CREDIT, billingId);
    }

    /**
     * Return string url for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/account/%s</i>
     *
     * @param billingId Account ID for account from Billing
     * @return string representation for URL to get Account Info
     */
    public static String getAccountURL(String billingId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() +
                format(GET_ACCOUNT, billingId);
    }

    /**
     * Return string url for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/search/account</i>
     *
     * @return string representation for URL to search account
     */
    public static String getSearchAccountURL() {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() + SEARCH_ACCOUNT;
    }

    /**
     * Return string url for request to
     * <i>{ngbs.api.endpoint}/restapi/account-manager/v1.0/account/%s/package/%s/billinginfo/summary/license</i>
     *
     * @param billingId Account ID for account from Billing
     * @param packageId Package ID for account from Billing
     * @return string representation for URL to get summary about license(s)
     */
    public static String getBillingInfoSummaryLicensesURL(String billingId, String packageId) {
        return NGBS_REST_API_SETTINGS.getEndpoint() + NGBS_REST_API_SETTINGS.getAccountManagerPath() +
                format(LICENSES_SUMMARY, billingId, packageId);
    }

    /**
     * Get body/payload for the "Search the account in NGBS by Last Name" request.
     *
     * @param contactLastName last name of the Account's primary contact
     * @return string representation for JSON body for 'search' request to NGBS API
     */
    public static String getAccountSearchByContactLastNameJsonBody(String contactLastName) {
        return format(ACCOUNT_SEARCH_LAST_NAME_PAYLOAD, contactLastName);
    }

    /**
     * Get body/payload for the "Search promotions in NGBS by Promo Code" request.
     *
     * @param promoCode last name of the Account's primary contact
     * @return string representation for JSON body for 'search' request to NGBS API
     */
    public static String getPromoSearchByPromoCodeJsonBody(String promoCode) {
        return format(PROMO_SEARCH_PROMO_CODE_PAYLOAD, promoCode);
    }
}
