package org.example.mybatisplus;

import org.example.mybatisplus.mapper.TenantMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("org.example.mybatisplus.mapper")
public class Main {

    @Autowired
    private TenantMapper mapper;

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Main.class);
        ApplicationContext context = application.run(args);
        Main main = (Main) context.getBean(Main.class);
        main.test();
    }

    public void test() {
        BasePO base1 = new BasePO();
        base1.setId(1L);
        base1.setDeleted(false);
        base1.setName("a");

        BasePO base2 = new BasePO();
        base2.setId(2L);
        base2.setDeleted(true);
        base2.setName("b");

        List<BasePO> list = new ArrayList<>();
        list.add(base1);
        list.add(base2);
        mapper.insert(11L, "11", true);
    }
}