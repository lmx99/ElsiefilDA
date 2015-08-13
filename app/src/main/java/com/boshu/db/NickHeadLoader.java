package com.boshu.db;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.domain.NickHeadUser;
import com.boshu.image.ImageDowloader;
import com.boshu.utils.Model;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONObject;

import java.util.Map;


public class NickHeadLoader {
    private Context context;
    private ImageDowloader mImageDownloader;
    public NickHeadLoader(Context context){
        this.mImageDownloader = new ImageDowloader(context);
        this.context=context;
    }
    public void setNickImage(final String userName, final OnNickImageLoadListener listener){
        final NickHeadUserDao userDao=new NickHeadUserDao(context);
       final NickHeadUser user= userDao.find(userName);
        if(user!=null){
            System.out.println("user不为空呀");
            String str_url=Model.PitureLoad+user.getHeadImage();
            System.out.println(str_url+"==========");
            mImageDownloader.downloadImage(45, 45, str_url,
                    new ImageDowloader.OnImageDownloadListener() {
                @Override
                public void onImageDownload(String url, Bitmap bitmap) {
                    listener.OnNickImage(user.getNickName(),bitmap);
                }
            });
        }else{
            getNickImage(userName, new OnNickLoadListener() {
                @Override
                public void OnNickLoad(final String nickName, final String headUrl) {
                    final  String headUrl1=headUrl;
                    final  String nickName1=nickName;
                    final String str_url=Model.PitureLoad+headUrl;
                    System.out.println("图片路径：="+str_url);
                   mImageDownloader.downloadImage(40, 40, str_url, new ImageDowloader.OnImageDownloadListener() {
                       @Override
                       public void onImageDownload(String url, Bitmap bitmap) {

                          NickHeadUser user=new NickHeadUser();
                           System.out.println("插入的量有：" + userName + "--" + nickName + "--" + headUrl);
                          user.setUserName(userName);
                           user.setNickName(nickName);
                           user.setHeadImage(headUrl1);
                           userDao.addNHUser(user);
                           listener.OnNickImage(nickName1, bitmap);

                       }
                   });

                }
            });
        }

    }
    public interface  OnNickLoadListener{
        void OnNickLoad(String nickName,String headUrl);
    }
    public interface OnNickImageLoadListener{
        void OnNickImage(String nickName,Bitmap bitMap);
    }
    public void getNickImage(final String userName, final OnNickLoadListener listener){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.start();

        requestQueue.add(new AutoLoginRequest(context, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                String nickanme = response.getString("nick_name");
                String head_image = response.getString("head_image");

                    listener.OnNickLoad(nickanme,head_image);
                    System.out.println(nickanme + "-------" + head_image);
                }catch (Exception e){
                      e.printStackTrace();
                    System.out.println("出错 了"+e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected void setParams(Map<String, String> params) {
                params.put("user_name",userName);
                params.put("sys", "user");
                params.put("ctrl", "user");
                params.put("action", "user_info");
            }
        });
    }


    }


