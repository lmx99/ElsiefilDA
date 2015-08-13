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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.adapter.Adapter_Boshu_alterDialog;
import com.boshu.adapter.ArrayWheelAdapter;
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
import java.util.Map;

@SuppressLint("SdCardPath")
public class Activity_boshu_EditBaseMessage extends Activity implements
        OnClickListener, OnItemClickListener {
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
    private static final String FILE_PATH = "/sdcard/lifeIsland.jpg";
    private String[] ss = new String[]{"相册", "拍照"};
    private String[] sexStrings = new String[]{"男", "女"};
    private String pathImage;
    private int FLAG;
    private int EDITFLAG;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CAMERA = 18;
    private FileCacheUtils filcaCacheUtils = null;
    private ImageDowloader mImageDowloader;
    private AlertDialog alertHDialog;

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

    private LinearLayout ll_boshu_nickNameLayout;
    private LinearLayout ll_boshu_sexLayout;
    private LinearLayout ll_boshu_ageLayout;
    private LinearLayout ll_boshu_hightLayout;
    private LinearLayout ll_boshu_bodyLayout;
    private LinearLayout ll_boshu_schoolLayout;
    private LinearLayout ll_boshu_etranceyearLayout;
    private LinearLayout ll_boshu_jobLayout;

    private EditText nickNamEditText;
    private EditText schoolEditText;
    private EditText jobEditText;
    private EditText realNamEditText;
    private EditText myphoneEditText;
    private EditText friendPhoneEditText;
    private EditText emailEditText;
    private EditText qqEditText;
    private EditText intentEditText;
    private EditText resumEditText;
    private EditText idCardEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_editbasemessage);
        init();
        init2();
        initCach();
        getUser();

    }

    public void setSelect(int viewId, final String[] ss, Double widthFlag,
                          Double heightFlag) {
        Builder builder = new Builder(this);
        View view1 = this.getLayoutInflater().inflate(viewId, null);
        final WheelView hight = (WheelView) view1
                .findViewById(R.id.wheel_select);

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
                    System.out.println(string);
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
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / widthFlag);
        int height = (int) (dm.heightPixels / heightFlag);
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
                    tv_boshu_editbody.setText(str_boshu_figure);
                }
                if (ss.length == 70) {
                    tv_boshu_edithight.setText(str_boshu_hight + "cm");
                }
                if (ss.length == 60) {
                    tv_boshu_editage.setText(str_boshu_age);
                }
                if (ss.length == 20) {
                    tv_boshu_enrollment.setText(str_boshu_entrance_year);
                }
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
        ll_boshu_nickNameLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_nickname);
        ll_boshu_schoolLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_school);
        ll_boshu_etranceyearLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_enrollmentNew);
        ll_boshu_jobLayout = (LinearLayout) this
                .findViewById(R.id.ll_boshu_job);
        ll_boshu_ageLayout.setOnClickListener(this);
        ll_boshu_hightLayout.setOnClickListener(this);
        ll_boshu_nickNameLayout.setOnClickListener(this);
        ll_boshu_schoolLayout.setOnClickListener(this);
        ll_boshu_etranceyearLayout.setOnClickListener(this);
        ll_boshu_jobLayout.setOnClickListener(this);
        ll_boshu_sexLayout.setOnClickListener(this);
        ll_boshu_bodyLayout.setOnClickListener(this);
    }

    public void init() {
        myphoneEditText = (EditText) this.findViewById(R.id.et_boshu_phone);
        myphoneEditText.setVisibility(View.VISIBLE);
        friendPhoneEditText = (EditText) this
                .findViewById(R.id.et_boshu_friendPhone);
        friendPhoneEditText.setVisibility(View.VISIBLE);
        qqEditText = (EditText) this.findViewById(R.id.et_boshu_qq);
        qqEditText.setVisibility(View.VISIBLE);
        intentEditText = (EditText) this.findViewById(R.id.et_boshu_intention);
        intentEditText.setVisibility(View.VISIBLE);
        emailEditText = (EditText) this.findViewById(R.id.et_boshu_email);
        emailEditText.setVisibility(View.VISIBLE);
        idCardEditText = (EditText) this.findViewById(R.id.et_boshu_idCard);
        idCardEditText.setVisibility(View.VISIBLE);
        resumEditText = (EditText) this.findViewById(R.id.et_boshu_resum);
        resumEditText.setVisibility(View.VISIBLE);
        nickNamEditText = (EditText) this
                .findViewById(R.id.et_boshu_editnickName);
        nickNamEditText.setVisibility(View.VISIBLE);
        schoolEditText = (EditText) this.findViewById(R.id.et_boshu_school);
        schoolEditText.setVisibility(View.VISIBLE);
        jobEditText = (EditText) this.findViewById(R.id.et_boshu_job);
        jobEditText.setVisibility(View.VISIBLE);
        realNamEditText = (EditText) this.findViewById(R.id.et_boshu_realName);
        realNamEditText.setVisibility(View.VISIBLE);
        sl = (ScrollView) this.findViewById(R.id.sl_boshu_editMessageNew);
        tv_boshu_editname = (TextView) this
                .findViewById(R.id.tv_boshu_editname);
        tv_boshu_editname.setVisibility(View.GONE);
        tv_boshu_editsex = (TextView) this.findViewById(R.id.tv_boshu_editsex);

        tv_boshu_editage = (TextView) this.findViewById(R.id.tv_boshu_editage);
        tv_boshu_edithight = (TextView) this
                .findViewById(R.id.tv_boshu_edithight);
        tv_boshu_editbody = (TextView) this
                .findViewById(R.id.tv_boshu_editbody);
        tv_boshu_editphone = (TextView) this
                .findViewById(R.id.tv_boshu_editphone);
        tv_boshu_editphone.setVisibility(View.GONE);
        tv_boshu_editmail = (TextView) this
                .findViewById(R.id.tv_boshu_editmail);
        tv_boshu_editmail.setVisibility(View.GONE);
        tv_boshu_editscholl = (TextView) this
                .findViewById(R.id.tv_boshu_editschool);
        tv_boshu_editscholl.setVisibility(View.GONE);
        tv_boshu_enrollment = (TextView) this
                .findViewById(R.id.tv_boshu_enrollment);
        tv_boshu_editjob = (TextView) this.findViewById(R.id.tv_boshu_editjob);
        tv_boshu_editjob.setVisibility(View.GONE);
        tv_boshu_editreal = (TextView) this
                .findViewById(R.id.tv_boshu_real_name);
        tv_boshu_editreal.setVisibility(View.GONE);
        tv_boshu_editqq = (TextView) this.findViewById(R.id.tv_boshu_qq);
        tv_boshu_editqq.setVisibility(View.GONE);
        tv_boshu_editphone1 = (TextView) this
                .findViewById(R.id.tv_boshu_editphone1);
        tv_boshu_editphone1.setVisibility(View.GONE);
        tv_boshu_editintention = (TextView) this
                .findViewById(R.id.tv_boshu_editintention);
        tv_boshu_editintention.setVisibility(View.GONE);
        tv_boshu_editresum = (TextView) this
                .findViewById(R.id.tv_boshu_editresum);
        tv_boshu_editresum.setVisibility(View.GONE);
        tv_boshu_editidnumber = (TextView) this
                .findViewById(R.id.tv_boshu_editidnumber);
        tv_boshu_editidnumber.setVisibility(View.GONE);

        bt_boshu_indent = (Button) this.findViewById(R.id.bt_boshu_ident);
        bt_boshu_indent.setVisibility(View.GONE);

        bt_boshu_finish = (Button) this.findViewById(R.id.bt_boshu_finish);
        bt_boshu_finish.setVisibility(View.VISIBLE);
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
            Bitmap bitmap = null;
            final ProgressDialog dialog = new ProgressDialog(this);
            sl.fullScroll(ScrollView.FOCUS_DOWN);
            try {
                File file1 = new File(pathImage);
                byte[] bis = data.getByteArrayExtra("bitmap");
                final Bitmap bt = BitmapUtils.decodeSampleBitmapFromByteArray(
                        bis, 40, 40);
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
                params.put("action", "mod_all");
                client.post(Model.PathLoad, params,
                        new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                // TODO Auto-generated method stub
                                UserDao ud = new UserDao(
                                        Activity_boshu_EditBaseMessage.this);
                                User user = ud.find(Preferences.getUserName());
                                String result = new String(arg2);
                                System.out.println(result);
                                try {
                                    JSONObject obj = new JSONObject(new String(
                                            arg2));
                                    if (FLAG == 2) {
                                        String afterImage = null;
                                        String aImage = obj.getJSONObject(
                                                "user_info").getString(
                                                "id_back_image");
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
                                        }

                                    }
                                    if (FLAG == 1) {
                                        String bImage = obj.getJSONObject(
                                                "user_info").getString(
                                                "id_image");
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
                                        }

                                    }
                                    if (FLAG == 3) {
                                        String studentImage = null;
                                        String sImage = obj.getJSONObject(
                                                "user_info").getString(
                                                "prv_image");
                                        img_boshu_studentCard
                                                .setImageBitmap(bt);
                                        if (user != null) {
                                            user.setStudentImage(sImage);

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
                                        }
                                    }
                                    if (user != null) {
                                        ud.update(user);
                                    } else {
                                        ud.addUser(user);
                                    }
                                    dialog.dismiss();
                                    Toast.makeText(
                                            Activity_boshu_EditBaseMessage.this,
                                            "身份证图片上传成功", 0).show();
                                    String str = new String(arg2);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                                // TODO Auto-generated method stub
                                String str = new String(arg2);

                            }
                        });

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                CompressPicture.setSoftBitmap(bitmap);
            }
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.img_boshu_beforehead:
                FLAG = 1;
                setMoreDialog();
                break;
            case R.id.img_boshu_afterhead:
                FLAG = 2;
                setMoreDialog();
                break;
            case R.id.img_boshu_studentCard:
                setMoreDialog();
                FLAG = 3;
                break;

            case R.id.bt_boshu_finish:
                str_boshu_name = nickNamEditText.getText().toString();
                str_boshu_realName = realNamEditText.getText().toString();
                str_boshu_school = schoolEditText.getText().toString();
                str_boshu_myPhone = myphoneEditText.getText().toString();
                str_boshu_mail = emailEditText.getText().toString();
                str_boshu_major = jobEditText.getText().toString();
                str_boshu_qQ = qqEditText.getText().toString();
                str_boshu_friendPhone = friendPhoneEditText.getText().toString();
                str_boshu_jobWant = intentEditText.getText().toString();
                str_boshu_resume = resumEditText.getText().toString();
                str_boshu_IdCard = idCardEditText.getText().toString();

                postMessage1();
                break;
            // �Ա�
            case R.id.ll_boshu_sex:
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = (int) (dm.widthPixels / 1.2);
                int height = (int) (dm.heightPixels / 2.8);
                setAlertDialog(this, sexStrings, width, height);
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
        sl.fullScroll(ScrollView.FOCUS_DOWN);
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
            tv_boshu_editsex.setText(sexString);
        } else {
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
    }

    public void setMoreDialog() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / 1.2);
        int height = (int) (dm.heightPixels / 2.8);
        setAlertDialog(this, ss, width, height);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    public void back(View view) {
        finish();
    }

    public void postMessage() {

    }

    //
    public void postMessage1() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.start();

        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setJsonDb(response);
                if (Activity_boshu_Message.context != null) {
                    Activity_boshu_Message.context.finish();
                }
                System.out.println(response + "----------");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error: " + error);

            }
        }) {

            @Override
            protected void setParams(Map<String, String> params) {
                if (str_boshu_sex != null) {
                    params.put("sex", str_boshu_sex);
                }
                setViewString(params, "nick_name", str_boshu_name);
                setViewString(params, "email", str_boshu_mail);
                setViewString(params, "school", str_boshu_school);
                setViewString(params, "entrance_year", str_boshu_entrance_year);
                setViewString(params, "major", str_boshu_major);
                setViewString(params, "age", str_boshu_age);
                setViewString(params, "height", str_boshu_hight);
                setViewString(params, "figure", str_boshu_figure);
                setViewString(params, "qq", str_boshu_qQ);
                setViewString(params, "real_name", str_boshu_realName);
                setViewString(params, "mobile_phone", str_boshu_myPhone);
                setViewString(params, "agent_phone", str_boshu_friendPhone);
                setViewString(params, "job_want", str_boshu_jobWant);
                setViewString(params, "cer_id", str_boshu_IdCard);
                setViewString(params, "work_info", str_boshu_resume);
                params.put("sys", "user");
                params.put("ctrl", "user");
                params.put("action", "mod_all");
            }
        });
    }

    public void setViewString(Map<String, String> params, String key,
                              String value) {
        if (value != null) {
            params.put(key, value);
        }
    }

    public void setJsonDb(JSONObject obj) {
        try {
            User user = new User();
            String accountString = obj.getJSONObject("account").getString("status");
            String recruitString = obj.getJSONObject("recruit").getString("status");
            String resumeString = obj.getJSONObject("resume").getString("status");
            String basicString = obj.getJSONObject("recruit").getString("status");
            if (accountString.equals("0") || recruitString.equals("0") || resumeString.equals("0") || basicString.equals("0")) {


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
                System.out.println(user1.getUserName() + "-----------0000000"
                        + user1.getNickName() + "---" + user1.getAferIdCard()
                        + user1.getAge() + user1.getBeforeIdCard()
                        + user1.getEntrance_year() + user1.getFigure()
                        + user1.getHeadImage() + user1.getHight()
                        + user1.getJobWant() + user1.getqQ());

            }
            FinishBack();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }

    public void getUser() {
        EDITFLAG = this.getIntent().getIntExtra("FLAG", 0);
        if (EDITFLAG == 1) {

        }
        if (EDITFLAG == 2) {
            UserDao ud = new UserDao(this);
            User user = ud.find(Preferences.getUserName());
            realNamEditText.setText(user.getRealName());
            myphoneEditText.setText(user.getMyPhone());
            nickNamEditText.setText(user.getNickName());
            emailEditText.setText(user.getMail());
            friendPhoneEditText.setText(user.getFriendPhone());
            qqEditText.setText(user.getqQ());
            intentEditText.setText(user.getJobWant());
            schoolEditText.setText(user.getSchool());
            jobEditText.setText(user.getMajor());
            resumEditText.setText(user.getResume());
            idCardEditText.setText(user.getIdCard());

            tv_boshu_editage.setText(user.getAge());
           /*  * tv_boshu_editbody.setText(user.getFigure());
             * tv_boshu_editmail.setText(user.getMail());
             * tv_boshu_edithight.setText(user.getHight());
             * tv_boshu_editidnumber.setText(user.getIdCard());
             * tv_boshu_editintention.setText(user.getJobWant());
             * tv_boshu_editjob.setText(user.getMajor());
             * tv_boshu_editname.setText(user.getNickName());
             * tv_boshu_editscholl.setText(user.getSchool());
             * tv_boshu_editreal.setText(user.getRealName());
             * tv_boshu_editphone.setText(user.getMyPhone());
             * tv_boshu_editphone1.setText(user.getFriendPhone());
             * tv_boshu_editqq.setText(user.getqQ());
             * tv_boshu_editresum.setText(user.getResume());
             * tv_boshu_editidnumber.setText(user.getIdCard());*/
            tv_boshu_enrollment.setText(user.getEntrance_year());
            tv_boshu_editbody.setText(user.getFigure());
            String sexString = user.getSex();

            tv_boshu_editsex.setText(user.getSex());
            tv_boshu_edithight.setText(user.getHight() + "cm");

            String afterUrl = Model.PitureLoad + user.getAferIdCard();
            String beforeUrl = (Model.PitureLoad + user.getBeforeIdCard());
            String studentUrl = (Model.PitureLoad + user.getStudentImage());
            Bitmap afterBitmap = mImageDowloader.showCacheBitmap(afterUrl
                    .replaceAll("[^\\w]", ""));
            Bitmap beforeBitmap = mImageDowloader.showCacheBitmap(beforeUrl
                    .replaceAll("[^\\w]", ""));
            Bitmap studentBitmap = mImageDowloader.showCacheBitmap(studentUrl
                    .replaceAll("[^\\w]", ""));
            setNetBitmap(img_boshu_afterhead, afterBitmap, afterUrl);
            setNetBitmap(img_boshu_beforehead, beforeBitmap, beforeUrl);
            setNetBitmap(img_boshu_studentCard, studentBitmap, studentUrl);
        }

    }

    // ��ȡ���֤ͼƬ
    public void setNetBitmap(final ImageView img, Bitmap bitmap, String url) {

        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {
            img.setImageResource(R.drawable.delete3);
            mImageDowloader.downloadImage(40, 40, url,
                    new OnImageDownloadListener() {
                        @Override
                        public void onImageDownload(String url, Bitmap bitmap) {
                            // TODO Auto-generated method stub
                            if (bitmap == null) {
                                img.setImageResource(R.drawable.delete3);
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

}
