package com.xyz.pojo;

public class StockDaily {

	String tsCode;
	String name;
	String tradeDate;
	Float closePrice;
	Float pctChg;
	String tradeDateHist;
	Float closePriceHist;
	Float pctChgHist;
	Float totalPctChg;
	
	public Float getTotalPctChg() {
		return totalPctChg;
	}
	public void setTotalPctChg(Float totalPctChg) {
		this.totalPctChg = totalPctChg;
	}
	public String getTsCode() {
		return tsCode;
	}
	public void setTsCode(String tsCode) {
		this.tsCode = tsCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public Float getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(Float closePrice) {
		this.closePrice = closePrice;
	}
	public Float getPctChg() {
		return pctChg;
	}
	public void setPctChg(Float pctChg) {
		this.pctChg = pctChg;
	}
	public String getTradeDateHist() {
		return tradeDateHist;
	}
	public void setTradeDateHist(String tradeDateHist) {
		this.tradeDateHist = tradeDateHist;
	}
	public Float getClosePriceHist() {
		return closePriceHist;
	}
	public void setClosePriceHist(Float closePriceHist) {
		this.closePriceHist = closePriceHist;
	}
	public Float getPctChgHist() {
		return pctChgHist;
	}
	public void setPctChgHist(Float pctChgHist) {
		this.pctChgHist = pctChgHist;
	}
	
	
}
