<%--
  Created by IntelliJ IDEA.
  User: jjzhou
  Date: 13-3-18
  Time: 下午5:40
  To change this template use File | Settings | File Templates.
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>People Management</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <r:require modules="bimgo-layout,bootstrap,zTree,json2"/>
    <r:layoutResources/>
</head>

<body>
<div id="wrapper">
    <g:render template="../layouts/navBar"/>
    <div class="container">
        <div class="row" style="margin-left: 0px; margin-bottom: -10px; text-align: left;">
            <legend>People Management <small>Edit</small></legend>
        </div>

        <!-- Alert Message -->
        <g:if test="${flash.message}">
            <div class="alert alert-info">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                ${flash.message}
            </div>
        </g:if>

        <div style="margin-top:5px;" class="row">
            <!-- Tree List -->
            <div class="span5">
                <div style="min-height:470px; border: solid; border-width: 1px; border-color: silver;">
                    <ul class="breadcrumb" style="margin-bottom: 5px;">
                        <li><a href="javascript:void(0);" onclick="$.fn.zTree.getZTreeObj('tree').expandAll(true)">Expand All</a> <span
                                class="divider">/</span></li>
                        <li><a href="javascript:void(0);" onclick="$.fn.zTree.getZTreeObj('tree').expandAll(false)">Collapse All</a> <span
                                class="divider">/</span></li>
                        <li><a href="javascript:void(0);" onclick="getTreeData()">Save</a></li>
                    </ul>

                    <div id="tree" class="ztree" style="height:auto; overflow:auto; margin-left:5px; margin-bottom: 10px;"></div>
                </div>
                <g:form action="save" name="treeForm">
                    <g:hiddenField name="treeData" value="${treeJson}"/>
                    <g:hiddenField name="treeId" value="${treeId}"/>
                </g:form>
            </div>

            <div class="span7">
                <div style="min-height:470px; border: solid; border-width: 1px; border-color: silver;">
                    <ul class="breadcrumb">
                        <li>Basic Info</li>
                    </ul>

                    <div style="margin-top:10px;">
                        <div class="form-horizontal">
                            <fieldset>
                                <div class="control-group">
                                    <label class="control-label" for="employeeCode">Employee Code</label>

                                    <div class="controls">
                                        <g:field type="text" class="input-large" id="employeeCode" name="employeeCode"/>
                                    </div>
                                </div>

                                <div class="control-group">
                                    <label class="control-label" for="nameEn">Name(EN)</label>

                                    <div class="controls">
                                        <g:field type="text" class="input-large" id="nameEn" name="nameEn"/>
                                    </div>
                                </div>
                            </fieldset>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div><!-- wrapper -->
<g:render template="../layouts/footer"/>

<r:layoutResources/>
<script type="text/javascript">
    var setting = {
        data:{
            key:{
                name:"key",
                title:"key",
                children:"children"
            }
        },
        view:{
            showTitle:true,
            addHoverDom:addHoverDom,
            removeHoverDom:removeHoverDom,
            selectedMulti:false
        },
        edit:{
            enable:true,
            showRemoveBtn:function(elementId,node){
                return true; //node.level < 3 || node.key == 'new name';
            },
            showRenameBtn:function(elementId,node){
                return true; //node.level < 3 || node.key == 'new name';
            },
            removeTitle:"remove",
            renameTitle:"rename",
            editNameSelectAll:true
        },
        callback:{
            onClick:getNodeData,
            beforeRemove:confirmRemoveNode,
            beforeEditName:function (treeId, treeNode) {
                if (treeNode.level == 0) {
                    alert("You won't have the right to change the name of the GOD!")
                    return false
                }
            },
            beforeRename: function (treeId, treeNode, newName) {
                if(inCancelRename){
                    inCancelRename = false;
                    return false;
                }else{
                    if(newName!='new name' && treeNode.key!='new name'){
                        alert("Be careful to change the name of a team or sub-team");
                        var zTree = $.fn.zTree.getZTreeObj("tree");
                        inCancelRename = true;
                        zTree.editName(treeNode);
                        return false;
                    }

                    //需要判断修改的名字是否与现有节点的名字冲突
                    var zTree = $.fn.zTree.getZTreeObj("tree");
                    var node = zTree.getNodes();
                    if(checkDuplicateNode(node[0], newName)){
                        alert("Duplicate Name! Same Person?");
                        inCancelRename = true;
                        zTree.editName(treeNode);
                        return false;
                    }
                }
            }
        }
    };

    function checkDuplicateNode(node, newName){
        if(node!=null){
            if(node.key==newName){
                //有重复命名节点
                return true;
            }
            if(node.children!=null && node.children.length>0){
                for(var i=0; i<node.children.length;i++){
                    if(checkDuplicateNode(node.children[i], newName)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    var nodes = ${treeJson};
    var t = $("#tree");
    var inCancelRename = false;

    var newCount = 1;
    function addHoverDom(treeId, treeNode) {
        if (treeNode.level == 3) {//三级节点下面不允许添加节点
            return
        }
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='add node' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_" + treeNode.tId);
        if (btn) btn.bind("click", function () {
            var zTree = $.fn.zTree.getZTreeObj("tree");
            zTree.addNodes(treeNode, {key:"new name", data:[], children:[]});
            return false;
        });
    }
    ;
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_" + treeNode.tId).unbind().remove();
    }
    ;

    function getNodeData(event, treeId, treeNode) {
        /*if (treeNode.level == 3) {
            $("#cityLevel").val(treeNode.data.cityLevel)
            $("#cityLevelSet").show()
        } else {
            $("#cityLevel").val("")
            $("#cityLevelSet").hide()
        }*/
        $("#employeeCode").val(treeNode.data.employeeCode)
        $("#nameEn").val(treeNode.data.nameEn)
    }

    function confirmRemoveNode(treeId, treeNode) {
        if (treeNode.level == 0) {
            alert("Why you wanna erase the GOD???")
            return false
        }

        if (treeNode.isParent) {
            alert("Delete the WHOLE solution team?")
            return confirm("Double confirm! Won't regret?")
        }else {
            return confirm("Watch out! You are kicking out somebody! Sure?")
        }
    }

    $(function () {

        /*$("#cityLevel").change(function () {
            var zTree = $.fn.zTree.getZTreeObj("tree");
            var node = zTree.getSelectedNodes();
            node[0].cityLevel = $(this).val()
        })*/
        $("#employeeCode").blur(function () {
            var zTree = $.fn.zTree.getZTreeObj("tree");
            var node = zTree.getSelectedNodes();
            node[0].data.employeeCode = $(this).val()
        })
        $("#nameEn").blur(function () {
            var zTree = $.fn.zTree.getZTreeObj("tree");
            var node = zTree.getSelectedNodes();
            node[0].data.nameEn = $(this).val()
        })

        t = $.fn.zTree.init(t, setting, nodes);
        var zTree = $.fn.zTree.getZTreeObj("tree");
        zTree.expandAll(false);

    });

    function getTreeData() {
        var zTree = $.fn.zTree.getZTreeObj("tree");
        var treeData = zTree.getNodes();
        $("#treeData").val(JSON.stringify(treeData[0], ['key', 'data', 'children']));
        $("#treeForm").submit()
    }
</script>
</body>
</html>