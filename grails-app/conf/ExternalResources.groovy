/**
 * Created with IntelliJ IDEA.
 * User: jjzhou
 * Date: 13-3-8
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
modules = {
    'bimgo-layout' {
        resource url: 'css/commonSection.css'
    }

    'bootstrap' {
        dependsOn 'jquery','jquery-ui'

        resource url: 'js/bootstrap/css/bootstrap-responsive.css'
        resource url: 'js/bootstrap/css/bootstrap-responsive.min.css'
        resource url: 'js/bootstrap/css/bootstrap.css'
        resource url: 'js/bootstrap/css/bootstrap.min.css'

        resource url: 'js/bootstrap/js/bootstrap.js'
        resource url: 'js/bootstrap/js/bootstrap.min.js'
    }

    'jquery-ui-addon-file-upload' {
        dependsOn 'jquery-ui','bootstrap'
        resource url: 'js/jqueryui-addon/fileupload/css/jquery.fileupload-ui.css'
        resource url: 'js/jqueryui-addon/fileupload/tmpl.min.js'
        resource url: 'js/jqueryui-addon/fileupload/jquery.fileupload.js'
        resource url: 'js/jqueryui-addon/fileupload/jquery.fileupload-fp.js'
        resource url: 'js/jqueryui-addon/fileupload/jquery.fileupload-ui.js'
        resource url: 'js/jqueryui-addon/fileupload/jquery.iframe-transport.js'
        resource url: 'js/jqueryui-addon/fileupload/locale.js'
    }

    'jquery-theme-bootstrap' {
        dependsOn 'bootstrap'
        resource url: 'js/jqueryui/css/ui-bootstrap/jquery-ui-1.8.16.custom.css'
    }

    'jquery-theme-lightness' {
        resource url: 'js/jqueryui/css/ui-lightness/jquery-ui-1.8.22.custom.css'
    }

    'jquery-ui-addon-timepicker'{
        dependsOn 'jquery-ui'
        resource url: 'js/jqueryui-addon/timepicker/jquery-ui-timepicker-addon.js'
        resource url: 'js/jqueryui-addon/timepicker/css/timepicker.css'
    }

    'jquery-ui-addon-fullcalendar'{
        dependsOn 'jquery-ui'
        resource url: 'js/jqueryui-addon/fullCalendar/fullcalendar.css'
        resource url: 'js/jqueryui-addon/fullCalendar/fullcalendar.min.js'
    }

    'jquery-ui' {
        dependsOn 'jquery'
        resource url: 'js/jqueryui/js/jquery-ui-1.8.22.custom.min.js'
    }

    'jquery-addon-validation' {
        dependsOn 'jquery'
        resource url: 'js/jqueryui-addon/validation/css/validationEngine.jquery.css'
        resource url: 'js/jqueryui-addon/validation/js/languages/jquery.validationEngine-zh_CN.js'
        resource url: 'js/jqueryui-addon/validation/js/jquery.validationEngine.js'
    }
}
