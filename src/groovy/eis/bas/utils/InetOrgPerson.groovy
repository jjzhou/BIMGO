/*
package eis.bas.utils
import gldapo.schema.annotation.GldapoNamingAttribute
*/
/**
 * Created with IntelliJ IDEA.
 * User: TONWANG
 * Date: 12-9-19
 * Time: 下午4:46
 * To change this template use File | Settings | File Templates.
 *//*


class InetOrgPerson{
    @GldapoNamingAttribute
    String cn                    //cn is the unique id, which cannot be changed once created
    String displayName
    String sn
    String givenName
    //String telephoneNumber
    String userPassword
    String mail
    String mobile
    String carLicense
    String departmentNumber
    String businessCategory
    String title
    String objectClass
    String st


    public createInetOrgPerson(bmwtap.UserEntity userEntity){
        cn=userEntity.userName
        updateInetOrgPerson(userEntity)
    }

    public updateInetOrgPerson(bmwtap.UserEntity userEntity){
        displayName=userEntity.realName
        sn=userEntity.lastName
        givenName=userEntity.firstName
        //telephoneNumber=userEntity.telephone
        userPassword=userEntity.password
        mail=userEntity.email
        mobile=userEntity.mobile
        carLicense=userEntity.idNo
        departmentNumber=userEntity.companyCode
        businessCategory=userEntity.userType
        title=userEntity.gender
        st=userEntity.status
    }
    String toString(){
        return cn
    }
}*/
