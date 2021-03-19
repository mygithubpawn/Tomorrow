package com.pawn.blog.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共返回结果
 *
 * @author NieChangan
 */

@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();

    /**
     * 构造方法私有化,里面的方法都是静态方法
     * 达到保护属性的作用
     */
    private Result() {

    }

    /**
     * 这里是使用链式编程
     * 成功
     *
     * @return
     */
    public static Result ok() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        return result;
    }

    /**
     * 失败
     *
     * @return
     */
    public static Result error() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.COMMON_FAIL.getCode());
        result.setMessage(ResultCode.COMMON_FAIL.getMessage());
        return result;
    }

    /**
     * 自定义返回成功与否
     *
     * @param success
     * @return
     */
    public Result success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    /**
     * 返回的异常信息
     *
     * @param message
     * @return
     */
    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 返回的状态码
     *
     * @param code
     * @return
     */
    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 返回的数据
     *
     * @param key
     * @param value
     * @return
     */
    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
