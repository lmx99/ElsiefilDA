package com.boshu.utils;

/**
 * Created by amou on 6/9/2015.
 */
public class DateUtils {
   /* java.sql.Date转为java.util.Date
    java.sql.Date date=new java.sql.Date();
    java.util.Date d=new java.util.Date (date.getTime());
    java.util.Date转为java.sql.Date
    java.util.Date utilDate=new Date();
    java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
    java.util.Date utilDate=new Date();
    java.sql.Date sqlDate=new java.sql.Date(utilDate.getTime());
    java.sql.Time sTime=new java.sql.Time(utilDate.getTime());
    java.sql.Timestamp stp=new java.sql.Timestamp(utilDate.getTime());
    -- 这里所有时间日期都可以被SimpleDateFormat格式化format()
    SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    f.format(stp);
    f.format(sTime);
    f.format(sqlDate);
    f.format(utilDate)
    java.sql.Date sqlDate=java.sql.Date.valueOf(" 2005-12-12");
    utilDate=new java.util.Date(sqlDate.getTime());
    -- 另类取得年月日的方法：
            import java.text.SimpleDateFormat;
    import java.util.*;
    java.util.Date date = new java.util.Date();
    -- 如果希望得到YYYYMMDD的格式SimpleDateFormat
            sy1=new SimpleDateFormat("yyyyMMDD");
    String dateFormat=sy1.format(date);
    -- 如果希望分开得到年，月，日SimpleDateFormat
            sy=new SimpleDateFormat("yyyy");
    SimpleDateFormat sm=new SimpleDateFormat("MM");
    SimpleDateFormat sd=new SimpleDateFormat("dd");
    String syear=sy.format(date);
    String smon=sm.format(date);
    String sday=sd.format(date);*/
    public void transSqlDate(){

    }


    

}
