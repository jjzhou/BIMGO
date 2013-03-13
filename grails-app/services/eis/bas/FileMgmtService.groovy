package eis.bas

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import org.bson.types.ObjectId
import eis.bas.constant.ParameterConstant

class FileMgmtService extends BaseService{
    static transactional =  false
    def mongo
    def grailsApplication

    @Override
    def makeConditions(Map conditions) {
        def query = FileItem.where {
            if(conditions?.fileName)
                fileName =~ '%' + conditions.fileName + '%'
        }
        return query
    }

    /**
     * 上传文件保存到集中存储的地方
     * @return
     */
    GridFSInputFile saveFile(UploadFile file) {

        //存储数据库
        GridFS  gridFS = new GridFS(mongo.getDB(grailsApplication.config.grails.mongo.databaseName))
        GridFSInputFile gridFSInputFile =  gridFS.createFile(file.data)
        gridFSInputFile.filename = file.name
        gridFSInputFile.save()

        return gridFSInputFile
    }

    void deleteFile(String id){
        GridFS  gridFS = new GridFS(mongo.getDB(grailsApplication.config.grails.mongo.databaseName))
        gridFS.remove(new ObjectId(id))
    }

    GridFSDBFile getFile(String id){
        GridFS  gridFS = new GridFS(mongo.getDB(grailsApplication.config.grails.mongo.databaseName))
        return gridFS.find(new ObjectId(id))
    }
}

class UploadFile{
    String name      //文件名
    byte[] data     //文件内容
    String description     //备注（可选）
    String username     //上传文件的用户名(可选)
}

