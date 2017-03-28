package org.my.local;

public enum Operator {

	GT(">"),
	LT("<"),
	GTE();
	
	private String str;
	
	private Operator(String str) {
		this.str = str;
	}
	
	private Operator() {
		
	}
	
	public String getValue() {
		return this.str;
	}
}
