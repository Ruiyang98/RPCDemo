package core.client;

import core.codec.RpcRequestBody;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequestBody rpcRequestBody = RpcRequestBody.builder()
                .interfaceName()
                .methodName()
                .parameters()
                .paramTypes()
                .build();


        return null;
    }

    // 1、表示忽略unchecked警告
    // 2、<T>表示泛型方法，T表示返回值是泛型，Class<T>表示形参clazz为Class类型
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                this
        );
    }


}
