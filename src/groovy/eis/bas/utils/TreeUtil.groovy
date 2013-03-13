package eis.bas.utils

import eis.bas.constant.ParameterConstant

/**
 * Created with IntelliJ IDEA.
 * User: liuyanlu
 * Date: 12-8-10
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
class TreeUtil {

    /**
     * 在树状结构对象中，根据属性名和值查找符合条件的节点集合
     * @param treeObj   树结构对象，在项目中可以是Domain Class或者是MongoDB的DBObject
     * @param keyName 属性名称用来做为返回HashMap中的Key,每个节点必须包含该属性
     * @param subCatalogName 子目录所对应的对象中的属性名
     * @param keyValueFilters 用来查找符合条件的属性名和值的过滤器,是一个HashMap
     * @param returnAttrNames 返回的节点中应包含的属性名称，如果为空则仅返回Key属性
     *
     * @return 符合过滤条件的key数组列表
     */
    HashMap<String, HashMap> findMatchKeys(def treeObj, String keyName, String subCatalogName, HashMap keyValueFilters, ArrayList<String> returnAttrNames){
       HashMap<String, HashMap> resultMap = new HashMap<String, HashMap>()
       getherMatchKeys(treeObj, keyName, subCatalogName, keyValueFilters,returnAttrNames,resultMap)
       return resultMap
    }

    private void getherMatchKeys(def treeObj, String keyName, String subCatalogName, HashMap keyValueFilters, ArrayList<String> returnAttrNames, HashMap<String, HashMap> resultMap){

        //0. 检查对象是否为空
        if(!treeObj){
            return
        }

        //1. 分析自己是否有子目录，如果有的话，每个子目录做为一个treeObj，递归调用
        def subCatalogs = treeObj[subCatalogName]
        if(subCatalogs){
            subCatalogs.each {
                getherMatchKeys(it, keyName, subCatalogName, keyValueFilters, returnAttrNames, resultMap)
            }
        }

        //2. 分析自身属性是否有符合Filter过滤条件的
        boolean matched = true
        keyValueFilters.each{
             if( it.value != treeObj[it.key] ){ //在Groovy语法中字符串可以直接用＝＝来进行比较的
                matched = false
             }
        }

        if(matched){
            HashMap entityMap = new HashMap()
            entityMap.put(keyName, treeObj[keyName]) //把主键放进去
            returnAttrNames.each {
                if(treeObj[it]){
                    entityMap.put(it, treeObj[it])
                }
            }

            //以主键做为映射的
            resultMap.put(keyName, entityMap)
        }
    }

    /**
     * 将树状结构对象，按叶节点水平铺开变成列表信息
     * @param treeObj  树结构对象，在项目中可以是Domain Class或者是MongoDB的DBObject
     * @param keyName  属性名称用来识别一个节点的唯一编码
     * @param subCatalogName  子目录所对应的对象中的属性名
     * @param returnListNames  返回的水平列表中应包含的属性名称
     * @return 水平展开后的数组列表信息
     */
    ArrayList<Map>  transformToList(def treeObj, String keyName, String subCatalogName, ArrayList<String> returnAttrNames){
       ArrayList<Map> resultList = new ArrayList<Map>()
       gatherList(treeObj, keyName, subCatalogName, returnAttrNames, new HashMap(), resultList, 0)
       return resultList
    }

    private void gatherList(def treeObj, String keyName, String subCatalogName, ArrayList<String> returnAttrNames, HashMap entityMap, ArrayList<Map> resultList, int currentLevel){
        currentLevel++
        //0
        if(!treeObj){
            return
        }

        HashMap cloneMap = entityMap.clone() as HashMap
        cloneMap.put(currentLevel+"_"+keyName, treeObj[keyName])
        returnAttrNames.each{
            if(treeObj[it]){
                cloneMap.put(currentLevel+"_"+it, treeObj[it])
            }
        }

        //1. 如果包含子节点，则说明当前节点不是需要展开的叶节点，仅把自己的信息和需要的属性信息写入到entityMap中，然后就调用子节点迭代处理
        if(treeObj[subCatalogName]){
           treeObj[subCatalogName].each{
                gatherList(it, keyName, subCatalogName, returnAttrNames, cloneMap, resultList, currentLevel)
           }

        }else{
            //2. 如果自身是子节点，则当前信息是需要展开的，同时把父节点及其相关属性填写到entityMap中
            resultList.add(cloneMap)

        }
    }

    /**
     * 检查树状结构对象中，key值是否有重复，如果有则返回的列表中包含重复key的信息
     * @param treeObj  树结构对象
     * @param keyName  属性名称用来识别一个节点的唯一编码
     * @param subCatalogName 子目录所对应的对象中的属性名
     * @return 重复键值的数组列表
     */
    ArrayList<String> checkDuplicateKeys(def treeObj, String keyName, String subCatalogName){
        ArrayList<String>  duplicatedKeyList = new ArrayList<String>()
        ArrayList<String>  keyList = new ArrayList<String>()
        checkDuplication(treeObj, keyName, subCatalogName, keyList, duplicatedKeyList)
        return duplicatedKeyList.unique()
    }

    private void checkDuplication(def treeObj, String keyName, String subCatalogName, ArrayList<String> keyList, ArrayList<String> duplicatedKeyList){
        //0. Null Check
        if(!treeObj){
            return;
        }

        //1. 如果当前节点包含子节点，则对每个子节点进行调用
        if(treeObj[subCatalogName]){
            treeObj[subCatalogName].each{
                checkDuplication(it, keyName, subCatalogName, keyList, duplicatedKeyList)
            }
        }

        //2. 判断在keyList中是否存在与自己节点相同的key,如果是则在duplicatedKeyList中记录
        if(keyList.contains(treeObj[keyName])){
            duplicatedKeyList.add(treeObj[keyName])
        }else{
            keyList.add(treeObj[keyName])
        }
    }

    /**
     * 检查树状结构对象中，必填的属性在每个节点上都必须存在，如果由异常则记录错误的层节点信息
     * @param treeObj 树结构对象
     * @param keyName 用于标识节点名称的主键值
     * @param subCatalogName   子目录所对应的对象中的属性名
     * @param requiredAttrs   需要检查的节点中必填的属性值
     * @return
     */
    ArrayList<String> checkMissingValues(def treeObj, String keyName, String subCatalogName, ArrayList<String> requiredAttrs,String currentLang){
        ArrayList<String> resultList = new ArrayList<String>()
        int currentLevel = 0
        checkMissing(treeObj, keyName, subCatalogName, requiredAttrs, resultList, currentLevel,currentLang)
        return resultList
    }

    private void checkMissing(def treeObj, String keyName, String subCatalogName, ArrayList<String> requiredAttrs, ArrayList<String> resultList, int currentLevel,String currentLang){
        //0. Null Check
        if(!treeObj){
            return;
        }

        //1. 如果当前节点包含子节点，则对每个子节点进行调用
        if(treeObj[subCatalogName]){
            treeObj[subCatalogName].each{
                checkMissing(it, keyName, subCatalogName, requiredAttrs, resultList, currentLevel+1, currentLang)
            }
        }

        //2. 判断在keyList中是否存在与自己节点相同的key,如果是则在duplicatedKeyList中记录
        requiredAttrs.each{
            if(!treeObj[it]){
                if(currentLang == ParameterConstant.LOCALE_ZH_CN){
                    resultList << "错误：级别${currentLevel}节点上主键为${treeObj[keyName]}缺少属性${it}"
                }
                if(currentLang == ParameterConstant.LOCALE_EN_US){
                    resultList << "Error: level ${currentLevel} node primary key ${treeObj[keyName]} missing attribute ${it}"

                }
            }
        }
    }

    /**
     * 将指定树对象进行平面参数化，变成key:displayValue的HashMap，注意根节点不包括在内
     * @param treeObj  树结构对象
     * @param subCatalogName  子目录所对应的对象中的属性名
     * @param keyName   于标识节点名称的主键名
     * @param displayName  需要显示的属性名
     * @return
     */
    HashMap parameterizeTree(def treeObj, String subCatalogName, String keyName, String displayName ){
        def map = [:]
        if(!treeObj) return map

        //由于根节点不需要放入HashMap中，所以仅对第一层子节点调用递归函数
        if(treeObj[subCatalogName]){
            treeObj[subCatalogName].each{
                parameterizeSubTree(it, subCatalogName, keyName, displayName, map)
            }
        }
        return map
    }

    private void parameterizeSubTree(def treeObj, String subCatalogName, String keyName, String displayName, HashMap map){

        if(!treeObj) return

        //放入本节点的key:value信息
        if(treeObj[keyName] && treeObj[displayName]){
            map.put(treeObj[keyName], treeObj[displayName])
        }

        //如果当前节点包含子节点，调用递归函数
        if(treeObj[subCatalogName]){
            treeObj[subCatalogName].each{
                parameterizeSubTree(it, subCatalogName, keyName, displayName, map)
            }
        }
    }

    /**
     * 将指定树对象进行指定层的节点名称转化为List
     * @param treeObj  树结构对象
     * @param subCatalogName  子目录所对应的对象中的属性名
     * @param keyName   于标识节点名称的主键名
     * @param displayName  需要显示的属性名
     * @return
     */
    List listTreeNode (def treeObj, String subCatalogName = "subCatalogs", String keyName = "key", List displayNameList = ["displayZH", "displayEN","tmdReportRequired"], int level){
        List nodeList = []
        if(!treeObj) return nodeList

        //如果level大于0，则调用递归函数
        if(level > 0 && treeObj[subCatalogName]){
            treeObj[subCatalogName].each{
                listSubTreeNode(it, level-1, nodeList)
            }
        }
        else if(level == 0) {   //如果level为0，则返回根节点信息
            Map nodeInfoMap = [:]

            if(treeObj[keyName]) {
                nodeInfoMap.put(keyName, treeObj[keyName])
            }

            displayNameList.each {displayName ->
                if (treeObj[displayName]) {
                    nodeInfoMap.put(displayName, treeObj[displayName])
                }
            }

            nodeList << nodeInfoMap
        }

        return nodeList
    }

    private void listSubTreeNode(def treeObj, String subCatalogName = "subCatalogs", String keyName = "key", List displayNameList = ["displayEN", "displayZH","tmdReportRequired"], int level, List nodeList){

        if(!treeObj) return

        //如果level大于0，则调用递归函数
        if(level > 0 && treeObj[subCatalogName]){
            treeObj[subCatalogName].each{
                listSubTreeNode(it, level-1, nodeList)
            }
        }
        else if(level == 0) {   //如果level为0，则返回根节点信息
            Map nodeInfoMap = [:]

            if(treeObj[keyName]) {
                nodeInfoMap.put(keyName, treeObj[keyName])
            }

            displayNameList.each {displayName ->
                if (treeObj[displayName]) {
                    nodeInfoMap.put(displayName, treeObj[displayName])
                }
            }

            nodeList << nodeInfoMap
        }
    }

    //移出树状信息中的非活动节点
    def removeInactive(def treeDoc){
        if(treeDoc == null) return treeDoc

        //根节点，不会被移出

        //如果有子节点，则调用每个子节点，如果每个子节点返回是false,则删除这个子节点
        if(treeDoc.subCatalogs){
           treeDoc.subCatalogs.removeAll {
               removeInactiveNode(it)
           }
        }

        return treeDoc
    }

    private boolean removeInactiveNode(def treeDoc){

        if(treeDoc.subCatalogs){
           treeDoc.subCatalogs.removeAll{
               removeInactiveNode(it)
           }
        }

        if(treeDoc.status && treeDoc.status==ParameterConstant.ACTIVE_STATUS_MAP.INACTIVE){
            return true
        }else{
            return false
        }
    }

}
