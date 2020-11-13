package org.whu.mya.remoting.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RpcMessage {
    //rpc message type
    private byte messageType;
    //serialization type
    private byte codec;
    //compress type
    private byte compress;
    //request id
    private int requestId;
    //request data
    private Object data;

}
