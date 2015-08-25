package com.lifeisle.jekton.order.list;

/**
 * @author Jekton
 * @version 0.01 7/27/2015
 */
public interface OrderView {

    void showDialog();

    void closeDialog();

    void notifyDataSetChanged();

    void setFailCount(int count);

    void showErrMsg(int msgId);

    void stopRefreshView();
}
