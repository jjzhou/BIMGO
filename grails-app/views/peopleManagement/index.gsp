<%--
  Created by IntelliJ IDEA.
  User: jjzhou
  Date: 13-3-18
  Time: 上午11:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>People Management</title>
    <r:require modules="bimgo-layout, bootstrap, jquery-ui-addon-file-upload, jit"/>
    <r:layoutResources/>

    <!-- Example JS File -->
    <script language="javascript" type="text/javascript" src="../js/jit/Examples/Treemap/example1.js"></script>

    <!-- CSS Files -->
    <link type="text/css" href="../js/jit/Examples/css/base.css" rel="stylesheet"/>
    <link type="text/css" href="../js/jit/Examples/css/Treemap.css" rel="stylesheet"/>
</head>

<body onload="init();">
<div id="wrapper">
    <g:render template="../layouts/navBar"/>
    <div class="container">
        <div class="row" style="margin-left: 0px; margin-bottom: -10px; text-align: left;">
            <legend>People Management <small>The Big Picture</small></legend>
        </div>

        <!-- Alert Message -->
        <g:if test="${flash.message}">
            <div class="alert alert-info">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                ${flash.message}
            </div>
        </g:if>

        <div class="row" style="margin-left: 0px; margin-top: 5px; text-align: left;">
            %{--<div class="span4 form-inline" style="margin-left: 0px;">
                <a id="deletePeople" href="javascript:void(0);" class="btn btn-danger"><i
                        class="icon-minus icon-white"></i> Delete Selected</a>
                <button id="addPeople" class="btn btn-success" onclick="addPeople();"><i
                        class="icon-plus icon-white"></i> Add People</button>
            </div>

            <div class="span7" style="margin-left: -50px; text-align: left;"><g:fileUpload maxSize="10000000"
                                                                                           autoUpload="false"
                                                                                           completed="processUploadFile"
                                                                                           fileType="xlsx"/></div>--}%
        </div>

        <div class="row" style="margin-left: 0px; margin-top: 0px;">
            <div id="id-list" style="margin-left: 0px; margin-top: 0px; width: 930px;">
                <table>
                    <tr>
                        <td>
                            <label for="r-sq">Squarified</label>
                        </td>
                        <td style="width: 60px; vertical-align: top; text-align: left;">
                            <input type="radio" id="r-sq" name="layout" checked="checked" value="left"/>
                        </td>
                        <td>
                            <label for="r-st">Strip</label>
                        </td>
                        <td style="width: 60px; vertical-align: top; text-align: left;">
                            <input type="radio" id="r-st" name="layout" value="top"/>
                        </td>
                        <td>
                            <label for="r-sd">SliceAndDice</label>
                        </td>
                        <td style="width: 60px; vertical-align: top; text-align: left;">
                            <input type="radio" id="r-sd" name="layout" value="bottom"/>
                        </td>
                        <td style="width: 555px; vertical-align: top; text-align: right;">
                            <g:link action="truth" class="btn btn-warning pull-right">Show Me The TRUTH</g:link>
                            <g:link action="edit" class="btn btn-primary pull-right" style="margin-right: 10px;"><i class="icon-edit icon-white"></i>&nbsp;Edit&nbsp;</g:link>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div id="infovis" style="width:945px; margin-top: 10px; margin-bottom: 10px; border: 1px;"></div>
    </div>
</div><!-- wrapper -->
<g:render template="../layouts/footer"/>

<!-- Modal -->
<div id="addPeopleModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="width: 888px; left: 460px;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>

        <h3 id="myModalLabel">Yep, A New Colleague Wanna Join Our Team...</h3>
    </div>

    <div class="modal-body">
        <g:form action="add" id="addForm" name="addForm">
            <div class="span5 form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="employeeCode">Employee Code:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="employeeCode" name="employeeCode"
                                   value="${params.employeeCode}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="nameCn">Name (CN):</label>

                        <div class="controls">
                            <input type="text" class="span2" id="nameCn" name="nameCn" value="${params.nameCn}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="nameEn">Name (EN):</label>

                        <div class="controls">
                            <input type="text" class="span2" id="nameEn" name="nameEn" value="${params.nameEn}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="gender">Gender:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="gender" name="gender" value="${params.gender}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="telephone">Telephone:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="telephone" name="telephone"
                                   value="${params.telephone}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="mobile">Mobile:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="mobile" name="mobile" value="${params.mobile}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="email">Email:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="email" name="email" value="${params.email}"/>
                        </div>
                    </div>
                </fieldset>
            </div>

            <div class="span5 form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="position">Position:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="position" name="position" value="${params.position}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="serviceLine">Service Line:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="serviceLine" name="serviceLine"
                                   value="${params.serviceLine}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="subTeam">Sub-Team:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="subTeam" name="subTeam" value="${params.subTeam}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="subSolution">Sub-Solution:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="subSolution" name="subSolution"
                                   value="${params.subSolution}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="grade">Grade:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="grade" name="grade" value="${params.grade}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="baseLocation">Base Location:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="baseLocation" name="baseLocation"
                                   value="${params.baseLocation}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="status">Status:</label>

                        <div class="controls">
                            <input type="text" class="span2" id="status" name="status" value="${params.status}"/>
                        </div>
                    </div>
                </fieldset>
            </div>
        </g:form>
    </div>

    <div class="modal-footer">
        <button class="btn btn-warning" data-dismiss="modal">Wait, Wait, Discard...</button>
        <button class="btn btn-primary" onclick="$('#addForm').submit();">Sure, Join Us!</button>
    </div>
</div>


<r:layoutResources/>
<script type="text/javascript">
    function processUploadFile() {
        window.location.href = "${createLink(controller: 'peopleManagement',action: 'processUpload')}";
    }

    function addPeople() {
        $("#addPeopleModal").modal('show');
        return false
    }

    $(function () {
     $("#deletePeople").click(function () {
     if ($(".rmparam:checked").length < 1) {
     alert("I'm afraid you should select at least one person.")
     return
     }
     if (confirm("Sure to DELETE?")) {
     $("#deleteForm").submit()
     }
     })
     })
</script>

</body>
</html>
