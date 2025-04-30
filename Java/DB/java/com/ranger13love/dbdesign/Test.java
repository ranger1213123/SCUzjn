package com.ranger13love.dbdesign;

import lombok.Builder;
import lombok.Data;
//提供类的构造器和基础的equals方法
@Data
@Builder
public class Test {
    private String name;
    private String gender;
    private String job;
}
