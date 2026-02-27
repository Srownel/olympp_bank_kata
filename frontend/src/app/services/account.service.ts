import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {
  AccountResponse,
  TransactionRequest,
  TransactionResponse,
  StatementResponse,
  ErrorResponse
} from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8080/api/account';
  private accountId = 1;

  constructor(private http: HttpClient) { }

  getAccountInfo(): Observable<AccountResponse> {
    return this.http.get<AccountResponse>(`${this.apiUrl}/${this.accountId}`)
      .pipe(catchError(this.handleError));
  }

  deposit(amount: number): Observable<TransactionResponse> {
    const request: TransactionRequest = { amount };
    return this.http.post<TransactionResponse>(
      `${this.apiUrl}/${this.accountId}/deposit`,
      request
    ).pipe(catchError(this.handleError));
  }

  withdraw(amount: number): Observable<TransactionResponse> {
    const request: TransactionRequest = { amount };
    return this.http.post<TransactionResponse>(
      `${this.apiUrl}/${this.accountId}/withdraw`,
      request
    ).pipe(catchError(this.handleError));
  }

  getStatement(): Observable<StatementResponse> {
    return this.http.get<StatementResponse>(`${this.apiUrl}/${this.accountId}/statement`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => error);
  }
}