package com.xyz.pojo;

public class DailyBasic {

	String tsCode; //	TS股票代码
	String tradeDate;	//	交易日期
	Float close; //	float	当日收盘价
	Float turnoverRate; //	float	换手率（%）
	Float turnoverRateF; //	float	换手率（自由流通股）
	Float volumeRatio; //	float	量比
	Float pe; //	float	市盈率（总市值/净利润）
	Float peTtm; //	float	市盈率（TTM）
	Float pb; //	float	市净率（总市值/净资产）
	Float ps; //	float	市销率
	Float psTtm; //	float	市销率（TTM）
	Float totalShare; //	float	总股本 （万）
	Float floatShare; //	float	流通股本 （万）
	Float freeShare; //	float	自由流通股本 （万）
	Float totalMv; //	float	总市值 （万元）
	Float circMv; //	float
	public String getTsCode() {
		return tsCode;
	}
	public void setTsCode(String tsCode) {
		this.tsCode = tsCode;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public Float getClose() {
		return close;
	}
	public void setClose(Float close) {
		this.close = close;
	}
	public Float getTurnoverRate() {
		return turnoverRate;
	}
	public void setTurnoverRate(Float turnoverRate) {
		this.turnoverRate = turnoverRate;
	}
	public Float getTurnoverRateF() {
		return turnoverRateF;
	}
	public void setTurnoverRateF(Float turnoverRateF) {
		this.turnoverRateF = turnoverRateF;
	}
	public Float getVolumeRatio() {
		return volumeRatio;
	}
	public void setVolumeRatio(Float volumeRatio) {
		this.volumeRatio = volumeRatio;
	}
	public Float getPe() {
		return pe;
	}
	public void setPe(Float pe) {
		this.pe = pe;
	}
	public Float getPeTtm() {
		return peTtm;
	}
	public void setPeTtm(Float peTtm) {
		this.peTtm = peTtm;
	}
	public Float getPb() {
		return pb;
	}
	public void setPb(Float pb) {
		this.pb = pb;
	}
	public Float getPs() {
		return ps;
	}
	public void setPs(Float ps) {
		this.ps = ps;
	}
	public Float getPsTtm() {
		return psTtm;
	}
	public void setPsTtm(Float psTtm) {
		this.psTtm = psTtm;
	}
	public Float getTotalShare() {
		return totalShare;
	}
	public void setTotalShare(Float totalShare) {
		this.totalShare = totalShare;
	}
	public Float getFloatShare() {
		return floatShare;
	}
	public void setFloatShare(Float floatShare) {
		this.floatShare = floatShare;
	}
	public Float getFreeShare() {
		return freeShare;
	}
	public void setFreeShare(Float freeShare) {
		this.freeShare = freeShare;
	}
	public Float getTotalMv() {
		return totalMv;
	}
	public void setTotalMv(Float totalMv) {
		this.totalMv = totalMv;
	}
	public Float getCircMv() {
		return circMv;
	}
	public void setCircMv(Float circMv) {
		this.circMv = circMv;
	}
	
	
}
