package test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MainTest {
    private static void fullmap(Object object){
        Class clazz  = object.getClass();

    }

    public static void main(String[] args) {
        BeanTest1 beanTest1 = new BeanTest1();
        BeanTest2 beanTest2 = new BeanTest2();

        Map<String,String> map = new HashMap<>();

        Field[] fields = beanTest1.getClass().getFields();

        String bean1_simple_name = beanTest1.getClass().getSimpleName().toLowerCase();
        String bean2_simple_name = beanTest2.getClass().getSimpleName().toLowerCase();

        String sql = "select * from "+bean1_simple_name+","+bean2_simple_name
                +" where ";


    }

}
