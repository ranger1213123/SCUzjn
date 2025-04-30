package com.ranger13love.dbdesign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TestDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //实现用户的添加
    public void addTest(){
        Test test = Test.builder().
                job("当牛做马").gender("TS").name("牛马").build();
        jdbcTemplate.update(
                "insert test values(?,?,?)",//避免sql注入
                    new Object[]{test.getName(),test.getGender(),test.getJob()}
        );
        log.info("元组添加成功！",test);
    }//这里要设置相应的依赖，对应实现抛出异常

    //获取总数
    private Long getCount(){
        return jdbcTemplate.queryForObject("select count(1) from test",Long.class);
    }

}
