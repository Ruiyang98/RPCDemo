package server;

import IDL.Hello.HelloService;
import core.server.RpcServer;

public class ServerTest {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();
        HelloService helloService = new HelloServiceImpl();
        rpcServer.register(helloService);

        rpcServer.serve(9000);
    }
}
