package page.opportunity.ngbsquotingwizard.modal;

import page.opportunity.ngbsquotingwizard.NGBSQuotingWizardPage;
import page.opportunity.ngbsquotingwizard.carttab.CartPage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Modal window in {@link NGBSQuotingWizardPage}
 * activated by clicking on "Promos" button on the {@link CartPage}.
 * <br/>
 * This dialog manages promos for products or categories of products.
 */
public class PromotionsManagerModal {
    private final SelenideElement dialogContainer = $("promo-modal");

    //  Buttons
    public final SelenideElement cancelButton = dialogContainer.$(byText("Cancel"));
    public final SelenideElement submitButton = dialogContainer.$(byText("Submit"));
    public final SelenideElement removeButton = dialogContainer.$(byText("Remove"));

    /**
     * Click on 'Apply' button for current promo.
     *
     * @param promo Promo Code to be applied (e.g. "QA-AUTO-DL-USD", "NYPROMO2").
     */
    public void clickApplyPromoButton(String promo) {
        var promoRow = dialogContainer.$("[title='" + promo + "']").closest("tbody");
        promoRow.$(byText("Apply")).click();
    }
}
