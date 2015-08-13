package com.easemob.chatuidemo.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boshu.activity.Activity_boshu_Message;
import com.boshu.activity.Activity_boshu_Wallet;
import com.boshu.customview.CircleImageView;
import com.boshu.db.UserDao;
import com.boshu.domain.User;
import com.boshu.image.ImageDowloader;
import com.boshu.image.ImageDowloader.OnImageDownloadListener;
import com.boshu.utils.Model;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.Preferences;

public class Fragment_boshu_Person extends Fragment implements OnClickListener{
    private RelativeLayout rl_Boshu_Message;
    private RelativeLayout rl_Boshu_Wallet;
    private TextView tv_boshu_experience;
    private CircleImageView circl_boshu_head;
    private ImageDowloader mImageDowloader;
    private headImageBroadCast broadCast=new headImageBroadCast();
    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_boshu_person, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
      init();
      getHead();//获取头像
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void init(){
       rl_Boshu_Wallet= (RelativeLayout) getView().findViewById(R.id.rl_boshu_wallet);
        rl_Boshu_Message= (RelativeLayout) getView().findViewById(R.id.rl_boshu_message);
        rl_Boshu_Wallet.setOnClickListener(this);
        rl_Boshu_Message.setOnClickListener(this);
        tv_boshu_experience=(TextView) getView().findViewById(R.id.tv_boshu_experience);
        tv_boshu_experience.setOnClickListener(this);
        circl_boshu_head=(CircleImageView) getView().findViewById(R.id.circl_boshu_head);
        mImageDowloader=new ImageDowloader(getActivity());
        this.RegisterBroadCast(broadCast);
        
    }
    
    //获取头像等
    public void getHead(){
        UserDao ud=new UserDao(getActivity());
        User user=ud.find(Preferences.getUserName());
        String headUrl=Model.PitureLoad+user.getHeadImage();
        Bitmap headBitmap= mImageDowloader.showCacheBitmap(headUrl.replaceAll(
                "[^\\w]", ""));
       setNetBitmap(circl_boshu_head,headBitmap, headUrl);
          
        
    }
    public void setNetBitmap(final ImageView img,Bitmap bitmap,String url){
        
        if(bitmap!=null){
            img.setImageBitmap(bitmap);
        }else{
            mImageDowloader.downloadImage(80, 80,url , new OnImageDownloadListener() {
                @Override
                public void onImageDownload(String url, Bitmap bitmap) {
                    // TODO Auto-generated method stub
                    img.setImageBitmap(bitmap);
                    if(bitmap==null){
                        circl_boshu_head.setImageResource(R.drawable.delete);
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent it=new Intent();
        switch (v.getId()) {
       
        case R.id.rl_boshu_message:
            it.setClass(getActivity(),Activity_boshu_Message.class);
            getActivity().startActivity(it);
            
            break;
        case R.id.tv_boshu_experience:
            layout();
            break;
        case R.id.rl_boshu_wallet:
            it.setClass(getActivity(), Activity_boshu_Wallet.class);
            getActivity().startActivity(it);
            break;
        }
        
    }
    
    public void layout(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        MyApplication.getInstance().logout(new EMCallBack() {
            
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        ((MainActivity) getActivity()).finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        
                    }
                });
            }
            
            @Override
            public void onProgress(int progress, String status) {
                
            }
            
            @Override
            public void onError(int code, String message) {
                
            }
        });
    
    } public void RegisterBroadCast(headImageBroadCast broadCast){
        IntentFilter filter=new IntentFilter();
        filter.addAction("lifeisland.boshu.headimage");
       getActivity().registerReceiver(broadCast, filter);
    }
    public class headImageBroadCast extends BroadcastReceiver{
        
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            byte[] b=intent.getByteArrayExtra("bitmap");
            Bitmap bitmap=BitmapFactory.decodeByteArray(b, 0, b.length);
            circl_boshu_head.setImageBitmap(bitmap);
        }
       
        
    }

}
