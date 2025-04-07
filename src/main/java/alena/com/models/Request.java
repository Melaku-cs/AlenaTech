package alena.com.models;


import java.io.Serializable;

public class Request implements Serializable {
    private String msisdn;
    private String response;
    private String transactionId;
    private String transactionTime;
    private String ussdrequestString;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUssdrequestString() {
        return ussdrequestString;
    }

    public void setUssdrequestString(String ussdrequestString) {
        this.ussdrequestString = ussdrequestString;
    }
}