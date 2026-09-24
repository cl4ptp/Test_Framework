package utilities.salesforce.sobjectfactories;

import utilities.salesforce.EnterpriseConnectionUtils;
import utilities.salesforce.ToolingConnectionUtils;

/**
 * Common factory class for creating various Salesforce's standard and custom SObjects,
 * like Accounts, Contacts, Opportunities, Leads, custom objects, etc...
 * <br/><br/>
 * By design, all child factories return the instance of SObject with its Salesforce ID
 * (and other parameters used during its creation).
 * Salesforce ID is obtained by inserting SObject into Salesforce database.
 */
public abstract class SObjectFactory {
    /**
     * Enterprise connection utility to interact with SFDC API in factories.
     */
    protected static final EnterpriseConnectionUtils CONNECTION_UTILS = EnterpriseConnectionUtils.getInstance();
    /**
     * Tooling connection utility to interact with SFDC API in factories.
     */
    protected static final ToolingConnectionUtils TOOLING_CONNECTION_UTILS = ToolingConnectionUtils.getInstance();
}
