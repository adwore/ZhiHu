package com.computer.mazhihuapp.model;

import java.io.Serializable;

/**
 * Created by computer on 2015/10/19.
 */
public class MyData implements Serializable {

    private String name;
    private int age;

    public MyData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "MyData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
