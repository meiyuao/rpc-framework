package org.whu.mya.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum  RpcResponseCodeEnum {

    SUCCESS(200, "The remote call is successful"),
    FAIL(500, "The remote call is fail");

    private final int code;
    private final String message;

}
