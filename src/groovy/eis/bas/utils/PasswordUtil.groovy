package eis.bas.utils

/**
 * Created with IntelliJ IDEA.
 * User: liuyanlu
 * Date: 12-10-26
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
class PasswordUtil {
    public static String getPass(int passLenth) {
        StringBuffer buffer = new StringBuffer(
                "0123456789abcdefghijklmnopqrstuvwxyz");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < passLenth; i++) {
            //生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
}
