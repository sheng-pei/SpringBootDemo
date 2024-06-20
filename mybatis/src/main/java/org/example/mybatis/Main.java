package org.example.mybatis;

import org.example.mybatis.entity.Aaa;
import org.example.mybatis.mapper.AaaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSetMetaData;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootApplication
public class Main {

//    @Autowired
//    private TestService service;
//
//    @Autowired
//    private B b;

//    @Audit("test")
//    public void test() {
//        Entity entity = new Entity();
//        entity.setId(1L);
//        entity.setCode("kmfiwi222");
//        service.test(entity);
//        service.test1(entity);
//    }

    @Autowired
    private AaaMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Value("{user.dir}")

    public static void main(String[] args) throws Exception {
        System.out.println(UUID.randomUUID().toString());
        ApplicationContext context = SpringApplication.run(Main.class, args);
        Main main = context.getBean(Main.class);
//        main.transactionTemplate.execute(status -> {
//            main.jdbcTemplate.update("update test set content='1' where id = 1");
//            Aaa aaa = new Aaa();
//            aaa.setName("87");
//            aaa.setDeleted(false);
//            main.mapper.insert(aaa);
//            return null;
//        });
//        File file = new File("afg");
//        System.out.println(Paths.get("kk", "/ll"));
//        System.out.println(file.mkdirs());
//
//        Files.walk(Paths.get(""), 2, FileVisitOption.FOLLOW_LINKS)
//                .filter(Files::isRegularFile)
//                .forEach(System.out::println);
//        try (A a = new A()) {
//            System.out.println("start");
//        } finally {
//            System.out.println("finally");
//        }
    }

    private static class A implements AutoCloseable {

        @Override
        public void close() throws Exception {
            System.out.println("close");
        }
    }

}
