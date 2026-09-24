package model.scp;

import model.DataModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Data object with response information for usage with SCP API services.
 */
@JsonInclude(value = NON_NULL)
public class ScpOperationResponseDTO extends DataModel {
    public Extensions extensions;
    public Error[] errors;
    public Data data;

    /**
     * Data object that represents response's service data (id, timestamp).
     */
    @JsonInclude(value = NON_NULL)
    public static class Extensions {
        public String requestId;
        public String timestamp;

        //  For errors only
        public String status;
        public String code;
        public String classification;
    }

    /**
     * Data object that represents error details in the response (if there's any).
     */
    @JsonInclude(value = NON_NULL)
    public static class Error extends DataModel {
        public String message;
        public String[] path;
        public Extensions extensions;
    }

    /**
     * Data object that represents an actual data (e.g. account data)
     * that was returned inside the response.
     */
    @JsonInclude(value = NON_NULL)
    public static class Data {
        public Account account;

        /**
         * Data object that represents any account data
         * (personal, service, verification, etc.).
         */
        @JsonInclude(value = NON_NULL)
        public static class Account {
            public String[] testerFlags;
            public AccountInfo accountInfo;

            /**
             * Data object that represents the account's data.
             */
            @JsonInclude(value = NON_NULL)
            public static class AccountInfo {
                public String accountId;
                public ServiceInfo serviceInfo;

                /**
                 * Data object that represents service data on the account
                 * (brand name, brand ID, tester flags, etc.).
                 */
                @JsonInclude(value = NON_NULL)
                public static class ServiceInfo {
                    public String[] testerFlags;
                }
            }
        }
    }
}
