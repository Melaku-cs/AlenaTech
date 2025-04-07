# USSD Flight Payment Flow for Wegagen Bank SC

## Overview
This document describes the USSD flow specifically designed for flight payment transactions at Wegagen Bank SC. The process guides users through the following steps: selecting an account, entering the order ID, confirming the transaction, and completing payment securely via password authentication.

## Flow of Operations

### 1. **Account Selection (Iteration 2)**
- **Action:** After the user is authenticated, they are presented with a list of their available bank accounts. The user selects the account they wish to use for the payment.
- **Example Response:**

Select your account:

1. 1234567890
2. 0987654321

- **User Action:** The user selects an account by entering the corresponding number.

### 2. **Order ID Entry (Iteration 3)**
- **Action:** After selecting an account, the user is prompted to enter the order ID for their flight booking.
- **Example Prompt:**

- **User Action:** The user enters their flight order ID.

### 3. **Order Information Display (Iteration 4)**
- **Action:** The USSD service retrieves and displays the flight order details, including the order's name and amount, based on the provided order ID.
- **Example Response:**


### 4. **Transaction Confirmation (Iteration 5)**
- **Action:** The user is asked to confirm the transaction. The user can confirm by pressing "1" or cancel by pressing "2."
- **Example Prompt:**

Confirm your transaction:

1. Confirm
2. Cancel

- **User Action:** 
- If the user presses "1", they proceed to the next step for entering their password.
- If the user presses "2", they will be informed that the transaction is canceled.

### 5. **Password Entry for Final Confirmation (Iteration 6)**
- **Action:** If the user confirmed the transaction, they are prompted to enter their password for final authentication to complete the payment.
- **Example Prompt:**

Enter your password:

- **User Action:** The user enters their password.

### 6. **Transaction Completion or Cancellation**
- **If Password is Correct:**
- The payment is successfully processed, and the user is notified with a success message.
- **Example Message:**
  ```
  Your payment has been successfully processed. Thank you for using Wegagen Bank SC.
  ```
- **If User Presses "2" to Cancel:**
- The user is notified that the transaction has been canceled.
- **Example Message:**
  ```
  Your transaction has been canceled.
  ```
- **If Password is Incorrect:**
- The user is notified of the invalid password and asked to try again or cancel the transaction.

## Error Handling
- **Account Selection Failure:** If no accounts are available or the user fails to select a valid account, an error message is shown.
- **Invalid Order ID:** If the entered order ID is invalid, the user will be asked to try again.
- **Transaction Cancellation:** If the user cancels the transaction at any point, they will be informed that the transaction was canceled.

## Conclusion
This USSD flow ensures a smooth and secure flight payment experience for users of Wegagen Bank SC. It includes multiple verification steps such as account selection, order validation, transaction confirmation, and password entry for final payment authorization.
