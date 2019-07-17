/** 
 * @Title:TODO  
 * @Desription:TODO
 * @Company:CSN
 * @ClassName:CreateSerialNo.java
 * @Author:Administrator
 * @CreateDate:2018-2-28   
 * @UpdateUser:Administrator  
 * @Version:0.1 
 *    
 */

package com.nfdw.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
/** 
 * @ClassName: CreateSerialNo 
 * @Description: TODO 
 * @author: Administrator 
 * @date: 2018-2-28
 * 
 */

/**
 * 生成序号
 * 
 * @author feizi
 * @time 2014-11-5下午5:27:23
 */
public class CreateSerialNo {
    
    private static Map<String, String> map = new HashMap<String, String>();
    private static String STATNUM = "000001";
    
    /**
     * 获取年月日
     * 
     * @return
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        
        return df.format(cal.getTime());
    }
    
    /**
     * 判断序号是否到了最后一个
     * 
     * @param s
     * @return
     */
    public String getLastSixNum(String s) {
        String rs = s;
        int i = Integer.parseInt(rs);
        i += 1;
        rs = "" + i;
        for (int j = rs.length(); j < 6; j++) {
            // rs="0"+rs;
            // 直接使用StringUtils类的leftPad方法处理补零
            rs = StringUtils.leftPad(rs, j + 1, "0");
        }
        return rs;
    }
    
    /**
     * 判断序号是否到了最后一个
     * 
     * @param
     * @return
     */
    public static String getLastSixNum2() {
        String rs = "";
        Long curMlTime = System.currentTimeMillis();
        String curMsTimeStr = String.valueOf(curMlTime);
        rs = StringUtils.substring(curMsTimeStr, curMsTimeStr.length() - 4);
        return rs;
    }
    
    /**
     * 产生不重复的号码 加锁
     * 
     * @return
     */
    public synchronized static String getNum() {
        String yearAMon = getTime();
        String last6Num = getLastSixNum2();
        return yearAMon + last6Num;
    }
    public synchronized String getNums() {
        String yearAMon = getTime();
        return yearAMon;
    }
    /**
     * main测试
     * 
     * @param args
     */
    public static void main(String[] args) {
        CreateSerialNo t = new CreateSerialNo();
        /*
         * for (int i = 0; i < 200; i++) { System.out.println(t.getNum()); }
         */
    	Integer gnum = 1;
        String curNum = t.getNums();
        for (int i = 0; i < 10; i++) {
        	gnum = gnum+1;
	    	DecimalFormat df=new DecimalFormat("0000");
	        String str2=df.format(gnum);
        	System.out.println(curNum+str2);
		}
    }
}
