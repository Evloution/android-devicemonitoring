package com.evloution.devicemonitoring.together;

import android.graphics.Point;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合网点信息列表用于保存聚合网点信息
 */
class TogMarketBean {
	private Point mPoint;// 中心网点
	private LatLng mLatLng;// 中心网点经纬度
	private List<Marker> mClusterItems;// 网点列表
	private int dotCount;// 网点个数
	private TextView textView;// 聚合网点显示


	TogMarketBean(Point point, LatLng latLng) {
		mPoint = point;
		mLatLng = latLng;
		mClusterItems = new ArrayList<>();
	}

	void addClusterItem(Marker clusterItem) {
		mClusterItems.add(clusterItem);
	}

	Point getCenterPoint() {
		return mPoint;
	}

	LatLng getCenterLatLng() {
		return mLatLng;
	}

	public int getDotCount() {
		return dotCount;
	}

	public void setDotCount(int dotCount) {
		this.dotCount = dotCount;
	}

	public List<Marker> getmClusterItems() {
		return mClusterItems;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}
}
