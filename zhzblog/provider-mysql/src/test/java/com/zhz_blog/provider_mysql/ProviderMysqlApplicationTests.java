package com.zhz_blog.provider_mysql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;

//@SpringBootTest
class ProviderMysqlApplicationTests {

    @Test
    void contextLoads() {

    }
}

class c1 implements Comparator<c1> {

    @Override
    public int compare(c1 o1, c1 o2) {
        return 0;
    }
}

class c2 implements Comparable<c2> {
    @Test
    public void read() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String s = bufferedReader.readLine();
            System.out.println(s);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public int compareTo(c2 o) {
        return 0;
    }
}
