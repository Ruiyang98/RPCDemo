package core.rpc_protocol;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sry
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private String header;
    private byte[] body;
}
