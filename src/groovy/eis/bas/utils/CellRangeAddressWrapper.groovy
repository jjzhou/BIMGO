package eis.bas.utils

import org.apache.poi.ss.util.CellRangeAddress

/**
 * Created with IntelliJ IDEA.
 * User: HUASYU
 * Date: 12-5-31
 * Time: 上午9:20
 * To change this template use File | Settings | File Templates.
 */


public class CellRangeAddressWrapper implements Comparable<CellRangeAddressWrapper> {

    public CellRangeAddress range;

    /**
     * @param theRange the CellRangeAddress object to wrap.
     */
    public CellRangeAddressWrapper(CellRangeAddress theRange) {
        this.range = theRange;
    }

    /**
     * @param o the object to compare.
     * @return -1 the current instance is prior to the object in parameter, 0: equal, 1: after...
     */
    public int compareTo(CellRangeAddressWrapper o) {

        if (range.getFirstColumn() < o.range.getFirstColumn()
                && range.getFirstRow() < o.range.getFirstRow()) {
            return -1;
        } else if (range.getFirstColumn() == o.range.getFirstColumn()
                && range.getFirstRow() == o.range.getFirstRow()) {
            return 0;
        } else {
            return 1;
        }

    }

}