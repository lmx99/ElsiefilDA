package com.lifeisle.jekton.job.data.bean;

import com.lifeisle.jekton.job.data.MyJobContract;
import com.lifeisle.jekton.job.data.MyJobContract.PeriodContract;
import com.lifeisle.jekton.job.data.MyJobContract.PeriodContract.PeerContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public class MyJobItem {

    public String title;
    public String intro;
    public String address;
    public Period[] periods;
    public SubordinateItem[] subordinates;


    public static MyJobItem makeInstance(JSONObject response) throws JSONException {
        JSONObject myJob = response.getJSONObject(MyJobContract.OWM_MY_JOB);

        MyJobItem item = new MyJobItem();
        item.title = myJob.getString(MyJobContract.OWM_TITLE);
        item.intro = myJob.getString(MyJobContract.OWM_INTRO);
        item.address = myJob.getString(MyJobContract.OWM_ADDRESS);

        JSONArray periodArray = myJob.getJSONArray(MyJobContract.OWM_PERIODS);
        item.periods = Period.makeInstances(periodArray);


        JSONArray subordinateArray = response.getJSONArray(MyJobContract.OWM_SUBORDINATES);
        item.subordinates = SubordinateItem.makeInstances(subordinateArray);

        return item;
    }


    public static class Period {
        public String startTime;
        public String endTime;
        public Peer[] peers;


        public static Period makeInstance(JSONObject jsonObject) throws JSONException {
            Period period = new Period();
            period.startTime = jsonObject.getString(PeriodContract.OWM_START_TIME);
            period.endTime = jsonObject.getString(PeriodContract.OWM_END_TIME);

            JSONArray peersArray = jsonObject.getJSONArray(PeriodContract.OWM_PEERS);
            period.peers = Peer.makeInstances(peersArray);

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
            public String userName;
            public String eStatus;
            public long statusId;


            public static Peer makeInstance(JSONObject jsonObject) throws JSONException {
                Peer peer = new Peer();
                peer.userName = jsonObject.getString(PeerContract.OWM_USER_NAME);
                peer.eStatus = jsonObject.getString(PeerContract.OWM_E_STATUS);
                peer.statusId = jsonObject.getLong(PeerContract.OWM_STATUS_ID);

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
}
