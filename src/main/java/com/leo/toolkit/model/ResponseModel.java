package com.leo.toolkit.model;

import com.leo.toolkit.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {

    private String code;
    private String msg;
    private T data;

    public ResponseModel(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getMsg();
    }

    public ResponseModel(T data){
        this.code = ResponseEnum.SUCCESS.getCode();
        this.msg = ResponseEnum.SUCCESS.getMsg();
        this.data = data;
    }

    public ResponseModel<T> success() {
        return new ResponseModel<>(ResponseEnum.SUCCESS);
    }


}
