package com.nfdw.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.nfdw.entity.CurrentUser;
import com.nfdw.entity.SysUser;

/**

 */
public class ShiroUtil {

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static Session getSession(){
        return getSubject().getSession();
    }
    public static CurrentUser getCurrentUse(){
        return (CurrentUser) getSession().getAttribute("curentUser");
    }
    public static SysUser getUser(){
        return (SysUser) getSession().getAttribute("user");
    }
}
