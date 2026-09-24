package utilities.salesforce.sobjecthelper;

import com.sforce.soap.enterprise.sobject.Area_Codes__c;

/**
 * Helper class to facilitate operations on {@link Area_Codes__c} objects.
 */
public class AreaCodeHelper extends SObjectHelper {
    /**
     * Get composite full name from Area_Codes__c object.
     * Useful in some cases when user needs to choose value in area code full name format from UI.
     * <p></p>
     * <i>Example: San-Carlos Belmont, CA, United States (650)</i>
     *
     * @param areaCode Area_Codes__c object to get the name from
     * @return full name of Area Code
     */
    public static String getFullName(Area_Codes__c areaCode) {
        var result = new StringBuilder();

        result.append(areaCode.getCity__c()).append(", ");

        if (areaCode.getState_Abbreviation__c() != null) {
            result.append(areaCode.getState_Abbreviation__c()).append(", ");
        }

        result.append(areaCode.getCountry__c());
        result.append(" (").append(areaCode.getName()).append(")");

        return result.toString();
    }
}
