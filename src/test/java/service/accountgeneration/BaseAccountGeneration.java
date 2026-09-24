package service.accountgeneration;

import base.BaseSFDCTest;
import model.accountgeneration.CreateNgbsAccountsDTO;
import model.ags.AccountAgsDTO;
import utilities.ags.AGSRestApiClient;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static model.scp.ScpOperationRequestDTO.Variables.TesterFlagsItem.*;
import static utilities.scp.ScpRestApiClient.getTesterFlagsOnAccount;
import static utilities.scp.ScpRestApiClient.removeTesterFlagsOnAccount;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Base class for classes related to creating Accounts in NGBS and SFDC.
 * <br/>
 * Make sure to provide  System Configuration Portal REST API base URL (optional)
 * via system properties: {@code scp.rest.baseUrl}.
 */
public abstract class BaseAccountGeneration extends BaseSFDCTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("AccountGeneration");

    /**
     * Generate a new account in NGBS via AGS.
     * <br/>
     * This method also handles additional actions:
     * <p> - removing tester flags from the account via System Configuration Portal </p>
     * <p> - creating discount(s) in NGBS (if user has provided discounts' data) </p>
     * <p> - creating contract in NGBS (if user has provided contract's data) </p>
     *
     * @param accountData data object parsed from user's input parameter
     *                    for creating Account in NGBS
     */
    protected void createAccountInNGBS(CreateNgbsAccountsDTO accountData) {
        if (accountData.scenario == null || accountData.scenario.isBlank()) {
            throw new IllegalArgumentException("AGS Scenario is not provided in the current Account data object! \n" +
                    "Account data: " + accountData);
        }

        var accountAGS = AGSRestApiClient.createAccount(accountData.scenario);

        removeTesterFlagsOnAccountViaSCP(accountAGS);

        accountData.billingId = accountAGS.getAccountBillingId();
        accountData.packageId = accountAGS.getAccountPackageId();
        accountData.rcUserId = accountAGS.rcUserId;

        if (accountData.contract != null &&
                accountData.contract.contractExtId != null && accountData.contract.contractProduct != null) {
            stepCreateContractInNGBS(
                    accountData.billingId, accountData.packageId,
                    accountData.contract.contractExtId, accountData.contract.contractProduct
            );
        }

        if (accountData.discounts != null && accountData.discounts.length != 0) {
            stepCreateDiscountsInNGBS(accountData.billingId, accountData.packageId, accountData.discounts);
        }
    }

    /**
     * Connect to System Configuration Portal via REST API, remove RC tester flags on the NGBS account,
     * and check that this change is successful.
     *
     * @param account account data from AGS with NGBS billing ID, package ID, RC User ID, etc...
     */
    @Step("Remove RC Tester flags on the NGBS account via System Configuration Portal API")
    private void removeTesterFlagsOnAccountViaSCP(AccountAgsDTO account) {
        var rcUserId = account.rcUserId;

        step("Remove Tester Flags ('Tester', 'Auto-delete', 'Send real request to Zoom', 'Send Real Request to Distributor') " +
                "on the NGBS Account", () -> {
            var removeTesterFlagsResponse = removeTesterFlagsOnAccount(rcUserId);

            assertThat(removeTesterFlagsResponse.errors)
                    .as("Errors in the response on changing Tester Flags (should not exist)")
                    .isNull();
            assertThat(removeTesterFlagsResponse.data.account.testerFlags)
                    .as("List of RC Tester Flags in the response on changing Tester Flags")
                    .contains(NO_EMAIL_NOTIFICATIONS)
                    .doesNotContain(TESTER, AUTO_DELETE, SEND_REAL_REQUESTS_TO_ZOOM, SEND_REAL_REQUESTS_TO_DISTRIBUTOR);
        });

        step("Check the list of the Tester Flags on the NGBS account after the change", () -> {
            var getTesterFlagsResponse = getTesterFlagsOnAccount(rcUserId);

            assertThat(getTesterFlagsResponse.errors)
                    .as("Errors in the response on getting Tester Flags (should not exist)")
                    .isNull();
            assertThat(getTesterFlagsResponse.data.account.accountInfo.serviceInfo.testerFlags)
                    .as(String.format("List of RC Tester Flags on NGBS Account with User ID = %s after the change", rcUserId))
                    .contains(NO_EMAIL_NOTIFICATIONS)
                    .doesNotContain(TESTER, AUTO_DELETE, SEND_REAL_REQUESTS_TO_ZOOM, SEND_REAL_REQUESTS_TO_DISTRIBUTOR);
        });

        LOGGER.info("RC Tester Flags are removed successfully for NGBS account with billingId = " +
                account.getAccountBillingId());
    }
}
