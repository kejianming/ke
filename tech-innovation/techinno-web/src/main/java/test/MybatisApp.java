package test;


import org.junit.Test;
import org.mybatis.generator.api.ShellRunner;

import java.lang.reflect.Method;

/**

 */
public class MybatisApp {


  public static void main(String[] args) {
    args = new String[] { "-configfile", "nfdw-web\\src\\main\\resources\\auto-config\\mybatis-config.xml", "-overwrite" };
    ShellRunner.main(args);
  }

}
