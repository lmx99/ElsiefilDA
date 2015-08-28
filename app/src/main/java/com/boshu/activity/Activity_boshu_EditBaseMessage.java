package com.boshu.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.adapter.Adapter_Boshu_alterDialog;
import com.boshu.adapter.ArrayWheelAdapter;
import com.boshu.crop.Crop;
import com.boshu.customview.OnWheelChangedListener;
import com.boshu.customview.WheelView;
import com.boshu.db.UserDao;
import com.boshu.domain.User;
import com.boshu.image.BitMapLruCacheHelper;
import com.boshu.image.BitmapUtils;
import com.boshu.image.FileCacheUtils;
import com.boshu.image.ImageDowloader;
import com.boshu.image.ImageDowloader.OnImageDownloadListener;
import com.boshu.utils.CompressPicture;
import com.boshu.utils.Model;
import com.boshu.utils.UserIndent;
import com.easemob.chatuidemo.activity.MainActivity;
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
import java.util.ArrayList;
import java.util.Map;

@SuppressLint("SdCardPath")
public class Activity_boshu_EditBaseMessage extends Activity implements
        OnClickListener, OnItemClickListener {
    String path = null;
    private static String strImgPath = null;
    private String TAG = "Activity_boshu_EditBaseMessage";
    private ScrollView sl;
    private Button bt_boshu_finish;
    private Button bt_boshu_indent;
    private String str_boshu_name;
    private String str_boshu_age;
    private String str_boshu_sex;
    private String str_boshu_hight;
    private String str_boshu_figure;
    private String str_boshu_school;
    private String str_boshu_major;
    private String str_boshu_entrance_year;
    private String str_boshu_realName;
    private String str_boshu_myPhone;
    private String str_boshu_friendPhone;
    private String str_boshu_mail;
    private String str_boshu_qQ;
    private String str_boshu_jobWant;
    private String str_boshu_resume;
    private String str_boshu_IdCard;
    private ImageView img_boshu_beforehead;
    private ImageView img_boshu_afterhead;
    private ImageView img_boshu_studentCard;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogCude;
    private static final String FILE_PATH = "/sdcard/lifeIsland.jpg";
    private String[] ss = new String[]{"相册", "拍照", "查看图片"};
    private String[] ssSchool = new String[]{"中山大学", "华南理工大学", "华南师范大学", "广东工业大学", "广东外语外贸大学", "广州中医药大学", "广东药学院", "广州大学", "广州美术学院", "星海音乐学院"};
    private String[] sexStrings = new String[]{"男", "女"};
    private String pathImage;
    private int FLAG;
    private int EDITFLAG;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CAMERA = 18;
    private FileCacheUtils filcaCacheUtils = null;
    private ImageDowloader mImageDowloader;
    private AlertDialog alertHDialog;
    private ProgressDialog dialog = null;
    private TextView tv_boshu_editname;
    private TextView tv_boshu_editsex;
    private TextView tv_boshu_editage;
    private TextView tv_boshu_edithight;
    private TextView tv_boshu_editbody;
    private TextView tv_boshu_editphone;
    private TextView tv_boshu_editmail;
    private TextView tv_boshu_editscholl;
    private TextView tv_boshu_enrollment;
    private TextView tv_boshu_editjob;
    private TextView tv_boshu_editreal;
    private TextView tv_boshu_editqq;
    private TextView tv_boshu_editphone1;
    private TextView tv_boshu_editintention;
    private TextView tv_boshu_editresum;
    private TextView tv_boshu_editidnumber;
    private View viewSelect;

    private LinearLayout ll_boshu_sexLayout;
    private LinearLayout ll_boshu_ageLayout;
    private LinearLayout ll_boshu_hightLayout;
    private LinearLayout ll_boshu_bodyLayout;
    private LinearLayout ll_boshu_etranceyearLayout;
    private ProgressDialog mProngressDialog = null;
    private ProgressDialog pd;
    private ImageView circl_touxiang;
    private RelativeLayout rl_boshu_head;
    private User user;
    InputMethodManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_editbasemessage);
        init();
        init2();
        initCach();
        UserDao ud = new UserDao(this);
        user = ud.find(Preferences.getUserName());
        if(user!=null){
            getUser(user);
        }else {
            user=new User();
        }

    }

    public void setSelect(int viewId, final String[] ss, Double widthFlag,
                          Double heightFlag) {
        Builder builder = new Builder(this);
        View view1 = this.getLayoutInflater().inflate(viewId, null);
        final WheelView hight = (WheelView) view1
                .findViewById(R.id.wheel_select);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int relWith=26*dm.widthPixels/480;
        hight.setTextSize(relWith);
        // country.getBackground().setAlpha(20);
        hight.setVisibleItems(5);
        hight.setCyclic(true);

        hight.setAdapter(new ArrayWheelAdapter<String>(ss));
        hight.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String string = hight.getAdapter().getItem(
                        hight.getCurrentItem());
                if (6 < ss.length && ss.length < 10) {
                    str_boshu_figure = string;
                }
                if (ss.length == 70) {
                    str_boshu_hight = string.substring(0, 3);
                }
                if (ss.length == 60) {
                    str_boshu_age = string;
                }
                if (ss.length == 20) {
                    str_boshu_entrance_year = string;
                }
            }
        });
        if (ss.length == 60) {
            hight.setCurrentItem(4);
        } else {
            hight.setCurrentItem(ss.length / 2);
        }
        builder.setView(view1);
        alertHDialog = builder.create();
        alertHDialog.show();
        DisplayMetrics dmm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dmm);
        int width = (int) (dmm.widthPixels / widthFlag);
        int height = (int) (dmm.heightPixels / heightFlag);
        WindowManager.LayoutParams params = alertHDialog.getWindow()
                .getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = width;//
        params.height = height;//
        alertHDialog.getWindow().setAttributes(params);
        Button yesButton = (Button) view1.findViewById(R.id.yes);
        Button noButton = (Button) view1.findViewById(R.id.no);
        yesButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (6 < ss.length && ss.length < 10) {
                   // tv_boshu_editbody.setText(str_boshu_figure);
                    user.setFigure(str_boshu_figure);
                }
                if (ss.length == 70) {
                   // tv_boshu_edithight.setText(str_boshu_hight + "cm");
                    user.setHight(str_boshu_hight);
                }
                if (ss.length == 60) {
                   // tv_boshu_editage.setText(str_boshu_age);
                    user.setAge(str_boshu_age);
                }
                if (ss.length == 20) {
                  //  tv_boshu_enrollment.setText(str_boshu_entrance_year);
                    user.setEntrance_year(str_boshu_entrance_year);
                }
                postMessage1(user);
                alertHDialog.dismiss();
            }
        });
        noButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertHDialog.dismiss();
            }
        });

    }

    public void initCach() {
        filcaCacheUtils = new FileCacheUtils(this);
        this.mImageDowloader = new ImageDowloader(this);
    }

    public void init2() {
        ll_boshu_sexLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_sex);
        ll_boshu_ageLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_age);
        ll_boshu_bodyLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_body);
        ll_boshu_hightLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_hight);


        ll_boshu_etranceyearLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_enrollmentNew);
        ll_boshu_ageLayout.setOnClickListener(this);
        ll_boshu_hightLayout.setOnClickListener(this);
        ll_boshu_etranceyearLayout.setOnClickListener(this);

        ll_boshu_sexLayout.setOnClickListener(this);
        ll_boshu_bodyLayout.setOnClickListener(this);
    }

    public void init() {
        EDITFLAG = this.getIntent().getIntExtra("FLAG", 0);
        circl_touxiang= (ImageView) this.findViewById(R.id.circle_touxiang);
        rl_boshu_head= (RelativeLayout) this.findViewById(R.id.rl_boshu_head);
        rl_boshu_head.setOnClickListener(this);
        sl = (ScrollView) this.findViewById(R.id.sl_boshu_editMessageNew);
        tv_boshu_editname = (TextView) this
                .findViewById(R.id.tv_boshu_editname);
        tv_boshu_editname.setOnClickListener(this);
        tv_boshu_editsex = (TextView) this.findViewById(R.id.tv_boshu_editsex);
        tv_boshu_editage = (TextView) this.findViewById(R.id.tv_boshu_editage);
        tv_boshu_edithight = (TextView) this
                .findViewById(R.id.tv_boshu_edithight);
        tv_boshu_editbody = (TextView) this
                .findViewById(R.id.tv_boshu_editbody);
        tv_boshu_editphone = (TextView) this
                .findViewById(R.id.tv_boshu_editphone);
        tv_boshu_editphone.setOnClickListener(this);
        tv_boshu_editmail = (TextView) this
                .findViewById(R.id.tv_boshu_editmail);
        tv_boshu_editmail.setOnClickListener(this);
        tv_boshu_editscholl = (TextView) this
                .findViewById(R.id.tv_boshu_editschool);
        tv_boshu_editscholl.setOnClickListener(this);
        tv_boshu_enrollment = (TextView) this
                .findViewById(R.id.tv_boshu_enrollment);
        tv_boshu_editjob = (TextView) this.findViewById(R.id.tv_boshu_editjob);
        tv_boshu_editjob.setOnClickListener(this);
        tv_boshu_editreal = (TextView) this
                .findViewById(R.id.tv_boshu_real_name);
        tv_boshu_editreal.setOnClickListener(this);
        tv_boshu_editqq = (TextView) this.findViewById(R.id.tv_boshu_qq);
        tv_boshu_editqq.setOnClickListener(this);
        tv_boshu_editphone1 = (TextView) this
                .findViewById(R.id.tv_boshu_editphone1);
        tv_boshu_editphone1.setOnClickListener(this);
        tv_boshu_editintention = (TextView) this
                .findViewById(R.id.tv_boshu_editintention);
        tv_boshu_editintention.setOnClickListener(this);
        tv_boshu_editresum = (TextView) this
                .findViewById(R.id.tv_boshu_editresum);
        tv_boshu_editresum.setOnClickListener(this);
        tv_boshu_editidnumber = (TextView) this
                .findViewById(R.id.tv_boshu_editidnumber);
        tv_boshu_editidnumber.setOnClickListener(this);

        bt_boshu_indent = (Button) this.findViewById(R.id.bt_boshu_ident);

        bt_boshu_finish = (Button) this.findViewById(R.id.bt_boshu_finish);
        if(EDITFLAG!=2) {
            bt_boshu_finish.setVisibility(View.VISIBLE);
            bt_boshu_indent.setVisibility(View.GONE);
        }
        bt_boshu_finish.setOnClickListener(this);

        img_boshu_beforehead = (ImageView) this
                .findViewById(R.id.img_boshu_beforehead);
        img_boshu_afterhead = (ImageView) this
                .findViewById(R.id.img_boshu_afterhead);
        img_boshu_studentCard = (ImageView) this
                .findViewById(R.id.img_boshu_studentCard);
        img_boshu_afterhead.setOnClickListener(this);
        img_boshu_beforehead.setOnClickListener(this);
        img_boshu_studentCard.setOnClickListener(this);
        bt_boshu_indent.setOnClickListener(this);

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
                                new String[]{MediaStore.Images.Media.DATA},
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
            upPicture(pathImage);
        }
    }

    //提示
    public void setToast(Context context, String text) {
        Toast.makeText(
                context,
                text, 0).show();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_boshu_editname:
                setView("昵称",1);
                break;
            case R.id.tv_boshu_editjob:
                setView("专业",2);
                break;
            case R.id.tv_boshu_real_name:
                setView("真实姓名",3);
                break;
            case R.id.tv_boshu_editphone:
                setView("手机号码",4);
                break;
            case R.id.tv_boshu_editphone1:
                setView("应急号码",5);
                break;
            case R.id.tv_boshu_editmail:
                setView("邮箱号码",6);
                break;
            case R.id.tv_boshu_qq:
                setView("qq号码",7);
                break;
            case R.id.tv_boshu_editintention:
                setView("就职意向",8);
                break;case R.id.tv_boshu_editresum:
                setView("工作经历",9);
                break;case R.id.tv_boshu_editidnumber:
                setView("身份证号码",10);
                break;


            case R.id.tv_boshu_editschool:
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = (int) (dm.widthPixels / 1.2);
                int height = (int) (dm.heightPixels / 1.5);
                setAlertDialog(this, ssSchool, width, height);
                break;
            case R.id.img_boshu_beforehead:
                FLAG = 1;
                setMoreDialog();
                break;
            case R.id.img_boshu_afterhead:
                FLAG = 2;
                setMoreDialog();
                break;
            case R.id.img_boshu_studentCard:
               // setView(this,getWindowManager(),getLayoutInflater(),"",9);
               setMoreDialog();
                FLAG = 3;
                break;
            case R.id.rl_boshu_head:
                setMoreDialog();
                FLAG=4;
                break;
            case R.id.bt_boshu_finish:
                Intent it=new Intent(this,MainActivity.class);
                this.startActivity(it);
                break;
            case R.id.ll_boshu_sex:
                DisplayMetrics dm1 = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm1);
                int width1 = (int) (dm1.widthPixels / 1.2);
                int height1 = (int) (dm1.heightPixels / 2.8);
                setAlertDialog(this, sexStrings, width1, height1);
                break;
            case R.id.ll_boshu_hight:
                int count = 70;
                final String[] countries = new String[count];
                for (int i = 0; i < count; i++) {
                    countries[i] = 135 + i + "cm";
                }
                this.setSelect(R.layout.item_boshu_select, countries, 1.2, 1.7);
                break;
            case R.id.ll_boshu_age:
                int age = 60;
                final String[] ages = new String[age];
                for (int i = 0; i < age; i++) {
                    ages[i] = 16 + i + "";
                }
                this.setSelect(R.layout.item_boshu_select, ages, 1.2, 1.7);
                break;
            case R.id.ll_boshu_enrollmentNew:
                int year = 20;
                final String[] years = new String[year];
                for (int i = 0; i < year; i++) {
                    years[i] = 2005 + i + "";
                }
                this.setSelect(R.layout.item_boshu_select, years, 1.2, 1.7);
                break;
            case R.id.ll_boshu_body:
                String[] bodyStrings = new String[]{"微胖", "偏瘦", "强壮", "正常", "丰满",
                        "苗条", "其他"};

                this.setSelect(R.layout.item_boshu_select, bodyStrings, 1.2, 1.7);
                break;
            case R.id.bt_boshu_ident:
               if( !UserIndent.getuserIndent(user)) {
                   Toast.makeText(this, "请完善全部信息，么么哒!", 0).show();
                   return;
               }else {
                   postIndent();
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
            Toast.makeText(getApplicationContext(), st, 0).show();
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
        if(FLAG!=4) {
            sl.fullScroll(ScrollView.FOCUS_DOWN);
        }
        alertDialog.dismiss();
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
        alertDialog.show();
        WindowManager.LayoutParams params = alertDialog.getWindow()
                .getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = width;//
        params.height = height;//
        alertDialog.getWindow().setAttributes(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        TextView tv = (TextView) view.findViewById(R.id.tv);
        String flagString = tv.getText().toString();
        if (flagString.equals("男") || flagString.equals("女")) {
            String sexString = null;
            if (id == 0) {
                str_boshu_sex = "1";
                sexString = "男";
            }
            if (id == 1) {
                str_boshu_sex = "0";
                sexString = "女";
            }
            alertDialog.dismiss();
            user.setSex(sexString);
            postMessage1(user);
            //tv_boshu_editsex.setText(sexString);
        } else {
            if (flagString.equals("相册") || flagString.equals("拍照") || flagString.equals("查看图片")) {
                switch (parent.getId()) {
                    case R.id.lv:
                        if (id == 0) {
                            selectPicFromLocal();
                        }
                        if (id == 1) {
                            selectPicFromCamera();
                        }
                        if (id == 2) {
                            ArrayList<String> list = new ArrayList<String>();
                            Intent it = new Intent(this, ActivityPictureShow.class);
                            if(FLAG==4){
                                it.putExtra("position", 0);
                                list.add(Model.PitureLoad+user.getHeadImage());
                            }else {
                                list.add(Model.PitureLoad + user.getStudentImage());
                                list.add(Model.PitureLoad + user.getBeforeIdCard());
                                list.add(Model.PitureLoad + user.getAferIdCard());
                                it.putExtra("position", changInt(FLAG) - 1);
                            }
                            it.putStringArrayListExtra("list", list);
                            this.startActivity(it);
                            alertDialog.dismiss();
                        }
                        break;
                }

            } else {
                String schoolString = tv.getText().toString();
               // tv_boshu_editscholl.setText(schoolString);
                user.setSchool(schoolString);
                alertDialog.dismiss();
                postMessage1(user);
            }

        }
    }

    public void setMoreDialog() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / 1.2);
        int height = (int) (dm.heightPixels / 2.5);
        setAlertDialog(this, ss, width, height);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
       // setCute(this, getWindowManager(), getLayoutInflater(), "是否要保存个人信息？");
        if(EDITFLAG==1) {
            Intent it = new Intent(this, MainActivity.class);
            this.startActivity(it);
        }else {
            this.finish();
        }


    }

    public void back(View view) {
        if(EDITFLAG==1) {
            Intent it = new Intent(this, MainActivity.class);
            this.startActivity(it);
        }else {
            this.finish();
        }

    }
  public void setText(EditText editText,int type,User user){
      if(type==1){

          editText.setText(user.getNickName());
          if(user.getRealName()!=null)
          editText.setSelection(user.getNickName().length());
      }
      if(type==2){
          editText.setText(user.getMajor());
          if(user.getRealName()!=null)
          editText.setSelection(user.getMajor().length());
      }if(type==3){
          System.out.println(user.getRealName() + "--------");

          editText.setText(user.getRealName());
          if(user.getRealName()!=null)
          editText.setSelection(user.getRealName().length());
      }if(type==4){
          editText.setText(user.getMyPhone());
          if(user.getRealName()!=null)
          editText.setSelection(user.getMyPhone().length());
      }if(type==5){
          editText.setText(user.getFriendPhone());
          if(user.getRealName()!=null)
          editText.setSelection(user.getFriendPhone().length());
      }if(type==6){
          editText.setText(user.getMail());
          if(user.getRealName()!=null)
          editText.setSelection(user.getMail().length());
      }if(type==7){
          editText.setText(user.getqQ());
          if(user.getRealName()!=null)
          editText.setSelection(user.getqQ().length());
      }if(type==8){
          editText.setText(user.getJobWant());
          if(user.getRealName()!=null)
          editText.setSelection(user.getJobWant().length());
      }if(type==9){
          editText.setText(user.getResume());
          if(user.getRealName()!=null)
          editText.setSelection(user.getResume().length());
      }if(type==10) {
          editText.setText(user.getIdCard());
          if(user.getRealName()!=null)
          editText.setSelection(user.getIdCard().length());
      }
     // postMessage1();

  }
    //
    public void postMessage1(final User user) {
        pd = new ProgressDialog(this);
        pd.setMessage("加载中~");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.start();

        Request request = new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Activity_boshu_EditBaseMessage.this,"资料提交成功",0).show();
                setJsonDb(response);
                if (Activity_boshu_Message.context != null) {
                    Activity_boshu_Message.context.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_boshu_EditBaseMessage.this, "网络出错", 0).show();
                System.out.println("网络出错的原因"+error.toString());
                pd.dismiss();


            }
        }) {
            @Override
            protected void setParams(Map<String, String> params) {
                if (str_boshu_sex != null) {

                    params.put("sex", str_boshu_sex);
                }
                setViewString(params, "nick_name",user.getNickName());
                setViewString(params, "email", user.getMail());
                setViewString(params, "school", user.getSchool());
                setViewString(params, "entrance_year",user.getEntrance_year());
                setViewString(params, "major", user.getMajor());
                setViewString(params, "age", user.getAge());
                setViewString(params, "height",user.getHight());
                setViewString(params, "figure", user.getFigure());
                setViewString(params, "qq",user.getqQ());
                setViewString(params, "real_name", user.getRealName());
                setViewString(params, "mobile_phone", user.getMyPhone());
                setViewString(params, "agent_phone", user.getFriendPhone());
                setViewString(params, "job_want", user.getJobWant());
                setViewString(params, "cer_id", user.getIdCard());
                setViewString(params, "work_info", user.getResume());
                params.put("sys", "user");
                params.put("ctrl", "user");
                params.put("action", "mod_all");
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(request);
    }

    public void setViewString(Map<String, String> params, String key,
                              String value) {
        if (value != null) {
            params.put(key, value);
        }
    }

    public void setJsonDb(JSONObject obj) {
        try {
            System.out.println("返回的json值"+obj);
            String accountString = obj.getJSONObject("account").getString("status");
            String recruitString = obj.getJSONObject("recruit").getString("status");
            String resumeString = obj.getJSONObject("resume").getString("status");
            String basicString = obj.getJSONObject("recruit").getString("status");

                JSONObject user_obj = obj.getJSONObject("user_info");

                String sex = user_obj.getString("sex");
                if (sex.equals("1")) {
                    sex = "男";
                } else {
                    sex = "女";
                }

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
                user.setUser(Preferences.getUserName(), nickanme, sex, age, hight,
                        figure, school, major, entrance_year, real_name, myPhone,
                        friendPhone, email, qq, job_want, resume, idCard,
                        beforeIdCard, afterIdCard, head_image, student_image);
                UserDao ud = new UserDao(this);
                ud.addUser(user);
                User user1 = ud.find(Preferences.getUserName());
                if (user1 != null) {
                    ud.update(user);
                } else {
                    ud.addUser(user);
                }
                getUser(user);
                System.out.println(user1.getUserName() + "-----------0000000"
                        + user1.getNickName() + "---" + user1.getAferIdCard()
                        + user1.getAge() + user1.getBeforeIdCard()
                        + user1.getEntrance_year() + user1.getFigure()
                        + user1.getHeadImage() + user1.getHight()
                        + user1.getJobWant() + user1.getqQ());


            pd.dismiss();
         //   FinishBack();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            Toast.makeText(this, "网络出错，请稍后再试", 0).show();
        }
    }

    public void getUser(User user) {
            tv_boshu_editreal.setText(user.getRealName());
            tv_boshu_editname.setText(user.getNickName());
            tv_boshu_editjob.setText(user.getMajor());
            tv_boshu_editscholl.setText(user.getSchool());
            tv_boshu_editage.setText(user.getAge().equals("0") ? "" : user.getAge());
            tv_boshu_enrollment.setText(user.getEntrance_year());
            tv_boshu_editbody.setText(user.getFigure());
            tv_boshu_editphone.setText(user.getMyPhone());
            tv_boshu_editphone1.setText(user.getFriendPhone());
            tv_boshu_editmail.setText(user.getMail());
            tv_boshu_editqq.setText(user.getqQ());
            tv_boshu_editintention.setText(user.getJobWant());
            tv_boshu_editresum.setText(user.getResume());
            tv_boshu_editidnumber.setText(user.getIdCard());
            String sexString = user.getSex();
            tv_boshu_editsex.setText(user.getSex());
            tv_boshu_edithight.setText(user.getHight().equals("") ? "" : user.getHight() + "cm");
            String headUrl=Model.PitureLoad+user.getHeadImage();
            String afterUrl = Model.PitureLoad + user.getAferIdCard();
            String beforeUrl = (Model.PitureLoad + user.getBeforeIdCard());
            String studentUrl = (Model.PitureLoad + user.getStudentImage());
            Bitmap afterBitmap = mImageDowloader.showCacheBitmap(afterUrl
                    .replaceAll("[^\\w]", ""));
            Bitmap beforeBitmap = mImageDowloader.showCacheBitmap(beforeUrl
                    .replaceAll("[^\\w]", ""));
            Bitmap studentBitmap = mImageDowloader.showCacheBitmap(studentUrl
                    .replaceAll("[^\\w]", ""));
            Bitmap headBitmap=mImageDowloader.showCacheBitmap(headUrl.replaceAll("[^\\w]", ""));
            setNetBitmap(img_boshu_afterhead, afterBitmap, afterUrl);
            setNetBitmap(img_boshu_beforehead, beforeBitmap, beforeUrl);
            setNetBitmap(img_boshu_studentCard, studentBitmap, studentUrl);
            setNetBitmap(circl_touxiang,headBitmap,headUrl);
        }



    public void setNetBitmap(final ImageView img, Bitmap bitmap, String url) {

        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            img.setImageResource(R.drawable.default_add);
            mImageDowloader.downloadImage(200, 200, url,
                    new OnImageDownloadListener() {
                        @Override
                        public void onImageDownload(String url, Bitmap bitmap) {
                            // TODO Auto-generated method stub
                            if (bitmap == null) {
                                img.setImageResource(R.drawable.default_add);
                            } else {
                                img.setImageBitmap(bitmap);
                            }

                        }
                    });
        }
    }

    public void FinishBack() {
        if (EDITFLAG == 1) {
            Intent intent = new Intent(Activity_boshu_EditBaseMessage.this,
                    MainActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(Activity_boshu_EditBaseMessage.this,
                    Activity_boshu_Message.class);
            startActivity(intent);
        }
        this.finish();
    }

    public void setCute(Context context, WindowManager window, LayoutInflater inflater, String cudeString) {
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / 1.2);
        int height = (int) (dm.heightPixels / 2.8);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = inflater.inflate(
                R.layout.alterdialog_boshu_update, null);
        Button no = (Button) view.findViewById(R.id.bt_boshu_updateno);
        Button yes = (Button) view.findViewById(R.id.bt_boshu_updateyes);
        TextView tv = (TextView) view.findViewById(R.id.tv_boshu_cude);
        tv.setText(cudeString);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPost();
                alertDialogCude.dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDITFLAG == 2) {
                    finish();
                } else {
                    Intent intent = new Intent(Activity_boshu_EditBaseMessage.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                alertDialogCude.dismiss();

            }
        });
        builder.setView(view);
        alertDialogCude = builder.create();
        alertDialogCude.setCancelable(false);
        alertDialogCude.show();
        WindowManager.LayoutParams params = alertDialogCude.getWindow()
                .getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = width;//
        params.height = height;//
        alertDialogCude.getWindow().setAttributes(params);

    }

    public void finishPost() {
       str_boshu_name = tv_boshu_editname.getText().toString();
        str_boshu_realName = tv_boshu_editreal.getText().toString();
        str_boshu_school = tv_boshu_editscholl.getText().toString();
        str_boshu_myPhone = tv_boshu_editphone.getText().toString();
        str_boshu_mail = tv_boshu_editmail.getText().toString();
        str_boshu_major = tv_boshu_editjob.getText().toString();
        str_boshu_qQ = tv_boshu_editqq.getText().toString();
        str_boshu_friendPhone = tv_boshu_editphone.getText().toString();
        str_boshu_jobWant = tv_boshu_editintention.getText().toString();
        str_boshu_resume = tv_boshu_editresum.getText().toString();
        str_boshu_IdCard = tv_boshu_editidnumber.getText().toString();

        postMessage1(user);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void beginCrop(Uri source) {


        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        System.out.println(source + "剪切路径:" + destination);
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            System.out.println("回来路径：" + uri.toString());
            try {
                img_boshu_studentCard.setImageURI(uri);
                System.out.println(uri.toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Bitmap newbitmap = BitmapUtils.decodeSampleBitmapFromBitmap(bitmap);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                    //  System.gc();
                }
                String bitmapPath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/" + System.currentTimeMillis() + ".JPG";
                byte[] bytes = com.boshu.utils.BitmapUtils.getBitmapByte(newbitmap);
                com.boshu.utils.BitmapUtils.write(bytes, bitmapPath);
                upPicture(bitmapPath);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "图片出错", 0).show();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void upPicture(String pathImage) {
        Bitmap bitmap = null;
        if(FLAG!=4)
        sl.fullScroll(ScrollView.FOCUS_DOWN);
        try {
            File file1 = new File(pathImage);
            final Bitmap bt = BitmapUtils.decodeSampledBitmapFromSDCard(pathImage, 200, 200);
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("cookie", Preferences.getCookie());
            RequestParams params = new RequestParams();
            params.put("sys", "user");
            params.put("ctrl", "user");

            if (FLAG == 2) {
                params.put("id_back_image", file1);
            }
            if (FLAG == 1) {
                params.put("id_image", file1);

            }
            if (FLAG == 3) {
                params.put("prv_image", file1);

            }
            if(FLAG==4){
                params.put("head_image",file1);
            }
            final int[] progressFlag = {0};
            params.put("action", "mod_all");
            client.post(Model.PathLoad, params,
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onProgress(int bytesWritten, int totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            int progress = 0;
                            if (progressFlag[0] != 100) {
                                if (bytesWritten != totalSize && bytesWritten != 0) {
                                    progress = (int) (bytesWritten / (float) totalSize * 100);
                                    mProngressDialog.setProgress(progress);
                                    Log.e(TAG, bytesWritten + "/" + totalSize + progress);
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
                            mProngressDialog = new ProgressDialog(Activity_boshu_EditBaseMessage.this);
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
                            mProngressDialog.dismiss();
                            UserDao ud = new UserDao(
                                    Activity_boshu_EditBaseMessage.this);

                            String result = new String(arg2);
                            System.out.println(result);
                            try {
                                JSONObject obj = new JSONObject(new String(
                                        arg2));
                                // {"account":{"status":"2"},"basic":{"status":"1"},"resume":{"err":"不允许的文件后缀","status":"3"},"recruit":{"status":"1"},"status":"0"}
                                String erroString = obj.getJSONObject("resume").getString("status");
                                if (erroString.equals("3")) {
                                    Toast.makeText(Activity_boshu_EditBaseMessage.this, obj.getJSONObject("resume").getString("err"), 0).show();
                                }
                                if(FLAG==4){
                                    String aHeadImage=obj.getJSONObject("user_info").getString("head_image");
                                    user.setHeadImage(aHeadImage);
                                    if (user != null) {
                                        user.setHeadImage(aHeadImage);
                                        ud.update(user);
                                    } else {
                                        user = new User();
                                        user.setHeadImage(aHeadImage);
                                        ud.addUser(user);
                                    }
                                    circl_touxiang.setImageBitmap(bt);
                                    if (user != null) {
                                        user.setHeadImage(aHeadImage);
                                        String aImage = Model.PitureLoad
                                                + aHeadImage;
                                        final String afterUrl = aImage
                                                .replaceAll("[^\\w]", "");
                                        filcaCacheUtils.addBitmapToFile(
                                                afterUrl, bt);
                                        BitMapLruCacheHelper.getInstance()
                                                .addBitmapToMemCache(
                                                        afterUrl, bt);
                                        Activity_boshu_EditBaseMessage.this.setToast(Activity_boshu_EditBaseMessage.this, "头像上传成功！");
                                    }

                                }
                                if (FLAG == 2) {
                                    String afterImage = null;
                                    String aImage = obj.getJSONObject(
                                            "user_info").getString(
                                            "id_back_image");
                                    System.out.println("身份证照背照：" + aImage);
                                    user.setAferIdCard(aImage);
                                    if (user != null) {
                                        user.setAferIdCard(aImage);
                                        ud.update(user);
                                    } else {
                                        user = new User();
                                        user.setAferIdCard(afterImage);
                                        ud.addUser(user);
                                    }
                                    img_boshu_afterhead.setImageBitmap(bt);
                                    if (user != null) {
                                        user.setAferIdCard(aImage);
                                        afterImage = Model.PitureLoad
                                                + aImage;
                                        final String afterUrl = afterImage
                                                .replaceAll("[^\\w]", "");
                                        filcaCacheUtils.addBitmapToFile(
                                                afterUrl, bt);
                                        BitMapLruCacheHelper.getInstance()
                                                .addBitmapToMemCache(
                                                        afterUrl, bt);
                                        Intent it = new Intent(
                                                "lifeisland.boshu.afteridcardimage");
                                        byte[] bitmap = com.boshu.utils.BitmapUtils
                                                .getBitmapByte(bt);
                                        it.putExtra("bitmap", bitmap);
                                        Activity_boshu_EditBaseMessage.this
                                                .sendBroadcast(it);
                                        Activity_boshu_EditBaseMessage.this.setToast(Activity_boshu_EditBaseMessage.this, "身份证背面照上传成功！");
                                    }

                                }
                                if (FLAG == 1) {
                                    String bImage = obj.getJSONObject(
                                            "user_info").getString(
                                            "id_image");
                                    System.out.println("身份证照前照：" + bImage);
                                    user.setBeforeIdCard(bImage);
                                    if (user != null) {
                                        ud.update(user);
                                    } else {
                                        user = new User();
                                        user.setBeforeIdCard(bImage);
                                        ud.addUser(user);
                                    }
                                    String beforeImage = null;
                                    img_boshu_beforehead.setImageBitmap(bt);
                                    if (user != null) {
                                        user.setBeforeIdCard(bImage);
                                        beforeImage = Model.PitureLoad
                                                + bImage;
                                        final String beforUrl = beforeImage
                                                .replaceAll("[^\\w]", "");
                                        filcaCacheUtils.addBitmapToFile(
                                                beforeImage, bt);
                                        BitMapLruCacheHelper.getInstance()
                                                .addBitmapToMemCache(
                                                        beforeImage, bt);
                                        Intent it = new Intent(
                                                "lifeisland.boshu.beforeidcardimage");
                                        byte[] bitmap = com.boshu.utils.BitmapUtils
                                                .getBitmapByte(bt);
                                        it.putExtra("bitmap", bitmap);
                                        Activity_boshu_EditBaseMessage.this
                                                .sendBroadcast(it);
                                        Activity_boshu_EditBaseMessage.this.setToast(Activity_boshu_EditBaseMessage.this, "身份证正面照上传成功！");
                                    }
                                }
                                if (FLAG == 3) {
                                    String studentImage = null;
                                    String sImage = obj.getJSONObject(
                                            "user_info").getString(
                                            "prv_image");
                                    user.setStudentImage(sImage);
                                    img_boshu_studentCard
                                            .setImageBitmap(bt);
                                    if (user != null) {
                                        user.setStudentImage(sImage);
                                        ud.update(user);
                                    } else {
                                        user = new User();
                                        user.setStudentImage(sImage);
                                        ud.addUser(user);
                                    }
                                    if (user != null) {

                                        studentImage = Model.PitureLoad
                                                + sImage;
                                        final String studentUrl = studentImage
                                                .replaceAll("[^\\w]", "");
                                        filcaCacheUtils.addBitmapToFile(
                                                studentUrl, bt);
                                        BitMapLruCacheHelper.getInstance()
                                                .addBitmapToMemCache(
                                                        studentImage, bt);
                                        Intent it = new Intent(
                                                "lifeisland.boshu.studentidcardimage");
                                        byte[] bitmap = com.boshu.utils.BitmapUtils
                                                .getBitmapByte(bt);
                                        it.putExtra("bitmap", bitmap);
                                        Activity_boshu_EditBaseMessage.this
                                                .sendBroadcast(it);
                                        Activity_boshu_EditBaseMessage.this.setToast(Activity_boshu_EditBaseMessage.this, "学生证照上传成功！");
                                    }
                                }


                                String str = new String(arg2);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Log.e(TAG, e.toString());
                                e.printStackTrace();
                                Toast.makeText(Activity_boshu_EditBaseMessage.this, "服务器出错了，图片上传失败！", Toast.LENGTH_SHORT).show();
                                mProngressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(int arg0, Header[] arg1,
                                              byte[] arg2, Throwable arg3) {
                            // TODO Auto-generated method stub
                            if (arg2 != null) {
                                String str = new String(arg2);
                                System.out.println(str);
                                Log.e(TAG, "图片上传失败，失败原因：" + str);
                            }
                            mProngressDialog.dismiss();
                            Toast.makeText(Activity_boshu_EditBaseMessage.this, "图片上传失败!", Toast.LENGTH_SHORT).show();

                        }
                    });

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            CompressPicture.setSoftBitmap(bitmap);
        }


    }

    public int changInt(int position) {
        if (position == 3)
            return 1;
        if (position == 1)
            return 2;
        if (position == 2)
            return 3;
        return 0;

    }
    public void setView( String cudeString,final  int type) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / 1.1);
        int height = (int) (dm.heightPixels /2.8);
        int paramsWith=0;
        int paramsHeight=(int) (dm.widthPixels / 9);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(
                R.layout.alterdialog_select, null);
        TextView tv_Title=(TextView)view.findViewById(R.id.show_realname);
        tv_Title.setText(cudeString);
        final EditText editText= (EditText) view.findViewById(R.id.et_entry);
        setText(editText,type,user);
        Button no = (Button) view.findViewById(R.id.bt_boshu_updateno);
        Button yes = (Button) view.findViewById(R.id.bt_boshu_updateyes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCude.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCude.dismiss();
                try {
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(Activity_boshu_EditBaseMessage.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){

                }
                        String userEntry = editText.getText().toString();
                if (type == 1) {
                    user.setNickName(userEntry);
                }
                if (type == 2) {
                    user.setMajor(userEntry);
                }
                if (type == 3) {
                    user.setRealName(userEntry);
                }
                if (type == 4) {
                    user.setMyPhone(userEntry);
                }
                if (type == 5) {
                    user.setFriendPhone(userEntry);
                }
                if (type == 6) {
                    user.setMail(userEntry);

                }
                if (type == 7) {
                    user.setqQ(userEntry);
                }
                if (type == 8) {
                    user.setJobWant(userEntry);
                }
                if (type == 9) {
                    user.setResume(userEntry);
                }
                if (type == 10) {
                    user.setIdCard(userEntry);
                }
                postMessage1(user);

            }
        });
        builder.setView(view);
        alertDialogCude = builder.create();
        alertDialogCude.setCancelable(false);
        alertDialogCude.show();
        WindowManager.LayoutParams params = alertDialogCude.getWindow()
                .getAttributes();
        params.x = paramsWith;
        params.y = paramsHeight;
        params.width = width;//
        params.height = height;//
        alertDialogCude.getWindow().setAttributes(params);
        editText.post(new Runnable() {
            @Override
            public void run() {
                im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                im.showSoftInput(editText, 0);
            }
        });
        //alertDialogCude.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }
    public void postIndent(){
        pd = new ProgressDialog(this);
        pd.setMessage("正在加载中~");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String flagString= response.getString("status");
                    if(flagString.equals("1")){
                        Toast.makeText(Activity_boshu_EditBaseMessage.this, "待审核", Toast.LENGTH_SHORT).show();

                    }if(flagString.equals("0")){
                        Toast.makeText(Activity_boshu_EditBaseMessage.this, "已提交审核", Toast.LENGTH_SHORT).show();
                    }if(flagString.equals("2")){
                        Toast.makeText(Activity_boshu_EditBaseMessage.this, "审核已通过", Toast.LENGTH_SHORT).show();

                    }
                    pd.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(Activity_boshu_EditBaseMessage.this, "服务器出错，请联系工作人员", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error: " + error);
                Toast.makeText(Activity_boshu_EditBaseMessage.this, "网络出错啦，么么哒！", Toast.LENGTH_SHORT).show();


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
    public void setApplication(){
    }
}
