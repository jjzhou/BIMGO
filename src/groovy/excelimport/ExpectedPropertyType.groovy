package excelimport

import org.apache.poi.ss.formula.FormulaType


public enum ExpectedPropertyType {

	IntType([name: 'number']),
	StringType([name: 'text']),
	DateType([name: 'date']),
	DateJavaType([name: 'date']),
	DoubleType([name: 'number']),
	EmailType([name: 'email']),
    FormulaType([name: 'formula']);

	final String userViewableName


	public ExpectedPropertyType(Map parameters = [:]) {
		this.userViewableName = parameters?.name ?: this.name()
	}


}
