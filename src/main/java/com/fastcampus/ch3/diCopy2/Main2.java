package com.fastcampus.ch3.diCopy2;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class Car{}
class SportsCar extends Car{}
class Truck extends Car{}
class Engine {}

class AppContext{
    Map map; // 객체 저장소

    AppContext(){
        map = new HashMap();


        try {
            Properties p = new Properties();
            p.load(new FileReader("config.txt"));
            
            // Properties에 저장된 내용을 Map에 저장
            map = new HashMap(p);
            
            //반복문으로 클래스 이름을 얻어서 객체를 생성해서 다시 map에 저장
            for(Object key : map.keySet()){
                Class clazz = Class.forName((String)map.get(key));
                map.put(key, clazz.newInstance());
            }


            Class clazz = Class.forName(p.getProperty("car"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 클래스 객체(설계도)를 얻어서

    }
    Object getBean(String key){
        return map.get(key);
    }
}

public class Main2 {

    public static void main(String[] args) throws Exception {
        AppContext ac = new AppContext();
        Car car = (Car)ac.getBean("car");
        Engine engine = (Engine) ac.getBean("engine");
        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
    }

    static Object getBean(String key) throws Exception {
        //config.txt를 읽어서 Properties에 저장
        Properties p = new Properties();
        p.load(new FileReader("config.txt"));
    
        // 클래스 객체(설계도)를 얻어서
        Class clazz = Class.forName(p.getProperty(key));

        return clazz.newInstance(); //객체를 생성해서 반환
    }

    static Car getCar() throws Exception {
        //config.txt를 읽어서 Properties에 저장
        Properties p = new Properties();
        p.load(new FileReader("config.txt"));

        // 클래스 객체(설계도)를 얻어서
        Class clazz = Class.forName(p.getProperty("car"));

        return (Car)clazz.newInstance(); //객체를 생성해서 반환
    }
}
