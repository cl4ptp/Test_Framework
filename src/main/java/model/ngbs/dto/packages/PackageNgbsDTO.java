package model.ngbs.dto.packages;

import model.DataModel;
import page.components.packageselector.PackageSelector;
import utilities.ngbs.NGBSRestApiClient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data object with general package information for usage with NGBS API services.
 * <p></p>
 * Useful data structure for parsing responses from NGBS API package service
 * (see {@link NGBSRestApiClient} for a reference).
 */
public class PackageNgbsDTO extends DataModel {
    public String id;
    public String version;
    public String displayName;
    public String productName;
    public String currency;
    public String offerType;
    public Labels labels;

    /**
     * Get full name for the package.
     * Normally, it includes the display name and the version.
     * <p>
     * Useful when working with {@link PackageSelector}.
     * </p>
     *
     * @return package's full name (e.g. "RingCentral MVP Standard - v.1")
     */
    public String getFullName() {
        return displayName + " - v." + version;
    }

    /**
     * Get the brand name in the 'labels' object of the package info.
     *
     * @return main brand name for the package (e.g. "RingCentral", "Avaya Cloud Office", etc...)
     */
    public String getLabelsBrandName() {
        return labels.brand[0].brandName;
    }

    /**
     * Inner data structure for NGBS Package Info data object.
     * Represents data for package's labels that are used in sales flows
     * (package selection, signing up, etc...).
     */
    @JsonInclude(value = NON_NULL)
    public static class Labels {
        @JsonProperty("Brand")
        public Brand[] brand;

        /**
         * Inner data structure that stores Brand label values from NGBS Account's Package.
         */
        @JsonInclude(value = NON_NULL)
        public static class Brand {
            public String brandName;

            /**
             * Parameterized constructor for data mapper.
             *
             * @param brandName name of the brand to sell (e.g. "RingCentral")
             */
            public Brand(String brandName) {
                this.brandName = brandName;
            }
        }
    }
}
