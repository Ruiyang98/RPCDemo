package core.server;

import core.codec.RpcRequestBody;
import core.codec.RpcResponseBody;
import core.rpc_protocol.RpcRequest;
import core.rpc_protocol.RpcResponse;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class RpcServerWorker implements Runnable {
    private Socket socket;
    private HashMap<String, Object> registeredService;

    public RpcServerWorker(Socket socket, HashMap<String, Object> registeredService) {
        this.socket = socket;
        this.registeredService = registeredService;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            // 1、【Transfer层】
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            // 2、【protocol层】解析版本号，并判断
            if (rpcRequest.getHeader().equals("version=1")) {

                // 3、【codec层】解码body变成RpcRequestBody
                byte[] body = rpcRequest.getBody();
                ByteArrayInputStream bais = new ByteArrayInputStream(body);
                ObjectInputStream ois = new ObjectInputStream(bais);
                RpcRequestBody rpcRequestBody = (RpcRequestBody) ois.readObject();

                // 4、调用服务
                Object service = registeredService.get(rpcRequestBody.getInterfaceName());
                Method method = service.getClass().getMethod(rpcRequestBody.getMethodName(), rpcRequestBody.getParamTypes());
                Object returnObject = method.invoke(service, rpcRequestBody.getParameters());

                // 1、【codec层】将returnObject编码成bytes[]
                RpcResponseBody rpcResponseBody = RpcResponseBody.builder()
                        .retObject(returnObject)
                        .build();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(rpcResponseBody);
                byte[] bytes = baos.toByteArray();

                // 2、【protocol层】body加上header，生成RpcResponse协议
                RpcResponse rpcResponse = RpcResponse.builder()
                        .header("version=1")
                        .body(bytes)
                        .build();

                // 3、【transfer层】发送
                objectOutputStream.writeObject(rpcResponse);
                objectOutputStream.flush();   //刷新此流，并将任何缓冲输出的字节立即写入基础流。
            }

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
