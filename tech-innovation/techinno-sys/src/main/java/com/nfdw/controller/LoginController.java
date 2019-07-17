package com.nfdw.controller;

import com.alibaba.fastjson.JSONArray;
import com.nfdw.base.controller.BaseController;
import com.nfdw.core.annotation.Log;
import com.nfdw.core.shiro.ShiroUtil;
import com.nfdw.entity.PageData;
import com.nfdw.entity.SysMenu;
import com.nfdw.entity.SysUser;
import com.nfdw.service.MenuService;
import com.nfdw.service.SysUserService;
import com.nfdw.util.BaseDecodeUtil;
import com.nfdw.util.StringUtils;
import com.nfdw.util.VerifyCodeUtils;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 登录、退出页面
 */
@Controller
@Slf4j
public class LoginController  extends BaseController{

    @Autowired
    private SysUserService userService;

    @Autowired
    private MenuService menuService;
    
    @GetMapping(value = "")
    public String loginInit() {
        return loginCheck();
    }

    @GetMapping(value = "goLogin")
    public String goLogin(Model model, ServletRequest request) {
        Subject sub = SecurityUtils.getSubject();
        if (sub.isAuthenticated()) {
            return "/main/main";
        } else {
            model.addAttribute("message", "请重新登录");
            return "/login";
        }
    }

    @GetMapping(value = "/login")
    public String loginCheck() {
        Subject sub = SecurityUtils.getSubject();
        Boolean flag2 = sub.isRemembered();
        boolean flag = sub.isAuthenticated() || flag2;
        Session session = sub.getSession();
        if (flag) {
            return "/main/main";
        }
        return "/login";
    }

    /**
     * 登录动作
     *
     * @param user
     * @param model
     * @param rememberMe
     * @return
     */
    @Log(desc="登入系统",type = Log.LOG_TYPE.LOGIN)
    @ApiOperation(value = "/login", httpMethod = "POST", notes = "登录method")
    @PostMapping(value = "/login")
    public String login(SysUser user, Model model, String rememberMe, HttpServletRequest request) {
//        String codeMsg = (String) request.getAttribute("shiroLoginFailure");
//        if ("code.error".equals(codeMsg)) {
//            model.addAttribute("message", "验证码错误");
//            return "/login";
//        }
    	String username= new String(BaseDecodeUtil.decode(user.getUsername().trim()));
    	String password= new String(BaseDecodeUtil.decode(user.getPassword().trim()));
    	user.setUsername(username);
    	user.setPassword(password);
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername().trim(),
                user.getPassword());
        Subject subject = ShiroUtil.getSubject();
        String msg = null;
        try {
            subject.login(token);
            //subject.hasRole("admin");
            if (subject.isAuthenticated()) {
                return "redirect:/main";
            }
        } catch (UnknownAccountException e) {
            msg = "用户名/密码错误";
        } catch (IncorrectCredentialsException e) {
            msg = "用户名/密码错误";
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败多次，账户锁定10分钟";
        }
        if (msg != null) {
            model.addAttribute("message", msg);
        }
        return "/login";
    }
    @GetMapping("/main")
    public String main(Model model){
    	PageData pd =this.getPageData();
    	model.addAttribute("regionId", pd.getString("regionId"));
        return "main/main";
    }

    @Log(desc = "用户退出平台")
    @GetMapping(value = "/logout")
    public String logout() {
        Subject sub = SecurityUtils.getSubject();
        sub.logout();
        return "/login";
    }

    /**
     * 组装菜单json格式
     * update by 17/12/13
     *
     * @return
     */
    public JSONArray getMenuJson() {
        List<SysMenu> mList = menuService.getMenuNotSuper();
        JSONArray jsonArr = new JSONArray();
        for (SysMenu sysMenu : mList) {
            SysMenu menu = getChild(sysMenu.getId());
            jsonArr.add(menu);
        }
        return jsonArr;
    }

    public SysMenu getChild(String id) {
        SysMenu sysMenu = menuService.selectByPrimaryKey(id);
        List<SysMenu> mList = menuService.getMenuChildren(id);
        for (SysMenu menu : mList) {
            SysMenu m = getChild(menu.getId());
            //sysMenu.addChild(m);
        }
        return sysMenu;
    }


    @GetMapping(value = "/getCode")
    public void getYzm(HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpg");

            //生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            log.info("verifyCode:{}",verifyCode);
            //存入会话session
            HttpSession session = request.getSession(true);
            session.setAttribute("_code", verifyCode.toLowerCase());
            //生成图片
            int w = 146, h = 33;
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/menuMain")
    public ModelAndView  menuMain(HttpServletRequest request){
    	ModelAndView mv = new ModelAndView("main/main");
    	String type =request.getParameter("type");
    	String url =request.getParameter("url");
    	String name =request.getParameter("name");
    	mv.addObject("type", type);
    	mv.addObject("url", url);
    	mv.addObject("name", name);
    	return mv;
    }
    @RequestMapping("/domain")
    public ModelAndView domain(HttpServletRequest request) {
    	String url="";
    	String cardType =request.getParameter("cardType");
    	String value =request.getParameter("cardValue");
    	String type =request.getParameter("type");
    	if ("1".equals(type)) {
    		url="card/cardList";
    	} else if ("2".equals(type)) {
    		url="terminal/terminalList";
    	} else {
    		url="equipment/gjgl/warningList";
    	}
    	ModelAndView mv=new ModelAndView(url);
    	mv.addObject("cardType", cardType);
    	mv.addObject("cardValue", value);
    	return mv;
	
	}
    
    
    /**
     * APP登录动作
     *
     * @param user
     * @param model
     * @param rememberMe
     */
    @RequestMapping(value = "/applogin")
    public void applogin(SysUser user,HttpServletResponse response, HttpServletRequest request) {
    	PageData result = new PageData();
    	PageData pd =this.getPageData();
        UsernamePasswordToken token = new UsernamePasswordToken(pd.getString("username").trim(),
        		pd.getString("password"));
        Subject subject = ShiroUtil.getSubject();
        String msg = "登录成功";
    	boolean isRightUserAndPassword = true;
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            msg = "用户名/密码错误";
            isRightUserAndPassword = false;
        } catch (IncorrectCredentialsException e) {
            msg = "用户名/密码错误";
          	isRightUserAndPassword = false;
        } catch (ExcessiveAttemptsException e) {
            msg = "登录失败多次，账户锁定10分钟";
          	isRightUserAndPassword = false;
        }
        if(isRightUserAndPassword == false){
			result.put(RSP_CODE, "-1");
			result.put(RSP_MSG, "用户名或密码不正确");
			writeJson(response, result);
			return;
		}
        result.put(RSP_CODE, "1");
		result.put(RSP_MSG, msg);
		writeJson(response, result);
    }
}
