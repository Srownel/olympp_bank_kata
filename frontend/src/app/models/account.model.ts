export interface AccountResponse {
  accountId: number;
  fullName: string;
  balance: number;
  allowedNegativeBalance: number;
}

export interface TransactionRequest {
  amount: number;
}

export interface TransactionResponse {
  transactionId: number;
  timestamp: string;
  type: TransactionType;
  amount: number;
  balanceAfter: number;
}

export interface StatementResponse {
  accountId: number;
  currentBalance: number;
  transactions: TransactionResponse[];
}

export interface ErrorResponse {
  error: string;
  message: string;
}

export enum TransactionType {
  DEPOSIT = 'DEPOSIT',
  WITHDRAWAL = 'WITHDRAWAL'
}