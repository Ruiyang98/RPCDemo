package IDL.Hello;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

// @Data省去set、get方法，@AllArgsConstructor提供类的全参构造
@Data
@AllArgsConstructor
public class HelloResponse implements Serializable {
    private String msg;
}
