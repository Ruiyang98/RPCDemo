package core.rpc_protocol;

import java.io.Serializable;

/**
 * @author sry
 */
public class RpcRequest implements Serializable {
    private String header;
    private byte[] body;
}
