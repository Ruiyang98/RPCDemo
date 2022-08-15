package core.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.*;

public class RpcServer {
    private final ExecutorService threadpool;
    private final HashMap<String, Object> registeredService;

    public RpcServer() {
        // 线程池参数
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadpool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
        this.registeredService = new HashMap<String, Object>();
    }

    // server是interface的implementation object（？）
    public void register(Object service) {
        registeredService.put(service.getClass().getInterfaces()[0].getName(), service);
    }

    public void serve(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server starting...");
            Socket handleSocket;
            while((handleSocket = serverSocket.accept()) != null) {
                System.out.println("client connected, ip:" + handleSocket.getInetAddress());
                threadpool.execute(new RpcServerWorker(handleSocket, registeredService));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
