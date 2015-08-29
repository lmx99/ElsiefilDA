package com.lifeisle.jekton.job.data;

/**
 * @author Jekton
 */
public class MyJobContract {

    public static final String OWM_MY_JOB = "my_job";

    public static final String OWM_SUBORDINATES = "under_job";

    public static final String OWM_TITLE = "title";
    public static final String OWM_INTRO = "intro";
    public static final String OWM_ADDRESS = "address";
    public static final String OWM_PERIODS = "infos";


    public static class PeriodContract {
        public static final String OWM_START_TIME = "start_time";
        public static final String OWM_END_TIME = "end_time";
        public static final String OWM_PEERS = "epls";

        public static class PeerContract {
            public static final String OWM_USER_NAME = "user_name";
            public static final String OWM_E_STATUS = "estatus";
            public static final String OWM_STATUS_ID = "status_id";
        }
    }
}
