package net.xdclass.xdclassredis.model;

import java.io.Serializable;

/**
 * 小滴課堂,願景：讓技術不再難學
 *
 * @Description
 * @Author 二當家小D
 * @Remark 有問題直接聯繫我，源碼-筆記-技術交流群
 * @Version 1.0
 **/

public class UserDO implements Serializable {

    private int id;

    private String name;

    private String pwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

