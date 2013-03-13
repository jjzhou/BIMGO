package eis.bas

import eis.bas.constant.ParameterConstant
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
//import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.multipart.MultipartFile

class FileUploadController {
    def basFileMgmtService

    def index() { render view: 'fileupload'}

    def upload() {
        MultipartFile f = request.getFile('myFile')
        if (f.empty) {
            flash.message = ParameterConstant.ERROR_PREFIX +'file cannot be empty'
            redirect controller: "errorGuide", action: "index"
            return
        }
        String username = ''
        /*def principal = SecurityContextHolder.context?.authentication?.principal
        if(principal){
            if(principal instanceof String){
                username = principal
            }else{
                username = principal.username
            }
        }*/
        UploadFile uploadFile = new UploadFile(name: f.originalFilename, data: f.bytes, description: '', username: username)
        flash.put('uploadFile', uploadFile)

        response.contentType = 'text/plain'
        render([[name: "${f.originalFilename}", size: f.size]].encodeAsJSON())
    }

    def zipUpload() {
        MultipartFile f = request.getFile('myFile')
        if (f.empty) {
            flash.message = ParameterConstant.ERROR_PREFIX +'file cannot be empty'
            redirect controller: "errorGuide", action: "index"
            return
        }
        String username = ''
        /*def principal = SecurityContextHolder.context?.authentication?.principal
        if(principal){
            if(principal instanceof String){
                username = principal
            }else{
                username = principal.username
            }
        }*/
        UploadFile uploadFile
        ArrayList<UploadFile> uploadFiles = new ArrayList<UploadFile>()

        if (f.originalFilename.endsWith('.zip')){
            //Is a compressed file, try to unzip and extract all files
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(f.bytes)
            ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory()
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", byteArrayInputStream)
            ArchiveEntry entry = archiveInputStream.getNextEntry()
            while(entry){
                //处理zip流中的每一个文件
                if (!(entry instanceof  ZipArchiveEntry)){
                    flash.message = ParameterConstant.ERROR_PREFIX +'File Format is Error'
                    redirect controller: 'errorGuide'
                    return
                }
                ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry)entry
                if (!zipArchiveEntry.name){
                    flash.message = ParameterConstant.ERROR_PREFIX +'File Format is Error'
                    redirect controller: 'errorGuide'
                    return
                }
                if (zipArchiveEntry.isDirectory() || zipArchiveEntry.name.startsWith('_') || zipArchiveEntry.name.startsWith('.')){
                    //是压缩文件中的目录，忽略
                    entry = archiveInputStream.getNextEntry()
                    continue
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
                int bit = archiveInputStream.read()
                int position = 0
                while(bit!=-1){
                    byteArrayOutputStream.write(bit)
                    bit = archiveInputStream.read()
                }
                uploadFile = new UploadFile(name: zipArchiveEntry.name, data: byteArrayOutputStream.toByteArray(), description: '', username: username)
                uploadFiles << uploadFile
                entry = archiveInputStream.getNextEntry()
            }
            archiveInputStream.close()
            byteArrayInputStream.close()
        }else{
            uploadFile = new UploadFile(name: f.originalFilename, data: f.bytes, description: '', username: username)
        }

        flash.put('uploadFiles', uploadFiles)
        flash.put('uploadFile', uploadFile)

        response.contentType = 'text/plain'
        render([[name: "${f.originalFilename}", size: f.size]].encodeAsJSON())
    }
}
