package core.rpc_protocol;

import java.io.Serializable;

/**
 * @author sry
 */
public class RpcResponse implements Serializable {
    // 协议头
    private String header;
    // 协议体
    private byte[] body;
}
