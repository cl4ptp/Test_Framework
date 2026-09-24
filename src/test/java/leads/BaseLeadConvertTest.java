package leads;

import base.BaseSFDCTest;
import model.leadConvert.Dataset;
import model.ngbs.testdata.AreaCode;
import page.LoginPage;
import page.lead.convert.LeadConvertPage;
import page.opportunity.OpportunityRecordPage;
import utilities.ags.AGSRestApiClient;
import com.sforce.soap.enterprise.sobject.*;
import com.sforce.ws.ConnectionException;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static page.components.packageselector.PackageSelector.getChargeTermSelectorCssClass;
import static utilities.StringHelper.EMPTY_STRING;
import static utilities.salesforce.sobjectfactories.LeadFactory.createCustomerLeadInSFDC;
import static utilities.salesforce.sobjectfactories.LeadFactory.createPartnerLeadInSFDC;
import static utilities.salesforce.sobjectutils.UserUtils.getUserByProfile;
import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;
import static java.time.Duration.ofSeconds;

/**
 * Base test class for test cases related to Lead Convert functionality.
 */
public abstract class BaseLeadConvertTest extends BaseSFDCTest {

    protected User salesUser;
    protected User dealDeskUser;
    protected Lead salesLead;
    protected Lead partnerLead;

    //  Used pages
    protected final LoginPage loginPage = new LoginPage();
    protected final LeadConvertPage leadConvertPage = new LeadConvertPage();
    protected final OpportunityRecordPage opportunityPage = new OpportunityRecordPage();

    //  Test data
    /**
     * Main test data object that encapsulates data provided by test classes to be used in tests
     * (in the form of JSON parsed files or otherwise).
     */
    protected final Dataset data;
    protected final AreaCode localAreaCode;
    protected final AreaCode indiaAreaCode;

    /**
     * Constructor for test case class.
     *
     * @param data test data object in the form of
     *             {@link model.ngbs.testdata.Dataset} parsed from JSON file.
     */
    public BaseLeadConvertTest(Dataset data) {
        this.data = data;

        localAreaCode = new AreaCode("Local", "United States", "California", EMPTY_STRING, "619");
        indiaAreaCode = new AreaCode("Local", "India", "Maharashtra", EMPTY_STRING, "22");
    }

    @BeforeAll
    public void setUpBaseLeadConvertTestAll() {
        step("Get test users via SFDC API", () -> {
            salesUser = getUserByProfile("Sales Rep - Lightning");
            dealDeskUser = getUserByProfile("Deal Desk Lightning");
        });

        generateBillingAccounts();
    }

    @BeforeEach
    public void setUpBaseLeadConvertTestEach() {
        step("Create test Leads via SFDC API", () -> {
            salesLead = createCustomerLeadInSFDC(salesUser);
            partnerLead = createPartnerLeadInSFDC(salesUser);
        });

        step("Open test sandbox login page, log in to SF as test user and switch to Sales user", () -> {
            loginPage.openPage().login();
            loginAsUser(salesUser);
        });
    }

    /**
     * Check that Selected Package Name and Version, Charge Term, Contract that are shown in the right part of
     * Package Selector are equal to the expected values.
     *
     * @param expectedPackageName    expected Package Name (eg. <b>"RingCentral MVP Standard"<b/>)
     * @param expectedPackageVersion expected Package Version (eg. <b>"1"<b/>)
     * @param expectedChargeTerm     expected Charge Term value (eg. <b>"Monthly" or "Annual"<b/>)
     * @param expectedContractName   expected Contract Name (eg. <b>"Office Contract"<b/>)
     */
    @Step("Check a selected package in the Opportunity Info section")
    protected void checkSelectedPackage(String expectedPackageName,
                                        String expectedPackageVersion,
                                        String expectedChargeTerm,
                                        String expectedContractName) {
        leadConvertPage.packageSelector.selectedPackageName
                .shouldHave(exactTextCaseSensitive(expectedPackageName), ofSeconds(10));
        leadConvertPage.packageSelector.selectedPackageVersion
                .shouldHave(exactTextCaseSensitive("Version: " + expectedPackageVersion));
        leadConvertPage.packageSelector.chargeTermSelector
                .shouldHave(cssClass(getChargeTermSelectorCssClass(expectedChargeTerm)));
        leadConvertPage.packageSelector.contractSelector
                .shouldHave(text(expectedContractName));
    }

    /**
     * Press 'Convert' button on the Lead Convert page
     * and wait for a converted Opportunity's record page to load.
     * <p></p>
     * Note: only works for the positive flow
     * (where a Lead is supposed to be successfully converted into an Opportunity).
     */
    @Step("Press 'Convert' button on the Lead Convert page")
    protected void pressConvertButton() {
        leadConvertPage.convertButton.click();

        opportunityPage.entityTitle.shouldBe(visible, ofSeconds(120));
        opportunityPage.waitUntilLoaded();
    }

    /**
     * Check that the Lead has been successfully converted
     * (i.e. Account, Contact and Opportunity were created from the Lead).
     *
     * @param lead Lead object that was converted
     * @throws ConnectionException in case of errors while accessing API
     */
    @Step("Check that the Lead has been successfully converted")
    protected void checkLeadConversion(Lead lead) throws ConnectionException {
        var convertedLead = enterpriseConnectionUtils.querySingleRecord(
                "SELECT Id, ConvertedAccountId, ConvertedOpportunityId, ConvertedContactId " +
                        "FROM Lead " +
                        "WHERE Id = '" + lead.getId() + "'",
                Lead.class);

        assertThat(convertedLead.getConvertedOpportunityId())
                .as("Converted Opportunity ID")
                .isNotNull();
        assertThat(convertedLead.getConvertedAccountId())
                .as("Converted Account ID")
                .isNotNull();
        assertThat(convertedLead.getConvertedContactId())
                .as("Converted Contact ID")
                .isNotNull();
    }

    /**
     * Test steps to make account available for search on Partner Lead Conversion page.
     *
     * @param account Account to be updated.
     * @param contact Contact to get phone for Lead update.
     */
    protected void preparePartnerLeadTestSteps(Account account, Contact contact) {
        step("Set Account.Partner_ID__c = Lead.LeadPartnerID__c for the test Account", () -> {
            account.setPartner_ID__c(partnerLead.getLeadPartnerID__c());
            enterpriseConnectionUtils.update(account);
        });

        step("Set Lead.Phone = Account's Contact.Phone for the test Lead", () -> {
            partnerLead.setPhone(contact.getPhone());
            enterpriseConnectionUtils.update(partnerLead);
        });
    }

    /**
     * Generate accounts in Billing for testing of Existing Business functionality.
     * <p> Account generation works if account generation is active either globally or for a single test. </p>
     * <p> Account generation works according to scenarios that can be found in test data. </p>
     */
    private void generateBillingAccounts() {
        for (var dataSet : data.dataSets) {
            if (isGenerateAccounts() && dataSet.scenario != null && !dataSet.scenario.isBlank()) {
                step("Generate Existing Business Account in Billing for scenario '" + dataSet.scenario + "'", () -> {
                    var accountDetailsAGS = AGSRestApiClient.createAccount(dataSet.scenario);

                    dataSet.billingId = accountDetailsAGS.getAccountBillingId();
                    dataSet.packageId = accountDetailsAGS.getAccountPackageId();
                });
            }
        }
    }
}
