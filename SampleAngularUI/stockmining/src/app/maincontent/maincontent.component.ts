import { Component, OnInit } from '@angular/core';
import { StockMiningServiceService } from '../stock-mining-service.service';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';

@Component({
  selector: 'app-maincontent',
  providers: [ StockMiningServiceService ],
  templateUrl: './maincontent.component.html',
  styleUrls: ['./maincontent.component.css']
})
export class MaincontentComponent implements OnInit {

  validateForm: FormGroup;

  pageIndex = 1;
  pageSize = 10;
  total = 1;
  dataSet = [];
  histDataSet = [];
  loading = true;
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
    // tslint:disable-next-line:max-line-length
    this.stockMiningService.findStocksLower30Percent(this.pageIndex, this.pageSize, this.sortKey, this.sortValue, this.searchGenderList).subscribe((data: any) => {
      this.loading = false;
      this.total = 200;
      this.histDataSet = data;
      console.log(data);
      console.log(data.results);
    });
  }



  updateFilter(value: string[]): void {
    this.searchGenderList = value;
    this.searchData(true);
  }

  constructor(private fb: FormBuilder, private stockMiningService: StockMiningServiceService) {
  }

  submitForm(): void {
    // tslint:disable-next-line:forin
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[ i ].markAsDirty();
      this.validateForm.controls[ i ].updateValueAndValidity();
    }
  }

  ngOnInit(): void {

    this.validateForm = this.fb.group({
      priceChgPercent : [ null, [ Validators.required ] ],
      profit : [ null, [ Validators.required ] ]
    });
  }

}
