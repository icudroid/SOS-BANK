package net.dkahn.starter.apps.webapps.common.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class WebResponseBean<T> implements Serializable{
    private Integer resultCode;
    private String errorMessage;
    private T data;


    public static <T> WebResponseBean OK_WITH_DATA(T data){
        return builder().resultCode(0).data(data).build();
    }

    public static <T> WebResponseBean OK(){
        return builder().resultCode(0).build();
    }

    public static <T> WebResponseBean FAILED(Integer errorCode,String errorMessage){
        return builder().resultCode(errorCode).errorMessage(errorMessage).build();
    }

}
