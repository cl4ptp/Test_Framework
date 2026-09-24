package base;

import model.ngbs.testdata.Product;
import page.opportunity.ngbsquotingwizard.carttab.CartPage;
import utilities.ngbs.DiscountNgbsFactory;
import utilities.salesforce.EnterpriseConnectionUtils;
import com.sforce.soap.enterprise.sobject.*;
import com.sforce.ws.ConnectionException;
import io.qameta.allure.Step;

import static model.ngbs.dto.contracts.ContractNgbsDTO.CONTRACT_ACTIVE;
import static utilities.Constants.*;
import static utilities.ngbs.ContractNgbsFactory.createContractForOneLicense;
import static utilities.ngbs.NGBSRestApiClient.*;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

/**
 * Base test class for test cases related to working with Salesforce.
 * <p>
 * Contains tools to work with SFDC API via Enterprise/Tooling connection utility classes.
 */
public abstract class BaseSFDCTest extends BaseTest {
    //  Utilities to interact with SFDC API in tests
    protected EnterpriseConnectionUtils enterpriseConnectionUtils = EnterpriseConnectionUtils.getInstance();

    /**
     * Set this variable to "true" in the test's constructor
     * if the test needs to generate a new NGBS account on every run.
     */
    protected boolean isGenerateAccountsForSingleTest = false;

    //  Used pages
    protected final CartPage cartPage = new CartPage();

    /**
     * Is AGS Dynamic Account generation active for a given test?
     *
     * @return true, if either "-DgenerateAccounts=true" for the build
     * or {@code isGenerateAccountsForSingleTest = true} for a single test
     */
    protected boolean isGenerateAccounts() {
        return IS_GENERATE_ACCOUNTS || isGenerateAccountsForSingleTest;
    }

    /**
     * Log in as different user in active user's session.
     * <p><b>
     * Note: only works for active SYSTEM ADMINISTRATOR user session!
     * <p>
     * To login as different user in active non-admin user session,
     * please use {@link BaseSFDCTest#reLoginAsUser(User)}.
     * </p>
     * </b></p>
     *
     * @param user different user to login as
     * @throws Exception in case of malformed DB queries or network failures
     */
    @Step("Log in as different user in current admin user session")
    protected void loginAsUser(User user) throws Exception {
        var organization = enterpriseConnectionUtils.querySingleRecord(
                "SELECT Id " +
                        "FROM Organization",
                Organization.class);

        var loginAsUserUrl = String.format(BASE_URL +
                        "/servlet/servlet.su" +
                        "?oid=%s" +
                        "&suorgadminid=%s" +
                        "&retURL=/home/home.jsp" +
                        "&targetURL=/home/home.jsp",
                organization.getId(), user.getId()
        );

        open(loginAsUserUrl);
        openDefaultApp();
    }

    /**
     * Open the app with provided name in the current user's session.
     *
     * @param appName name of the application to be opened
     * @throws ConnectionException in case of malformed DB queries or network failures
     */
    @Step("Open App in the current user's session")
    protected void openApp(String appName) throws ConnectionException {
        var app = enterpriseConnectionUtils.querySingleRecord(
                "SELECT DurableId " +
                        "FROM AppDefinition " +
                        "WHERE Label = '" + appName + "'" +
                        "AND MasterLabel = '" + appName + "'",
                AppDefinition.class
        );

        var changeAppUrl = String.format(BASE_URL +
                "/lightning/app/%s", app.getDurableId()
        );

        open(changeAppUrl);
    }


    /**
     * Open the default app in the current user's session.
     *
     * @throws ConnectionException in case of malformed DB queries or network failures
     * @see utilities.Constants#DEFAULT_APP_LABEL
     */
    @Step("Open the Default App in the current user's session")
    protected void openDefaultApp() throws ConnectionException {
        openApp(DEFAULT_APP_LABEL);
    }

    /**
     * Log in as different user in active user's session.
     * <p><b>
     * Note: only works for active NON-ADMIN user session,
     * if it was previously accessed via ADMIN user session.
     * <p>
     * To login as different user in active admin user session,
     * please use {@link BaseSFDCTest#loginAsUser(User)}.
     * </p>
     * </b></p>
     *
     * @param user different user to re-login as
     * @throws Exception in case of malformed DB queries or network failures
     */
    @Step("Log in as different user in current non-admin user session")
    protected void reLoginAsUser(User user) throws Exception {
        logout();
        loginAsUser(user);
    }

    /**
     * Log out from current user's session in Salesforce.
     */
    @Step("Log out from current user's session in Salesforce.")
    protected void logout() {
        open(LOGOUT_LINK);
    }

    /**
     * Create a contract in NGBS with an initial state if there are no Active contracts on the Account.
     *
     * @param billingId     ID for the NGBS account (e.g. "235714001")
     * @param packageId     ID for the package on the account in NGBS (e.g. "235798001")
     * @param contractExtId special ID for a contract to map it to the custom SFDC contract object
     *                      (e.g. "Office", "Autotest_Contract_42").
     *                      See {@link Contract__c#getExtID__c()}.
     * @param contractItem  test data for the account's contracted item
     *                      (e.g. "DigitalLine Unlimited Standard").
     *                      Note: it should contain contract's quantity
     *                      and product's data name in NGBS (e.g. "LC_DL-UNL_50")!
     */
    protected void stepCreateContractInNGBS(String billingId, String packageId,
                                            String contractExtId, Product contractItem) {
        var contractsOnAccount = getContractsInNGBS(billingId, packageId);
        var activeContract = contractsOnAccount.stream()
                .filter(contract -> contract.startBillingCycleNumber == CONTRACT_ACTIVE)
                .findFirst();

        if (activeContract.isEmpty()) {
            step("Create a contract for the selected product on the NGBS account", () -> {
                var contractToCreate = createContractForOneLicense(contractExtId, contractItem);
                createContractInNGBS(billingId, packageId, contractToCreate);
            });
        }
    }

    /**
     * Create discount(s) for the NGBS account on the given product(s).
     * <br/>
     * Should be placed in {@code @BeforeAll/@BeforeEach} hooks for the tests
     * that require discounts on the existing NGBS account as a precondition.
     * <br/>
     * <b> Note: do NOT place this method in a loop to create multiple discounts!
     * Provide the collection of discounted products as an argument right away. </b>
     *
     * @param billingId             ID for the NGBS account (e.g. "235714001")
     * @param packageId             ID for the package on the account in NGBS (e.g. "235798001")
     * @param productsWithDiscounts test data for products that need to have a discount in NGBS
     *                              (must have non-null {@code dataName, discount, discountType, chargeTerm}
     *                              variables)
     */
    @Step("Create discount(s) for the given product(s) on the NGBS account")
    protected void stepCreateDiscountsInNGBS(String billingId, String packageId,
                                             Product... productsWithDiscounts) {
        var discountFactory = new DiscountNgbsFactory();
        for (var product : productsWithDiscounts) {
            var discountTemplateGroup = discountFactory.createDiscountTemplateGroup(product);
            createDiscountInNGBS(billingId, packageId, discountTemplateGroup);
        }
    }
}
