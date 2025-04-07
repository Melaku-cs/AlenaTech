package alena.com.service;

import alena.com.models.Request;
import alena.com.session.SessionManager;
import alena.com.dao.CustomerInfoViewDao;
import alena.com.dto.RequestResponseCol;
import alena.com.models.CustomerInfoView;
import alena.com.models.L10n;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.json.JSONObject;
import javax.net.ssl.SSLContext;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
@Path("/alena")
@RequestScoped
@Stateful
public class AlenaTech {
//    private static final Logger LOGGER = Logger.getLogger(AlenaTech.class.getName());
private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @PersistenceContext(unitName = "primary")
    private EntityManager em;
    @EJB
    CustomerInfoViewDao customerInfoViewDao;
    private static final String nextCharacter = "+";
    private static final int maxBankListValue = 8;
    private static final int accountListStartIteration = 3;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response call(final RequestResponseCol reqres) {
        List<CustomerInfoView> customerAccounts = this.customerInfoViewDao.getAccounts(reqres.getMsisdn());
        javax.ws.rs.core.Response.ResponseBuilder builder = null;
        RequestResponseCol sessionState = SessionManager.getSession(reqres.getSessionId());
        int originalIteration = this.calculateOriginalIteration(reqres);
        if (sessionState != null) {
            reqres.setBankNumber(sessionState.getBankNumber());
        }
        System.out.print(customerAccounts);
        try {
            if (customerAccounts.isEmpty()) {
                String message = this.getLanguageString(reqres.getLanguage(), "no_account");
                alena.com.models.Response res = new alena.com.models.Response();
                res.setAction("end");
                res.setTransactionId(reqres.getSessionId());
                builder = buildResponse(reqres, message, res);
            }
            System.out.println("*********" + originalIteration);
            LOGGER.warning("*********wwwwswsd" + originalIteration);;
            LOGGER.fine("*********weddddwwwswsd" + originalIteration);;
            LOGGER.finer("*********wwwwswsdiukiuk" + originalIteration);;
            LOGGER.config("*********wwwwswsd" + originalIteration);;
            if (originalIteration == 2) {
                builder = returnAccountList(reqres, customerAccounts, builder);
                return builder.build();
            } else if (originalIteration == 3)
            {
                System.out.println("*********   3" + originalIteration);
//                String bankNumber = reqres.getCurrentRequest();
//                int selectedIndex;
//                try {
//                    selectedIndex = Integer.parseInt(bankNumber) - 1;
//                } catch (NumberFormatException e) {
//                    String message = this.getLanguageString(reqres.getLanguage(), "inv");
//                    alena.com.models.Response res = new alena.com.models.Response();
//                    res.setAction("end");
//                    res.setTransactionId(reqres.getSessionId());
//                    builder = buildResponse(reqres, message, res);
//                    return builder.build();
//                }
//                if (selectedIndex < 0 || selectedIndex >= customerAccounts.size()) {
//                    String message = this.getLanguageString(reqres.getLanguage(), "inv");
//                    alena.com.models.Response res = new alena.com.models.Response();
//                    res.setAction("end");
//                    res.setTransactionId(reqres.getSessionId());
//                    builder = buildResponse(reqres, message, res);
//                } else {
//                    CustomerInfoView selectedAccount = customerAccounts.get(selectedIndex);
//                    String selectedBankNumber = selectedAccount.getAccount();
////                    LOGGER.info("selectedBankNumber: " + selectedBankNumber);
//                    reqres.setBankNumber(selectedBankNumber);
//                }
                builder = this.getTenantList(reqres);
            } else if (originalIteration == 4) {

                try {
                    int selectedTenantIdIndex = Integer.parseInt(reqres.getCurrentRequest()) - 1;

//               LOGGER.info("index  on 4"+selectedTenantIdIndex);
                List<String> allTenantIds = this.customerInfoViewDao.getAllTenantIds();
                if (selectedTenantIdIndex >= 0 && selectedTenantIdIndex < allTenantIds.size()) {
                    String selectedTenantId = allTenantIds.get(selectedTenantIdIndex);
                    reqres.setSelectedTenantId(selectedTenantId);
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("request");
                    String message = this.getLanguageString(reqres.getLanguage(), "enacc");
                    builder = buildResponse(reqres, message, res);
                } else {
                    String message = this.getLanguageString(reqres.getLanguage(), "inv");
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("end");
                    builder = buildResponse(reqres, message, res);
                }
                } catch (NumberFormatException e) {
                    String message = this.getLanguageString(reqres.getLanguage(), "inv");
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setAction("end");
                    res.setTransactionId(reqres.getSessionId());
                    builder = buildResponse(reqres, message, res);
                    return builder.build();
                }
            }
            else if (originalIteration == 5) {
                String accountNumber = reqres.getCurrentRequest();
//                LOGGER.info("Account number received: " + accountNumber);
                if (accountNumber == null || accountNumber.isEmpty()) {
                    String message = this.getLanguageString(reqres.getLanguage(), "invAcc");
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("end");
                    builder = buildResponse(reqres, message, res);
                } else {
                    try {
                        Integer.parseInt(accountNumber);
                        reqres.setAccountNumber(accountNumber);
                        String response_token = fetchToken();
                        JSONObject token = new JSONObject(response_token);
                        JSONObject payload = new JSONObject();
                        String access_token = token.getString("access_token");

//                        LOGGER.info("Total number of requests in list: " + reqres.getRequestList().size());
//                        for (int i = 0; i < reqres.getRequestList().size(); i++) {
//                            LOGGER.info("Request at index " + i + ": " + reqres.getRequestList().get(i).getUssdrequestString());
//                        }
                        int requestListSize = reqres.getRequestList().size();
                        String selectedOption3 = reqres.getRequestList().get(requestListSize - 1).getUssdrequestString();
//                        LOGGER.info("User selected option (Index 2): " + selectedOption3);
                        int selectedIndex;
                        try {
                            selectedIndex = Integer.parseInt(selectedOption3) ; // Convert to zero-based index
                        } catch (NumberFormatException e) {
//                            LOGGER.info("Invalid selection, not a number: " + selectedOption3);
                            String message = this.getLanguageString(reqres.getLanguage(), "inv");
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("end");
                            builder = buildResponse(reqres, message, res);
                            return null;
                        }
                        List<String> allTenantIds = this.customerInfoViewDao.getAllTenantIds();
//                        LOGGER.info("Available Tenant IDs: " + allTenantIds);

                        if (selectedIndex < 0 || selectedIndex >= allTenantIds.size()) {
//                            LOGGER.info("Invalid Tenant selection index: " + selectedIndex);
                            String message = this.getLanguageString(reqres.getLanguage(), "inv");
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("end");
                            builder = buildResponse(reqres, message, res);
                            return builder.build();
                        }
                        // Retrieve the correct Tenant ID
                        String selectedTenantId = allTenantIds.get(selectedIndex);
//                        LOGGER.info("Selected Tenant ID: " + selectedTenantId);
                        if (selectedTenantId == null || selectedTenantId.isEmpty()) {
//                            LOGGER.info("------Selected tenant ID is empty");
                            String message = this.getLanguageString(reqres.getLanguage(), "inv");
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("end");
                            builder = buildResponse(reqres, message, res);
                        } else {
                            payload.put("accountNumber", accountNumber);
                            payload.put("Tenantid", selectedTenantId);
                            String jsonPayload = payload.toString();
                            String verify_response = verifyAccount(access_token, jsonPayload);
//                            LOGGER.info("Verify response: " + verify_response);
                            JSONObject jsonResponse = new JSONObject(verify_response);
                            String responseCode = jsonResponse.optString("ResponseCode");
                            String responseDesc = jsonResponse.optString("ResponseDesc");
                            String entityId = jsonResponse.optString("entityId");
                            String fullName = jsonResponse.optString("FullName");
                            reqres.setFullName(fullName);
                            reqres.setEntityId(entityId);
                            reqres.setSelectedTenantId(selectedTenantId);
                            SessionManager.storeSession(reqres.getSessionId(), reqres);
                            if ("0".equals(responseCode)) {
                                alena.com.models.Response res = new alena.com.models.Response();
                                res.setTransactionId(reqres.getSessionId());
                                res.setAction("request");
                                String message = this.getLanguageString(reqres.getLanguage(), "amou");
//
                                message=message.replace("@name", fullName);
//
                                builder = buildResponse(reqres, message, res);
                            } else {
                                String message = this.getLanguageString(reqres.getLanguage(), "invAc");
                                alena.com.models.Response res = new alena.com.models.Response();
                                res.setTransactionId(reqres.getSessionId());
                                res.setAction("end");
                                builder = buildResponse(reqres, message, res);
                            }
                        }
                    } catch (NumberFormatException e) {
//                        LOGGER.info("Invalid account number format: " + accountNumber);
                        String message = this.getLanguageString(reqres.getLanguage(), "invAc");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setTransactionId(reqres.getSessionId());
                        res.setAction("end");
                        builder = buildResponse(reqres, message, res);
                    }
                }
            }
            else if (originalIteration == 6) {
                String amount = reqres.getCurrentRequest();
                if (amount == null || amount.isEmpty()) {
                    String message = this.getLanguageString(reqres.getLanguage(), "'invAm");
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("end");
                    builder = buildResponse(reqres, message, res);
                } else {
                    try {
                        double transactionAmount = Double.parseDouble(amount);
                        if (transactionAmount <= 0) {
                            String message = this.getLanguageString(reqres.getLanguage(), "invAm");
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("end");
                            builder = buildResponse(reqres, message, res);
                        } else {
                            reqres.setTransactionAmount(amount);
//                            SessionManager.storeSession(reqres.getSessionId(), reqres);
//                            LOGGER.info("Total number of requests in list: " + reqres.getRequestList().size());
//                            for (int i = 0; i < reqres.getRequestList().size(); i++) {
//                                LOGGER.info("Request at index " + i + ": " + reqres.getRequestList().get(i).getUssdrequestString());
//
//                            }
                            String selectedOption3 = reqres.getRequestList().get(reqres.getRequestList().size() - 2).getUssdrequestString();
                           int tenantId= Integer.parseInt(selectedOption3) ;
                            List<String> allTenantIds = this.customerInfoViewDao.getAllTenantIds();
                            String selectedTenantId = allTenantIds.get(tenantId);
                            String accountNumber = reqres.getRequestList().get(reqres.getRequestList().size() -1).getUssdrequestString();
//                            String beneficiaryName = reqres.getRequestList().get(reqres.getRequestList().size() - 3).getUssdrequestString();
                            String beneficiaryName = sessionState.getFullName();
                            String message = this.getLanguageString(reqres.getLanguage(), "alconf");
                            LOGGER.info("massagesdskjdksd"+message);
                            message = message.replace("@beneficiaryName", beneficiaryName + "\n")
                                    .replace("@transactionAmount", amount + "\n")
                                    .replace("@accountNumber", accountNumber + "\n")
                                    .replace("@tenantId", selectedTenantId + "\n");

//                            LOGGER.info("transactionPayload:======= " + message);
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("request");
                            builder = buildResponse(reqres, message, res);
                        }
                    } catch (NumberFormatException e) {
                        String message = this.getLanguageString(reqres.getLanguage(), "invAm");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setTransactionId(reqres.getSessionId());
                        res.setAction("end");
                        builder = buildResponse(reqres, message, res);
                    }
                }
            } else if (originalIteration == 7) {
                if (Integer.parseInt(reqres.getCurrentRequest()) == 1) {
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("request");
                    String message = this.getLanguageString(reqres.getLanguage(), "pass");
                    builder = buildResponse(reqres, message, res);
                } else {
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("end");
                    String message =  this.getLanguageString(reqres.getLanguage(), "tcnl");
                    builder = buildResponse(reqres, message, res);
                }
            } else if (originalIteration == 8) {
                String pin = reqres.getCurrentRequest();
                Boolean auth = PinAuthentication(pin, reqres.getMsisdn());
                if (auth) {
                    String bankNumber = reqres.getRequestList().get(reqres.getRequestList().size() -5).getUssdrequestString();
                    //====================
                    int selectedIndex;
                    LOGGER.info("selectedBankNumber: " + bankNumber);
                    try {
                        selectedIndex = Integer.parseInt(bankNumber) - 1;
                    } catch (NumberFormatException e) {
                        String message = this.getLanguageString(reqres.getLanguage(), "inv");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setAction("end");
                        res.setTransactionId(reqres.getSessionId());
                        builder = buildResponse(reqres, message, res);
                        return builder.build();
                    }
                    if (selectedIndex < 0 || selectedIndex >= customerAccounts.size()) {
                        String message = this.getLanguageString(reqres.getLanguage(), "inv");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setAction("end");
                        res.setTransactionId(reqres.getSessionId());
                        builder = buildResponse(reqres, message, res);
                    } else {
                        CustomerInfoView selectedAccount = customerAccounts.get(selectedIndex);
                        String selectedBankNumber = selectedAccount.getAccount();
                        LOGGER.info("selectedBankNumber: " + selectedBankNumber);
                        reqres.setBankNumber(selectedBankNumber);

                    //===================
                    String accountNumber = reqres.getRequestList().get(reqres.getRequestList().size() - 3).getUssdrequestString();
                    String transactionAmount = reqres.getRequestList().get(reqres.getRequestList().size() - 2).getUssdrequestString();
                    String entityId =sessionState.getEntityId();
                    LOGGER.info("Total number of requests in list: " + reqres.getRequestList().size());
                    for (int i = 0; i < reqres.getRequestList().size(); i++) {
                        LOGGER.info("Request at index " + i + ": " + reqres.getRequestList().get(i).getUssdrequestString());
                    }
                    int requestListSize = reqres.getRequestList().size();
                    String selectedOption4 = reqres.getRequestList().get(requestListSize - 4).getUssdrequestString();
                    LOGGER.info("User selected option (selectedOption4): " + selectedOption4);
//                    LOGGER.info("bank number from the list: " + selectedBankNumber);
                    int TenantIndex;
                    try {
                        TenantIndex = Integer.parseInt(selectedOption4) ; // Convert to zero-based index
                    } catch (NumberFormatException e) {
//                        LOGGER.info("Invalid selection, not a number: " + selectedOption3);
                        String message = this.getLanguageString(reqres.getLanguage(), "inv");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setTransactionId(reqres.getSessionId());
                        res.setAction("end");
                        builder = buildResponse(reqres, message, res);
                        return null;
                    }

                    List<String> allTenantIds = this.customerInfoViewDao.getAllTenantIds();
//                    LOGGER.info("Available Tenant IDs: " + allTenantIds);
                    // Validate selected index is within range
                    if (TenantIndex < 0 || TenantIndex >= allTenantIds.size()) {
                        LOGGER.info("Invalid Tenant selection index: " + TenantIndex);
                        String message = this.getLanguageString(reqres.getLanguage(), "inv");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setTransactionId(reqres.getSessionId());
                        res.setAction("end");
                        builder = buildResponse(reqres, message, res);
                        return builder.build();
                    }

                    String selectedTenantId = allTenantIds.get(TenantIndex);
//                    LOGGER.info("Selected Tenant ID: " + selectedTenantId);
                    if (selectedTenantId == null || selectedTenantId.isEmpty()) {
                        LOGGER.info("------Selected tenant ID is empty");
                        String message = this.getLanguageString(reqres.getLanguage(), "inv");
                        alena.com.models.Response res = new alena.com.models.Response();
                        res.setTransactionId(reqres.getSessionId());
                        res.setAction("end");
                        builder = buildResponse(reqres, message, res);
                    } else {
                        String wegaSettlementAcc = this.customerInfoViewDao.getWegaSettlementAcc(selectedTenantId);
                        JSONObject transactionPayload = new JSONObject();
                        transactionPayload.put("accountNumber", accountNumber);
                        transactionPayload.put("bankNumber", selectedBankNumber);
                        transactionPayload.put("checkNumber", "wwww");
                        transactionPayload.put("note", "ttttt");
                        transactionPayload.put("transactionAmount", transactionAmount);
                        transactionPayload.put("Tenantid", selectedTenantId);
                        transactionPayload.put("WegaSettlementAcc", wegaSettlementAcc);
                        transactionPayload.put("entityId", entityId);
                        String url = "https://wso2apim.wegagentraining.com:8243/WegagenAllena/1.0.0/transaction";
                        String response_token = fetchToken();
                        JSONObject token = new JSONObject(response_token);
                        String access_token = token.getString("access_token");
                        String PerformPayload = transactionPayload.toString();
                        //=============================liit check
                        String limitResponse=  limitCheck(Integer.parseInt(transactionAmount),reqres.getMsisdn());
                        JSONObject limitjsonResponse = new JSONObject(limitResponse);
                        Boolean status=limitjsonResponse.optBoolean("status");
                        String  description=limitjsonResponse.optString("description");
                        System.out.println("limitResponse +++++++++++"+limitResponse);
                        System.out.println("description +++++++++++"+description);
                        System.out.println("status +++++++++++"+status);
                        if (!status) {
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("end");
                            builder = buildResponse(reqres, description, res);
                            SessionManager.removeSession(reqres.getSessionId());
                            return builder.build();
                        }

                        //==============================
                        String transaction_response = performTransaction(url, access_token, PerformPayload);
//                        System.out.println("Transaction Response====" + transaction_response);
                        if (transaction_response != null) {
                            transaction_response = transaction_response.replace("[\"", "").replace("\"]", "");
                            try {
                                JSONObject jsonResponse = new JSONObject(transaction_response);
                                String responseCode = jsonResponse.optString("ResponseCode");
                                String responseDesc = jsonResponse.optString("ResponseDesc");
                                String transactionRef = jsonResponse.optString("TransactionReference");
                                if ("0".equals(responseCode)) {
                                    alena.com.models.Response res = new alena.com.models.Response();
                                    res.setTransactionId(reqres.getSessionId());
                                    res.setAction("end");
                                    String message = this.getLanguageString(reqres.getLanguage(), "alts");
                                    message = message.replace("@ref", transactionRef);
                                    builder = buildResponse(reqres, message, res);
                                } else {
                                    String message = this.getLanguageString(reqres.getLanguage(), "altf");
                                    alena.com.models.Response res = new alena.com.models.Response();
                                    res.setTransactionId(reqres.getSessionId());
                                    res.setAction("end");
                                    builder = buildResponse(reqres, message, res);
                                }
                            } catch (Exception e) {
                                String message = this.getLanguageString(reqres.getLanguage(), "con");
                                alena.com.models.Response res = new alena.com.models.Response();
                                res.setTransactionId(reqres.getSessionId());
                                res.setAction("end");
                                builder = buildResponse(reqres, message, res);
                            }
                        } else {
                            String message = this.getLanguageString(reqres.getLanguage(), "con");
                            alena.com.models.Response res = new alena.com.models.Response();
                            res.setTransactionId(reqres.getSessionId());
                            res.setAction("end");
                            builder = buildResponse(reqres, message, res);
                        }
                    } }} else {
                    String message = this.getLanguageString(reqres.getLanguage(), "invlp");
                    alena.com.models.Response res = new alena.com.models.Response();
                    res.setTransactionId(reqres.getSessionId());
                    res.setAction("end");
                    builder = buildResponse(reqres, message, res);
                }}
             else {
                alena.com.models.Response res = new alena.com.models.Response();
                res.setAction("end");
                builder = buildResponse(reqres, this.getLanguageString(reqres.getLanguage(), "cho"), res);// returns for client "I do not know what you chose"
            }
            return builder.build();
         }catch (Exception e) {
            System.out.println("Catch Error: " + e.getMessage());
            alena.com.models.Response res = new alena.com.models.Response();
            res.setAction("end");
            builder = buildResponse(reqres, "connection lost please try again !", res);
        }
        return builder.build();
    }

    private String limitCheck(int amount, String phoneNumber) {
        if (phoneNumber.startsWith("09")) {
            phoneNumber = "251" + phoneNumber.substring(1);
        } else if (phoneNumber.startsWith("9")) {
            phoneNumber = "251" + phoneNumber;
        }
        String url = "http://10.57.40.121:8080/Limit/rest/getLimit";
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("phone", phoneNumber);
        jsonPayload.put("txnType", "FTOTHR");
        jsonPayload.put("amount", amount);
        String limitPayload=jsonPayload.toString();
        System.out.println("limit  payload: " + jsonPayload);
        Client client = ClientBuilder.newBuilder().build();
        Response pinResponse = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(limitPayload, MediaType.APPLICATION_JSON));
        String responseString = pinResponse.readEntity(String.class);
        System.out.println("limit response: " + responseString);
        client.close();
        return responseString;
    }

    public int calculateOriginalIteration(RequestResponseCol reqres) {
        return reqres.getCurrentItteration() - nextCount(reqres);
    }

    public int nextCount(RequestResponseCol reqres) {
        boolean isTrackingNext = false;
        int nextCountValue = 0;
        if (reqres.getRequestList() != null && !reqres.getRequestList().isEmpty()) {
            for (int i = 0; i < reqres.getRequestList().size(); i++) {
                Request request = reqres.getRequestList().get(i);
                if (i == accountListStartIteration || request
                        .getUssdrequestString().equalsIgnoreCase(nextCharacter))
                    isTrackingNext = true;
                if (request.getUssdrequestString().equalsIgnoreCase(nextCharacter)) {
                    if (isTrackingNext)
                        nextCountValue++;
                    continue;
                }
                isTrackingNext = false;
            }
            if (reqres.getCurrentRequest().equalsIgnoreCase(nextCharacter))
                nextCountValue++;
        }
        return nextCountValue;
    }

    public int calculateQueryStartValue(RequestResponseCol reqres) {
        return this.nextCount(reqres) * maxBankListValue;
    }

    public List<String> returnTenatList(RequestResponseCol reqres) throws Exception {
        List<String> allTenantIds = this.customerInfoViewDao.getAllTenantIds();
        List<String> customerTenantIds = new ArrayList<>();

        if (allTenantIds != null && !allTenantIds.isEmpty()) {
            String message = this.getLanguageString(reqres.getLanguage(), "sltid");
            message = message.replace("@tenant", "");
            customerTenantIds.add(message);
            for (int i = 0; i < allTenantIds.size(); i++) {
                String temp = i + ". " + allTenantIds.get(i);
                customerTenantIds.add(temp);
            }
        } else return null;
        return customerTenantIds;
    }
    public String paginateBankList(String lang, String stringid, RequestResponseCol reqres) throws Exception {
        StringBuilder bankListSection = new StringBuilder();
        int calculatedStartValue = calculateQueryStartValue(reqres);
        List<String> banksList = returnTenatList(reqres);
        List<String> refinedBanks = new ArrayList<>();
        String nextMessage = this.getLanguageString(reqres.getLanguage(), "nmsg");
        int startIndex = calculatedStartValue;
        int endIndex = 0;
        if (startIndex > banksList.size()) {
            endIndex = banksList.size() - 1;
            startIndex = banksList.size() - 1 - maxBankListValue;
        } else if (startIndex + maxBankListValue > banksList.size()) {
            endIndex = banksList.size() - 1;
        } else {
            endIndex = startIndex + maxBankListValue;
        }
        if (calculatedStartValue == 0) {
            if (endIndex > banksList.size()) {
                refinedBanks.addAll(banksList.subList(startIndex, endIndex));
            } else {
                refinedBanks.addAll(banksList.subList(startIndex, endIndex + 1));
            }
        } else {
            refinedBanks.add(banksList.get(0));
            if (endIndex >= banksList.size() - 1) {
                endIndex = banksList.size();
                refinedBanks.addAll(banksList.subList(startIndex + 1, endIndex));
            } else {
                refinedBanks.addAll(banksList.subList(startIndex + 1, endIndex + 1));
            }
        }
        if (endIndex + 2 < banksList.size())
            refinedBanks.add(nextMessage);
        for (String bank : refinedBanks)
            bankListSection.append(bank).append("\n");
        return bankListSection.toString();
    }

    private Response.ResponseBuilder getTenantList(RequestResponseCol reqres) throws Exception {
        List<CustomerInfoView> customerAccounts = this.customerInfoViewDao.getAccounts(reqres.getMsisdn());
        int size = customerAccounts.size();
        boolean isInvalid = this.validationAccountSize(reqres, size);
        System.out.println("isInvalid: " + isInvalid);
        List<String> bankWithHeader = returnTenatList(reqres);
        List<String> banks = bankWithHeader.subList(1, bankWithHeader.size() - 1);
        alena.com.models.Response res = new alena.com.models.Response();
        String message = paginateBankList(reqres.getLanguage(), "bank", reqres);
        if (!reqres.getCurrentRequest().equalsIgnoreCase(nextCharacter)) {
            if (!isInvalid) {
                res.setAction("request");
            } else {
                message = this.getLanguageString(reqres.getLanguage(), "inv");
                res.setAction("end");
            }
        } else {
            if (maxBankListValue * this.nextCount(reqres) > banks.size()) {
                res.setAction("end");
                return this.buildResponse(reqres, this.getLanguageString(reqres.getLanguage(), "inv"), res);
            }
            res.setAction("request");
        }
        return this.buildResponse(reqres, message, res);
    }

    public boolean validationAccountSize(RequestResponseCol reqres, int size) {
        boolean isInvalid = true;
        for (int i = 1; i <= size; i++) {
            if (reqres.getCurrentRequest().equals(i + ""))
                isInvalid = false;
        }
        return isInvalid;
    }

    public Boolean PinAuthentication(String pin, String phone) {
        String url = "http://10.57.40.116:8080/keycloak/rest/keycloak/authenticate";
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("phone", phone);
        jsonPayload.put("password", pin);
        System.out.println("Pin Authentication payload: " + jsonPayload.toString());
        Client client = ClientBuilder.newBuilder().build();
        Response pinResponse = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(jsonPayload.toString(), MediaType.APPLICATION_JSON));
        String responseString = pinResponse.readEntity(String.class);
        System.out.println("Pin response: " + responseString);
        client.close();
        JSONObject jsonResponse = new JSONObject(responseString);
        return jsonResponse.optBoolean("status", false);
    }

    private String fetchToken() throws Exception {
//        String url = "http://10.57.40.158:8280/wegagenmpsea/1.0.0/wegagenToken";
//        String username = "mHuruOPOka2kb23tXk_OYtL1KNUa";
//        String password = "A0z8vl9OW6vwsgVdiE80iAnJFeYa";
        String url="https://wso2apim.wegagentraining.com:9443/oauth2/token";
        String username="cC1Zp4mbTl5pf0f34uCoJrd8n2Ya";
        String password ="UONX4gK8RWu8ohjyPj9z4lyH8Uoa";
        String grantType = "grant_type=client_credentials";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " +
                java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (OutputStream os = connection.getOutputStream()) {
            os.write(grantType.getBytes());
            os.flush();
        }
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (java.io.InputStream is = connection.getInputStream()) {
                java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            throw new Exception("Failed to fetch token. HTTP Code: " + responseCode);
        }
    }

    public String verifyAccount(String jwtToken, String jsonPayload) {
        System.out.println("New Jwt token " + jwtToken);
        System.out.println("Verfy Payload " + jsonPayload);
        String url = "http://wso2apim.wegagentraining.com:8280/WegagenAllena/1.0.0/verifyAccount";
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target(url)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwtToken)
                    .post(Entity.entity(jsonPayload, MediaType.APPLICATION_JSON));
            // Process the response
            int status = response.getStatus();
            if (status == Response.Status.OK.getStatusCode()) {
                return response.readEntity(String.class);
            } else {
                String errorMessage = response.readEntity(String.class);
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("status", "error");
                errorResponse.put("message", errorMessage);
                int statusCode = response.getStatus();
                errorResponse.put("statusCode", statusCode);
                return errorResponse.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return errorResponse.toString();
        } finally {
            client.close();
        }
    }

    public String performTransaction(String url, String access_token, String transactionPayload) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("performTransaction token" + access_token);
        System.out.println("performTransaction payload" + transactionPayload);
        System.out.println("performTransaction url" + url);
        SSLContext sslContext = alena.com.SSLContext.newContext();
        Client client = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .build();
        try {
            Response response = client.target(url)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + access_token)
                    .post(Entity.entity(transactionPayload, MediaType.APPLICATION_JSON));
            int status = response.getStatus();
            if (status == Response.Status.OK.getStatusCode()) {
                return response.readEntity(String.class);
            } else {
                String errorMessage = response.readEntity(String.class);
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("status", "error");
                errorResponse.put("message", errorMessage);
                int statusCode = response.getStatus();
                errorResponse.put("statusCode", statusCode);
                return errorResponse.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return errorResponse.toString();
        } finally {
            client.close();
        }
    }
    private javax.ws.rs.core.Response.ResponseBuilder returnAccountList(
            RequestResponseCol reqres, List<CustomerInfoView> customerAccounts,
            javax.ws.rs.core.Response.ResponseBuilder builder) throws Exception {
        String message = this.getLanguageString(reqres.getLanguage(), "acc");
        if (customerAccounts != null && !customerAccounts.isEmpty()) {
            StringBuilder accounts = new StringBuilder();
            int index = 1;
            for (CustomerInfoView account : customerAccounts) {
                accounts.append("\n").append(index).append(". ").append(account.getAccount());
                index++;
            }
            if (message.contains("@accounts")) {
                message = message.replace("@accounts", accounts.toString());
            } else {
                message += "\n" + accounts.toString(); // Append if placeholder is missing
            }
            alena.com.models.Response res = new alena.com.models.Response();
            res.setAction("request");
            res.setTransactionId(reqres.getSessionId());

            builder = buildResponse(reqres, message, res);
        } else {

            String message1 = this.getLanguageString(reqres.getLanguage(), "no_account");
            alena.com.models.Response res = new alena.com.models.Response();
            res.setAction("end");
            res.setTransactionId(reqres.getSessionId());

            builder = buildResponse(reqres, message1, res);
        }

        return builder;
    }

    private javax.ws.rs.core.Response.ResponseBuilder buildResponse(RequestResponseCol reqres, String message, alena.com.models.Response res) {
        javax.ws.rs.core.Response.ResponseBuilder builder;
        res.setUssdresponseString(message);
        res.setTransactionId(reqres.getSessionId());
        Date date = new Date();
        res.setTransactionTime(((Long) date.getTime()).toString());
        builder = javax.ws.rs.core.Response.ok().entity(res);
        return builder;
    }

    public String getLanguageString(String lang, String stringid) throws Exception {
        String userDisplay = "";
        Query q = this.em.createNamedQuery("GetMenu");
        q.setParameter("language", lang);
        q.setParameter("stringid", stringid);
        if (q.getResultList().size() > 0) {
            userDisplay = ((L10n) q.getSingleResult()).getContent();
        } else {
            Query q1 = this.em.createNamedQuery("GetMenu");
            q1.setParameter("language", "ENG");
            q1.setParameter("stringid", stringid);
            if (q1.getResultList().size() <= 0) {
                Exception E = new Exception("ACT-ERROR-DATA Menu does not exist");
                throw E;
            }
            userDisplay = ((L10n) q1.getSingleResult()).getContent();
        }
        return userDisplay;
    }
    public List<String> optionfinder(String options) {
        List<String> menu = new ArrayList<String>();
        menu.add("Heading");
        try {
            String[] optionList = options.split("\n");
            if (optionList.length > 0) {
                int counter = 0;
                for (String option : optionList) {
                    if (counter > 0) {
                        String[] row = option.split("\\.");
                        int index = Integer.parseInt(row[0]);
                        menu.add(index, row[1]);
                    }
                    counter++;
                }
                return menu;
            } else {
                return menu;
            }
        } catch (Exception E) {
            E.printStackTrace();
            return menu;
        }
    }
}