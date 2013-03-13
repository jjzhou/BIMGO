package eis.bas

import excelimport.ExpectedPropertyType
import excelimport.ImportCellCollector
import excelimport.NoopImportCellCollector
import org.apache.poi.ss.util.CellReference
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.joda.time.LocalDate
import org.apache.poi.ss.usermodel.*

/**
 * Created by IntelliJ IDEA.
 * User: Jean Barmash
 * Date: Sep 29, 2009
 * Time: 5:08:39 PM
 * LiuYanLu 更新于2012年5月10日
 * 增加对Formula类型Cell的读取和写入支持
 */
public class ExcelService {

    static scope = "prototype"
    static transactional = false


	//PROPERTY_TYPE_* was inlined in HEAD
	//not removing this old definitions for a while as there could be merge into another branch which still uses it
	static ExpectedPropertyType PROPERTY_TYPE_INT = excelimport.ExpectedPropertyType.IntType
	static ExpectedPropertyType PROPERTY_TYPE_STRING = excelimport.ExpectedPropertyType.StringType
	static ExpectedPropertyType PROPERTY_TYPE_DATE = excelimport.ExpectedPropertyType.DateType
	static ExpectedPropertyType PROPERTY_TYPE_DATE_JAVA = excelimport.ExpectedPropertyType.DateJavaType
	static ExpectedPropertyType PROPERTY_TYPE_DOUBLE = excelimport.ExpectedPropertyType.DoubleType
    static ExpectedPropertyType PROPERTY_TYPE_FORMULA = excelimport.ExpectedPropertyType.FormulaType

    static String FORMULAR_EVALUATOR_FLAG = "_"

       static getService() {
    		def ctx = ApplicationHolder.application.mainContext;
    		return ctx.getBean("excelService");
	} 

	
	/**
	 * Looks through supplied index (columnIndex), and matches a siteID in that column, returning the row of the match
	 */
	Row findRowById(Sheet sheet, String siteId, int columnIndex) {
		for (Row r: sheet) {
			if (r.getCell(columnIndex)?.stringCellValue == siteId) {
				log.info "found ID $siteId"
				return r
			}
		}
		return null
	}

	/**
	 * receives a list of column names, i.e. ['Site', 'ID'], and tries to find the indexes of the columns matching those names
	 * returning a map, i.e. ['Site':2, 'ID':8]
	 */
	Map createColumnMapFromColumnNames(List COLUMN_NAMES, Row headerRow) {
		def columnIndexMap = [:]

		for (Cell c: headerRow) {
			if (COLUMN_NAMES.contains(c.stringCellValue)) {
				log.debug "Found index for column ${c.stringCellValue}"
				columnIndexMap[c.stringCellValue] = c.columnIndex
			}
		}

		log.info "columnIndexMap $columnIndexMap"
		columnIndexMap
	}

	//To Transpose the Values
	Map verticalValues(Workbook workbook, Map config) {
		return columns(workbook, config).inject([:]) {acc, nameValuePair ->
			acc << [(nameValuePair.name): nameValuePair.value]
		}
	}

	def columns(Workbook workbook, Map config, ImportCellCollector pcc = null, propertyConfigurationMap = [:], int lastRow = -1) {
		convertColumnMapConfigManyRows(workbook, config, pcc, null, propertyConfigurationMap, lastRow)
	}

	/**
	 * dual columns(Workbook, ...
	 */
	def setColumns(List inputList, Workbook workbook, Map config, propertyConfigurationMap = [:], int lastRow = -1) {
		convertManyRowsToColumnMapConfig(inputList, workbook, config, null, propertyConfigurationMap, lastRow)
	}


	def convertColumnMapConfigManyRows(Workbook workbook, Map config, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, propertyConfigurationMap = [:], int lastRow = -1) {
		if (!evaluator) evaluator = workbook.creationHelper.createFormulaEvaluator()
		def sheet = workbook.getSheet(config.sheet)
		if (propertyConfigurationMap == [:]) {
			propertyConfigurationMap = config.configMap ?: [:]
		}
		if (config.containsKey('lastRow')) {
			lastRow = config.lastRow
		}
		return convertColumnMapManyRows(sheet, config, config.startRow, pcc, evaluator, propertyConfigurationMap, lastRow)
	}

	/**
	 * dual convertColumnMapConfigManyRows(Workbook, ...
	 */
	def convertManyRowsToColumnMapConfig(List inputList, Workbook workbook, Map config, FormulaEvaluator evaluator = null, propertyConfigurationMap = [:], int lastRow = -1) {
		if (!evaluator) evaluator = workbook.creationHelper.createFormulaEvaluator()
		def sheet = workbook.getSheet(config.sheet)
		if (propertyConfigurationMap == [:]) {
			propertyConfigurationMap = config.configMap ?: [:]
		}
		convertManyRowsToColumnMap(inputList, sheet, config.columnMap, config.startRow, evaluator, propertyConfigurationMap, lastRow)
	}

	
	/**
	 * Receives sheet, offset, and map of columns.  Map is between a column name (i.e. B) and what will become returning map key.
	 * For example, columnMap could be ['B':'endDate', 'D':cost], which will cause going down the B and D columns and retrieving values
	 * packaging up as maps to be returned, in this case something like [[endDate:LocalDate(2009/1/2), cost:30], [endDate: LocalDate(2009,1,3), cost:20]]
	 * This method is very generic, and could be used by anything
	 */
	def convertColumnMapManyRows(Sheet currentSheet, Map config, int firstRow, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, propertyConfigurationMap = null, int lastRow = -1) {
		if (currentSheet == null) return []
		boolean blankRowBreak = false
		int blankRowCount = 0
		def returnList = []
		for (int rowIndex = firstRow; (rowIndex < lastRow || ((lastRow == -1)) && !blankRowBreak); rowIndex++) {
			//println "ColumnMap $columnMap"
			Map returnParams = convertColumnMapOneRow(currentSheet, config, rowIndex, pcc, evaluator, propertyConfigurationMap)
			//println "Row Columns - returning $returnParams"
			log.debug "Index $rowIndex Result map values $returnParams"
			//println "Index $rowIndex Result map values $returnParams"
			if (!returnParams) {
				blankRowCount += 1
			} else {
				blankRowCount = 0
            }
            returnList << returnParams
            blankRowBreak = (blankRowCount > 10)
		}
		returnList
	}

	/**
	 * dual convertColumnMapManyRows(Sheet, ...
	 */
	def convertManyRowsToColumnMap(List inputList, Sheet currentSheet, Map columnMap, int firstRow, FormulaEvaluator evaluator = null, propertyConfigurationMap = null, int lastRow = -1) {
		if (currentSheet == null) return
		int rowIndex = firstRow
		inputList.each {inputParams ->
			convertOneRowToColumnMap(inputParams, currentSheet, columnMap, rowIndex, evaluator, propertyConfigurationMap)
			rowIndex++
		}
	}

	
	//Form of sitePropertyConfigurationMap:  ['squareFeet':[type:"integer", defaultValue:-1]]
	def convertColumnMapOneRow(Sheet currentSheet, Map config, int rowIndex, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, propertyConfigurationMap = null) {

		pcc = pcc ?: NoopImportCellCollector.NoopInstance

		def returnParams = [:]
		def anyNonNullValues = false
		def row = currentSheet.getRow(rowIndex)
		if (!row) {
			log.info "Row is null at row $rowIndex, sheet ${currentSheet.sheetName}"
			return returnParams
		}
		
		config.columnMap.each {String columnName, propertyName ->
			try {
                def value
                String realColumnName = columnName
                if(columnName.endsWith(FORMULAR_EVALUATOR_FLAG)){
                    realColumnName = columnName.substring(0,columnName.length()-1)
                    value = getCellValueByColName(row, realColumnName, pcc, evaluator, propertyConfigurationMap?.get(propertyName))
                }else{
                    value = getCellValueByColName(row, realColumnName, pcc, null, propertyConfigurationMap?.get(propertyName))
                }

				log.trace "\t\tValue for $propertyName (column ${columnName}) is $value "
				if (value == null || ''.equals(value)) { //cheking for null, because otherwise 0 value will fail here
					log.debug "Value for column $columnName row $rowIndex is null or empty String.  Was trying to find property $propertyName.  Skipping setting its value in param map"
					returnParams[propertyName] = propertyConfigurationMap?.get(propertyName)?.defaultValue
				} else {
					anyNonNullValues = true
					returnParams[propertyName] = value
				}
			} catch (Exception e) {
				//pcc.reportCell(cell, propertyConfiguration)
				log.error "Exception caught at row $rowIndex column $columnName while trying to set property $propertyName", e
				//println "Exception caught at row $rowIndex column $columnName while trying to set property $propertyName", e
				//continue in the loop, so can collect other properties
			}
		}
		log.debug "Returning $returnParams for Row $rowIndex, columnMap $config.columnMap"

		if (anyNonNullValues){ 
			//validate rows
			pcc.checkReportRow(row, config, this)
			return returnParams
		}
		return [:]
	}

	/**
	 * dual convertOneRowToColumnMap(Sheet, ...
	 */
	void convertOneRowToColumnMap(inputParams, Sheet currentSheet, Map columnMap, int rowIndex, FormulaEvaluator evaluator = null, propertyConfigurationMap = null) {
		def row = currentSheet.getRow(rowIndex) ?: currentSheet.createRow(rowIndex)
		columnMap.each {columnName, propertyName ->
			try {
				def value = inputParams[propertyName]
				setCellValueByColName(value, row, columnName, evaluator, propertyConfigurationMap?.get(propertyName))
			} catch (Exception e) {
				log.warn "Exception caught at row $rowIndex column $columnName while trying to get property $propertyName", e
				//continue in the loop, so can collect other properties
			}
		}
	}



	def getCellValueForSheetNameAndCell(Workbook workbook, String sheetName, String cellName) {
		def sheet = workbook.getSheet(sheetName)
		if (!sheet) return null
		FormulaEvaluator evaluator = workbook.creationHelper.createFormulaEvaluator()
		try {
			def cell = getCell(sheet, cellName)
			def value = getCellValue(cell, null, evaluator)
			log.debug "\t\tValue for $cellName is $value "
			//println "\t\tValue for $cellName is $value "
			return value
		} catch (Exception e) {
			log.error "Exception in cell $cellName thrown while getting cell values", e
			//println "Exception in cell $cellName thrown while getting cell values $e"
			return null
		}
	}


	//cells and values are equivalent
    /**
     *  static Map CONFIG_BOOK_CELL_MAP = [
     *      sheet:'Sheet2',
     *       cellMap: [ 'D3':'title', 'D4':'author','D6':'numSold']
     *    ]
     *   static Map propertyConfigurationMap = [
     *       authorName:([expectedType: ExpectedPropertyType.StringType, defaultValue:null]),
     *       numSold:([expectedType: ExpectedPropertyType.StringType, defaultValue:0]),
     *       ('bookType'):[expectedType: ExpectedPropertyType.StringType, defaultValue:null, valueEquivalentToNull:'Select One']
     *   ]
     *
     *   Map bookParams = excelImportService.cells(workbook, CONFIG_BOOK_CELL_MAP, defaultImportCellCollector, propertyConfigurationMap )
     * 获取按CellMap格式指定的固定点位数值
     * @param workbook  POI中打开的Excel文件对应Workbook
     * @param config    CellMap配置信息，其中cellMap中的key如果为标准的"D13"就获取值或者公式对应的Cache值，如果最后带有下划线"D13_"，则调用
     *                   Formulor Evaluator进行实时计算
     * @param pcc       用来收集处理错误信息的收集器，可使用DefaultImportCellCollector
     * @param propertyConfigurationMap  用来声明CellMap配置中的类型转换，默认数据值等
     * @return
     */
	def cells(Workbook workbook, Map config, ImportCellCollector pcc = null, Map propertyConfigurationMap = [:]) {
		convertFromCellMapToMapWithValues(workbook, config, pcc, null, propertyConfigurationMap)
	}

	//cells and values are equivalent
	def values(Workbook workbook, Map config, ImportCellCollector pcc = null, Map propertyConfigurationMap = [:]) {
		convertFromCellMapToMapWithValues(workbook, config, pcc, null, propertyConfigurationMap)
	}


	//setCells and setValues are equivalent
	void setCells(Map inputMap, Workbook workbook, Map config, Map propertyConfigurationMap = [:]) {
		convertMapWithValuesToCellMap(inputMap, workbook, config, propertyConfigurationMap)
	}

	/** * dual to values(Workbook, ...  */
	void setValues(Map inputMap, Workbook workbook, Map config, Map propertyConfigurationMap = [:]) {
		convertMapWithValuesToCellMap(inputMap, workbook, config, propertyConfigurationMap)
	}


	def convertFromCellMapToMapWithValues(Workbook workbook, Map config, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, Map propertyConfigurationMap = [:]) {
		def sheet = workbook.getSheet(config.sheet)
		if (!sheet) throw new IllegalArgumentException("Did not find sheet named ${config.sheet}")
		if (propertyConfigurationMap == [:]) {
			propertyConfigurationMap = config.configMap ?: [:]
		}
		convertFromCellMapToMapWithValues(sheet, config.cellMap, pcc, evaluator, propertyConfigurationMap)
	}

	/**
	 * dual convertFromCellMapToMapWithValues(Workbook, ...
	 */
	void convertMapWithValuesToCellMap(Map inputMap, Workbook workbook, Map config, Map propertyConfigurationMap = [:]) {
		def sheet = workbook.getSheet(config.sheet)
		if (!sheet) throw new IllegalArgumentException("Did not find sheet named ${config.sheet}")
		if (propertyConfigurationMap == [:]) {
			propertyConfigurationMap = config.configMap ?: [:]
		}
		convertMapWithValuesToCellMap(inputMap, sheet, config.cellMap, propertyConfigurationMap)
	}


    //squareFeet:([expectedType: IntType, defaultValue:null]),
	def convertFromCellMapToMapWithValues(Sheet currentSheet, Map cellMap, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, Map propertyConfigurationMap = [:]) {
		Map objectParams = [:]
		evaluator = evaluator?:currentSheet.workbook.creationHelper.createFormulaEvaluator()
		cellMap.each { String cellName, String propertyName ->
			try {
                String realCellName = cellName
                if(cellName.endsWith(FORMULAR_EVALUATOR_FLAG)){
                   realCellName=cellName.substring(0,cellName.length()-1)
                }
				def cell = getCell(currentSheet, realCellName)
                def value
                if(cellName.endsWith(FORMULAR_EVALUATOR_FLAG)){
                    //如果cell名称后面跟下划线，则表明该字段需要进行实时公式的计算
                    value = getCellValue(cell, pcc, evaluator, propertyConfigurationMap[propertyName])
                }else{
                    value = getCellValue(cell, pcc, null, propertyConfigurationMap[propertyName])
                }

				//println "\t\tValue for $propertyName is $value "
				log.debug "\t\tValue for $propertyName is $value "
				//fix by Jackson
				if (value == null) {
					log.warn "No value in cell $cellName set.  Was trying to find $propertyName"
				} else {
					objectParams[propertyName] = value
				}

			} catch (Exception e) {
				log.error "Exception in cell $cellName getting $propertyName thrown while getting cell values", e
				//println "Exception in cell $cellName getting $propertyName thrown while getting cell values $e"
			}
		}
		log.debug "Returning objectParams $objectParams"
		objectParams
	}


	/**
	 * dual convertFromCellMapToMapWithValues(Sheet, ...
	 */
	void convertMapWithValuesToCellMap(Map objectParams, Sheet currentSheet, Map cellMap, Map propertyConfigurationMap = [:]) {
		FormulaEvaluator evaluator = currentSheet.workbook.creationHelper.createFormulaEvaluator()
		cellMap.each { String cellName, String propertyName ->
			try {
				def cell = getCellOrCreate(currentSheet, cellName)
				def value = objectParams[propertyName]
				setCellValue(value, cell, evaluator, propertyConfigurationMap[propertyName])
			} catch (Exception e) {
				log.error "Exception in cell $cellName setting $propertyName thrown while setting cell values", e
				//println "Exception in cell $cellName setting $propertyName thrown while setting cell values $e"
			}
		}
	}


	Serializable getCellValueByColName(Row row, String columnName, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, Map propertyConfiguration = [:]) {
		int colIndex = CellReference.convertColStringToIndex(columnName)
		log.debug "getCellValueByColName $columnName row ${row.rowNum}, propConfig $propertyConfiguration, colIndex = $colIndex"
		Cell cell = row.getCell(colIndex, Row.CREATE_NULL_AS_BLANK )
		//println "\t\t\tCell is [$cell], colIndex $colIndex, colName $columnName, ${row.rowNum}"
		getCellValue(cell, pcc, evaluator, propertyConfiguration)
	}

	/**
	 * dual getCellValueByColName(Row, ...
	 */
	void setCellValueByColName(value, Row row, String columnName, FormulaEvaluator evaluator = null, Map propertyConfiguration = [:]) {
		int colIndex = CellReference.convertColStringToIndex(columnName)
		Cell cell = row.getCell(colIndex) ?: row.createCell(colIndex, Cell.CELL_TYPE_BLANK)
		setCellValue(value, cell, evaluator, propertyConfiguration)
	}

	void setCellValueByColIndex(value, Row row, int colIndex, FormulaEvaluator evaluator = null, Map propertyConfiguration = [:]) {
		Cell cell = row.getCell(colIndex) ?: row.createCell(colIndex, Cell.CELL_TYPE_BLANK)
		setCellValue(value, cell, evaluator, propertyConfiguration)
	}


	def getCellValueByCellName(Sheet currentSheet, String cellName, FormulaEvaluator evaluator = null) {
		if (evaluator == null) {
			evaluator = currentSheet.workbook.creationHelper.createFormulaEvaluator()
		}
		def cell = getCell(currentSheet, cellName)
		getCellValue(cell, null, evaluator)
	}

	/**
	 * Returns date or string, or numeric, depending on what's in the cell
	 */
	Serializable getCellValue(Cell origcell, ImportCellCollector pcc = null, FormulaEvaluator evaluator = null, propertyConfiguration = [:]) {
		
		pcc = pcc ?: NoopImportCellCollector.NoopInstance

        //如果该cell是一个公式，那么结算得出值
		Cell cell = evaluator ? evaluator.evaluateInCell(origcell) : origcell; //evaluates formula and returns value
		if (cell == null) {
			pcc.reportCell(origcell, propertyConfiguration)
			return propertyConfiguration?.defaultValue?:null
		}

        def cellType = cell.cellType
        //如果该cell是一个公式，且没有要求进行公式计算，那么就直接取CachedFormulaResult值
        if(cellType==Cell.CELL_TYPE_FORMULA){
            cellType=cell.cachedFormulaResultType
        }

		switch (cellType) {
			case Cell.CELL_TYPE_STRING:
				//println "string cell $origcell"
				//println "string cell propertyConfig $propertyConfiguration"
				if(!propertyConfiguration || propertyConfiguration.expectedType == excelimport.ExpectedPropertyType.StringType) {
					String strValue = cell.stringCellValue?.trim()
					if (propertyConfiguration && strValue == propertyConfiguration?.valueEquivalentToNull) {
						log.info("Found a value that's not null (value ${strValue}), but configuration property says to return null anyway")
						return null
					}
					//log.warn "Encountered unexpected string cell Value ${cell.stringCellValue} at row ${cell.getRowIndex()} column ${cell.getColumnIndex()}"
					return strValue
				}

				if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.DateType || propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.DateJavaType) {
					def stringDate = cell.stringCellValue?.trim()
					//println "Expected type - Date for String value ${cell.stringCellValue}"
					//println "Expected type - Date for String value ${stringDate.class}"
					try {
						def df = new java.text.SimpleDateFormat('MM/dd/yy')
						df.setLenient(false) //would fail on Nonexistent dates (ie. February 30th, April 31)
						def javaDate = df.parse(stringDate)

						if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.DateJavaType) {
							if(pcc.checkReportValue(javaDate, cell, propertyConfiguration)) {
								return propertyConfiguration?.defaultValue
							}
							return javaDate
						}

						LocalDate localDate = LocalDate.fromDateFields(javaDate)
						if(pcc.checkReportValue(localDate, cell, propertyConfiguration)) {
							return propertyConfiguration?.defaultValue
						}
						// println "Returning javaDate  ${javaDate}, ${javaDate.class}"
						return localDate
					} catch (e) {
						pcc.reportCell(cell, propertyConfiguration)
						return propertyConfiguration?.defaultValue
					}
					//return propertyConfiguration.defaultValue
				}

				if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.IntType) {
					log.warn "${getCellAddresString(cell)} Expected Type is INT, but cell type is String, trying to extract numeric value"
					try {
						return cell.stringCellValue?.toInteger()
					} catch (Exception e) {
						log.warn "${getCellAddresString(cell)} Cannot get numeric value, returning default value specified for this type, which is ${propertyConfiguration.defaultValue}"
						pcc.reportCell(cell, propertyConfiguration)
						return propertyConfiguration.defaultValue
					}
				}

				if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.DoubleType) {
					log.warn "${getCellAddresString(cell)}Expected Type is DOUBLE, but cell type is String, trying to extract numeric value"
					try {
						return cell.stringCellValue?.toDouble()
					} catch (Exception e) {
						log.warn "${getCellAddresString(cell)} Cannot get double value, returning default value specified for this type", e
						pcc.reportCell(cell, propertyConfiguration)
						return propertyConfiguration.defaultValue
					}
				}

				if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.EmailType) {
					String strValue = cell.stringCellValue?.trim()
					if (propertyConfiguration && strValue == propertyConfiguration?.valueEquivalentToNull) {
						log.info("Found a value that's not null (value ${strValue}), but configuration property says to return null anyway")
						return null
					}
					def emailValidator = org.apache.commons.validator.EmailValidator.newInstance()
					if(emailValidator.isValid(strValue)) {
						return strValue
					}
					log.warn "${getCellAddresString(cell)} Cannot get email value, returning default value specified for this type, which is ${propertyConfiguration.defaultValue}"
					pcc.reportCell(cell, propertyConfiguration)
					return propertyConfiguration.defaultValue
				}

				log.warn "${getCellAddresString(cell)} Potential issue -  ${getCellAddresString(cell)}the excel file claims the type is String, but expecting something else for cell with value $origcell. Returning default value of ${propertyConfiguration?.defaultValue}"
				//println "Potential issue - the excel file claims the type is String, but expecting something else for cell with value $origcell. Returning default value of ${propertyConfiguration.defaultValue}"

				pcc.reportCell(cell, propertyConfiguration)
				return propertyConfiguration?.defaultValue

			case Cell.CELL_TYPE_NUMERIC:
				//println "numeric cell $origcell"
				if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.StringType) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					return cell.stringCellValue?.trim()
				}
				if (DateUtil.isCellDateFormatted(cell)) {
					def javaDate = cell.dateCellValue

					if (propertyConfiguration?.expectedType == excelimport.ExpectedPropertyType.DateJavaType) {
						if(pcc.checkReportValue(javaDate, cell, propertyConfiguration)) {
							return propertyConfiguration?.defaultValue
						}
						return javaDate
					}

					LocalDate localDate = new LocalDate(javaDate)
					if(pcc.checkReportValue(localDate, cell, propertyConfiguration)) {
						return propertyConfiguration?.defaultValue
					}
					return localDate
				} else {

/*
				//println "numeric cell $origcell"
				if (propertyConfiguration?.expectedType == StringType) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					def stringValue = cell.stringCellValue
					if (stringValue!=null) {
						return stringValue
					} else {
						return propertyConfiguration.defaultValue
					}
				}
				if (DateUtil.isCellDateFormatted(cell)) {
					if (propertyConfiguration?.expectedType == DateJavaType) {
						return cell.dateCellValue
					} else {
						return new LocalDate(cell.dateCellValue)
					}
				} else {
*/
					def numeric = cell.numericCellValue
					if(pcc.checkReportValue(numeric, cell, propertyConfiguration)) {
						return propertyConfiguration?.defaultValue
					}
					if (numeric!=null) {
						return numeric
					} else {
						return propertyConfiguration?.defaultValue
					}
				}
				break;
			case Cell.CELL_TYPE_ERROR:
				log.warn "CELL Type is ERROR value: $cell.errorCellValue ${getCellAddresString(cell)}"
				pcc.reportCell(cell, propertyConfiguration)
				return null
			case Cell.CELL_TYPE_FORMULA:
				//log.warn "Cell type is formula, returning null  ${getCellAddresString(cell)}"
                log.warn "Cell type is formula, returning null  ${getCellAddresString(cell)}"
                pcc.reportCell(cell, propertyConfiguration)
                return null
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.booleanCellValue
			case Cell.CELL_TYPE_BLANK:
				log.debug "Found blank cell at ${getCellAddresString(cell)}"
				pcc.reportCell(cell, propertyConfiguration)
				return null;
			default:
				log.warn "Unexpected cell type.  ${getCellAddresString(cell)} Ignoring.  Cell Value [${cell}] type ${cell.cellType}"
		}
		log.error "WARNING: RETURNING NULL FROM getCellValue.  UNEXPECTED CONDITION ${getCellAddresString(cell)}"
		pcc.reportCell(cell, propertyConfiguration)
		return null;
	}


	def getCellAddresString(cell) {
		"Cell row ${cell.getRowIndex()}  column ${cell.getColumnIndex()}"
	}

	/**
	 * dual getCellValue(Cell, ...
	 */
	void setCellValue(value, Cell origcell, FormulaEvaluator evaluator = null, propertyConfiguration = [:]) {
		if (!propertyConfiguration || !propertyConfiguration.expectedType) {
			//... null handling
			//directly compatible with expected type of setCellValue
			if ([Double.TYPE, String.class, Boolean.TYPE].any {it.isInstance(value)}) {
				origcell.setCellValue(value)
				return
			}
			//conversion compatible with expected type of setCellValue
			if (value instanceof Number) {
				//avoid float->double widening to generate double values like 441.489990234375 instead of 441.49
				origcell.setCellValue(Double.valueOf(value.toString()))
				return
			}
			//Date
			if ([Date.class, LocalDate.class].any {it.isInstance(value)}) {
				origcell.setCellValue((value instanceof LocalDate)? value.toDateTimeAtStartOfDay().toDate(): value)
				return
			}

            if(!value){
                //origcell.setCellType(Cell.CELL_TYPE_BLANK)
                origcell.setCellValue(value)
            }
			return
		}

		switch (propertyConfiguration.expectedType) {
			case excelimport.ExpectedPropertyType.EmailType:
			case excelimport.ExpectedPropertyType.StringType:
				if (value == null) {
					//dsq-OSM - maybe prefer to write out the valueEquivalentToNull if defined
					origcell.setCellValue('')
					return
				}
				if (value instanceof String) {
					origcell.setCellValue(value)
					return
				}
				//... type convertion handling
				break;
		    case excelimport.ExpectedPropertyType.IntType:
		    case excelimport.ExpectedPropertyType.DoubleType:
				if (value == null) {
					return
				}
				if (value instanceof Number) {
					origcell.setCellValue(Double.valueOf(value.toString()))
					origcell.setCellType(Cell.CELL_TYPE_NUMERIC)
					return
				}
			    log.warn "value $value was supposed tobe numeric but isn't"
				//... type convertion handling
				break;
			case excelimport.ExpectedPropertyType.DateType:
			case excelimport.ExpectedPropertyType.DateJavaType:
				if (value == null) {
					return
				}
				if ([Date.class, LocalDate.class].any {it.isInstance(value)}) {
					//dsq-OSM - not sure how to properly handle this date related stuff
					def style = origcell.sheet.workbook.createCellStyle()
					style.cloneStyleFrom(origcell.getCellStyle())
					style.setDataFormat((short)0x0e)
					origcell.setCellStyle(style)
					origcell.setCellValue((value instanceof LocalDate)? value.toDateTimeAtStartOfDay().toDate(): value)
					return
				}
				//... type convertion handling
				break;
            case excelimport.ExpectedPropertyType.FormulaType:
                if(value == null){
                    return
                }

                if(value instanceof String){
                    origcell.setCellFormula(value)
                }

                break;
			default:
				log.error "Unexpected property type.  Ignoring.  Property Value [${value}] type ${propertyConfiguration.expectedType}"
		}
		log.error "WARNING: RETURNING FROM setCellValue.  UNEXPECTED CONDITION expectedType ${propertyConfiguration.expectedType} / ${value}"
		return;
	}




	Cell getCell(Sheet currentSheet, String ref) {
		CellReference cellReference = new CellReference(ref);
		Row row = currentSheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol())
		//println "returning cell $cell"
		cell
	}

	Cell getCellOrCreate(Sheet currentSheet, String ref) {
		CellReference cellReference = new CellReference(ref);
		Row row = currentSheet.getRow(cellReference.getRow());
		row.getCell(cellReference.getCol()) ?: row.createCell(cellReference.getCol(), Cell.CELL_TYPE_BLANK)
	}


}