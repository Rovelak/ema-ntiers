package ema.tpjee.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {
	
	public static void debugRequestParameters(Class servletClass,HttpServletRequest request){
		System.out.println("[[[debugRequestParameters("+servletClass.getName()+")");
		Enumeration<String> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object value = request.getParameter(key);
			System.out.println(key + " = " + value);
		}
		/*
		String referer = request.getHeader("referer");
		referer= referer.substring(referer.lastIndexOf("/")+1);
	
		Enumeration<String> keyhs = request.getHeaderNames();
		while (keyhs.hasMoreElements()) {
			String key = keyhs.nextElement(); //referer = http://localhost:8080/webank/bank.do
			Object value = request.getHeader(key);
			System.out.println(key + " = " + value);
		}
		*/
		System.out.println("debugRequestParameters]]]");

	}
	
	public static void debugSessionAttributes(Class servletClass,HttpServletRequest request){
		System.out.println("[[[debugSessionAttributes("+servletClass.getName()+")");
		Enumeration<String> keys = request.getSession().getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object value = request.getSession().getAttribute(key);
			System.out.println(key + " = " + value);
		}
		System.out.println("debugSessionAttributes]]]");
	}

}
