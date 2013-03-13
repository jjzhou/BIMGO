package eis.web

class FileUploadTagLib {
    /**
     * 异步文件上传,Tag 会生成一个form，使用时请避免form嵌套
     * @attr 文件上传控件属性设置
     * @attr fileType    允许上传的文件类型，用逗号隔开，如：fileType="txt,xls,xlsx,doc",默认为 xls，xlsx 两种类型
     * @attr completed     文件上传成功后回调的函数名
     * @attr autoUpload  是否自动上传 true  false
     * 其他标准属性参照 https://github.com/blueimp/jQuery-File-Upload/wiki/Options
     */
    def fileUpload = { attrs ->
        out << render(template: '/_common/fileUpload', model: [options: attrs])
    }

    /**
    * 异步文件上传,Tag 会生成一个form，使用时请避免form嵌套
    * @attr 文件上传控件属性设置
    * @attr fileType    允许上传的文件类型，用逗号隔开
    * @attr completed     文件上传成功后回调的函数名
    * @attr autoUpload  是否自动上传 true  false
    * 其他标准属性参照 https://github.com/blueimp/jQuery-File-Upload/wiki/Options
    */
    def zipFileUpload = { attrs ->
        out << render(template: '/_common/zipFileUpload', model: [options: attrs])
}

}
