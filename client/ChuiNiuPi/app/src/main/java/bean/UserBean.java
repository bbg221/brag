package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/25.
 * 封装用户实体类
 */

public class UserBean implements Serializable{

    private String loginNum;
    private String userName;
    private String userPic;

    public String getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(String loginNum) {
        this.loginNum = loginNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
