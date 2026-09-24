package leads.newbusiness;

import com.sforce.soap.enterprise.sobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static page.lead.convert.LeadConvertPage.NO_CONTACTS_MATCH_THE_CHOSEN_ACCOUNT_MESSAGE;
import static utilities.salesforce.sobjectfactories.AccountFactory.createNewCustomerAccountWithoutContactInSFDC;
import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;
import static java.time.Duration.ofSeconds;

@Tag("LeadConvert")
public class CreatingContactLeadConvertFlowTest extends BaseRcNewBusinessLeadConvertFlowTest {
    private Account newBusinessAccount;

    public CreatingContactLeadConvertFlowTest() {
        super();
    }

    @BeforeEach
    public void setUpTest() {
        step("Create New Business Account (without Contact record) via API", () -> {
            newBusinessAccount = createNewCustomerAccountWithoutContactInSFDC(salesUser, data.dataSets[0].currencyISOCode);
        });
    }

    @Test
    public void test() {
        step("1. Open Lead Convert page for the test lead", () ->
                leadConvertPage.openPage(salesLead)
        );

        step("2. Select account that exists in SFDC, and click 'Apply' button in Account Info section " +
                "and verify that Contact section is empty. No matching contacts found.", () -> {
            leadConvertPage.existingAccountSearchInput.selectItemInCombobox(newBusinessAccount.getName());
            leadConvertPage.accountInfoApplyButton.click();

            leadConvertPage.contactInfoSection.shouldBe(visible, ofSeconds(30));
            leadConvertPage.contactDetailsInfoSection.shouldHave(
                    exactTextCaseSensitive(NO_CONTACTS_MATCH_THE_CHOSEN_ACCOUNT_MESSAGE));
        });

        step("3. Click 'Edit' in Opportunity Section, select 'RingCentral' business identity, Office package, " +
                "populate area code and click 'Apply'", () -> {
            leadConvertPage.opportunityLoadingBar.shouldBe(hidden, ofSeconds(60));
            leadConvertPage.opportunityInfoEditButton.click();
            leadConvertPage.opportunityLoadingBar.shouldBe(hidden, ofSeconds(60));

            leadConvertPage.businessIdentityPicklist.selectOption(ringCentralBI);
            leadConvertPage.packageSelector.selectPackage(officePackageFolderName, officePackage);

            leadConvertPage.defaultAreaCodeSelector.selectCode(localAreaCode);
            leadConvertPage.opportunityInfoApplyButton.scrollIntoView(true).click();
        });

        step("4. Select Contact Role and click 'Apply' button in Contact Role section",
                leadConvertPage::selectDefaultOpportunityRole
        );

        step("5. Press 'Convert' button",
                this::pressConvertButton
        );
    }
}
