package utilities.salesforce.sobjecthelper;

import com.sforce.soap.enterprise.ProcessWorkitemRequest;
import com.sforce.soap.enterprise.sobject.ProcessInstanceWorkitem;
import com.sforce.ws.ConnectionException;

/**
 * Helper class to facilitate operations on {@link ProcessWorkitemRequest} objects.
 */
public class ProcessWorkitemRequestHelper extends SObjectHelper {

    //  For 'Action' field
    public static final String APPROVE_ACTION = "Approve";
    public static final String REJECT_ACTION = "Reject";

    /**
     * Set default values for approval process work item to be sent via API
     * to approve SFDC record.
     *
     * @param request   ProcessWorkitemRequest to set up
     * @param sObjectId ID of the SFDC record to be approved by this work item
     * @throws ConnectionException in case of errors while accessing API
     */
    public static void setFieldsForApproveAction(ProcessWorkitemRequest request, String sObjectId)
            throws ConnectionException {
        var piWorkItem = CONNECTION_UTILS.querySingleRecord(
                "SELECT Id " +
                        "FROM ProcessInstanceWorkitem " +
                        "WHERE ProcessInstance.TargetObjectId = '" + sObjectId + "'",
                ProcessInstanceWorkitem.class);
        request.setWorkitemId(piWorkItem.getId());

        request.setAction(APPROVE_ACTION);
        request.setComments("Approved by QA Automation SOAP API Call");
    }

    /**
     * Set default values for approval process work item to be sent via API
     * to reject SFDC record.
     *
     * @param request   ProcessWorkitemRequest to set up
     * @param sObjectId ID of the SFDC record to be rejected by this work item
     * @throws ConnectionException in case of errors while accessing API
     */
    public static void setFieldsForRejectAction(ProcessWorkitemRequest request, String sObjectId)
            throws ConnectionException {
        var piWorkItem = CONNECTION_UTILS.querySingleRecord(
                "SELECT Id " +
                        "FROM ProcessInstanceWorkitem " +
                        "WHERE ProcessInstance.TargetObjectId = '" + sObjectId + "'",
                ProcessInstanceWorkitem.class);
        request.setWorkitemId(piWorkItem.getId());

        request.setAction(REJECT_ACTION);
        request.setComments("Rejected by QA Automation SOAP API Call");
    }
}
