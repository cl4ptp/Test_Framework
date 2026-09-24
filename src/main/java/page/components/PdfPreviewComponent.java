package page.components;

import page.salesforce.IframePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Custom web container for previewing PDF documents.
 * <br/>
 * It allows to either download the generated PDF file,
 * or to view it in the browser (depending on the browser's settings).
 */
public class PdfPreviewComponent extends IframePage {
    public final SelenideElement mainMessage = $("#main-message");
    public final SelenideElement openButton = $("#open-button");

    /**
     * Main no-arg constructor.
     * Defines the default iframe's location of the component.
     */
    public PdfPreviewComponent() {
        this($("iframe.pdf"));
    }

    /**
     * Parameterized constructor.
     *
     * @param iframeElement iframe's web element of the component
     */
    public PdfPreviewComponent(SelenideElement iframeElement) {
        super(iframeElement);
    }
}
