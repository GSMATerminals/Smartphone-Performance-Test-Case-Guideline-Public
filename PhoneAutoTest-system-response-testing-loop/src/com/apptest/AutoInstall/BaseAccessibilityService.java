package com.apptest.AutoInstall;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.*;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import com.apptest.AutoInstall.utils.SystemUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseAccessibilityService extends AccessibilityService {
    private Intent intent = new Intent(MainActivity.ACTION_SERVICE_STATE_CHANGE);
    private AccessibilityManager mAccessibilityManager;
    private Context mContext;
    private static BaseAccessibilityService mInstance;
    private static String TAG = "AutoInstallService";
//    final ContentResolver cr = getContentResolver();

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mAccessibilityManager = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.v(TAG, event.toString());
        String model = SystemUtil.getDeviceBrand();
        Run_case(event);

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "服务连接了,开始初始化工作...");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "服务关闭了...");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "服务解绑了,释放资源操作...");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "服务停止了...");
    }

    public static BaseAccessibilityService getInstance() {
        if (mInstance == null) {
            mInstance = new BaseAccessibilityService();
        }
        return mInstance;
    }

    private void sendAction(boolean state) {
        intent.putExtra("state", state);
        sendBroadcast(intent);
    }

    /**
     * 邮箱
     * @return
     */
    public long mail(){
        Home();
        Date start = new Date();
        Uri uri = Uri.parse ("mailto: xxx@abc.com");
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }catch (Exception e){

        }
        Date end = new Date();
        long time = end.getTime() -start.getTime();
        return time;
    }

    /**
     *打开谷歌
     * @param
     * @param
     */
    public long Brower() {
        Home();
        Date start = new Date();
        Uri uri = Uri.parse ("http://www.google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        Date end = new Date();
        long time = end.getTime() -start.getTime();
        return time;
    }

    /**
     * 打开地图
     * @return
     */
    public long Map(){
        Home();
        Date start = new Date();
        Uri uri = Uri.parse("geo: 38.899533, -77.036476");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        Date end = new Date();
        long time = end.getTime() -start.getTime();
        return time;
    }

    /**
     * 进入主页
     */
    public void Home(){
        init(mContext);
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        mContext.startActivity(intent);
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        init(mContext);
    }
    /**
     * 拨打电话
     * @param event
     */
    public void Run_case(AccessibilityEvent event){
        if(event.getPackageName().equals("com.android.dialer") && !isExits("com.android.dialer:id/mzc_geo_text")){
            clickInstallButton("com.android.dialer:id/floating_action_button");
            clickInstallButton("com.android.dialer:id/one");
            clickInstallButton("com.android.dialer:id/eight");
            clickInstallButton("com.android.dialer:id/seven");
            clickInstallButton("com.android.dialer:id/three");
            clickInstallButton("com.android.dialer:id/seven");
            clickInstallButton("com.android.dialer:id/eight");
            clickInstallButton("com.android.dialer:id/eight");
            clickInstallButton("com.android.dialer:id/nine");
            clickInstallButton("com.android.dialer:id/four");
            clickInstallButton("com.android.dialer:id/six");
            clickInstallButton("com.android.dialer:id/six");
        } else if(event.getPackageName().equals("com.android.incallui")){
        }
    }

    public Boolean isExits(String id){
        List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
        if(nodes.size() > 0){
            return true;
        }else{
            return false;
        }
    }


    private void clickInstallButton(String id){
        if(getRootInActiveWindow() == null){
            return;
        }
        try{
            List<AccessibilityNodeInfo> nodes = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(id);
            for(int i = 0;i<nodes.size();i++){
                AccessibilityNodeInfo node = nodes.get(i);
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }catch (NullPointerException e){
            Log.d("AutoInstallService","没有找到控件");
        }
    }
}