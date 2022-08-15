package core.client;

import core.codec.RpcRequestBody;
import core.codec.RpcResponseBody;
import core.rpc_protocol.RpcRequest;
import core.rpc_protocol.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// 动态代理，实现InvocationHandler，重写invoke方法
public class RpcClientProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 1、【codec编码层】通过反射获取接口名、方法名、参数类型，包装成rpcRequestBody，
        //                 然后将信息编码为byte[]
        RpcRequestBody rpcRequestBody = RpcRequestBody.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();

        // rpcRequestBody转换成二进制字节流再转换成byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(rpcRequestBody);
        byte[] bytes = baos.toByteArray();  // 从此ByteArrayOutputStream返回一个“字节”类型的数组。


        // 2、【protocol协议层】创建RPC协议
        RpcRequest rpcRequest = RpcRequest.builder()
                .header("version=1")
                .body(bytes)
                .build();



        // 3、发送RpcRequest，获得RpcResponse
        RpcClientTransfer rpcClient = new RpcClientTransfer();
        RpcResponse rpcResponse = rpcClient.sendRequest(rpcRequest);


        // 4、【protocol协议层】解析RpcResponse
        String header = rpcResponse.getHeader();
        byte[] body = rpcResponse.getBody();
        if(header.equals("version=1")) {
            // 5、【codec编码层】body解码成Object对象并返回
            ByteArrayInputStream bais = new ByteArrayInputStream(body);
            ObjectInputStream ois = new ObjectInputStream(bais);
            RpcResponseBody rpcResponseBody = (RpcResponseBody) ois.readObject();
            Object retObject = rpcResponseBody.getRetObject();
            return retObject;
        }
        return null;
    }

    // 1、表示忽略unchecked警告
    // 2、<T>表示泛型方法，T表示返回值是泛型，Class<T>表示形参clazz为T类型
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this
        );
    }


}
