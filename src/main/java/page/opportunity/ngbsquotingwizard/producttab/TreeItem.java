package page.opportunity.ngbsquotingwizard.producttab;

import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Any item on the {@link ProductsPage}.
 * <br/>
 * Usually it has a tree-like structure, with items that can be single products,
 * or can contain expandable folders with products or without.
 *
 * @see Cbox
 * @see ProductItem
 */
public abstract class TreeItem {
    protected final SelenideElement treeItem;

    /**
     * Default constructor that defines the item in the DOM.
     *
     * @param treeItem web element for the main container of the item
     */
    public TreeItem(SelenideElement treeItem) {
        this.treeItem = treeItem;
    }

    /**
     * Get the main container of the item.
     *
     * @return web element for the main container
     */
    public SelenideElement getSelf() {
        return treeItem;
    }

    /**
     * Get the name of the item.
     * Can be the element for the name of the product or product folder.
     *
     * @return web element for the name of the item
     */
    public abstract SelenideElement getNameElement();

    /**
     * Get all the visible cboxes inside the item.
     * Note: if the item is {@link Cbox} it should be expanded first
     * to get the inner elements!
     *
     * @return list of all the visible product categories (cboxes) inside the item
     */
    public List<Cbox> getChildCboxes() {
        return treeItem.$$("[data-ui-auto='cbox-item']")
                .stream()
                .map(Cbox::new)
                .collect(toList());
    }

    /**
     * Get all the visible products inside the item.
     * Note: if the item is {@link Cbox} it should be expanded first
     * to get the inner elements!
     *
     * @return list of all the visible products inside the item
     */
    public List<ProductItem> getChildProducts() {
        return treeItem.$$("[data-ui-auto='license-item']")
                .stream()
                .map(ProductItem::new)
                .collect(toList());
    }
}
