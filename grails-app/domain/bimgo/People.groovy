package bimgo

class People {
    static constraints = {
    }
    static mapping = {
        version(false)
    }

    String userName             //用户名
    String password             //密码

    String employeeCode        //员工号

    String nameCn               //中文名称
    String nameEn               //拼音名
    String gender               //性别
    String telephone           //电话
    String mobile               //手机
    String email                //E-Mail

    String position             //岗位
    String serviceLine         //服务线
    String subTeam              //团队
    String subSolution         //解决方案
    String grade                //级别
    String baseLocation        //Base地

    String status               //状态 Booked/On-project/Idle...
    String timeLine             //时间安排
    //boolean passwordReset      //是否需要重设密码
}