package com.minseungseon.helmetrev;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private Button testButton;
    private Button resetButton;
    private BluetoothSPP bt;
    private List<Float> pot_values = new ArrayList<>();

    private static final String TAG = "MainActivity";
    private LineChart mChart;
    private Thread thread;
    private int mFillColor = Color.argb(150,51,181,229);
    private Toolbar toolbar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // toolbar
        toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);

        pot_values.add(0f);
        bt = new BluetoothSPP(this); //Initialize bluetooth

        testButton = findViewById(R.id.testBtn);
        resetButton = findViewById(R.id.resetBtn);

        testButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bt.stopService();

            }

        });
        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChart.invalidate();
               mChart.clear();
               pot_values.clear();
            }

        });
        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
        final int[] cnt = {0};

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
//            TextView pot = findViewById(R.id.pot);
            public void onDataReceived(byte[] data, String message) {
                pot_values.clear();
                String[] array = message.split(" ");
               cnt[0]++;
                System.out.print("\n\n\n" + cnt[0] + ": message  :" );
                System.out.print(message);


                for(int i=0; i<array.length; i++) {
                    pot_values.add(Float.parseFloat(array[i]));
                }
                addEntry();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        mChart = (LineChart) findViewById(R.id.mchart);

        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("실시간 그래프 데이터");
        mChart.setDescription(description);

        mChart.setBackgroundColor(Color.WHITE);
        mChart.setGridBackgroundColor(Color.WHITE);
        mChart.setBorderColor(Color.GRAY);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setDrawGridBackground(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawBorders(true);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLUE);
        mChart.setData(data);

    Legend legend = mChart.getLegend();
    legend.setForm(Legend.LegendForm.LINE);
    legend.setTextColor(Color.BLACK);

    XAxis x1 = mChart.getXAxis();
    x1.setTextColor(Color.BLACK);
    x1.setTextSize(10f);
    x1.setDrawGridLines(false);
//    x1.setAvoidFirstLastClipping(true);

    YAxis y1 = mChart.getAxisLeft();
    y1.setTextColor(Color.BLACK);
    y1.setAxisMaximum(1000f);
    y1.setDrawGridLines(true);

    YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);

    }

    private void addEntry(){
        LineData data = mChart.getData();

            if (data != null) {
                LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);

                if (set == null) {
                    set = createSet();
                    data.addDataSet(set);
                }
                for(int i=0; i<pot_values.size(); i++) {
                    data.addEntry(new Entry(set.getEntryCount(),
                            pot_values.get(i)),
                            0);
                    mChart.notifyDataSetChanged();

                }

                mChart.setVisibleXRangeMaximum(10);
                mChart.moveViewToX(data.getEntryCount());
            }

    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "SPL Db");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 177));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(10f);

        return set;
    }
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);

        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {
//        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                bt.send("Text", true);
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.account:
                Intent intent = new Intent( GraphActivity.this, SettingsActivity.class );
                startActivity( intent );
                break;
            case android.R.id.home:
                Intent intent2 = new Intent( GraphActivity.this, HomeActivity.class );
                startActivity( intent2 );
                break;
        }
        return super.onOptionsItemSelected(item);
    }





}
