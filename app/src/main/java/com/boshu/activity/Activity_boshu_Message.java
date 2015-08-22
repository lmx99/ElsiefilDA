package com.boshu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.adapter.Adapter_Boshu_alterDialog;
import com.boshu.customview.CircleImageView;
import com.boshu.db.UserDao;
import com.boshu.domain.User;
import com.boshu.image.BitMapLruCacheHelper;
import com.boshu.image.BitmapUtils;
import com.boshu.image.FileCacheUtils;
import com.boshu.image.ImageDowloader;
import com.boshu.image.ImageDowloader.OnImageDownloadListener;
import com.boshu.utils.Model;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.network.AutoLoginRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

public class Activity_boshu_Message extends Activity implements
        OnClickListener, OnItemClickListener {
    private TextView tv_boshu_BaseMessage;
    private RelativeLayout rl_boshu_head;
    private AlertDialog alertDialog;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CAMERA = 18;
    private File cameraFile;
    private String pathImage;
    private CircleImageView imageview;
    private static final String FILE_PATH = "/sdcard/lifeIsland.jpg";
    private ImageView afterImage;
    private ImageView beforeImage;
    private ImageView studentImage;
    private  FileCacheUtils fileCacheUtils;
    private Button bt_boshu_indentButton;
    public static Activity_boshu_Message context;
    private ProgressDialog mProngressDialog=null;

    private String[] ss = new String[] { "相册", "拍照" };
   private headImageBroadCast broadCast=new headImageBroadCast();
   
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_message);
        init();
        this.RegisterBroadCast(broadCast);
        context=this;



    }

    private void init() {
        tv_boshu_BaseMessage = (TextView) this
                .findViewById(R.id.tv_boshu_BaseMssageEit);
        rl_boshu_head = (RelativeLayout) this.findViewById(R.id.rl_boshu_head);
        imageview = (CircleImageView) this.findViewById(R.id.circle_touxiang);
        afterImage = (ImageView) this.findViewById(R.id.img_boshu_afterhead);
        beforeImage = (ImageView) this.findViewById(R.id.img_boshu_beforehead);
        studentImage=(ImageView) this.findViewById(R.id.img_boshu_studentCard);

        TextView tv_boshu_editname = (TextView) this
                .findViewById(R.id.tv_boshu_editname);
        TextView tv_boshu_editsex = (TextView) this
                .findViewById(R.id.tv_boshu_editsex);
        TextView tv_boshu_editage = (TextView) this
                .findViewById(R.id.tv_boshu_editage);
        TextView tv_boshu_edithight = (TextView) this
                .findViewById(R.id.tv_boshu_edithight);
        TextView tv_boshu_editbody = (TextView) this
                .findViewById(R.id.tv_boshu_editbody);
        TextView tv_boshu_editphone = (TextView) this
                .findViewById(R.id.tv_boshu_editphone);
        TextView tv_boshu_editmail = (TextView) this
                .findViewById(R.id.tv_boshu_editmail);
        TextView tv_boshu_editscholl = (TextView) this
                .findViewById(R.id.tv_boshu_editschool);
        TextView tv_boshu_enrollment = (TextView) this
                .findViewById(R.id.tv_boshu_enrollment);
        TextView tv_boshu_editjob = (TextView) this
                .findViewById(R.id.tv_boshu_editjob);
        TextView tv_boshu_editreal = (TextView) this
                .findViewById(R.id.tv_boshu_real_name);
        TextView tv_boshu_editqq = (TextView) this
                .findViewById(R.id.tv_boshu_qq);
        TextView tv_boshu_editphone1 = (TextView) this
                .findViewById(R.id.tv_boshu_editphone1);
        TextView tv_boshu_editintention = (TextView) this
                .findViewById(R.id.tv_boshu_editintention);
        TextView tv_boshu_editresum = (TextView) this
                .findViewById(R.id.tv_boshu_editresum);
        TextView tv_boshu_editidnumber = (TextView) this
                .findViewById(R.id.tv_boshu_editidnumber);
        bt_boshu_indentButton = (Button) this
                .findViewById(R.id.bt_boshu_ident);
        bt_boshu_indentButton.setOnClickListener(this);
        tv_boshu_BaseMessage.setOnClickListener(this);
        rl_boshu_head.setOnClickListener(this);
        UserDao ud = new UserDao(this);
        User user = ud.find(Preferences.getUserName());
        ImageDowloader mImageDowloader = new ImageDowloader(this);
        fileCacheUtils=new FileCacheUtils(this);
        if (user != null) {
            tv_boshu_editname.setText(user.getNickName());
            tv_boshu_editsex.setText(user.getSex());
            tv_boshu_editage.setText(user.getAge().equals("0")?"":user.getAge());
            tv_boshu_edithight.setText(user.getHight().equals("") ? "" : user.getHight() + "cm");
            tv_boshu_editbody.setText(user.getFigure());
            tv_boshu_editidnumber.setText(user.getIdCard());
            tv_boshu_editintention.setText(user.getJobWant());
            tv_boshu_editjob.setText(user.getMajor());
            tv_boshu_editmail.setText(user.getMail());
            tv_boshu_editphone.setText(user.getMyPhone());
            tv_boshu_editphone1.setText(user.getFriendPhone());
            tv_boshu_editqq.setText(user.getqQ());
            tv_boshu_editreal.setText(user.getRealName());
            tv_boshu_editresum.setText(user.getResume());
            tv_boshu_editscholl.setText(user.getSchool());
            tv_boshu_enrollment.setText(user.getEntrance_year());
            String url = Model.PitureLoad + user.getHeadImage();
            String beforeUrl = Model.PitureLoad + user.getBeforeIdCard();
            String afterUrl = Model.PitureLoad + user.getAferIdCard();
            String studentUrl=Model.PitureLoad+user.getStudentImage();
            setNetBitmap(imageview, url, mImageDowloader);
            setNetBitmap(afterImage, afterUrl, mImageDowloader);
            setNetBitmap(beforeImage, beforeUrl, mImageDowloader);
            setNetBitmap(studentImage, studentUrl, mImageDowloader);
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            Uri uri = Uri.fromFile(file);
            String path1 = uri.toString();
            String pa = path1.split("sdcard")[1];
            String path = Environment.getExternalStorageDirectory().toString();
            pathImage = path + pa;
            Intent it = new Intent(this, ClipPictureActivity.class);
            it.putExtra("pathImage", pathImage);
            startActivityForResult(it, 199);
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_LOCAL) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    // sendPicByUri(selectedImage);
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        Cursor cursor = getContentResolver().query(uri,
                                new String[] { MediaStore.Images.Media.DATA },
                                null, null, null);
                        if (null == cursor) {
                            return;
                        }
                        cursor.moveToFirst();
                        pathImage = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        Intent it = new Intent(this, ClipPictureActivity.class);
                        it.putExtra("pathImage", pathImage);
                        startActivityForResult(it, 199);
                    }
                }
            }
        }
        if (resultCode == 200 && requestCode == 199) {
            
            try {

                final int[] progressFlag = {0};
                File file1 = new File(pathImage);
                byte[] bis = data.getByteArrayExtra("bitmap");
               final Bitmap bitmap = BitmapUtils.decodeSampleBitmapFromByteArray(bis, 80,
                        80);

                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("cookie", Preferences.getCookie());
                RequestParams params = new RequestParams();
                params.put("head_image", file1);
                params.put("sys", "user");
                params.put("ctrl", "user");
                params.put("action", "mod_all");
                client.post(Model.PathLoad, params,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onProgress(int bytesWritten, int totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                int progress=0;
                                if(progressFlag[0]!=100) {
                                    if (bytesWritten != totalSize && bytesWritten != 0) {
                                        progress = (int) (bytesWritten / (float) totalSize * 100);
                                        mProngressDialog.setProgress(progress);
                                    } else {
                                        progress = 100;
                                        progressFlag[0] = 100;
                                        mProngressDialog.setProgress(progress);

                                    }
                                }
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                mProngressDialog=new ProgressDialog(Activity_boshu_Message.this);
                                mProngressDialog.setMessage("正在上传图片...");
                                mProngressDialog.setIndeterminate(false);
                                mProngressDialog.setMax(100);
                                mProngressDialog.setProgress(0);
                                mProngressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProngressDialog.setCancelable(false);
                                mProngressDialog.show();
                            }

                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                    byte[] arg2) {
                                // TODO Auto-generated method stub
                                //通知头像更新
                               /* Intent it=new Intent("lifeisland.boshu.headimage");
                                byte[] bit=com.boshu.utils.BitmapUtils.getBitmapByte(bitmap);
                                it.putExtra("bitmap",bit);
                                Activity_boshu_Message.this.sendBroadcast(it);*/
                                imageview.setImageBitmap(bitmap);
                                Toast.makeText(Activity_boshu_Message.this,
                                        "头像上传成功~", Toast.LENGTH_SHORT).show();
                                String result = new String(arg2);
                                
                                Activity_boshu_Message.this.setJsonDb(result,bitmap);
                               /* CompressPicture.setSoftBitmap(bitmap);*///释放内存
                                mProngressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                    byte[] arg2, Throwable arg3) {
                                // TODO Auto-generated method stub
                                // String str = new String(arg2);
                                mProngressDialog.dismiss();
                                Toast.makeText(Activity_boshu_Message.this,"头像上传失败,呵呵~", Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                mProngressDialog.dismiss();
                Toast.makeText(Activity_boshu_Message.this,"未知错误，呵呵~",0).show();
            } finally {
               
            }
        }

    }

    public void back(View view) {
        this.finish();
    }

    // 点击事件
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stu
        Intent it = new Intent();
        switch (v.getId()) {
        case R.id.tv_boshu_BaseMssageEit:

            it.setClass(this, Activity_boshu_EditBaseMessage.class);
            it.putExtra("FLAG", 2);
            this.startActivity(it);
            break;
        case R.id.rl_boshu_head:
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = (int) (dm.widthPixels / 1.2);
            int height = (int) (dm.heightPixels / 2.8);
            setAlertDialog(this, ss, width, height);
            break;
        case R.id.bt_boshu_ident:
            this.postIndent();
            break;

        default:
            break;
        }

    }

    public void setAlertDialog(Context context, String[] ss, int width,
            int height) {
        Builder builder = new Builder(this);

        View view = this.getLayoutInflater().inflate(
                R.layout.alterdialog_boshu_tx, null);
        ListView lv = (ListView) view.findViewById(R.id.lv);

        Adapter_Boshu_alterDialog adapter = new Adapter_Boshu_alterDialog(this,
                ss);
        // BaseData2Adapter adapter = new BaseData2Adapter(context, ss);//
        // this,salaryarr,R.id.salary
        lv.setAdapter(adapter);
        builder.setView(view);
        alertDialog = builder.create();
        lv.setOnItemClickListener(this);
        WindowManager.LayoutParams params = alertDialog.getWindow()
                .getAttributes();
        alertDialog.show();
        params.x = 0;
        params.y = 0;
        params.width = width;//
        params.height = height;//
        alertDialog.getWindow().setAttributes(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        switch (parent.getId()) {
        case R.id.lv:
            if (id == 0) {
                selectPicFromLocal();
            }
            if (id == 1) {
                selectPicFromCamera();
            }
            break;
        }

    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            String st = getResources().getString(
                    R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 0);
        alertDialog.dismiss();

    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
        alertDialog.dismiss();
    }

    // 获取json数据
    public void setJsonDb(String result,Bitmap bitmap) {
        try {
            System.out.println(result+"------------");
            JSONObject obj = new JSONObject(result);
            User user = new User();
            JSONObject user_obj = obj.getJSONObject("user_info");
            String sex = user_obj.getString("sex");
            String nickanme = user_obj.getString("nick_name");
            String age = user_obj.getString("age");
            String hight = user_obj.getString("height");
            String figure = user_obj.getString("figure");
            String school = user_obj.getString("school");
            String major = user_obj.getString("major");
            String entrance_year = user_obj.getString("entrance_year");
            String real_name = user_obj.getString("real_name");
            String myPhone = user_obj.getString("mobile_phone");
            String friendPhone = user_obj.getString("agent_phone");
            String email = user_obj.getString("email");
            String qq = user_obj.getString("qq");
            String job_want = user_obj.getString("job_want");
            String resume = user_obj.getString("work_info");
            String idCard = user_obj.getString("cer_id");
            String afterIdCard = user_obj.getString("id_back_image");
            String beforeIdCard = user_obj.getString("id_image");
            String head_image = user_obj.getString("head_image");
            String student_image = user_obj.getString("prv_image");
            if (sex.equals("1")) {
                sex = "男";
            } else {
                sex = "女";
            }
            fileCacheUtils.addBitmapToFile((Model.PitureLoad+head_image).replaceAll("[^\\w]", ""), bitmap);
            BitMapLruCacheHelper.getInstance().addBitmapToMemCache((Model.PitureLoad+head_image).replaceAll("[^\\w]", ""),bitmap);
            UserDao ud = new UserDao(this);
            
            user.setUser(Preferences.getUserName(), nickanme, sex, age, hight,
                    figure, school, major, entrance_year, real_name, myPhone,
                    friendPhone, email, qq, job_want, resume, idCard,
                    beforeIdCard, afterIdCard, head_image,student_image);
            if (Preferences.getUserName() == null) {
                ud.addUser(user);
            } else {
                ud.update(user);
            }

        } catch (Exception e) {
            // TODO: handle exceptione
            e.printStackTrace();
        }

    }

    public void setNetBitmap(final ImageView img, String url,
            ImageDowloader mImageDowloader) {

        Bitmap bitmap = mImageDowloader.showCacheBitmap(url.replaceAll(
                "[^\\w]", ""));
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            img.setImageResource(R.drawable.default_add);
            mImageDowloader.downloadImage(80, 80, url,
                    new OnImageDownloadListener() {
                        @Override
                        public void onImageDownload(String url, Bitmap bitmap) {
                            // TODO Auto-generated method stub
                            img.setImageBitmap(bitmap);
                            if (bitmap == null) {
                                img.setImageResource(R.drawable.default_add);
                            }
                        }
                    });
        }
    }
    //注册广播
    public void RegisterBroadCast(headImageBroadCast broadCast){
        IntentFilter filter=new IntentFilter();
        filter.addAction("lifeisland.boshu.beforeidcardimage");
        filter.addAction("lifeisland.boshu.afteridcardimage");
        filter.addAction("lifeisland.boshu.studentidcardimage");
        this.registerReceiver(broadCast, filter);
    }
    public class headImageBroadCast extends BroadcastReceiver{
        
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            byte[] b=intent.getByteArrayExtra("bitmap");
            Bitmap bitmap=BitmapFactory.decodeByteArray(b, 0, b.length);
            if(intent.getAction().equals("lifeisland.boshu.beforeidcardimage")){
                beforeImage.setImageBitmap(bitmap);
            }
            if(intent.getAction().equals("lifeisland.boshu.afteridcardimage")){
                afterImage.setImageBitmap(bitmap);
            }
            if(intent.getAction().equals("lifeisland.boshu.studentidcardimage")){
                studentImage.setImageBitmap(bitmap);
               
            }
        }
       
        
    }
    public void postIndent(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.start();

        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           String flagString= response.getString("status");
                           if(flagString.equals("1")){
                               Toast.makeText(Activity_boshu_Message.this, "待审核", Toast.LENGTH_SHORT).show();
                               
                           }if(flagString.equals("0")){
                               Toast.makeText(Activity_boshu_Message.this, "已提交审核", Toast.LENGTH_SHORT).show();
                           }if(flagString.equals("2")){
                               Toast.makeText(Activity_boshu_Message.this, "审核已通过", Toast.LENGTH_SHORT).show();
                               
                           }
                           
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley error: " + error);

                    }
                }) {

            @Override
            protected void setParams(Map<String, String> params) {
                params.put("sys", "user");
                params.put("ctrl", "user_iner");
                params.put("action", "verify");
            }
        });
        
    }

}
