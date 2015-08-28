package com.boshu.utils;

import com.boshu.domain.User;

/**
 * Created by amou on 27/8/2015.
 */
public class UserIndent {
    public static boolean getuserIndent(User user){
      if(judge(user.getAferIdCard())&&judge(user.getBeforeIdCard())&&judge(user.getStudentImage())&&judge(user.getRealName())&&judge(user.getSchool())&&judge(user.getNickName())&&judge(user.getMajor())&&judge(user.getSex())&&judge(user.getAge())&&judge(user.getHight())&&judge(user.getFigure())&&judge(user.getEntrance_year())&&judge(user.getRealName())&&judge(user.getMyPhone())&&judge(user.getFriendPhone())&&judge(user.getMail())&&judge(user.getqQ())&&judge(user.getJobWant())&&judge(user.getResume())&&judge(user.getIdCard())&&judge(user.getIdCard())){
        return  true;
        }else{
          return  false;
      }
      }

    public static boolean judge(String str){
        {
            if (str != null) {
                if (str.equals(""))
                    return false;
            }
        }
        return true;
    }
}
