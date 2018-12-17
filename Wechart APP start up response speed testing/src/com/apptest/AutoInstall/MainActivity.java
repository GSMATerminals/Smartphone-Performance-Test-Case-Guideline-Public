package com.apptest.AutoInstall;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.apptest.AutoInstall.utils.SystemUtil;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private PackageManager mPackageManager;
    private String[] mPackages;
    public static final int SET = Menu.FIRST;
    public static final int EXIT = Menu.FIRST+1;
    private TextView status;
    public static final String ACTION_SERVICE_STATE_CHANGE = "ACTION_SERVICE_STATE_CHANGE";
    String[] Xdata = {};
    Integer[] score = {};
    List<PointValue> mPointValues = new ArrayList<PointValue>();
    List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private LineChartView lineChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        BaseAccessibilityService.getInstance().init(this);
        TextView brand = (TextView) findViewById(R.id.brand);
        TextView model = (TextView) findViewById(R.id.model);
        TextView version = (TextView) findViewById(R.id.version);
        TextView service = (TextView) findViewById(R.id.service);
        brand.setText(brand.getText() + SystemUtil.getDeviceBrand());
        model.setText(model.getText() + SystemUtil.getSystemModel());
        version.setText(version.getText() + SystemUtil.getSystemVersion() + " (Android)");
        service.setText(service.getText() + " AutoTstService");
        lineChart = (LineChartView)findViewById(R.id.chart);
        initLineChart();//初始化
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Intent mIntent = new Intent();
                mIntent.setClass(this, MyPreference.class);
                startActivity(mIntent);
                break;
            case 2:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 循环执行
     */
    public void LoopRun(View view){
        for(int i = 0; i < 10; i++){
            Once_run(view);
        }
    }
    /**
     * 单次执行
     * @param view
     */
    public void Once_run(View view){
        int time1 = (int)BaseAccessibilityService.getInstance().weiXin();
        BaseAccessibilityService.getInstance().Home();
        score = insertIntElement(score, time1);
        Xdata = insertStringElement(Xdata,"weixin");
        buildCharts(Xdata,score);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERVICE_STATE_CHANGE);
    }

    public Integer[]  insertIntElement(Integer[] str,int element){
        List<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<str.length; i++) {
            list.add(str[i]);
        }
        list.add(element);
        Integer[] newStr =  list.toArray(new Integer[1]); //返回一个包含所有对象的指定类型的数组
        return newStr;
    }

    public String[]  insertStringElement(String[] str,String element) {
        List<String> list = new ArrayList<String>();
        for (int i=0; i<str.length; i++) {
            list.add(str[i]);
        }
        list.add(element);
        String[] newStr =  list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组
        return newStr;
    }

    public void buildCharts(String[] Xdata,Integer[] score){
        mPointValues.clear();;
        mAxisXValues.clear();
        for (int i = 0; i < Xdata.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(Xdata[i]));
        }
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
        initLineChart();
    }

    /**
     * 初始化测试报告
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.WHITE);
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(8);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);

        Axis axisY = new Axis();
        axisY.setName("%");
        axisY.setTextSize(10);
        data.setAxisYLeft(axisY);


        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

}
