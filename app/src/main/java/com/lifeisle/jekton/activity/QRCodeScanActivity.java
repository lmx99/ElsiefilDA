package com.lifeisle.jekton.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.client.android.AmbientLightManager;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.DecodeFormatManager;
import com.google.zxing.client.android.DecodeHintManager;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderController;
import com.lifeisle.jekton.order.OrderModel;
import com.lifeisle.jekton.order.OrderView;
import com.lifeisle.jekton.ui.adapter.OrderListAdapter;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Toaster;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;


/**
 * @author Jekton
 * @version 0.2 8/10/2015
 */
public class QRCodeScanActivity extends AppCompatActivity
        implements View.OnClickListener, OrderView, RadioGroup.OnCheckedChangeListener,
        View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener, SurfaceHolder.Callback {

    public static final String ORDER_DELIVERED = "OrderOperateReceiver.ORDER_DELIVERED";
    public static final String ORDER_LOGISTICS_UPDATE = "OrderOperateReceiver.ORDER_LOGISTICS_UPDATE";


    private static final String TAG = "QRCodeScanActivity";

    private static final int REQUEST_CODE_SCAN_QR_CODE = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView tvFailCount;
    private String failCountTemplateOne;
    private String failCountTemplateOther;

    private ProgressDialog progressDialog;
    private BaseAdapter orderListAdapter;

    private LinearLayout options;
    private RadioGroup orderOption;
    private RadioGroup sortOption;
    private RadioGroup sortOptionExtra;

    /**
     * used when click {@link QRCodeScanActivity#tvFailCount}, at {@link #onClick(View)}
     */
    private RadioButton rbPostFailed;
    /**
     * used when click the scan button, at {@link #onClick(View)}
     */
    private RadioButton rbAllOrder;

    private IntentFilter orderOperateFilter;
    private OrderOperateReceiver orderOperateReceiver = new OrderOperateReceiver();


    private OrderController orderController;
    private OrderModel orderModel;


    // qr scanner
    private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 0L;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet = null;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        orderOperateFilter = new IntentFilter();
        orderOperateFilter.addAction(ORDER_DELIVERED);
        orderOperateFilter.addAction(ORDER_LOGISTICS_UPDATE);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        initFailCount();

        View btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        btnScan.setOnTouchListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        initMVC();


        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
    }

    private void initFailCount() {
        tvFailCount = (TextView) findViewById(R.id.failCount);
        tvFailCount.setOnClickListener(this);

        Resources resources = getResources();
        failCountTemplateOne = resources.getQuantityString(R.plurals.error_fail_count, 1);
        failCountTemplateOther = resources.getQuantityString(R.plurals.error_fail_count, 2);
    }

    private void initMVC() {
        ListView listView = (ListView) findViewById(R.id.orderList);
        listView.setOnTouchListener(this);

        orderModel = new OrderModel(this);
        orderListAdapter = new OrderListAdapter(this, orderModel);
        orderController = new OrderController(this, orderModel);

        listView.setAdapter(orderListAdapter);

        initOptions();
    }

    private void initOptions() {
        options = (LinearLayout) findViewById(R.id.options);

        orderOption = (RadioGroup) options.findViewById(R.id.orderOption);
        sortOption = (RadioGroup) options.findViewById(R.id.sortOption);
        sortOptionExtra = (RadioGroup) options.findViewById(R.id.sortOptionExtra);

        orderOption.setOnCheckedChangeListener(this);
        sortOption.setOnCheckedChangeListener(this);
        sortOptionExtra.setOnCheckedChangeListener(this);

        setOptions();

        rbPostFailed = (RadioButton) findViewById(R.id.postFail);
        rbAllOrder = (RadioButton) findViewById(R.id.all);
    }



    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(orderOperateReceiver, orderOperateFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        resetStatusView();


        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        inactivityTimer.onResume();


        Intent intent = getIntent();
        decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
        decodeHints = DecodeHintManager.parseDecodeHints(intent);

        if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
            int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
            int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
            if (width > 0 && height > 0) {
                cameraManager.setManualFramingRect(width, height);
            }
        }
        if (intent.hasExtra(Intents.Scan.CAMERA_ID)) {
            int cameraId = intent.getIntExtra(Intents.Scan.CAMERA_ID, -1);
            if (cameraId >= 0) {
                cameraManager.setManualCameraId(cameraId);
            }
        }


        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(orderOperateReceiver);

        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        // deliverState at OrderOperateActivity
        orderController.notifyDataSetChanged(true);
    }



    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        orderModel.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;
            // Use volume up/down to turn on light
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (options.getVisibility() == View.VISIBLE) {
            options.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qr_code_scan, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.optionMenu) {
            if (options.getVisibility() == View.INVISIBLE) {
                options.setVisibility(View.VISIBLE);
            } else {
                options.setVisibility(View.INVISIBLE);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setOptions() {
        int orderOption = getOrderOption();
        int sortOption = getSortOption();
        boolean ascending = getSortOptionExtra();

        orderController.setOptions(orderOption, sortOption, ascending);
    }

    private int getOrderOption() {
        switch (orderOption.getCheckedRadioButtonId()) {
            default:
            case R.id.all:
                return OrderController.OPTION_ORDER_ALL;
            case R.id.undelivered:
                return OrderController.OPTION_ORDER_UNDELIVERED;
            case R.id.delivered:
                return OrderController.OPTION_ORDER_DELIVERED;
            case R.id.abnormal:
                return OrderController.OPTION_ORDER_ABNORMAL;
            case R.id.postFail:
                return OrderController.OPTION_ORDER_POST_FAIL;
        }
    }

    private int getSortOption() {
        switch (sortOption.getCheckedRadioButtonId()) {
            default:
            case R.id.sortByCreateTime:
                return OrderController.OPTION_SORT_BY_CREATE_TIME;
        }
    }

    private boolean getSortOptionExtra() {
        return sortOptionExtra.getCheckedRadioButtonId() == R.id.ascending;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SCAN_QR_CODE && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            Logger.d(TAG, "scan result: " + result);
            orderController.postQRCode(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (!rbAllOrder.isChecked())
                    rbAllOrder.setChecked(true);
                break;
            case R.id.failCount:
                rbPostFailed.setChecked(true);
                break;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        closeOptionMenu();
        return false;
    }

    private void closeOptionMenu() {
        if (options.getVisibility() == View.VISIBLE)
            options.setVisibility(View.INVISIBLE);
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setOptions();
    }

    @Override
    public void showDialog() {
        progressDialog.show();
    }

    @Override
    public void closeDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void notifyDataSetChanged() {
        orderListAdapter.notifyDataSetChanged();
        Logger.d(TAG, "notifyDataSetChanged()");
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setFailCount(int count) {
        if (count > 0) {
            String text;
            if (count > 1) {
                text = String.format(failCountTemplateOther, count);
            } else {
                text = String.format(failCountTemplateOne, count);
            }
            tvFailCount.setText(text);
            if (tvFailCount.getVisibility() == View.GONE) {
                tvFailCount.setVisibility(View.VISIBLE);
                tvFailCount.requestLayout();
            }
        } else if (tvFailCount.getVisibility() == View.VISIBLE) {
            tvFailCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void startScanActivity() {
        // TODO: 8/14/2015  delete relative function
//        Intent startScan = new Intent(this, CaptureActivity.class);
//        startActivityForResult(startScan, REQUEST_CODE_SCAN_QR_CODE);
    }

    @Override
    public void showErrMsg(int msgId) {
        Toaster.showShort(this, msgId);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        orderController.notifyDataSetChanged(false);
    }



    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();

        handleDecodeExternally(rawResult, barcode);
    }





    // Briefly show the contents of the barcode, then handle the result outside Barcode Scanner.
    private void handleDecodeExternally(Result rawResult, Bitmap barcode) {

        if (barcode != null) {
            viewfinderView.drawResultBitmap(barcode);
        }

        long resultDurationMS;
        if (getIntent() == null) {
            resultDurationMS = DEFAULT_INTENT_RESULT_DURATION_MS;
        } else {
            resultDurationMS = getIntent().getLongExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS,
                    DEFAULT_INTENT_RESULT_DURATION_MS);
        }


        // Hand back whatever action they requested - this can be changed to Intents.Scan.ACTION when
        // the deprecated intent is retired.
        Intent intent = new Intent(getIntent().getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
        intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
            intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
        }
        Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();
        if (metadata != null) {
            if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
                intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION,
                        metadata.get(ResultMetadataType.UPC_EAN_EXTENSION).toString());
            }
            Number orientation = (Number) metadata.get(ResultMetadataType.ORIENTATION);
            if (orientation != null) {
                intent.putExtra(Intents.Scan.RESULT_ORIENTATION, orientation.intValue());
            }
            String ecLevel = (String) metadata.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
            if (ecLevel != null) {
                intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL, ecLevel);
            }
            @SuppressWarnings("unchecked")
            Iterable<byte[]> byteSegments = (Iterable<byte[]>) metadata.get(ResultMetadataType.BYTE_SEGMENTS);
            if (byteSegments != null) {
                int i = 0;
                for (byte[] byteSegment : byteSegments) {
                    intent.putExtra(Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i, byteSegment);
                    i++;
                }
            }
        }
        sendReplyMessage(R.id.return_scan_result, intent, resultDurationMS);

    }

    private void sendReplyMessage(int id, Object arg, long delayMS) {
        if (handler != null) {
            Message message = Message.obtain(handler, id, arg);
            if (delayMS > 0L) {
                handler.sendMessageDelayed(message, delayMS);
            } else {
                handler.sendMessage(message);
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private class OrderOperateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {
                case ORDER_DELIVERED:
                    closeOptionMenu();
                    int orderID = intent.getIntExtra(OrderListAdapter.OrderListItem.EXTRA_ORDER_CODE, -1);
                    int eventID = intent.getIntExtra(OrderListAdapter.OrderListItem.EXTRA_EVENT_ID, -1);
                    orderController.postDeliveredOrder(orderID, eventID);
                    break;
                case ORDER_LOGISTICS_UPDATE:
                    orderController.notifyDataSetChanged(false);
                    break;
            }

        }
    }



}
