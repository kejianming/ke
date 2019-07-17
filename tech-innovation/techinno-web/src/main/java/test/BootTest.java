package test;

import com.nfdw.Application;
import com.nfdw.entity.SysUser;
import com.nfdw.service.SysUserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**

 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class BootTest {

  @Autowired
  SysUserService userService;

  @Test
  public void testStartJob() throws Exception {
    //12

    SysUser user=userService.selectByPrimaryKey("2211fec3e17c11e795ed201a068c6482");
    System.out.println(user.getUsername());
  }

}