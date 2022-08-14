package IDL.Hello;

import IDL.Hello.HelloRequest;
import IDL.Hello.HelloResponse;

public interface HelloService {
    HelloResponse hello(HelloRequest request);
    HelloResponse hi(HelloRequest request);
}
