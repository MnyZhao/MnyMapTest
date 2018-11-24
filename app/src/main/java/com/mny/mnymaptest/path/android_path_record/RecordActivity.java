package com.mny.mnymaptest.path.android_path_record;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CoordinateConverter;
import com.mny.mnymaptest.R;
import com.mny.mnymaptest.path.database.DbAdapter;
import com.mny.mnymaptest.path.record.PathRecord;

import java.util.ArrayList;
import java.util.List;


/**
 * 所有轨迹list展示activity
 */
public class RecordActivity extends Activity implements OnItemClickListener {

    private RecordAdapter mAdapter;
    private ListView mAllRecordListView;
    private DbAdapter mDataBaseHelper;
    private List<PathRecord> mAllRecord = new ArrayList<PathRecord>();
    public static final String RECORD_ID = "record_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordlist);
        mAllRecordListView = (ListView) findViewById(R.id.recordlist);
        mDataBaseHelper = new DbAdapter(this);
        mDataBaseHelper.open();
        searchAllRecordFromDB();
        mAdapter = new RecordAdapter(this, mAllRecord);
        mAllRecordListView.setAdapter(mAdapter);
        mAllRecordListView.setOnItemClickListener(this);
    }

    private void searchAllRecordFromDB() {
        mAllRecord = mDataBaseHelper.queryRecordAll();
        mAllRecord.add(getPath());
    }

    public void onBackClick(View view) {
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        PathRecord recorditem = (PathRecord) parent.getAdapter().getItem(
                position);
        Intent intent = new Intent(RecordActivity.this,
                RecordShowActivity.class);
        intent.putExtra(RECORD_ID, recorditem.getId());
        startActivity(intent);
    }

    public PathRecord getPath() {
        String lat = "116.380912,39.972718;116.380634,39.969836;116.380408,39.968173;116.374627,39.968045;116.370382,39.967908;116.370164,39.967365;116.370253,39.966428;116.370383,39.965304;116.370696,39.962102;116.370791,39.960708;116.370896,39.959558;116.371008,39.958673;116.371112,39.957571;116.371321,39.955314;116.371512,39.95307;116.371686,39.951343;116.371833,39.949859;116.371964,39.948596;116.371389,39.948539;116.370572,39.948377;116.369955,39.948216;116.36925,39.947998;116.368607,39.947762;116.367172,39.947178;116.365885,39.946629";
        PathRecord pathRecord = new PathRecord();
        pathRecord.setDate("2018-10-29");
        pathRecord.setAveragespeed("18km/h");
        pathRecord.setDuration("90000");
        pathRecord.setDistance("10km");
        String[] locationAll = lat.split(";");
        List<AMapLocation> locationAmap = new ArrayList<>();
        for (int i = 0; i < locationAll.length; i++) {
            String[] locations = locationAll[i].split(",");
            CoordinateConverter converter = new CoordinateConverter(this);
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(Double.parseDouble(locations[0]));
            location.setLongitude(Double.parseDouble(locations[1]));
            AMapLocation aMapLocation = new AMapLocation(location);
            locationAmap.add(aMapLocation);
        }
        pathRecord.setPathline(locationAmap);
        Location location = new Location(LocationManager.GPS_PROVIDER);
        String[] lo = locationAll[0].split(",");
        location.setLatitude(Double.parseDouble(lo[0]));
        location.setLongitude(Double.parseDouble(lo[1]));
        Location location1 = new Location(LocationManager.GPS_PROVIDER);
        String[] lo1 = locationAll[locationAll.length - 1].split(",");
        location.setLatitude(Double.parseDouble(lo1[0]));
        location.setLongitude(Double.parseDouble(lo1[1]));
        pathRecord.setStartpoint(new AMapLocation(location));
        pathRecord.setEndpoint(new AMapLocation(location1));
        pathRecord.setId(1);
        return pathRecord;
    }
}
