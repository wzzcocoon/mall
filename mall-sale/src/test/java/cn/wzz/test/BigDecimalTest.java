package cn.wzz.test;

import java.math.BigDecimal;

public class BigDecimalTest {
	public static void main(String[] args) {
		// 1.初始化
		BigDecimal b1 = new BigDecimal("0.2"); 
		BigDecimal b2 = new BigDecimal(0.2d); 
		BigDecimal b3 = new BigDecimal(0.2f); 
		System.out.println(b1);
		System.out.println(b2);
		System.out.println(b3);
		
		// 2.比较大小
		int compareTo = b1.compareTo(b2);
		System.out.println(compareTo);	// -1小于		0等于		1大于
		
		// 3.运算
		BigDecimal b4 = new BigDecimal("6"); 
		BigDecimal b5 = new BigDecimal("7"); 
		BigDecimal add = b4.add(b5);
		System.out.println(add);
		BigDecimal subtract = b4.subtract(b5);
		System.out.println(subtract);
		BigDecimal multiply = b4.multiply(b5);
		System.out.println(multiply);
//		BigDecimal divide = b4.divide(b5);
//		System.out.println(divide);
		//除不尽，报错
		
		// 4.取舍
		BigDecimal divide = b4.divide(b5, 2, BigDecimal.ROUND_HALF_DOWN);
		System.out.println(divide);
		
		// 对一个值进行取舍setScale("保留小数","取舍规则")
		BigDecimal add2 = b2.add(b3);
		System.out.println(add2);
		BigDecimal setScale = add2.setScale(2,BigDecimal.ROUND_HALF_DOWN);
		System.out.println(setScale);
	}
}
