package com.lifeisle.jekton.job.data.bean;

import com.lifeisle.jekton.job.data.SubordinateContract;
import com.lifeisle.jekton.job.data.SubordinateContract.PeriodContract;
import com.lifeisle.jekton.job.data.SubordinateContract.PeriodContract.PeerContract;
import com.lifeisle.jekton.job.data.SubordinateContract.RightContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public class SubordinateItem {
    public long jobId;
    public String jobSN;
    public long hisId;
    public String userName;
    public String title;
    public String briefIntro;
    public int country;
    public int province;
    public int city;
    public int district;
    public String address;
    public String map;
    public String contacts;
    public String telPhone;
    public String mobilePhone;
    public long jCatId;
    public Period[] periods;
    public Right[] rights;


    public static SubordinateItem makeInstance(JSONObject jsonObject) throws JSONException {
        SubordinateItem item = new SubordinateItem();
        item.jobId = jsonObject.getLong(SubordinateContract.OWM_JOB_ID);
        item.jobSN = jsonObject.getString(SubordinateContract.OWM_JOB_SN);
        item.hisId = jsonObject.getLong(SubordinateContract.OWM_HIS_ID);
        item.userName = jsonObject.getString(SubordinateContract.OWM_USER_NAME);
        item.title = jsonObject.getString(SubordinateContract.OWM_TITLE);
        item.briefIntro = jsonObject.getString(SubordinateContract.OWM_BRIEF_INTRO);
        item.country = jsonObject.getInt(SubordinateContract.OWM_COUNTRY);
        item.province = jsonObject.getInt(SubordinateContract.OWM_PROVINCE);
        item.city = jsonObject.getInt(SubordinateContract.OWM_CITY);
        item.district = jsonObject.getInt(SubordinateContract.OWM_DISTRICT);
        item.address = jsonObject.getString(SubordinateContract.OWM_ADDRESS);
        item.map = jsonObject.getString(SubordinateContract.OWM_MAP);
        item.contacts = jsonObject.getString(SubordinateContract.OWM_CONTACTS);
        item.telPhone = jsonObject.getString(SubordinateContract.OWM_TEL_PHONE);
        item.mobilePhone = jsonObject.getString(SubordinateContract.OWM_MOBILE_PHONE);
        item.jCatId = jsonObject.getLong(SubordinateContract.OWM_J_CAT_ID);

        JSONArray periodArray = jsonObject.getJSONArray(SubordinateContract.OWM_PERIODS);
        item.periods = Period.makeInstances(periodArray);

        JSONArray rightArray = jsonObject.getJSONArray(SubordinateContract.OWM_RIGHTS);
        item.rights = Right.makeInstances(rightArray);

        return item;
    }

    public static SubordinateItem[] makeInstances(JSONArray subordinateArray) throws JSONException {
        SubordinateItem[] items = new SubordinateItem[subordinateArray.length()];
        for (int i = 0; i < items.length; ++i) {
            items[i] = SubordinateItem.makeInstance(subordinateArray.getJSONObject(i));
        }

        return items;
    }

    public static class Period {
        public long infoId;
        public long jobId;
        public String startTime;
        public String endTime;
        public double salary;
        public double bonus;
        public String attachment;
        public String payWay;
        public long addTime;
        public Peer[] peers;


        public static Period makeInstance(JSONObject jsonObject) throws JSONException {
            Period period = new Period();
            period.infoId = jsonObject.getLong(PeriodContract.OWM_INFO_ID);
            period.jobId = jsonObject.getLong(PeriodContract.OWM_JOB_ID);
            period.startTime = jsonObject.getString(PeriodContract.OWM_START_TIME);
            period.endTime = jsonObject.getString(PeriodContract.OWM_END_TIME);
            period.salary = jsonObject.getDouble(PeriodContract.OWM_SALARY);
            period.bonus = jsonObject.getDouble(PeriodContract.OWM_BONUS);
            period.attachment = jsonObject.getString(PeriodContract.OWM_ATTACHMENT);
            period.payWay = jsonObject.getString(PeriodContract.OWM_PAY_WAY);
            period.addTime = jsonObject.getLong(PeriodContract.OWM_ADD_TIME);

            JSONArray peerArray = jsonObject.getJSONArray(PeriodContract.OWM_PEERS);
            period.peers = Peer.makeInstances(peerArray);

            return period;
        }

        public static Period[] makeInstances(JSONArray periodArray) throws JSONException {
            Period[] periods = new Period[periodArray.length()];
            for (int i = 0; i < periods.length; ++i) {
                periods[i] = Period.makeInstance(periodArray.getJSONObject(i));
            }

            return periods;
        }


        public static class Peer {
            public long statusId;
            public long infoId;
            public String userName;
            public int status;
            public long addTime;


            public static Peer makeInstance(JSONObject jsonObject) throws JSONException {
                Peer peer = new Peer();
                peer.statusId = jsonObject.getLong(PeerContract.OWM_STATUS_ID);
                peer.infoId = jsonObject.getLong(PeerContract.OWM_INFO_ID);
                peer.userName = jsonObject.getString(PeerContract.OWM_USER_NAME);
                peer.status = jsonObject.getInt(PeerContract.OWM_STATUS);
                peer.addTime = jsonObject.getLong(PeerContract.OWM_ADD_TIME);

                return peer;
            }

            public static Peer[] makeInstances(JSONArray peerArray) throws JSONException {
                Peer[] peers = new Peer[peerArray.length()];
                for (int i = 0; i < peers.length; ++i) {
                    peers[i] = Peer.makeInstance(peerArray.getJSONObject(i));
                }

                return peers;
            }
        }
    }

    public static class Right {
        public long hisId;
        public long rightId;
        public String source;
        public String target;
        public long catId;
        public String catName;
        public String introduce;
        public long addTime;


        public static Right makeInstance(JSONObject jsonObject) throws JSONException {
            Right right = new Right();
            right.hisId = jsonObject.getLong(RightContract.OWM_HIS_ID);
            right.rightId = jsonObject.getLong(RightContract.OWM_RIGHT_ID);
            right.source = jsonObject.getString(RightContract.OWM_SOURCE);
            right.target = jsonObject.getString(RightContract.OWM_TARGET);
            right.catId = jsonObject.getLong(RightContract.OWM_CAT_ID);
            right.catName = jsonObject.getString(RightContract.OWM_CAT_NAME);
            right.introduce = jsonObject.getString(RightContract.OWM_INTRODUCE);
            right.addTime = jsonObject.getLong(RightContract.OWM_ADD_TIME);

            return right;
        }

        public static Right[] makeInstances(JSONArray rightArray) throws JSONException {
            Right[] rights = new Right[rightArray.length()];
            for (int i = 0; i < rights.length; ++i) {
                rights[i] = Right.makeInstance(rightArray.getJSONObject(i));
            }

            return rights;
        }
    }
}
