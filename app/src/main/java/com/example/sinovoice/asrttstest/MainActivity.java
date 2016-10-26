package com.example.sinovoice.asrttstest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sinovoice.hcicloudsdk.common.HciErrorCode;
import com.sinovoice.hcicloudsdk.recorder.RecorderEvent;

import ttsutil.HciCloudAsrHelper;
import ttsutil.HciCloudSysHelper;
import ttsutil.HciCloudTtsHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button startRecorder;
    private TextView errorView;
    private TextView resultView;
    private TextView stateView;
    private HciCloudSysHelper mHciCloudSysHelper;
    private HciCloudAsrHelper mHciCloudAsrHelper;
    private HciCloudTtsHelper mHciCloudTtsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initAsrAndTts();
    }

    /**
     * 初始化TTS和ASR的功能
     */
    private void initAsrAndTts() {
        mHciCloudSysHelper = HciCloudSysHelper.getInstance();
        mHciCloudAsrHelper = HciCloudAsrHelper.getInstance();
        mHciCloudTtsHelper = HciCloudTtsHelper.getInstance();
        int errorCode = mHciCloudSysHelper.init(this);
        if (errorCode != HciErrorCode.HCI_ERR_NONE) {
            Log.e(TAG, "mHciCloudSysHelper.init failed and return " + errorCode);
            return;
        }
        mHciCloudAsrHelper.initAsrRecorder(this);
        mHciCloudTtsHelper.initTtsPlayer(this);
        mHciCloudAsrHelper.setMyHander(new MyHandler());
    }

    /**
     * 界面初始化
     */
    private void initView() {
        startRecorder = (Button) findViewById(R.id.startRecorder);
        errorView = (TextView) findViewById(R.id.errorView);
        resultView = (TextView) findViewById(R.id.resultView);
        stateView = (TextView) findViewById(R.id.stateView);

        startRecorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRecorder:
                mHciCloudAsrHelper.startAsrRecorder();
                break;
            default:
                break;
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 1:
                    Bundle bundle = msg.getData();
                    String result = bundle.getString("result");
                    resultView.setText(result);
                    break;
                case 2:
                    Bundle bundle1 = msg.getData();
                    String error = bundle1.getString("error");
                    errorView.setText(error);
                    break;
                case 3:
                    Bundle bundle2 = msg.getData();
                    String state = bundle2.getString("state");
                    stateView.setText(state);
                    break;
                default:
                    break;
            }
        }
    }
}
