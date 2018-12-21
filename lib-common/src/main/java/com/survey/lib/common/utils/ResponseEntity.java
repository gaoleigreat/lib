package com.survey.lib.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Data
public class ResponseEntity<T> implements Serializable {
    private String ret;
    private String msg;
    private T data;

   private ResponseEntity(T data){
       this.ret="success";
       this.msg="操作成功";
       this.data=data;
   }

    private ResponseEntity(String msg, T data) {
        this.ret="fail";
        this.msg=msg;
        this.data=data;
    }

    private ResponseEntity(String msg) {
        this.ret="fail";
        this.msg=msg;
    }


    private ResponseEntity(String ret,String msg, T data) {
        this.ret=ret;
        this.msg=msg;
        this.data=data;
    }


    private ResponseEntity() {
        this.ret="success";
        this.msg="操作成功";
    }


    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(data);
    }


    public static <T> ResponseEntity<T> success() {
        return new ResponseEntity<>();
    }


    public static <T> ResponseEntity<T> success(String ret,String msg, T data) {
        return new ResponseEntity<>(ret,msg,data);
    }


    public static <T> ResponseEntity<T> fail(String msg) {
        return new ResponseEntity<>(msg);
    }


    public static <T> ResponseEntity<T> fail(String msg,T data) {
        return new ResponseEntity<>(msg, data);
    }


    public static <T> ResponseEntity<T> fail(String ret,String msg,T data) {
        return new ResponseEntity<>(ret,msg, data);
    }

}