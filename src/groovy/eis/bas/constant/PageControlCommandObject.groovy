package eis.bas.constant

/**
 * Created with IntelliJ IDEA.
 * User: win7
 * Date: 12-4-29
 * Time: 下午8:02
 * 用于页面查询的变量存储基类
 */
class PageControlCommandObject {

    int total
    int pageSize
    int pageIndex
    String sortColumn
    String sortDirection = "asc"

    void processPagination(){
        pageSize = pageSize?pageSize:DEFAULT_PAGE_SIZE
        pageIndex = pageIndex?pageIndex:1
        if (pageIndex>Math.ceil(total/pageSize)){
            pageIndex= (Math.ceil(total/pageSize)) as int
        }
        //考虑到记录为0的情况下，仍然需要显示第一页
        pageIndex = pageIndex?pageIndex:1
    }


    static final DEFAULT_PAGE_SIZE = 10
    static final PAGE_SIZE_MAP = [ [ type : 10, name : 10], [ type : 20, name : 20 ], [ type : 50, name : 50 ],
            [ type : 100, name : 100 ], [ type : 200, name : 200 ], [ type : 500, name : 500 ], [ type : 1000, name : 1000 ]]
}
