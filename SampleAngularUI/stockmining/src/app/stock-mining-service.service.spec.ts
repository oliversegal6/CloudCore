import { TestBed } from '@angular/core/testing';

import { StockMiningServiceService } from './stock-mining-service.service';

describe('StockMiningServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StockMiningServiceService = TestBed.get(StockMiningServiceService);
    expect(service).toBeTruthy();
  });
});
