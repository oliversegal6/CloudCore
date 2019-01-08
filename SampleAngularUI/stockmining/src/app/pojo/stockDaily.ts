export class Stock {
    tsCode: string;
    name: string;
    tradeDate: string;
    closePrice: Float32Array;
    pctChg: Float32Array;
    tradeDateHist: string;
    closePriceHist: Float32Array;
    pctChgHist: Float32Array;
    totalPctChg: Float32Array;
  }