package client;

import IDL.Hello.HelloRequest;
import IDL.Hello.HelloResponse;
import IDL.Hello.HelloService;
import core.client.RpcClientProxy;

public class ClientTest {
    public static void main(String[] args) {
        // 1、获取RpcService
        RpcClientProxy proxy = new RpcClientProxy();
        HelloService helloService = proxy.getService(HelloService.class);

        // 2、构造请求对象
        HelloRequest helloRequest = new HelloRequest("Shana");

        // 3、rpc调用并返回结果
        HelloResponse helloResponse = helloService.hello(helloRequest);

        String helloMsg = helloResponse.getMsg();
        System.out.println(helloMsg);

        // 调用hi方法
        HelloResponse hiResponse = helloService.hi(helloRequest);
        String hiMsg = hiResponse.getMsg();
        System.out.println(hiMsg);





    }
}
