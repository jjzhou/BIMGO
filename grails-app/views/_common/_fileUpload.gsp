%{--<%@ page import="eis.eis.ParameterConstant; eis.eis.bas.Parameter" %>--}%
<form id="_mer_fileupload" action="${createLink(controller: 'fileUpload', action: 'upload')}" method="POST" enctype="multipart/form-data" style="margin-bottom: 0;">
    <!-- The fileupload-buttonbar contains buttons to add/delete files and start/cancel the upload -->
    <div class="span7 fileupload-buttonbar">

            <!-- The fileinput-button span is used to style the file input field as button -->
            <span class="btn btn-success fileinput-button" style="margin-bottom: 0;">
                <i class="icon-arrow-up icon-white"></i>
                <span>Upload File:</span>
                <input type="file" name="myFile">
            </span>
            <button type="submit" class="invisible btn btn-primary start" style="margin-bottom: 0;">
                <i class="icon-upload icon-white"></i>
                <span>Start Uploading</span>
            </button>
            <button type="reset" class="invisible btn btn-warning cancel" style="margin-bottom: 0;">
                <i class="icon-ban-circle icon-white"></i>
                <span>Cancel</span>
            </button>
            <button type="button" class="invisible btn btn-danger delete" style="margin-bottom: 0;">
                <i class="icon-trash icon-white"></i>
                <span>Delete</span>
            </button>
            <input type="checkbox" class="invisible toggle" style="margin-bottom: 0;">


       <%--
            <!-- The global progress information -->
            <div class="span5 fileupload-progress fade">
                <!-- The global progress bar -->
                <div class="progress progress-success progress-striped active">
                    <div class="bar" style="width:0%;"></div>
                </div>
                <!-- The extended global progress information -->
                <div class="progress-extended">&nbsp;</div>
            </div>
        --%>
    </div>
    <!-- The loading indicator is shown during file processing -->
    <div class="fileupload-loading"></div>
    <!-- The table listing the files available for upload/download -->
    <table class="table table-striped">
        <tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery">

        </tbody>
    </table>
</form>


<script id="template-upload" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td class="preview"><span class="fade"></span></td>
        <td class="name"><span>{%=file.name%}</span></td>
        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
        {% if (file.error) { %}
        <td class="error" colspan="2"><span
                class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}
        </td>
        {% } else if (o.files.valid && !i) { %}
        <td>
            <div class="progress progress-success progress-striped active"><div class="bar" style="width:0%;"></div>
            </div>
        </td>
        <td class="start">{% if (!o.options.autoUpload) { %}
            <button class="btn btn-primary">
                <i class="icon-upload icon-white"></i>
                <span>{%=locale.fileupload.start%}</span>
            </button>
            {% } %}</td>
        {% } else { %}
        <td colspan="2"></td>
        {% } %}
        <td class="cancel">{% if (!i) { %}
            <button class="btn btn-warning">
                <i class="icon-ban-circle icon-white"></i>
                <span>{%=locale.fileupload.cancel%}</span>
            </button>
            {% } %}</td>
    </tr>
    {% } %}
</script>

<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
    {% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
        <td></td>
        <td class="name"><span>{%=file.name%}</span></td>
        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
        <td class="error" colspan="2"><span
                class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}
        </td>
        {% } else { %}
        <td class="preview">{% if (file.thumbnail_url) { %}
            <a href="{%=file.url%}" title="{%=file.name%}" rel="gallery" download="{%=file.name%}"><img
                    src="{%=file.thumbnail_url%}"></a>
            {% } %}</td>
        <td class="name">
            <a href="{%=file.url%}" title="{%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}"
               download="{%=file.name%}">{%=file.name%}</a>
        </td>
        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
        <td colspan="2"></td>
        {% } %}
        <td class="delete">
            <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}">
                <i class="icon-trash icon-white"></i>
                <span>{%=locale.fileupload.destroy%}</span>
            </button>
            <input type="checkbox" name="delete" value="1">
        </td>
    </tr>
    {% } %}
</script>

<script type="text/javascript">
    $(function () {
        $("#_mer_fileupload").fileupload();
        $('#_mer_fileupload').fileupload('option', {
            <g:if test="${options?.maxNumberOfFiles}">
            maxNumberOfFiles:${options.maxNumberOfFiles},
            </g:if>
            <g:else>
            maxNumberOfFiles:1,
            </g:else>
            <g:if test="${options?.autoUpload}">
            autoUpload:${options.autoUpload},
            </g:if>
            <g:else>
            autoUpload:false,
            </g:else>
            <g:if test="${options?.maxFileSize}">
            maxFileSize:${options.maxFileSize},
            </g:if>
            <g:else>
            <%
                def maxSize = "10000000"
                /*Parameter parameter = Parameter.findByKey(ParameterConstant.MAX_UPLOAD_FILE_SIZE)
                if(parameter){
                    maxSize = parameter.value
                }*/
            %>
            maxFileSize:${maxSize},
            </g:else>
            <g:if test="${options.fileType}">
                <g:if test="${options.fileType!='any'}">
                acceptFileTypes:/(\.|\/)(${options.fileType.replace(",","|")})$/i
                </g:if>
            </g:if>
            <g:else>
            acceptFileTypes:/(\.|\/)(xls|xlsx)$/i
            </g:else>

        });
        <g:if test="${options.completed}">
        $('#_mer_fileupload').bind("fileuploadcompleted", ${options.completed})
        </g:if>
    });
</script>
