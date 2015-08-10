package com.lifeisle.jekton.schedule;

/**
 * @author Jekton
 * @version 0.1 8/10/2015
 */
public interface ScheduleDetailView {

    String getEventTitle();

    void setEventTitle(String title);

    String[] getStartTime();

    void setStartTime(String day, String clockTime);

    String[] getEndTime();

    void setEndTime(String day, String clockTime);

    int getRepeat();

    void setRepeat(int repeat);

    int getNotify();

    void setNotify(int notify);

    void showErrMsg(int msgId);

    void close();
}
