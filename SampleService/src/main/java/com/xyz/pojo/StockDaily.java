package com.xyz.pojo;

public class StockDaily {

	String tsCode;
	String name;
	String industry;
	String market;
	String tradeDate;
	Float closePrice;
	Float pctChg;
	String tradeDateHist;
	Float closePriceHist;
	Float pctChgHist;
	Float profit;
	Integer totalPctChg;
	String holders;
	Boolean hasSheBaoFunder;
	String selectedConcept;
	String concepts;
	
	public String getConcepts() {
		return concepts;
	}
	public void setConcepts(String concepts) {
		this.concepts = concepts;
	}
	public String getSelectedConcept() {
		return selectedConcept;
	}
	public void setSelectedConcept(String selectedConcept) {
		this.selectedConcept = selectedConcept;
	}
	public Boolean getHasSheBaoFunder() {
		return hasSheBaoFunder;
	}
	public void setHasSheBaoFunder(Boolean hasSheBaoFunder) {
		this.hasSheBaoFunder = hasSheBaoFunder;
	}
	public Float getProfit() {
		return profit;
	}
	public void setProfit(Float profit) {
		this.profit = profit;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getHolders() {
		return holders;
	}
	public void setHolders(String holders) {
		this.holders = holders;
	}
	public Integer getTotalPctChg() {
		return totalPctChg;
	}
	public void setTotalPctChg(Integer totalPctChg) {
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
