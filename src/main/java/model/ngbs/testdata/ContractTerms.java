package model.ngbs.testdata;

/**
 * Data object that represents terms of a contract.
 * It is used to contain a test data for actual test actions
 * (changing the state of SUT; assertions; etc...)
 * <p></p>
 * Normally, this object consists of:
 * <p> - initial term of a contract in months (e.g. "12"), could be more than one for test purposes </p>
 * <p> - renewal term of a contract in months (e.g. "24") </p>
 */
public class ContractTerms {
    public String[] initialTerm;
    public String renewalTerm;
}
