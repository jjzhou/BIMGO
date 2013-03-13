package eis.bas
/**
 * Created with IntelliJ IDEA.
 * User: jjzhou
 * Date: 13-3-12
 * Time: 下午4:34
 * To change this template use File | Settings | File Templates.
 */

import com.mongodb.WriteConcern
import org.bson.types.ObjectId

class FileItem {
    static mapWith = "mongo"
    static constraints = {
    }
    static mapping = {
        writeConcern WriteConcern.FSYNC_SAFE
    }

    ObjectId id
    String fileName         //文件名
    String fileType         //文件类型
    Long fileSize           //文件大小
    String fileDescription  //文件描述
    String fsId             //FSGrid存储ID

    Date dateCreated //创建日期
    Date lastUpdated //最后修改日期
    String createdBy //创建人
    String updatedBy //最后修改人
}

