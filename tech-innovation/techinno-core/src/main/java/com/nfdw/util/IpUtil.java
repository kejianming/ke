package com.nfdw.util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class IpUtil {

    public static String getIp(HttpServletRequest request){
        String ip=request.getHeader("x-forwarded-for");
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("X-Real-IP");
        }
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
        }
        return ip;
    }
    public final static long convertIP(String ipAddress) {
        long rtn = 0;
        String[] ss = ipAddress.split("\\.");
        for (int i = 0; i < ss.length; i++) {
            rtn <<= 8;
            rtn += Long.valueOf(ss[i]);
        }
        for (int i = ss.length; i < 4; i++) {
            rtn <<= 8;
        }
        return rtn;
    }

    /*
      求ip地址
    */
    public static String[] SetMoreIP(String beginip, String endip,Integer num) {
        try {
            long long_beginip = Long.MIN_VALUE;
            long long_endip = Long.MIN_VALUE;

            //转换成long值
            long_beginip = IpUtil.convertIP(beginip);
            //转换成long值
            long_endip = IpUtil.convertIP(endip);

            //求解范围之内的IP地址
            long[] long_ip = new long[ (int) Math.abs(num)];
            Long aaas =(long_beginip - long_endip);
            for (int k = 0; k <Math.abs(num); k++) {
                if (aaas < 0) {
                    aaas=aaas+ 1;
                    long_ip[k] = long_beginip + (long) k;
                }
            }

            //装换成字符串
            String[] strip = new String[4];
            String[] ipList = new String[long_ip.length];
            for (int m = 0; m < long_ip.length; m++) {
                long aaa =long_ip[m];
                if (long_ip[m]==0) {
                    break;
                }
                if ((long_ip[m] & 0x00000000000000ff)==0) {
                    continue;
                }
                strip[0] = String.valueOf((long_ip[m] & 0x00000000000000ff)==0?1:(long_ip[m] & 0x00000000000000ff));
                strip[1] = String.valueOf(long_ip[m] >> 8 & 0x00000000000000ff);
                strip[2] = String.valueOf(long_ip[m] >> 16 & 0x00000000000000ff);
                strip[3] = String.valueOf(long_ip[m] >> 24 & 0x00000000000000ff);
                ipList[m] = strip[3] + "." + strip[2] + "." + strip[1] + "." + strip[0];
            }

            return ipList;

        }
        catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
    /*
      求ip地址
    */
    public static String[] SetMoreIP2(String beginip, String endip) {
        try {
            long long_beginip = Long.MIN_VALUE;
            long long_endip = Long.MIN_VALUE;

            //转换成long值
            long_beginip = IpUtil.convertIP(beginip);
            //转换成long值
            long_endip = IpUtil.convertIP(endip);

            //求解范围之内的IP地址
            long[] long_ip = new long[ (int) Math.abs(long_beginip - long_endip)+1];
            for (int k = 0; k <=Math.abs(long_beginip - long_endip); k++) {
                if (long_beginip - long_endip < 0) {
                    long_ip[k] = long_beginip + (long) k;
                }
            }

            //装换成字符串
            String[] strip = new String[4];
            String[] ipList = new String[long_ip.length];
            for (int m = 0; m < long_ip.length; m++) {
                long aaa =long_ip[m];
                if (long_ip[m]==0) {
                    break;
                }
                if ((long_ip[m] & 0x00000000000000ff)==0) {
                    continue;
                }
                strip[0] = String.valueOf(long_ip[m] & 0x00000000000000ff);
                strip[1] = String.valueOf(long_ip[m] >> 8 & 0x00000000000000ff);
                strip[2] = String.valueOf(long_ip[m] >> 16 & 0x00000000000000ff);
                strip[3] = String.valueOf(long_ip[m] >> 24 & 0x00000000000000ff);
                ipList[m] = strip[3] + "." + strip[2] + "." + strip[1] + "." + strip[0];
            }

            return ipList;

        }
        catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
    public static List ipReturnInfo(String beginip, String endip, Integer num){
        List pdlist=new ArrayList();
        String[] ss=SetMoreIP(beginip,endip,num);
        for (int i = 0; i < ss.length; i++) {
            if (ss[i]==null) {
                continue;
            }
            pdlist.add(ss[i]);
        }
        return pdlist;

    }
    public static List ipReturnInfo2(String beginip,String endip){
        List pdlist=new ArrayList();
        String[] ss=SetMoreIP2(beginip,endip);
        for (int i = 0; i < ss.length; i++) {
            if (ss[i]==null) {
                continue;
            }
            pdlist.add(ss[i]);
        }
        return pdlist;

    }
    /****
     * 
     *  不包含255 和结尾 是0
     * */
    public static List ipReturnInfo3(String beginip,String endip){
        List pdlist=new ArrayList();
        String[] ss=SetMoreIP2(beginip,endip);
    	for (int i = 0; i < ss.length; i++) {
			 if (ss[i]==null) {
				 continue;
			 }
			 String[] ips= ss[i].split("\\.");
			 if (ss[i].contains("255") || ips[2].contains("0")) {
				 continue;
			 };
			 pdlist.add(ss[i]);
		}
        return pdlist;

    }
    public static void main(String[] args) {
        String ip="172.17.51.253";

//		String ips=ip.substring(ip.lastIndexOf(".")+1,ip.length());
  //      String[] ips=ip.split("\\.");
      //  System.out.println(ips[2]);
        
		List pdlist=new ArrayList();
		String[] ss=SetMoreIP2("172.17.254.1","172.17.255.2");
		for (int i = 0; i < ss.length; i++) {
			 if (ss[i]==null) {
				 continue;
			 }
			 String[] ips= ss[i].split("\\.");
			 if (ss[i].contains("255") || ips[2].contains("0")) {
				 continue;
			 };
			 pdlist.add(ss[i]);
		}
		for (int i = 0; i < pdlist.size(); i++) {
			String ipss =(String) pdlist.get(i);
			System.out.println(ipss);
		}
    }
}
