package org.cp4j.core.http.utils;


class LogInner {
	
	
	public static void print(String s){
		System.out.println("Genius: " + s);
	}
	
	public static void debug(String msg){
		if(msg == null) msg = "null";
	}
}
