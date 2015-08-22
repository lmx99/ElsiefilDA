package com.lifeisle.jekton.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.AmbientLightManager;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.InScanningTimer;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderController;
import com.lifeisle.jekton.order.OrderModel;
import com.lifeisle.jekton.order.OrderView;
import com.lifeisle.jekton.ui.adapter.OrderListAdapter;
import com.lifeisle.jekton.util.DimensionUtils;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Toaster;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
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
    private View startScanButton;
    private View cancelButton;
    private View background;
    private EditText barcodeInputText;

    private IntentFilter orderOperateFilter;
    private OrderOperateReceiver orderOperateReceiver = new OrderOperateReceiver();


    private OrderController orderController;
    private OrderModel orderModel;

    private boolean scanning;
    private boolean cameraInit;

    // qr scanner
    private static final long DEFAULT_SCAN_DELAY = 500L;

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceHolder previewSurfaceHolder;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints = null;
    private String characterSet = null;
    private InScanningTimer inScanningTimer;
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
        Logger.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        orderOperateFilter = new IntentFilter();
        orderOperateFilter.addAction(ORDER_DELIVERED);
        orderOperateFilter.addAction(ORDER_LOGISTICS_UPDATE);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        initFailCount();
        initScanRelativelyViews();

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        initMVC();

        inScanningTimer = new InScanningTimer(this);
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

    private void initScanRelativelyViews() {
        startScanButton = findViewById(R.id.btn_scan);
        startScanButton.setOnClickListener(this);
        startScanButton.setOnTouchListener(this);

        cancelButton = findViewById(R.id.btn_cancel_scan);
        cancelButton.setOnClickListener(this);

        background = findViewById(R.id.background);

        barcodeInputText = (EditText) findViewById(R.id.barcode);
        findViewById(R.id.ok).setOnClickListener(this);
    }

    private void initMVC() {
        ListView orderListView = (ListView) findViewById(R.id.orderList);
        orderListView.setOnTouchListener(this);

        orderModel = new OrderModel(this);
        orderListAdapter = new OrderListAdapter(this, orderModel);
        orderController = new OrderController(this, orderModel);

        orderListView.setAdapter(orderListAdapter);

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
    protected void onResume() {
        super.onResume();
        registerReceiver(orderOperateReceiver, orderOperateFilter);

        if (!cameraInit) {
            Logger.d(TAG, "onResume() init camera");
            cameraManager = new CameraManager(this);

            viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
            viewfinderView.setCameraManager(cameraManager);

            handler = null;

            resetStatusView();


            beepManager.updatePrefs();
            ambientLightManager.start(cameraManager);


            decodeFormats = new HashSet<>(2);
            decodeFormats.add(BarcodeFormat.CODE_128);
            decodeFormats.add(BarcodeFormat.QR_CODE);


            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            previewSurfaceHolder = surfaceView.getHolder();
            if (!hasSurface) {
                // Install the callback and wait for surfaceCreated() to init the camera.
                previewSurfaceHolder.addCallback(this);
            }

            cameraInit = true;
        }

    }

    private void startScan() {
        initCamera();
        inScanningTimer.onResume();
        setListHeight();
        startScanButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.VISIBLE);
        scanning = true;
        if (!rbAllOrder.isChecked())
            rbAllOrder.setChecked(true);
    }

    private void setListHeight() {
        Rect frameRect = cameraManager.getFramingRect();
        // called after init the camera, won't be null
        int top = frameRect.top;
        int height = top - DimensionUtils.dp2px(this, 60);

        swipeRefreshLayout.getLayoutParams().height = height;
        swipeRefreshLayout.requestLayout();

        background.getLayoutParams().height = height;
        background.requestLayout();
    }

    private void restoreListHeight() {
        background.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        background.requestLayout();

        swipeRefreshLayout.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        swipeRefreshLayout.requestLayout();
    }

    // make public to enable the InScanningTimer call it
    public void stopScan() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (scanning) {
            inScanningTimer.onPause();
            ambientLightManager.stop();
            beepManager.close();
            cameraManager.closeDriver();
        }
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        scanning = false;
        restoreListHeight();
        cancelButton.setVisibility(View.GONE);
        startScanButton.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPause() {
        unregisterReceiver(orderOperateReceiver);
        stopScan();
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
        inScanningTimer.shutdown();
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
        if (scanning) {
            stopScan();
        } else if (options.getVisibility() == View.VISIBLE) {
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
            if (!scanning) {
                if (options.getVisibility() == View.INVISIBLE) {
                    options.setVisibility(View.VISIBLE);
                } else {
                    options.setVisibility(View.INVISIBLE);
                }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                startScan();
                break;
            case R.id.failCount:
                rbPostFailed.setChecked(true);
                break;
            case R.id.btn_cancel_scan:
                stopScan();
                break;
            case R.id.ok:
                manuallyAddBarcode();
                break;
        }

    }

    private void manuallyAddBarcode() {
        String barcode = barcodeInputText.getText().toString();
        if (barcode.length() != 13) {
            showErrMsg(R.string.error_order_code_invalid);
        } else {
            orderController.postQRCode(barcode);
            barcodeInputText.setText("");
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
        Logger.d(TAG, "notifyDataSetChanged()");
        orderListAdapter.notifyDataSetChanged();
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
    public void showErrMsg(int msgId) {
        Toaster.showShort(this, msgId);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        orderController.notifyDataSetChanged(false);
    }




    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.d(TAG, "surfaceCreated");
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
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
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode) {
        inScanningTimer.onActivity();

        if (barcode != null) {
            viewfinderView.drawResultBitmap(barcode);
        }
        String text = rawResult.getText();
        Logger.d(TAG, "scanned result = " + text);
        orderController.postQRCode(text);
        restartPreviewAfterDelay(DEFAULT_SCAN_DELAY);
    }



    private void initCamera() {
        if (previewSurfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(previewSurfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet,
                        cameraManager);
            }
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


    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }


    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
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
