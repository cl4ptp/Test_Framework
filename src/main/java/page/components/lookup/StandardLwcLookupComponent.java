package page.components.lookup;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static java.time.Duration.ofSeconds;

/**
 * Standard LWC Lookup web component used to dynamically send text search requests as user inputs text into it.
 * <br/>
 * Can be found on many standard Lightning record pages.
 */
public class StandardLwcLookupComponent extends AbstractLookupComponent {

    private final SelenideElement spinner = searchInputComponentElement.$x(".//div[@role='status']");

    /**
     * Constructor with web element as a parameter.
     *
     * @param searchInputComponentElement SelenideElement that used to locate lookup element in DOM.
     */
    public StandardLwcLookupComponent(SelenideElement searchInputComponentElement) {
        super(searchInputComponentElement);
    }

    /**
     * Enter search query and select found element from the drop-down list.
     *
     * @param searchQuery name of the searched entity
     */
    public void selectItemInCombobox(String searchQuery) {
        clear();
        getInput().setValue(searchQuery);

        spinner.shouldBe(hidden);
        getSearchResults()
                .findBy(text(searchQuery))
                .shouldBe(visible, ofSeconds(20))
                .click();

        getInput().shouldHave(value(searchQuery));
    }
}
