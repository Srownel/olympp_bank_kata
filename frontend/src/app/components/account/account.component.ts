import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../services/account.service';
import {
  AccountResponse,
  TransactionResponse,
  StatementResponse,
  ErrorResponse
} from '../../models/account.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  account: AccountResponse | null = null;
  statement: StatementResponse | null = null;
  
  depositAmount: number | null = null;
  withdrawAmount: number | null = null;
  
  loading = true;
  statementVisible = false;
  statementLoading = false;
  
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private accountService: AccountService) { }

  ngOnInit(): void {
    this.loadAccountInfo();
  }

  loadAccountInfo(): void {
    this.loading = true;
    this.accountService.getAccountInfo().subscribe({
      next: (data) => {
        this.account = data;
        this.loading = false;
      },
      error: (error) => {
        this.handleError(error, 'Failed to load account information. Please check if the backend is running.');
        this.loading = false;
      }
    });
  }

  onDeposit(): void {
    if (!this.depositAmount || this.depositAmount <= 0) {
      this.showError('Please enter a valid amount greater than 0');
      return;
    }

    this.clearMessages();
    this.accountService.deposit(this.depositAmount).subscribe({
      next: (response) => {
        this.handleTransactionSuccess(response);
        this.depositAmount = null;
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }

  onWithdraw(): void {
    if (!this.withdrawAmount || this.withdrawAmount <= 0) {
      this.showError('Please enter a valid amount greater than 0');
      return;
    }

    this.clearMessages();
    this.accountService.withdraw(this.withdrawAmount).subscribe({
      next: (response) => {
        this.handleTransactionSuccess(response);
        this.withdrawAmount = null;
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }

  toggleStatement(): void {
    this.statementVisible = !this.statementVisible;
    
    if (this.statementVisible && !this.statement) {
      this.loadStatement();
    }
  }

  loadStatement(): void {
    this.statementLoading = true;
    this.accountService.getStatement().subscribe({
      next: (data) => {
        this.statement = data;
        this.statementLoading = false;
      },
      error: (error) => {
        this.handleError(error, 'Failed to load transaction history');
        this.statementLoading = false;
      }
    });
  }

  private handleTransactionSuccess(response: TransactionResponse): void {
    this.showSuccess(`Transaction successful! New balance: $${this.formatAmount(response.balanceAfter)}`);
    this.loadAccountInfo();
    
    if (this.statementVisible) {
      this.loadStatement();
    }
  }

  private handleError(error: HttpErrorResponse, defaultMessage?: string): void {
    let message = defaultMessage || 'An error occurred';

    if (error.error && typeof error.error === 'object') {
      const errorResponse = error.error as ErrorResponse;
      
      switch (errorResponse.error) {
        case 'INSUFFICIENT_FUNDS':
          message = '❌ ' + errorResponse.message;
          break;
        case 'INVALID_AMOUNT':
          message = '❌ ' + errorResponse.message;
          break;
        case 'ACCOUNT_NOT_FOUND':
          message = '❌ Account not found';
          break;
        default:
          message = '❌ ' + (errorResponse.message || 'An unexpected error occurred');
      }
    }

    this.showError(message);
  }

  private showSuccess(message: string): void {
    this.successMessage = message;
    this.errorMessage = null;
    setTimeout(() => this.clearMessages(), 5000);
  }

  private showError(message: string): void {
    this.errorMessage = message;
    this.successMessage = null;
    setTimeout(() => this.clearMessages(), 5000);
  }

  private clearMessages(): void {
    this.successMessage = null;
    this.errorMessage = null;
  }

  formatAmount(amount: number): string {
    return amount.toFixed(2);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString();
  }

  get isBalanceNegative(): boolean {
    return this.account ? this.account.balance < 0 : false;
  }
}