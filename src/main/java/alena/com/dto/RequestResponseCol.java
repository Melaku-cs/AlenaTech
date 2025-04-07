package alena.com.dto;
import alena.com.models.Request;
import alena.com.models.Response;
import java.util.ArrayList;
import java.util.List;
public class RequestResponseCol {
    private String sessionId;
    private String msisdn;
    private int currentItteration;
    private String headMenu;
    private String currentRequest;
    private String language;
    private List<Request> requestList = new ArrayList<Request>();
    private List<Response> responseList = new ArrayList<Response>();
    // New fields
    private String ipsOrgnlId;
    private String transactionAmount;
    private String note;
    private String bankNumber;
    private String wegaSettlementAcc;
    private String selectedTenantId;
    private String selectedAccount;
    private String selectedAccountNumber;
    private String accountNumber;
    private String tenantName;
    private String entityId ;
    private String fullName;

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getMsisdn() {
        return msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getCurrentItteration() {
        return currentItteration;
    }
    public void setCurrentItteration(int currentItteration) {
        this.currentItteration = currentItteration;
    }
    public String getHeadMenu() {
        return headMenu;
    }

    public void setHeadMenu(String headMenu) {
        this.headMenu = headMenu;
    }

    public String getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(String currentRequest) {
        this.currentRequest = currentRequest;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Response> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<Response> responseList) {
        this.responseList = responseList;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getBankNumber() {
        return bankNumber;
    }
    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }
    public String getWegaSettlementAcc() {
        return wegaSettlementAcc;
    }
    public void setWegaSettlementAcc(String wegaSettlementAcc) {
        this.wegaSettlementAcc = wegaSettlementAcc;
    }
    public String getSelectedTenantId() {
        return selectedTenantId;
    }
    public void setSelectedTenantId(String selectedTenantId) {
        this.selectedTenantId = selectedTenantId;
    }
    public String getSelectedAccount() {
        return selectedAccount;
    }
    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getIpsOrgnlId() {
        return ipsOrgnlId;
    }
    public void setIpsOrgnlId(String ipsOrgnlId) {
        this.ipsOrgnlId = ipsOrgnlId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName=tenantName;
    }

    public void setEntityId(String entityId) {
        this.entityId=entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setFullName(String fullName) {
        this.fullName=fullName;
    }
    public String getFullName() {
        return fullName;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    public List<Request> getRequestList() {
        return requestList;
    }
}