import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { StockDaily } from './pojo/stockDaily';
import { DatePipe } from '@angular/common';

@Injectable({
  providedIn: 'root'
})

export class StockMiningServiceService {

  randomUserUrl = 'http://localhost:8082';
  //randomUserUrl = 'http://106.14.219.109:8082';

  getStocks(pageIndex: number = 1, pageSize: number = 10, sortField: string, sortOrder: string, genders: string[]): Observable<{}> {
    let params = new HttpParams()
    .append('pageIndex', `${pageIndex}`)
    .append('pageSize', `${pageSize}`)
    .append('sortField', sortField)
    .append('sortOrder', sortOrder);
    genders.forEach(gender => {
      params = params.append('gender', gender);
    });
    return this.http.get(`${this.randomUserUrl}/findAllStocks`, {
      params
    });
  }

  findStocksLower30Percent(selectedConcept: string, hasSheBaoFunder: boolean, profit: Float32Array, totalPctChg: Float32Array, tradeDateHist: Date, pageIndex: number = 1, pageSize: number = 10, sortField: string, sortOrder: string, genders: string[]): Observable<{}> {
    let params = new HttpParams()
    .append('pageIndex', `${pageIndex}`)
    .append('pageSize', `${pageSize}`)
    .append('sortField', sortField)
    .append('sortOrder', sortOrder)
    .append('profit', `${profit}`)
    .append('totalPctChg', `${totalPctChg}`)
    .append('hasSheBaoFunder', `${hasSheBaoFunder}`)
    .append('selectedConcept', `${selectedConcept}`)
    .append('tradeDateHist', this.datePipe.transform(`${tradeDateHist}`, 'yyyyMMdd'));
    
    genders.forEach(gender => {
      params = params.append('gender', gender);
    });
    return this.http.get(`${this.randomUserUrl}/findStocksLower30Percent`, {
      params
    });
  }

  findConcept(): Observable<{}> {
    let params = new HttpParams();
    
    return this.http.get(`${this.randomUserUrl}/findConcept`, {
      params
    });
  }

  constructor(private datePipe: DatePipe, private http: HttpClient) { }
}
