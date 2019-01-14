import { Component, OnInit } from '@angular/core';
import { StockMiningServiceService } from '../stock-mining-service.service';
import {StockDaily} from '../pojo/stockDaily';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-maincontent',
  providers: [ StockMiningServiceService ],
  templateUrl: './maincontent.component.html',
  styleUrls: ['./maincontent.component.css']
})
export class MaincontentComponent implements OnInit {

  validateForm: FormGroup;
  stockDaily= new StockDaily;

  pageIndex = 1;
  pageSize = 10;
  total = 1;
  dataSet = [];
  histDataSet = [];
  conceptDataSet = [];
  loading = false;
  sortValue = null;
  sortKey = null;
  filterGender = [
    { text: 'male', value: 'male' },
    { text: 'female', value: 'female' }
  ];
  searchGenderList: string[] = [];

  sort(sort: { key: string, value: string }): void {
    this.sortKey = sort.key;
    this.sortValue = sort.value;
    this.searchData();
  }



  searchData(reset: boolean = false): void {
    if (reset) {
      this.pageIndex = 1;
    }
    this.loading = true;
    // tslint:disable-next-line:max-line-length
    this.stockMiningService.getStocks(this.pageIndex, this.pageSize, this.sortKey, this.sortValue, this.searchGenderList).subscribe((data: any) => {
      this.loading = false;
      this.total = 200;
      this.dataSet = data;
      console.log(data);
      console.log(data.results);
    });
  }

  findStocksLower30Percent(reset: boolean = false): void {
    if (reset) {
      this.pageIndex = 1;
    }
    this.loading = true;

    this.stockDaily.profit = this.validateForm.controls.profit.value;
    this.stockDaily.totalPctChg = this.validateForm.controls.totalPctChg.value;
    this.stockDaily.tradeDateHist = this.validateForm.controls.tradeDateHist.value;
    this.stockDaily.hasSheBaoFunder = this.validateForm.controls.hasSheBaoFunder.value;
    this.stockDaily.selectedConcept = this.validateForm.controls.selectedConcept.value;
    console.log(this.stockDaily.hasSheBaoFunder);
    console.log(this.validateForm.controls.selectedConcept.value);
    console.log(this.validateForm.controls.totalPctChg.value);
    // tslint:disable-next-line:max-line-length
    this.stockMiningService.findStocksLower30Percent(this.stockDaily.selectedConcept, this.stockDaily.hasSheBaoFunder, this.stockDaily.profit,this.stockDaily.totalPctChg, this.stockDaily.tradeDateHist, this.pageIndex, this.pageSize, this.sortKey, this.sortValue, this.searchGenderList).subscribe((data: any) => {
      this.loading = false;
      this.total = 200;
      this.histDataSet = data;
      console.log(data);
    });
  }

  findConcept(): void {

    this.stockMiningService.findConcept().subscribe((data: any) => {
      this.loading = false;
      this.conceptDataSet = data;
      console.log(data);
    });
  }



  updateFilter(value: string[]): void {
    this.searchGenderList = value;
    this.searchData(true);
  }

  constructor(private datePipe: DatePipe, private fb: FormBuilder, private stockMiningService: StockMiningServiceService) {
  }

  submitForm(): void {
    // tslint:disable-next-line:forin
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[ i ].markAsDirty();
      this.validateForm.controls[ i ].updateValueAndValidity();
    }
  }

  ngOnInit(): void {

    this.findConcept()

    this.validateForm = this.fb.group({
      totalPctChg : [ null, [ Validators.required ] ],
      profit : [ null, [ Validators.required ] ],
      tradeDateHist : [ null, [ Validators.required ] ],
      hasSheBaoFunder : [ null, [ Validators.required ] ],
      selectedConcept : [ null, [ Validators.max(10) ] ]
    });

    this.validateForm.controls.hasSheBaoFunder.setValue(true);
    this.validateForm.controls.tradeDateHist.setValue(new Date('2017-7-1'));
    this.validateForm.controls.profit.setValue(0);
    this.validateForm.controls.totalPctChg.setValue(80);
  }

}
