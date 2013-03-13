<%--
  Created by IntelliJ IDEA.
  User: jjzhou
  Date: 13-3-8
  Time: 下午2:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>People Management</title>
    <r:require modules="bimgo-layout, bootstrap, jquery-ui-addon-file-upload"/>
    <r:layoutResources/>
</head>

<body>
<div id="wrapper">
    <g:render template="../layouts/navBar"/>
    <div class="container">
        <div class="row" style="margin-left: 0px; margin-bottom: -10px;">
            <legend>People Management <small>The Big Picture</small></legend>
        </div>

        <!-- Alert Message -->
        <g:if test="${flash.message}">
            <div class="alert alert-info">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                ${flash.message}
            </div>
        </g:if>

        <div class="row" style="margin-left: 0px; margin-top: 5px;">
            <div class="span4 form-inline" style="margin-left: 0px;">
                <a id="deletePeople" href="javascript:void(0);" class="btn btn-danger"><i class="icon-minus icon-white"></i> Delete Selected</a>
                <button id="addPeople" class="btn btn-success" onclick="addPeople();"><i
                        class="icon-plus icon-white"></i> Add People</button>
            </div>

            <div class="span7" style="margin-left: -50px;"><g:fileUpload maxSize="10000000" autoUpload="false"
                                                                         completed="processUploadFile"
                                                                         fileType="xlsx"/></div>
        </div>

        <g:form name="deleteForm" action="delete" method="post" style="margin: 0;padding:0;">
            <div class="row" style="margin-left: 0px;">
                <table class="table table-bordered table-list" style=" margin-top: 0px;">
                    <thead>
                    <tr class="table-middle table-center" style="background-color:#f5f5f5;">
                        <th></th>
                        <th style="text-align:center;">Code</th>
                        <th style="text-align:center;">Name(EN)</th>
                        %{--<th style="text-align:center;">Name(CN)</th>
                        <th style="text-align:center;">Gender</th>
                        <th style="text-align:center;">Position</th>
                        <th style="text-align:center;">Service Line</th>
                        <th style="text-align:center;">Sub-Team</th>--}%
                        <th style="text-align:center;">Sub-Solution</th>
                        <th style="text-align:center;">Grade</th>
                        <th style="text-align:center;">Base Location</th>
                        %{--<th style="text-align:center;">Telephone</th>--}%
                        <th style="text-align:center;">Mobile</th>
                        <th style="text-align:center;">Email</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${peopleList}" status="i" var="people">
                        <tr>
                            <td><input type="checkbox" name="objIds" value="${people.id}" class="rmparam"></td>
                            <td>${people?.employeeCode}</td>
                            <td>${people?.nameEn}</td>
                            %{--<td>${people?.nameCn}</td>
                            <td>${people?.gender}</td>
                            <td>${people?.position}</td>
                            <td>${people?.serviceLine}</td>
                            <td>${people?.subTeam}</td>--}%
                            <td>${people?.subSolution}</td>
                            <td>${people?.grade}</td>
                            <td>${people?.baseLocation}</td>
                            %{--<td>${people?.telephone}</td>--}%
                            <td>${people?.mobile}</td>
                            <td>${people?.email}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </g:form>
    </div>
</div><!-- wrapper -->

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

<g:render template="../layouts/footer"/>
<r:layoutResources/>

<script type="text/javascript">
    function addPeople() {
        $("#addPeopleModal").modal('show');
        return false
    }

    function processUploadFile() {
        window.location.href = "${createLink(controller: 'peopleManagement',action: 'processUpload')}";
    }

    $(function () {
        $("#deletePeople").click(function () {
            if ($(".rmparam:checked").length < 1) {
                alert("请选择要删除的行")
                return
            }
            if (confirm("确定要删除吗？")) {
                $("#deleteForm").submit()
            }
        })
    })
</script>

</body>
</html>
