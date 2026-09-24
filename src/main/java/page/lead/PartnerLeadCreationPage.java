package page.lead;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static utilities.Constants.BASE_VF_URL;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * The Visualforce page that contains form of a new Partner Lead creation.
 */
public class PartnerLeadCreationPage extends BaseLeadCreationPage {

    public static final String PICKLIST_NONE_VALUE = "--None--";

    public final SelenideElement partnerContactInputField = $(".lookupInput input.parallelspace");
    public final SelenideElement partnerIdValueElement = $("[id$='partnerIdVal']");
    public final SelenideElement streetField = $("[id$='Street']");
    public final SelenideElement cityField = $("[title='City']");
    public final SelenideElement statePicklist = $("[id$='State']");
    public final ElementsCollection statesList = statePicklist.$$("option");
    public final SelenideElement zipCodeField = $("[title='Zip Code']");
    public final SelenideElement countryPicklist = $("[id$='Country']");
    public final SelenideElement forecastedUsersField = $("[title='Forecasted Users']");
    public final SelenideElement brandsPicklist = $("[id$='RCBrand']");
    public final ElementsCollection brandsList = brandsPicklist.$$("option");
    public final SelenideElement tierPicklist = $("[id$='tierPicklist']");
    public final ElementsCollection tiersList = tierPicklist.$$("option");

    public final SelenideElement estimatedCloseDateInput = $("[id$='closedate']");
    public final SelenideElement howDidYouAcquireThisLeadField = $("[id$='HowacquireLead']");
    public final SelenideElement descriptionField = $("[id$='desc']");

    /**
     * Constructor for Partner Lead Creation page with iframe's title.
     * Defines Partner Lead Creation page location.
     */
    public PartnerLeadCreationPage() {
        super("New Partner Lead");
    }

    /**
     * Open Partner Lead Creation page via direct link using Base URL.
     * <p> Note: contents for Base URL are usually provided via system properties. </p>
     *
     * @return opened Partner Lead Creation Page reference
     */
    public PartnerLeadCreationPage openPage() {
        open(BASE_VF_URL + "/apex/LeadCreationPartnerVAR");
        return this;
    }
}
