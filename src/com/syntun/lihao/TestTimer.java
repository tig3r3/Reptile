package com.syntun.lihao;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {
    static int count = 0;
    
    public static void showTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                CreateDataSum cds=new CreateDataSum();
                cds.aa();
                System.out.println("时间=" + new Date() + " 执行了" + count + "次"); // 1次
            }
		};
        //设置执行时间
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
        //定制每天的21:09:00执行，
       // calendar.set(year, month, day, 21, 9, 00);
       // Date date = calendar.getTime();
        Timer timer = new Timer();
       // System.out.println(date);
        
        int period = 3000 * 1000;
        //每天的date时刻执行task，每隔2秒重复执行
        timer.schedule(task,0,period);
        //每天的date时刻执行task, 仅执行一次
        //timer.schedule(task, date);
    }

    public static void main(String[] args) {
        showTimer();
    }
}