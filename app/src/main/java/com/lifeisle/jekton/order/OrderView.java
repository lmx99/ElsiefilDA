package com.lifeisle.jekton.order;

/**
 * @author Jekton
 * @version 0.01 7/27/2015
 */
public interface OrderView {

    void showDialog();

    void closeDialog();

    void notifyDataSetChanged();

    void setFailCount(int count);

    void startScanActivity();

    void showErrMsg(int msgId);
}
