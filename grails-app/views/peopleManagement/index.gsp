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
    <r:require module="bootstrap"/>
    <r:layoutResources/>
</head>
<body>
    <div id="wrapper">
        <g:render template="../layouts/navBar"/>

        <div class="container">
            <div>
                <legend>People Management <small>Overview</small></legend>
            </div>
            <p style="font-size: 72pt; margin-top: 50px; margin-left: -5px;">Hello World!</p>
            <p style="font-size: 36pt; margin-top: 86px;">I wanna do a lot of things here!</p>
            <p style="font-size: 36pt; margin-top: 86px;">Yeah! I am SERIOUS! @.@~!</p>
        </div>
    </div>
    <g:render template="../layouts/footer"/>
    <!--Good-->
    <r:layoutResources/>
</body>
</html>