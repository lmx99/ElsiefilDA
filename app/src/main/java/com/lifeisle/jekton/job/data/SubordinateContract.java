package com.lifeisle.jekton.job.data;

/**
 * @author Jekton
 */
public class SubordinateContract {

    public static final String OWM_SUBORDINATE = "under_job";

    public static final String OWM_JOB_ID = "job_id";
    public static final String OWM_JOB_SN = "job_sn";
    public static final String OWM_HIS_ID = "his_id";
    public static final String OWM_USER_NAME = "user_name";
    public static final String OWM_TITLE = "title";
    public static final String OWM_BRIEF_INTRO = "introduce";
    public static final String OWM_COUNTRY = "country";
    public static final String OWM_PROVINCE = "province";
    public static final String OWM_CITY = "city";
    public static final String OWM_DISTRICT = "district";
    public static final String OWM_ADDRESS = "address";
    public static final String OWM_MAP = "map";
    public static final String OWM_CONTACTS = "contacts";
    public static final String OWM_TEL_PHONE = "tel_phone";
    public static final String OWM_MOBILE_PHONE = "mobile_phone";
    public static final String OWM_J_CAT_ID = "jcat_id";
    public static final String OWM_PERIODS = "infos";
    public static final String OWM_RIGHTS = "rights";


    public static final class PeriodContract {

        public static final String OWM_INFO_ID = "info_id";
        public static final String OWM_JOB_ID = "job_id";
        public static final String OWM_START_TIME = "start_time";
        public static final String OWM_END_TIME = "end_time";
        public static final String OWM_SALARY = "salary";
        public static final String OWM_BONUS = "bonus";
        public static final String OWM_ATTACHMENT = "attachment";
        public static final String OWM_PAY_WAY = "pay_way";
        public static final String OWM_ADD_TIME = "add_time";
        public static final String OWM_PEERS = "epls";

        public static class PeerContract {
            public static final String OWM_STATUS_ID = "status_id";
            public static final String OWM_INFO_ID = "info_id";
            public static final String OWM_USER_NAME = "user_name";
            public static final String OWM_STATUS = "status";
            public static final String OWM_ADD_TIME = "add_time";

        }

    }


    public static final class RightContract {
        public static final String OWM_HIS_ID = "his_id";
        public static final String OWM_RIGHT_ID = "right_id";
        public static final String OWM_SOURCE = "source";
        public static final String OWM_TARGET = "target";
        public static final String OWM_CAT_ID = "cat_id";
        public static final String OWM_CAT_NAME = "cat_name";
        public static final String OWM_INTRODUCE = "introduce";
        public static final String OWM_ADD_TIME = "add_time";
    }


}
