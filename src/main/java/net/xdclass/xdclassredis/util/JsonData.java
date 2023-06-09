package net.xdclass.xdclassredis.util;

public class JsonData {

    /**
     * 狀態碼0 表示成功
     */
    private Integer code;
    /**
     * 數據
     */
    private Object data;
    /**
     * 描述
     */
    private String msg;

        
    public JsonData(int code,Object data,String msg){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功，不傳入數據
     * @return
     */
    public static JsonData buildSuccess() {
        return new JsonData(0, null, null);
    }

    /**
     * 成功，傳入數據
     * @param data
     * @return
     */
    public static JsonData buildSuccess(Object data) {
        return new JsonData(0, data, null);
    }

    /**
     * 失敗，傳入描述信息
     * @param msg
     * @return
     */
    public static JsonData buildError(String msg) {
        return new JsonData(-1, null, msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
