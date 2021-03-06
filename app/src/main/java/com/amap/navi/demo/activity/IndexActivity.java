package com.amap.navi.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.navi.demo.R;
import com.amap.navi.demo.activity.custom.AllCustomCameraActivity;
import com.amap.navi.demo.activity.custom.AllCustomCarRouteActivity;
import com.amap.navi.demo.activity.custom.AllCustomCrossingActivity;
import com.amap.navi.demo.activity.custom.AllCustomDriveWayActivity;
import com.amap.navi.demo.activity.custom.AllCustomNaviActivity;
import com.amap.navi.demo.activity.custom.AllCustomNextRoadInfoActivity;
import com.amap.navi.demo.activity.custom.AllCustomTrafficBarActivity;
import com.amap.navi.demo.activity.view.FeatureView;
import com.amap.navi.demo.util.CheckPermissionsActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by shixin on 16/8/23.
 * bug??????QQ:1438734562
 */
public class IndexActivity extends CheckPermissionsActivity implements INaviInfoCallback, AMapNaviListener {

    private static class DemoDetails {
        private final int titleId;
        private final int descriptionId;

        private final Class<? extends android.app.Activity> activityClass;

        public DemoDetails(int titleId, int descriptionId,
                           Class<? extends android.app.Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            featureView.setTitleId(demo.titleId, demo.activityClass!=null);
            return featureView;
        }
    }

    private static final DemoDetails[] DEMOS = {
//		    // ????????????
            new DemoDetails(R.string.navi_ui, R.string.blank, null),
			// ?????????????????????
            new DemoDetails(R.string.navi_start_end_poi_calculate_title, R.string.navi_start_end_poi_calculate_desc, IndexActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_end_poi_calculate_title, R.string.navi_end_poi_calculate_desc, IndexActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_bywayof_poi_calculate_title, R.string.navi_bywayof_poi_calculate_desc, IndexActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_ui_navi_title, R.string.navi_ui_navi_desc, IndexActivity.class),
            // ???????????????????????????????????????
            new DemoDetails(R.string.navi_ui_custom_activity, R.string.navi_ui_custom_activity, IndexActivity.class),

            // ????????????
            new DemoDetails(R.string.navi_route_line, R.string.blank, null),
            // ??????????????????
            new DemoDetails(R.string.navi_route_driver_title, R.string.navi_route_driver_desc, DriverListActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_route_motorcycle_title, R.string.navi_route_motorcycle_desc, MotorcycleRouteCalculateActivity.class),
            // ???????????????????????????
            new DemoDetails(R.string.navi_route_ele_bike_title, R.string.navi_route_ele_bike_desc, EleBikeRouteCalculateActivity.class),
            // ??????????????????
            new DemoDetails(R.string.navi_route_walk_title, R.string.navi_route_walk_desc, WalkRouteCalculateActivity.class),
            // ??????????????????
            new DemoDetails(R.string.navi_route_ride_title, R.string.navi_route_ride_desc, RideRouteCalculateActivity.class),
            // ????????????????????????
            new DemoDetails(R.string.navi_route_truck_title, R.string.navi_route_truck_title, TruckRouteCalculateActivity.class),
            // ??????????????????
            new DemoDetails(R.string.navi_route_independent_title, R.string.navi_route_independent_title, IndependentRouteCalculateActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_type, R.string.blank, null),
            // ??????????????????
            new DemoDetails(R.string.navi_type_inner_voice, R.string.blank, EmulatorActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_type_gps_title, R.string.navi_type_gps_desc, GPSNaviActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_type_emu_title, R.string.navi_type_emu_desc, EmulatorActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_type_truck, R.string.blank,SetTruckParamsActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_type_intelligent_title, R.string.navi_type_intelligent_desc, IntelligentBroadcastActivity.class),
            // HUD??????
            new DemoDetails(R.string.navi_type_hud_title, R.string.navi_type_hud_desc, HudDisplayActivity.class),

            // ??????UI????????????
            new DemoDetails(R.string.navi_ui_custom, R.string.blank, null),
            // ???????????????
            new DemoDetails(R.string.navi_ui_custom_car_icon, R.string.blank, CustomCarActivity.class),
            // ???????????????UI
            new DemoDetails(R.string.navi_ui_custom_route, R.string.blank, CustomRouteActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_ui_custom_route_style, R.string.blank, CustomRouteTextureInAMapNaviViewActivity.class),
            // ???????????????????????????
            new DemoDetails(R.string.navi_ui_custom_trun_across_tip, R.string.blank, CustomNextTurnTipViewActivity.class),
            // ????????????
            new DemoDetails(R.string.navi_ui_custom_northmode, R.string.blank, NorthModeActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_ui_custom_wholescan_button, R.string.blank, OverviewModeActivity.class),
            // ??????????????????
            new DemoDetails(R.string.navi_ui_custom_compass, R.string.blank, CustomDirectionViewActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_ui_custom_traffic_button, R.string.blank, CustomTrafficButtonViewActivity.class),
            // ???????????????????????????
            new DemoDetails(R.string.navi_ui_custom_zoom_button, R.string.blank, CustomZoomButtonViewActivity.class),
            // ????????????????????????
            new DemoDetails(R.string.navi_ui_custom_across_overlay, R.string.blank, CustomZoomInIntersectionViewActivity.class),
            // ?????????????????????(New)
            new DemoDetails(R.string.navi_ui_custom_traffic_bar_new, R.string.blank, CustomTrafficProgressBarActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_ui_custom_route_select, R.string.blank, CustomDriveWayViewActivity.class),

            // ???????????????????????????
            new DemoDetails(R.string.navi_custom_all, R.string.blank, null),
            // ??????????????????????????????????????????
            new DemoDetails(R.string.navi_custom_car_route, R.string.blank, AllCustomCarRouteActivity.class),
            // ???????????????????????????????????????????????????
            new DemoDetails(R.string.navi_custom_road_distance_nexttip, R.string.blank, AllCustomNextRoadInfoActivity.class),
            // ??????????????????????????????
            new DemoDetails(R.string.navi_custom_traffic_bar, R.string.blank, AllCustomTrafficBarActivity.class),
            // ???????????????????????????
            new DemoDetails(R.string.navi_custom_route_way, R.string.blank, AllCustomDriveWayActivity.class),
            // ??????????????????????????????
            new DemoDetails(R.string.navi_custom_crossing, R.string.blank, AllCustomCrossingActivity.class),
            // ????????????????????????????????????
            new DemoDetails(R.string.navi_custom_camera, R.string.blank, AllCustomCameraActivity.class),
            // ?????????????????????
            new DemoDetails(R.string.navi_custom_navi, R.string.blank, AllCustomNaviActivity.class),


            // ????????????
            new DemoDetails(R.string.navi_expand, R.string.blank, null),
            // ??????GPS????????????
            new DemoDetails(R.string.navi_expand_set_gps_data, R.string.blank, UseExtraGpsDataActivity.class),
            // ????????????????????????
            new DemoDetails(R.string.navi_expand_route_detail, R.string.blank, GetNaviStepsAndLinksActivity.class),
            // ???????????????
            new DemoDetails(R.string.navi_expand_switch_road, R.string.blank, SwitchMasterRoadNaviActivity.class),
            // ??????????????????????????????
            new DemoDetails(R.string.navi_expand_iflyt_voice, R.string.blank, IflyVoiceActivity.class),

    };

    LatLng p1 = new LatLng(39.993266, 116.473193);//????????????
    LatLng p2 = new LatLng(39.917337, 116.397056);//???????????????
    LatLng p3 = new LatLng(39.904556, 116.427231);//?????????
    LatLng p4 = new LatLng(39.773801, 116.368984);//???????????????(???5???)
    LatLng p5 = new LatLng(40.041986, 116.414496);//?????????(???5???)

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == 1) {
                AmapNaviParams params = new AmapNaviParams(new Poi("?????????", p3, ""), null, new Poi("???????????????", p2, ""), AmapNaviType.DRIVER);
                params.setUseInnerVoice(true);
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, IndexActivity.this);
            } else if (position == 2) {
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi("???????????????", p2, ""), AmapNaviType.DRIVER), IndexActivity.this);
            } else if (position == 3) {
                List<Poi> poiList = new ArrayList();
                poiList.add(new Poi("????????????", p1, ""));
                poiList.add(new Poi("???????????????", p2, ""));
                poiList.add(new Poi("?????????", p3, ""));

                AmapNaviParams params = new AmapNaviParams(new Poi("?????????(???5???)", p5, ""), poiList, new Poi("???????????????(???5???)", p4, ""), AmapNaviType.DRIVER);
                params.setUseInnerVoice(true);
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, IndexActivity.this);
            } else if (position == 4) {
                //??????
                Poi start = new Poi("?????????(???5???)", p5, "");
                //?????????
                List<Poi> poiList = new ArrayList();
                poiList.add(new Poi("????????????", p1, ""));
                poiList.add(new Poi("???????????????", p2, ""));
                poiList.add(new Poi("?????????", p3, ""));
                //??????
                Poi end = new Poi("???????????????(???5???)", p4, "");
                AmapNaviParams amapNaviParams = new AmapNaviParams(start, poiList, end, AmapNaviType.DRIVER, AmapPageType.NAVI);
                amapNaviParams.setUseInnerVoice(true);
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), amapNaviParams, IndexActivity.this);
            } else if (position == 5) {
                AmapNaviParams params = new AmapNaviParams(new Poi("?????????", p3, ""), null, new Poi("???????????????", p2, ""), AmapNaviType.DRIVER);
                params.setUseInnerVoice(true);
                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, IndexActivity.this,CustomAmapRouteActivity.class);
            } else if (position == 12) {
                Intent intent = new Intent(IndexActivity.this, EmulatorActivity.class);
                intent.putExtra("useInnerVoice", true);
                startActivity(intent);
            } else {
                DemoDetails demo = (DemoDetails) adapter.getItem(position);
                if (demo.activityClass != null) {
                    startActivity(new Intent(IndexActivity.this, demo.activityClass));
                }
            }

        }
    };

    protected AMapNavi mAMapNavi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        initView();
    }

    ListAdapter adapter;
    private void initView() {
        ListView listView = (ListView) findViewById(R.id.list);
        setTitle("??????SDK " + AMapNavi.getVersion());

        adapter = new CustomArrayAdapter(
                this.getApplicationContext(), DEMOS);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(mItemClickListener);
    }

    /**
     * ?????????????????????
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);// ????????????
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }


    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }


    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }


    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }


    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onStopSpeaking() {

    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        //??????null???????????????????????????
        return getCustomView("?????????????????????");
    }

    @Override
    public View getCustomNaviView() {
        //??????null???????????????????????????
        return getCustomView("?????????????????????");
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        Log.d(TAG, "onNaviInfoUpdate: "+naviInfo.getCurrentRoadName()+"???????????????"+naviInfo.getPathRetainTime()+",getNextRoadName:"+naviInfo.getNextRoadName()
                +","+naviInfo.getCurLink()+","+naviInfo.getCurPoint()+","+naviInfo.getCurStep()+","+naviInfo.getCurStepRetainDistance()+","+naviInfo.getCurStepRetainTime()
                +","+naviInfo.getExitDirectionInfo()+","+naviInfo.getIconBitmap()+",getIconType:"+naviInfo.getIconType()+","+naviInfo.getNaviType()+","+naviInfo.getNotAvoidInfo()
                +",getCurLink():"+naviInfo.getCurLink()+"getCurPoint():"+naviInfo.getCurPoint());

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }


    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public View getCustomMiddleView() {
        return null;
    }

    @Override
    public void onNaviDirectionChanged(int i) {

    }

    @Override
    public void onDayAndNightModeChanged(int i) {

    }

    @Override
    public void onBroadcastModeChanged(int i) {

    }

    @Override
    public void onScaleAutoChanged(boolean b) {

    }

    TextView text1;
    TextView text2;
    private View getCustomView(String title) {
        LinearLayout linearLayout = new LinearLayout(this);
        try {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            text1 = new TextView(this);
            text1.setGravity(Gravity.CENTER);
            text1.setHeight(90);
            text1.setMinWidth(300);
            text1.setText(title);

            text2 = new TextView(this);
            text2.setGravity(Gravity.CENTER);
            text1.setHeight(90);
            text2.setMinWidth(300);
            text2.setText(title);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(text1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(text2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 100;
            linearLayout.setLayoutParams(params);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return linearLayout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //since 1.6.0 ?????????naviview destroy?????????????????????AMapNavi.stopNavi();???????????????
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
    }
}
