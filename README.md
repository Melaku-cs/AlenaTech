========================================================================================================================================
=                                                                                                                                      =
=                              Alena Tech SCCO Integration with Wegagen Bank - USSD                                                    =  =                                                                                                                                      =
=                                                                                                                                      =
========================================================================================================================================
                  
                          Overview
This project integrates the Alena Tech SCCO (Smart Customer Communication Operation) system with Wegagen Bank using USSD (Unstructured Supplementary Service Data) technology. The code implements a series of iterations for user interaction, performing various operations such as account selection, amount input, authentication, and transaction execution.

The code snippet presented in this repository covers multiple transaction states such as account selection, beneficiary details, transaction amounts, PIN authentication, and transaction verification.


# Features
 1 Session Management: The system maintains session states to track user progress and input during the interaction.

2 Account Selection: Customers can select accounts from a list of their available accounts.

3 Beneficiary Management: Allows users to select a beneficiary (tenant) from a list.

4 Transaction Verification: Verifies transactions by interacting with the backend API using tokens.

5 Amount and PIN Validation: Ensures valid transaction amounts and PIN authentication.

6 Transaction Processing: Processes the transaction and provides a confirmation or failure message.

# Prerequisites
===============================================================================
Before you begin, ensure you have the following:

* Java (Version 8 or above)

* Maven (for managing dependencies)

* Alena Tech's Core SDK

* Wegagen Bank API credentials

* Internet connection (for API interaction)

Installation
==============================================================================================
Clone the repository:

#bash
#Copy
git clone https://github.com/your-repository/wegagen-ussd-integration.git
#Navigate to the project directory:

#bash
#Copy
cd wegagen-ussd-integration
Install dependencies using Maven:

#bash
#Copy
mvn clean install
#Configure the necessary environment variables:

#Set up API credentials for Wegagen Bank.

#Configure session management and transaction settings.

#Code Explanation
===========================================================================
Main Iteration Flow
============================================================================
1 Iteration 2:

This iteration checks if the user has available accounts. If accounts exist, it returns the list of accounts to the user. Otherwise, it prompts an error message.

2 Iteration 3:

After selecting an account, this iteration fetches tenant lists and processes the user's next step, either continuing to the next iteration or terminating the session.

3 Iteration 4-5:

In these iterations, the user selects a tenant and inputs their account number. A transaction request is then verified by interacting with an API using tokens.

4 Iteration 6:

The user enters the transaction amount. The system validates the amount, ensures it’s a positive number, and then constructs the transaction payload.

5 Iteration 7:

The system asks for user confirmation before proceeding with the transaction. A successful confirmation triggers the next iteration.

6 Iteration 8:

The user is asked to provide their PIN for authentication. If authentication succeeds, the transaction is processed. If not, the session is terminated with an error message.

#Key Classes and Methods
=================================================================================
# SessionManager: Handles session creation, storage, and retrieval.

#ResponseBuilder: Constructs the response messages sent back to the user.

#Transaction Methods:
==============================================================================
#performTransaction: Initiates the transaction request to the Wegagen Bank API.

#verifyAccount: Validates the user’s selected account with the bank.

Error Handling
=================================================================================
The code includes error handling for common issues:

#Invalid account numbers.

#Incorrect PIN entries.

#Invalid transaction amounts.

#API call failures.

If any error occurs, the system responds with an appropriate message, asking the user to retry or abort the process.

How to Use
==========================================================================================
Once the setup is complete and the system is configured, follow these steps to initiate a transaction:

1 Start the USSD session: A user dials the USSD code to begin the transaction.

2 Account Selection: The system will display available accounts for the user to choose from.

3 Beneficiary Selection: After choosing an account, the user selects the tenant/beneficiary to receive the payment.

4 Transaction Amount Input: The user provides the transaction amount.

5 PIN Authentication: The user is prompted to enter their PIN for verification.

6 Confirmation: The system displays a confirmation of the transaction, or an error message if any step fails.

API Integration
====================================================================================
The project integrates with the Wegagen Bank API using performTransaction to send and receive transaction details. The API expects a payload with transaction data, including:

#Account number: The account from which the user is sending the money.

#Amount: The transaction amount.

#Tenant ID: The beneficiary’s tenant ID.

#Bank Number: The selected bank number for transaction processing.

#########A successful transaction returns a TransactionReference, which is displayed to the user#####
==========================================================================================================================================
