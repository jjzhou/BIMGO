package eis.bas

abstract class BaseService {
    static transactional = false
    /**
     * @param params controller 层的params对象，用于获取查询条件的值
     * @return 满足条件的总记录数
     */
    def countClosure = {params ->
        def query = makeConditions(this.getValidCondition(params))
        return query.count()
    }

    /**
     * @param params controller 层的params对象，用于获取查询条件的值
     * @param max 每页显示的记录数
     * @param offset 起始记录数
     * @param sortColumn 排序字段
     * @param sortDirection 排序方向 1 or -1
     * @return 查询结果集list
     */
    def queryClosure = {params, max, offset, sortColumn, sortDirection ->
        def query = makeConditions(this.getValidCondition(params))

        //按照分页条件查询出记录
        def instanceList = query.list(max:max, offset:offset) {
            if (sortColumn && sortDirection) {
                order(sortColumn, sortDirection)
            }
        }
        return instanceList
    }

    /**
     * 过滤查询条件，只返回有值的条件map
     * @param params controller 层的params对象，用于获取查询条件的值
     * @return 返回有效的查询条件
     */
    protected Map getValidCondition(params) {
        def conditions = params.conditions
        Map search = [:]
        conditions.each {
            if(it.value){
                search << it
            }
        }
        return search
    }

    /**
     * 组装查询条件
     * @param conditions
     * @return
     */
    abstract makeConditions(Map conditions)

}
