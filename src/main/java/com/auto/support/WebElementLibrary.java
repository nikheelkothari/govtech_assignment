package com.auto.support;

public enum WebElementLibrary implements IDictionary {
	
	UPLOAD_EMP_RECORDS_FILE(new CssSelector(".custom-file-input")),
	EMP_TABLE_RECORDS_LIST(new CssSelector(".table.table-hover.table-dark>tbody>tr")),
	REFRESH_TAX_RELIEF_TABLE_BUTTON(new CssSelector(".btn.btn-primary")),
	TOTAL_TAX_RELIEVES_LABEL(new CssSelector(".jumbotron.jumbotron-fluid .lead")),
	DISPENSE_NOW_BUTTON(new CssSelector(".btn.btn-danger.btn-block")),
	CASH_DISPENSE_LABEL(new CssSelector(".display-4.font-weight-bold"));
	
	private ISelector selector;
	
	private WebElementLibrary(ISelector selector) {
		this.selector = selector;
	}

	public String getSelectorDict() {
		return this.selector.getSelector();
	}

	public ISelector getSelectorTypeDict() {
		return this.selector;
	}
}
