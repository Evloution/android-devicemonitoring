package com.evloution.devicemonitoring.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.evloution.devicemonitoring.R;
import com.evloution.devicemonitoring.adapter.WindowAdapter;
import com.evloution.devicemonitoring.bean.MarketBean;
import com.evloution.devicemonitoring.bean.MarketDataBean;
import com.evloution.devicemonitoring.httprequest.HttpRequestPost;
import com.evloution.devicemonitoring.log.L;
import com.evloution.devicemonitoring.together.MapTogetherManager;
import com.evloution.devicemonitoring.util.JSONAnalysisUtil;
import com.evloution.devicemonitoring.util.ToastUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    private ListView main_activity_device_listview;

    private OkHttpClient okHttpClient;
    private SimpleAdapter deviceAdapter;
    private List<Map<String, Object>> dataList = new ArrayList<>(); //设备list
    private List<MarketBean> marketBeanList = new ArrayList<>();
    private MarketBean marketBean;

    private List<MarketDataBean> marketDataBeanList = new ArrayList<>();
    private MarketDataBean marketDataBean;
    private Marker marker = null;
    private String institutionid;

    private MapView mMapView = null;
    private AMap aMap = null;
    private MyLocationStyle myLocationStyle;

    /**
     * marker数据集合
     */
    public Map<String, Marker> markerMap = new ConcurrentHashMap<>();

    public static final int MARKER_NORMA = 1;
    public static final int MARKER_TOGE = 2;
    public static int markerStatus = MARKER_NORMA;
    /**
     * 地图初始化比例尺,地图比例尺
     */
    public static float ORGZOON = 30;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1: // 说明请求失败
                    ToastUtil.show(MainActivity.this, "连接失败");
                    break;
                case 0: // 请求成功但数据有误
                    ToastUtil.show(MainActivity.this, (String) msg.obj);
                    break;
                case 1: // 请求成功
                    deviceAdapter = new SimpleAdapter(MainActivity.this, dataList, R.layout.item_main_device_listview, new String[]{"devicecode"}, new int[]{R.id.item_main_device_btn});
                    main_activity_device_listview.setAdapter(deviceAdapter);
                    loadMarker(marketDataBeanList, 0, 0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);
        initData();
        initEvent();
    }

    private void initView(Bundle savedInstanceState) {
        // Intent intent = getIntent();
        // institutionid = intent.getStringExtra("institutionid");
        institutionid = "1513f3a812a24200a446f03398f45b43";
        okHttpClient = new OkHttpClient();

        main_activity_device_listview = findViewById(R.id.main_activity_device_listview);
        // 向服务器请求数据
        getSendData();
        // 获取地图控件
        mMapView = findViewById(R.id.main_activity_device_mapview);
        // 在activity执行onCrete时执行mMapView.onCreate(savedInstanceState)。创建地图
        mMapView.onCreate(savedInstanceState);
    }

    private void initData() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        // myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE); // 设置地图模式。标准模式，
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.location_icon)));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true); // 设置默认定位按钮是否显示，非必需设置。
        aMap.showIndoorMap(true); // 是否显示室内地图。 true：显示室内地图；false：不显示；
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是 false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10)); // 设置地图缩放比例

        //设置自定义弹窗
        aMap.setInfoWindowAdapter(new WindowAdapter(this, marketDataBeanList));
        //绑定信息窗点击事件
        //aMap.setOnInfoWindowClickListener(new WindowAdapter(this));
        // 设置maker点击时的响应
        aMap.setOnMarkerClickListener(new WindowAdapter(this));
        // 地图的滑动监听
        // aMap.setOnCameraChangeListener(onCameraChangeListener);
        // 设置点击 地图的点击事件
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.hideInfoWindow();
            }
        });
    }

    private void initEvent() {
        main_activity_device_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                marketDataBean = new MarketDataBean();
                marketDataBean.setDeviceIP(marketDataBeanList.get(i).getDeviceIP());
                marketDataBean.setDevicelatitude(Double.valueOf(dataList.get(i).get("devicelatitude").toString()));
                marketDataBean.setDevicelongitude(Double.valueOf(dataList.get(i).get("devicelongitude").toString()));
                marketDataBean.setDevicecode(dataList.get(i).get("devicecode").toString());
                marketDataBean.setDevicehumidity(dataList.get(i).get("devicehumidity").toString());
                marketDataBean.setDevicePM25(dataList.get(i).get("devicePM25").toString());
                marketDataBean.setDevicetemperature(dataList.get(i).get("devicetemperature").toString());
                loadMarker(MarketDataBean.showData(marketDataBean), 1, i);
            }
        });
    }

    /**
     * 更新普通网点数据
     */
    private void updateNormalMarkers() {
        // 判断上一次更新marker操作的操作类型,若上次显示的是聚合网点,则先清空地图,然后清空网点信息,在刷新地图marker
        aMap.clear();
        markerMap.clear();

        loadMarker(marketDataBeanList, 0, 0);
    }

    /**
     * 初始化marker数据
     */
    private void loadMarker(List<MarketDataBean> marketDataBeanList, int code, int postion) {
        LatLng latLng = null;
        if (marketDataBeanList == null || marketDataBeanList.size() == 0) {
            return;
        }
        for (int i = 0; i < marketDataBeanList.size(); i++) {
            MarketDataBean marketDataBean = marketDataBeanList.get(i);
            latLng = new LatLng(marketDataBean.getDevicelatitude(), marketDataBean.getDevicelongitude());
            MarkerOptions options = new MarkerOptions();
            options.anchor(0.5f, 1.0f);
            options.position(latLng);
            options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_normal));
            // setIconToOptions(options);

            marker = aMap.addMarker(options);
            marker.setObject(marketDataBean);
            marker.setZIndex(ORGZOON);
            marker.setPeriod(postion);

            //markerMap.put(marketDataBean.getDeviceIP(), marker);
        }
        if (code == 1) { // code 等于 1 说明是点击的列表后走进这个方法
            // 点击列表后根据经纬度将地图缩放比例调到15
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            marker.showInfoWindow();
        }
    }

    private void getSendData() {
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("institutionUserId", institutionid);
        RequestBody requestBody = formEncodingBuilder.build();
        Request request = new Request.Builder().url("http://192.168.0.114/user/institutionDeviceSelect").post(requestBody).build();
        executeRequest(request);
    }

    // 对请求结果的封装
    private void executeRequest(Request request) {
        // 将request封装为call
        Call call = okHttpClient.newCall(request);
        // 执行call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                L.e("onFailure：" + e.getMessage());
                e.printStackTrace();
                if (e instanceof SocketTimeoutException) {
                    // 判断超时异常
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(MainActivity.this, "连接超时，请重新发送");
                        }
                    });
                }
                if (e instanceof ConnectException) {
                    // 判断连接异常，
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(MainActivity.this, "连接异常，请重新发送");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                L.e("onResponse：" + result);
                if (response.body() != null) {
                    response.body().close();
                }
                Message message = new Message();
                if ("ERRORCODE".equals(result) || "FAILURE".equals(result)) {
                    // 走这里说明请求失败
                    message.what = -1;
                    handler.sendMessage(message);
                } else {
                    String code = JSONAnalysisUtil.JSONCodeAnalysis(result);
                    // 走这里说明请求成功
                    if ("1".equals(code)) {
                        // 请求成功
                        JSONArray data = JSONAnalysisUtil.JSONDataJsonObject(result);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject partIndex;
                            Map<String, Object> parkItem = new HashMap<>();
                            try {
                                // JSON数组里面的具体-JSON对象
                                JSONObject jsonObject = data.getJSONObject(i);
                                String deviceIP = jsonObject.optString("deviceIP", null);
                                double devicelatitude = jsonObject.optDouble("devicelatitude", 0.0);
                                double devicelongitude = jsonObject.optDouble("devicelongitude", 0.0);
                                String devicecode = jsonObject.optString("devicecode", null);
                                String devicehumidity = jsonObject.optString("devicehumidity", null);
                                String devicePM25 = jsonObject.optString("devicePM25", null);
                                String devicetemperature = jsonObject.optString("devicetemperature", null);
                                MarketDataBean marketDataBean = new MarketDataBean();
                                marketDataBean.setDeviceIP(deviceIP);
                                marketDataBean.setDevicecode(devicecode);
                                marketDataBean.setDevicelatitude(devicelatitude);
                                marketDataBean.setDevicelongitude(devicelongitude);
                                marketDataBean.setDevicehumidity(devicehumidity);
                                marketDataBean.setDevicePM25(devicePM25);
                                marketDataBean.setDevicetemperature(devicetemperature);
                                marketDataBeanList.add(marketDataBean);

                                partIndex = data.getJSONObject(i);
                                Iterator iterator = partIndex.keys();
                                String key;
                                while (iterator.hasNext()) {
                                    key = (String) iterator.next();
                                    String value = partIndex.getString(key);
                                    parkItem.put(key, value);
                                }
                                dataList.add(parkItem);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        L.e("marketDataBeanList：" + marketDataBeanList.size());
                        L.e("解析组装的list数据：" + dataList);
                        message.what = 1;
                        message.obj = JSONAnalysisUtil.JSONDataAnalysis(result);
                        handler.sendMessage(message);
                    } else {
                        // 登陆失败
                        message.what = 0;
                        message.obj = JSONAnalysisUtil.JSONMsgAnalysis(result);
                        handler.sendMessage(message);
                    }
                }
            }
        });
    }

    /**
     * 放大缩小完成后对聚合点进行重新计算 (聚合地图上的点儿) 暂时没用到
     */
    private synchronized void updateMapMarkers() {
        if (marketBeanList != null && marketBeanList.size() > 0) {
            Log.i(TAG, "地图级别:" + aMap.getCameraPosition().zoom);
            // 若当前地图级别小于初始化比例尺,则显示聚合网点
            if (aMap.getCameraPosition().zoom < ORGZOON) {
                markerStatus = MARKER_TOGE;
                updateTogMarkers();
            }
            // 显示普通marker
            else {
                if (markerStatus == MARKER_TOGE) {
                    markerStatus = MARKER_NORMA;
                    updateNormalMarkers();
                }
            }
            System.gc();
        }
    }

    /**
     * 更新聚合网点,在updateMapMarkers方法中调用
     */
    private void updateTogMarkers() {
        Log.i(TAG, "开始显示聚合网点,清空地图normal marker...");
        aMap.clear();
        // 更新聚合marker
        MapTogetherManager.getInstance(this, aMap).onMapLoadedUpdateMarker(markerMap);

        // 设置marker点击事件,若是聚合网点此时点击marker则放大地图显示正常网点
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 初始化地图按指定的比例尺移动到定位的坐标
                aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(marker.getPosition(), ORGZOON, 3, 0)), 1000, null);
                return true;
            }
        });
    }

    /**
     * 反编码坐标转地址
     * RegeocodeQuery   [坐标转地址]
     * GeocodeQuery     [地址转坐标]
     */
    private void getAddressByLatLonPoint(final LatLonPoint latLonPoint) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        //一参数表示经纬度，二参数表示范围多少米，三参数表示是地图坐标系还是GPS坐标系
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 2000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);

        //address表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode都ok
        GeocodeQuery query = new GeocodeQuery("河北省", "河北省");
        geocodeSearch.getFromLocationNameAsyn(query);
        //监听逆地理编码结果
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                //i为1000为成功
                if (i == 1000) {
                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
                    //当前地图中心的地点(其他信息可通过regeocodeAddress.出来)
                    String formatAddress = regeocodeAddress.getFormatAddress();
                    //address.setText(formatAddress);
                    Log.e(TAG, "onCameraChange经纬度转换的地址: " + formatAddress);
                } else {
                    Log.e(TAG, "暂无此地信息...: ");
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    Log.e(TAG, "onCameraChange查询的地址: " + geocodeResult.getGeocodeQuery().getLocationName());
                    Log.e(TAG, "onCameraChange查询的城市: " + geocodeResult.getGeocodeQuery().getCity());
                    Log.e(TAG, "onCameraChange查询的纬度: " + geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLatitude());
                    Log.e(TAG, "onCameraChange查询的经度: " + geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLongitude());
                } else {
                    Log.e(TAG, "查询失败...: ");
                }
            }
        });
    }

    // 登陆按钮点击事件
    private void userLoginBtn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> httpList = new ArrayList<NameValuePair>();
                httpList.add(new BasicNameValuePair("institutionUserId", institutionid));
                String result = HttpRequestPost.sendDataPostRequest("/user/institutionDeviceSelect", httpList);
                L.e("请求结果：" + result);
                Message message = new Message();
                if ("ERRORCODE".equals(result) || "FAILURE".equals(result)) {
                    // 走这里说明请求失败
                    message.what = -1;
                    handler.sendMessage(message);
                } else {
                    String code = JSONAnalysisUtil.JSONCodeAnalysis(result);
                    // 走这里说明请求成功
                    if ("1".equals(code)) {
                        // 登陆成功
                        JSONArray data = JSONAnalysisUtil.JSONDataJsonObject(result);
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject partIndex;
                            Map<String, Object> parkItem = new HashMap<String, Object>();
                            try {
                                partIndex = data.getJSONObject(i);

                                Iterator iterator = partIndex.keys();
                                String key;
                                while (iterator.hasNext()) {
                                    key = (String) iterator.next();
                                    String value = partIndex.getString(key);
                                    //if ("devicecode".equals(key) || "PARKID".equals(key) || "EMPTYSPOT".equals(key))
                                    parkItem.put(key, value);

                                    Log.e("k->v", key + value);
                                }
                                dataList.add(parkItem);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        L.e("解析组装的list数据：" + dataList);
                        message.what = 1;
                        message.obj = JSONAnalysisUtil.JSONDataAnalysis(result);
                        handler.sendMessage(message);
                    } else {
                        // 登陆失败
                        message.what = 0;
                        message.obj = JSONAnalysisUtil.JSONMsgAnalysis(result);
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        updateNormalMarkers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
}
