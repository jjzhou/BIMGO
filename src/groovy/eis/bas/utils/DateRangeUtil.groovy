package eis.bas.utils

/**
 * Created with IntelliJ IDEA.
 * User: YANLLIU
 * Date: 12-11-2
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
class DateRangeUtil {

    //判断给定的两个时间段是否有冲突，是否有重叠，是否包含对方等等，如果有冲突就返回true, 没有冲突就返回false
    //前提是start1<=end1, start2<=end2,  这个前提条件在本函数中不进行判断
    public static boolean rangeConflict(Date start1, Date end1, Date start2, Date end2){
        //当start1比end2还要大时，肯定不会有冲突
        //或者
        //当end1比start2还要小时，肯定不会有冲突
        //那么有冲突的就是
        if( start1 <= end2 &&  end1>=start2){
            return true
        }
        return false
    }

}
