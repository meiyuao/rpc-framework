package org.whu.mya.remoting.dto;

import lombok.Getter;
import lombok.Setter;
import org.whu.mya.enums.RpcResponseCodeEnum;

@Setter
@Getter
public class RpcResponce<T> {

    private String requestId; // 与RpcRequest中的requestId对应
    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResponce<T> success(T data, String requestId) {
        RpcResponce<T> rpcResponce = new RpcResponce<>();
        rpcResponce.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        rpcResponce.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        rpcResponce.setRequestId(requestId);

        if (data != null) rpcResponce.setData(data);
        return rpcResponce;
    }

}
