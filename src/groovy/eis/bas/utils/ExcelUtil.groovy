package eis.bas.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.util.CellRangeAddress

/**
 * Created with IntelliJ IDEA.
 * User: HUASYU
 * Date: 12-5-31
 * Time: 上午9:20
 * To change this template use File | Settings | File Templates.
 */
class ExcelUtil {

    /**
     * @param newSheet the sheet to create from the copy.
     * @param sheet the sheet to copy.
     */
    public static void copySheets(Sheet newSheet, Sheet sheet) {
        copySheets(newSheet, sheet, true);
    }

    /**
     * @param newSheet the sheet to create from the copy.
     * @param sheet the sheet to copy.
     * @param copyStyle true copy the style.
     */
    public static void copySheets(Sheet newSheet, Sheet sheet, boolean copyStyle) {
        removeRow(newSheet)
        int maxColumnNum = 0;
        Map<Integer, CellStyle> styleMap = (copyStyle) ? new HashMap<Integer, CellStyle>() : null;
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            Row srcRow = sheet.getRow(i);
            Row destRow = newSheet.createRow(i);
            if (srcRow != null) {
                copyRow(sheet, newSheet, srcRow, destRow, styleMap);
                if (srcRow.getLastCellNum() > maxColumnNum) {
                    maxColumnNum = srcRow.getLastCellNum();
                }
            }
        }
        for (int i = 0; i <= maxColumnNum; i++) {
            newSheet.setColumnWidth(i, sheet.getColumnWidth(i));
        }
    }

    /**
     * 从一个Excel Sheet中拷贝指定的区域（矩形）到另一个Excel Sheet，目前仅支持在同一个workbook中
     * 注意系统不会校验源sourceSheet中所给出的坐标区域是否有效，请保证其合理性
     * 需要注意的是目标区域的如果没创建会动态创建， 坐标都是从0，0开始计算
     *
     * @param targetSheet   目标Sheet，被写入对象
     * @param sourceSheet   源Sheet,格式读取对象
     * @param targetInfo    targetInfo中包含2个值，分别是r0,c0, 代表粘帖到targetSheet的哪一个起始点，大小与原sourceSheet中相同
     * @param sourceInfo    sourceInfo中包含4个值，分别是r0,c0,r1,c1, 代表拷贝sourceSheet的那一块区域
     * @param repeatTimes   重复次数，如果大于1，则按照矩形的大小，横向展开重复粘帖
     */

    public static void copyPartSheets(Sheet targetSheet, Sheet sourceSheet, Map targetInfo, Map sourceInfo, int repeatTimes){
        //根据源Sheet的行信息进行处理
        def  widthSource =  sourceInfo.c1-sourceInfo.c0+1  //矩形的宽度
        def  heightSource = sourceInfo.r1-sourceInfo.r0+1  //矩形的高度
        Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();
        for(int i=0; i< heightSource; i++){
            Row srcRow = sourceSheet.getRow(sourceInfo.r0+i)
            def targetRowNum = targetInfo.r0 + i  //获取应该写入的目标行
            Row tarRow = targetSheet.getRow(targetRowNum)
            if(srcRow){
                if(!tarRow){
                    tarRow = targetSheet.createRow(targetRowNum)
                }
                //对该行进行复制处理
                tarRow.setHeight(srcRow.getHeight());
                for(int k=0; k<repeatTimes;k++){
                    // manage a list of merged zone in order to not insert two times a merged zone
                    //Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();
                    for(int m=0;m<widthSource;m++){
                         Cell srcCell = srcRow.getCell(m+sourceInfo.c0)
                         Cell tarCell = tarRow.getCell(k*widthSource+m+targetInfo.c0)
                         if(srcCell){
                             if(!tarCell){
                                 tarCell = tarRow.createCell(k*widthSource+m+targetInfo.c0)
                             }
                             copyCell(srcCell, tarCell, null)
                             CellRangeAddress mergedRegion = getMergedRegion(sourceSheet, srcRow.getRowNum(), (short) srcCell.getColumnIndex());
                             if (mergedRegion != null) {
                                 CellRangeAddress newMergedRegion = new CellRangeAddress(mergedRegion.getFirstRow()-sourceInfo.r0+targetInfo.r0,
                                         mergedRegion.getLastRow()-sourceInfo.r0+targetInfo.r0,
                                         mergedRegion.getFirstColumn()-sourceInfo.c0+targetInfo.c0+k*widthSource,
                                         mergedRegion.getLastColumn()-sourceInfo.c0+targetInfo.c0+k*widthSource);
                                 CellRangeAddressWrapper wrapper = new CellRangeAddressWrapper(newMergedRegion);
                                 if (isNewMergedRegion(wrapper, mergedRegions)) {
                                     mergedRegions.add(wrapper);
                                     //targetSheet.addMergedRegion(wrapper.range);
                                 }
                             }
                         }
                    }
                }
                //需要设置Column的宽度
                for(int j=targetInfo.c0;j<(targetInfo.c0+widthSource*repeatTimes);j++){
                    def sourceAddress = sourceInfo.c0 + ((j-targetInfo.c0) % widthSource)
                    targetSheet.setColumnWidth(j, sourceSheet.getColumnWidth(sourceAddress));
                }
            }
        }
        mergedRegions.unique()
        mergedRegions.each {
            targetSheet.addMergedRegion(it.range)
        }
    }

    /**
     * @param srcSheet the sheet to copy.
     * @param destSheet the sheet to create.
     * @param srcRow the row to copy.
     * @param destRow the row to create.
     * @param styleMap -
     */
    public static void copyRow(Sheet srcSheet, Sheet destSheet, Row srcRow, Row destRow, Map<Integer, CellStyle> styleMap) {
        // manage a list of merged zone in order to not insert two times a merged zone
        Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<CellRangeAddressWrapper>();
        destRow.setHeight(srcRow.getHeight());
        // pour chaque row
        int j = srcRow.getFirstCellNum();
        if (j < 0) {j = 0;}
        for (; j <= srcRow.getLastCellNum(); j++) {
            Cell oldCell = srcRow.getCell(j);   // ancienne cell
            Cell newCell = destRow.getCell(j);  // new cell
            if (oldCell != null) {
                if (newCell == null) {
                    newCell = destRow.createCell(j);
                }
                // copy chaque cell
                copyCell(oldCell, newCell, styleMap);
                // copy les informations de fusion entre les cellules
                //System.out.println("row num: " + srcRow.getRowNum() + " , col: " + (short)oldCell.getColumnIndex());
                CellRangeAddress mergedRegion = getMergedRegion(srcSheet, srcRow.getRowNum(), (short) oldCell.getColumnIndex());

                if (mergedRegion != null) {
                    //System.out.println("Selected merged region: " + mergedRegion.toString());
                    //CellRangeAddress newMergedRegion = new CellRangeAddress(mergedRegion.getFirstRow(), mergedRegion.getLastRow(), mergedRegion.getFirstColumn(), mergedRegion.getLastColumn());
                    CellRangeAddress newMergedRegion = new CellRangeAddress(destRow.getRowNum(), destRow.getRowNum(), mergedRegion.getFirstColumn(), mergedRegion.getLastColumn());
                    //System.out.println("New merged region: " + newMergedRegion.toString());
                    CellRangeAddressWrapper wrapper = new CellRangeAddressWrapper(newMergedRegion);
                    if (isNewMergedRegion(wrapper, mergedRegions)) {
                        mergedRegions.add(wrapper);
                        destSheet.addMergedRegion(wrapper.range);
                    }
                }
            }
        }

    }

    /**
     * @param oldCell
     * @param newCell
     * @param styleMap
     */
    public static void copyCell(Cell oldCell, Cell newCell, Map<Integer, CellStyle> styleMap) {
        if (styleMap != null) {
            if (oldCell.getSheet().getWorkbook() == newCell.getSheet().getWorkbook()) {
                newCell.setCellStyle(oldCell.getCellStyle());
            } else {
                int stHashCode = oldCell.getCellStyle().hashCode();
                CellStyle newCellStyle = styleMap.get(stHashCode);
                if (newCellStyle == null) {
                    newCellStyle = newCell.getSheet().getWorkbook().createCellStyle();
                    newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    styleMap.put(stHashCode, newCellStyle);
                }
                newCell.setCellStyle(newCellStyle);
            }
        }else{
            if (oldCell.getSheet().getWorkbook() == newCell.getSheet().getWorkbook()) {
                newCell.setCellStyle(oldCell.getCellStyle());
            }
        }
        switch (oldCell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                newCell.setCellType(Cell.CELL_TYPE_BLANK);
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            default:
                break;
        }

    }

    /**
     * Récupère les informations de fusion des cellules dans la sheet source pour les appliquer
     * à la sheet destination...
     * Récupère toutes les zones merged dans la sheet source et regarde pour chacune d'elle si
     * elle se trouve dans la current row que nous traitons.
     * Si oui, retourne l'objet CellRangeAddress.
     *
     * @param sheet the sheet containing the data.
     * @param rowNum the num of the row to copy.
     * @param cellNum the num of the cell to copy.
     * @return the CellRangeAddress created.
     */
    public static CellRangeAddress getMergedRegion(Sheet sheet, int rowNum, short cellNum) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheet.getMergedRegion(i);
            if (merged.isInRange(rowNum, cellNum)) {
                return merged;
            }
        }
        return null;
    }
    //根据索引号生成excel的列名，index 从0开始
    public static String getColumnName(int index) {
        int firstCharaCode = 65
        int charLength = 26
        int a = index / charLength
        int b = index % charLength
        StringBuffer sb = new StringBuffer()
        if (a < 1) {
            sb.append((char) (firstCharaCode + index))
        } else {
            sb.append(getColumnName(a - 1)).append(getColumnName(b))
        }
        return sb.toString()
    }

    /**
     * Check that the merged region has been created in the destination sheet.
     * @param newMergedRegion the merged region to copy or not in the destination sheet.
     * @param mergedRegions the list containing all the merged region.
     * @return true if the merged region is already in the list or not.
     */
    private static boolean isNewMergedRegion(CellRangeAddressWrapper newMergedRegion, Set<CellRangeAddressWrapper> mergedRegions) {
        return !mergedRegions.contains(newMergedRegion);
    }

    private static void removeRow(Sheet sheet){
        if(sheet){
            for(i in sheet.getFirstRowNum()..sheet.getLastRowNum()){
                Row row = sheet.getRow(i)
                if(!row){
                    continue
                }
                for(j in row.getFirstCellNum()..row.getLastCellNum()){
                    sheet.removeMergedRegion(j)
                }
                sheet.removeMergedRegion(i)
                sheet.removeRow(row)
            }
        }
    }

    /*private void transform(HSSFSheet sheetOld, XSSFSheet sheetNew) {
        System.out.println("transform Sheet");

        sheetNew.setDisplayFormulas(sheetOld.isDisplayFormulas());
        sheetNew.setDisplayGridlines(sheetOld.isDisplayGridlines());
        sheetNew.setDisplayGuts(sheetOld.getDisplayGuts());
        sheetNew.setDisplayRowColHeadings(sheetOld.isDisplayRowColHeadings());
        sheetNew.setDisplayZeros(sheetOld.isDisplayZeros());
        sheetNew.setFitToPage(sheetOld.getFitToPage());
        sheetNew.setForceFormulaRecalculation(sheetOld
                .getForceFormulaRecalculation());
        sheetNew.setHorizontallyCenter(sheetOld.getHorizontallyCenter());
        sheetNew.setMargin(Sheet.BottomMargin,
                sheetOld.getMargin(Sheet.BottomMargin));
        sheetNew.setMargin(Sheet.FooterMargin,
                sheetOld.getMargin(Sheet.FooterMargin));
        sheetNew.setMargin(Sheet.HeaderMargin,
                sheetOld.getMargin(Sheet.HeaderMargin));
        sheetNew.setMargin(Sheet.LeftMargin,
                sheetOld.getMargin(Sheet.LeftMargin));
        sheetNew.setMargin(Sheet.RightMargin,
                sheetOld.getMargin(Sheet.RightMargin));
        sheetNew.setMargin(Sheet.TopMargin, sheetOld.getMargin(Sheet.TopMargin));
        sheetNew.setPrintGridlines(sheetNew.isPrintGridlines());
        sheetNew.setRightToLeft(sheetNew.isRightToLeft());
        sheetNew.setRowSumsBelow(sheetNew.getRowSumsBelow());
        sheetNew.setRowSumsRight(sheetNew.getRowSumsRight());
        sheetNew.setVerticallyCenter(sheetOld.getVerticallyCenter());

        XSSFRow rowNew;
        for (Row row : sheetOld) {
            rowNew = sheetNew.createRow(row.getRowNum());
            if (rowNew != null)
                this.transform((HSSFRow) row, rowNew);
        }

        for (int i = 0; i < this.lastColumn; i++) {
            sheetNew.setColumnWidth(i, sheetOld.getColumnWidth(i));
            sheetNew.setColumnHidden(i, sheetOld.isColumnHidden(i));
        }

        for (int i = 0; i < sheetOld.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheetOld.getMergedRegion(i);
            sheetNew.addMergedRegion(merged);
        }
    }

    private void transform(HSSFRow rowOld, XSSFRow rowNew) {
        XSSFCell cellNew;
        rowNew.setHeight(rowOld.getHeight());
        if (rowOld.getRowStyle() != null) {
            Integer hash = rowOld.getRowStyle().hashCode();
            if (!this.styleMap.containsKey(hash))
                this.transform(hash, rowOld.getRowStyle(),
                        this.workbookNew.createCellStyle());
            rowNew.setRowStyle(this.styleMap.get(hash));
        }
        for (Cell cell : rowOld) {
            cellNew = rowNew.createCell(cell.getColumnIndex(),
                    cell.getCellType());
            if (cellNew != null)
                this.transform((HSSFCell) cell, cellNew);
        }
        this.lastColumn = Math.max(this.lastColumn, rowOld.getLastCellNum());
    }

    private void transform(HSSFCell cellOld, XSSFCell cellNew) {
        cellNew.setCellComment(cellOld.getCellComment());

        Integer hash = cellOld.getCellStyle().hashCode();
        if (!this.styleMap.containsKey(hash)) {
            this.transform(hash, cellOld.getCellStyle(),
                    this.workbookNew.createCellStyle());
        }
        cellNew.setCellStyle(this.styleMap.get(hash));

        switch (cellOld.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellNew.setCellValue(cellOld.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                cellNew.setCellValue(cellOld.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellNew.setCellValue(cellOld.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellNew.setCellValue(cellOld.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cellNew.setCellValue(cellOld.getStringCellValue());
                break;
            default:
                System.out.println("transform: Unbekannter Zellentyp "
                        + cellOld.getCellType());
        }
    }

    private void transform(Integer hash, HSSFCellStyle styleOld,
                           XSSFCellStyle styleNew) {
        styleNew.setAlignment(styleOld.getAlignment());
        styleNew.setBorderBottom(styleOld.getBorderBottom());
        styleNew.setBorderLeft(styleOld.getBorderLeft());
        styleNew.setBorderRight(styleOld.getBorderRight());
        styleNew.setBorderTop(styleOld.getBorderTop());
        styleNew.setDataFormat(this.transform(styleOld.getDataFormat()));
        styleNew.setFillBackgroundColor(styleOld.getFillBackgroundColor());
        styleNew.setFillForegroundColor(styleOld.getFillForegroundColor());
        styleNew.setFillPattern(styleOld.getFillPattern());
        styleNew.setFont(this.transform(styleOld.getFont(this.workbookOld)));
        styleNew.setHidden(styleOld.getHidden());
        styleNew.setIndention(styleOld.getIndention());
        styleNew.setLocked(styleOld.getLocked());
        styleNew.setVerticalAlignment(styleOld.getVerticalAlignment());
        styleNew.setWrapText(styleOld.getWrapText());
        this.styleMap.put(hash, styleNew);
    }

    private short transform(short index) {
        DataFormat formatOld = this.workbookOld.createDataFormat();
        DataFormat formatNew = this.workbookNew.createDataFormat();
        return formatNew.getFormat(formatOld.getFormat(index));
    }

    private XSSFFont transform(HSSFFont fontOld) {
        XSSFFont fontNew = this.workbookNew.createFont();
        fontNew.setBoldweight(fontOld.getBoldweight());
        fontNew.setCharSet(fontOld.getCharSet());
        fontNew.setColor(fontOld.getColor());
        fontNew.setFontName(fontOld.getFontName());
        fontNew.setFontHeight(fontOld.getFontHeight());
        fontNew.setItalic(fontOld.getItalic());
        fontNew.setStrikeout(fontOld.getStrikeout());
        fontNew.setTypeOffset(fontOld.getTypeOffset());
        fontNew.setUnderline(fontOld.getUnderline());
        return fontNew;
    }
*/
}


