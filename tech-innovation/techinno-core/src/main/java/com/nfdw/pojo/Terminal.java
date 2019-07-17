package com.nfdw.pojo;

import java.io.Serializable;

public class Terminal implements Serializable  {

	    //终端序列SN号
	    private String terminalsn;
	    //终端制式
		private String terminalmode;
		//终端版本号
		private String terminalver;
		//终端IMEI号
		private String terminalimei;
		//iccid
		private String iccid;
		//imsi
		private String imsi;
		//终端厂商编号
		private String factorycode;
		//终端型号
		private String terminaltype;
		
		
	@Override
		public String toString() {
			return "Terminal [terminalsn=" + terminalsn + ", terminalmode="
					+ terminalmode + ", terminalver=" + terminalver
					+ ", terminalimei=" + terminalimei + ", iccid=" + iccid
					+ ", imsi=" + imsi + ", factorycode=" + factorycode
					+ ", terminaltype=" + terminaltype + "]";
		}
	public String getTerminalsn() {
		return terminalsn;
	}
	public void setTerminalsn(String terminalsn) {
		this.terminalsn = terminalsn;
	}
	public String getTerminalmode() {
		return terminalmode;
	}
	public void setTerminalmode(String terminalmode) {
		this.terminalmode = terminalmode;
	}
	public String getTerminalver() {
		return terminalver;
	}
	public void setTerminalver(String terminalver) {
		this.terminalver = terminalver;
	}
	public String getTerminalimei() {
		return terminalimei;
	}
	public void setTerminalimei(String terminalimei) {
		this.terminalimei = terminalimei;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getFactorycode() {
		return factorycode;
	}
	public void setFactorycode(String factorycode) {
		this.factorycode = factorycode;
	}
	public String getTerminaltype() {
		return terminaltype;
	}
	public void setTerminaltype(String terminaltype) {
		this.terminaltype = terminaltype;
	}
	
	



}
