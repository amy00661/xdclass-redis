package net.xdclass.xdclassredis.vo;

import java.util.Objects;

public class UserPointVO {

    public UserPointVO(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public UserPointVO(String phone) {
        this.phone = phone;
    }

    private String username;

    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPointVO that = (UserPointVO) o;
        return username.equals(that.username) &&
                phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, phone);
    }
}
