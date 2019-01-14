export class StockDaily {
    tsCode: string;
    name: string;
    tradeDate: string;
    closePrice: Float32Array;
    pctChg: Float32Array;
    tradeDateHist: Date;
    closePriceHist: Float32Array;
    pctChgHist: Float32Array;
    totalPctChg: Float32Array;
    profit: Float32Array;
    hasSheBaoFunder: boolean;
    selectedConcept: string;
  }