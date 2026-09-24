package leads.newbusiness;

import model.leadConvert.Dataset;
import model.ngbs.testdata.Package;
import utilities.JsonUtils;
import com.sforce.soap.enterprise.sobject.Contact;
import leads.BaseLeadConvertTest;

import static utilities.salesforce.sobjecthelper.ContactHelper.getFullName;
import static com.codeborne.selenide.Condition.*;

/**
 * Base test class related to the checks for Lead Conversion flow for RingCentral New Business accounts.
 */
public abstract class BaseRcNewBusinessLeadConvertFlowTest extends BaseLeadConvertTest {

    //  Test data
    protected final String officePackageFolderName;
    protected final Package officePackage;
    protected final String ringCentralBrand;
    protected final String ringCentralBI;

    /**
     * Class constructor.
     * Child test classes share a common test data among each other.
     */
    public BaseRcNewBusinessLeadConvertFlowTest() {
        super(JsonUtils.readConfigurationResource(
                "path_to_json.json",
                Dataset.class));

        officePackageFolderName = data.dataSets[0].packageFolders[0].name;
        officePackage = data.dataSets[0].packageFolders[0].packages[0];
        ringCentralBrand = data.dataSets[0].brandName;
        ringCentralBI = data.dataSets[0].businessIdentity.name;
    }

    /**
     * Check the selected contact in "Contact" section on the Lead Convert page.
     *
     * @param contact contact object to be checked
     */
    public void checkSelectedContact(Contact contact) {
        leadConvertPage.matchedContactsTableRadioButtons
                .findBy(selected)
                .shouldHave(exactValue(contact.getId()));

        leadConvertPage.contactInfoSelectedContactFullName
                .shouldHave(exactText(getFullName(contact)));
    }
}