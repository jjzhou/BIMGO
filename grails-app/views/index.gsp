<!doctype html>
<html>
<head>
    <title>Welcome to BIMGO</title>
    <r:require modules="bimgo-layout, bootstrap"/>
    <r:layoutResources/>
</head>

<body style="background-color: #dcdcdc;">
<div id="wrapper">
    <div id="page-body" role="main">
        <div id="bimgo" style="font-size: 30pt; text-align: center; vertical-align: bottom; margin-top: 222px;"><g:link
                onclick="login(); return false"><g:img dir="images/logo" file="bimgo_logo.png"
                                                                    style="margin-bottom: -10px;"/></g:link> &nbsp;/ˈbiNGgō/</div>
    </div>
</div><!-- wrapper -->

<div class="row" style="margin-top: -222px;">
    <g:render template="../layouts/footer"/>
</div>

<!-- Modal -->
<div id="loginModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="width: 600px; left: 600px; top: 168px;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3 id="myModalLabel">Welcome to BIM Management System!</h3>
    </div>

    <div class="modal-body">
        <g:form controller="peopleManagement" action="index" id="loginForm" name="loginForm">
            <div class="form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="username">Username:</label>

                        <div class="controls">
                            <input type="text" class="span3" id="username" name="username"
                                   value="${params.username}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="passward">Password:</label>

                        <div class="controls">
                            <input type="text" class="span3" id="passward" name="passward" value="${params.passward}"/>
                        </div>
                    </div>
                </fieldset>
            </div>
        </g:form>
    </div>

    <div class="modal-footer">
        <button class="btn btn-warning" data-dismiss="modal">Slipped, Sorry...</button>
        <button class="btn btn-primary" onclick="$('#loginForm').submit();">Right, Get Me Through!</button>
    </div>
</div><!--Modal-->

<r:layoutResources/>
<script type="text/javascript">
    function login() {
        $("#loginModal").modal('show');
        return false
    }
</script>
</body>
</html>
