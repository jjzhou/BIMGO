package bimgo

import com.mongodb.WriteConcern
import org.bson.types.ObjectId

class People {
    static mapWith = "mongo"
    static constraints = {
    }
    static mapping = {
        writeConcern WriteConcern.FSYNC_SAFE
    }

    ObjectId id

    String userName             //用户名
    String password             //密码
    //boolean passwordReset      //是否需要重设密码

    String employeeCode        //员工号

    String nameEn               //拼音名
    String nameCn               //中文名称
    String gender               //性别

    String position             //岗位
    String serviceLine         //服务线
    String subTeam              //团队
    String subSolution         //解决方案
    String grade                //级别
    String baseLocation        //Base地

    String telephone           //电话
    String mobile               //手机
    String email                //E-Mail

    String status               //状态 Booked/On-project/Idle...
    String timeLine             //时间安排
}