package com.survey.lib.common.utils;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/21
 **/
public class CalculationUtils {


    /**
     * 计算  单次沉降量
     * @param currElevation   本次沉降量
     * @param preElevation    上次沉降量
     * @return   单次沉降量
     */
    public static double  settlementDiff(double currElevation,double preElevation){
        //单次沉降超限  单次沉降量 = 本次高程 - 上次高程
        return currElevation - preElevation;
    }


    /**
     * 计算  累积沉降量
     * @param currElevation  本次高程
     * @param initElevation   初始高程
     * @return   累积沉降量
     */
    public  static  double totalSettlement(double currElevation,double initElevation){
        //累积沉降超限 累积沉降量 = 本次高程 - 初次高程
        return currElevation-initElevation;
    }


    /**
     * 计算  沉降速率
     * @param totalSettlement   累积沉降量
     * @param currDate        当前时间
     * @param initDate        初始时间
     * @return    沉降速率
     */
    public  static  double settlementRate(double totalSettlement, Date currDate,Date initDate){
        //沉降速率超限  沉降速率 = 累积沉降量 / 历经的天数（自然天数）
        int days = DateUtils.betweenDays(initDate, currDate);
        return totalSettlement/days;
    }


}
