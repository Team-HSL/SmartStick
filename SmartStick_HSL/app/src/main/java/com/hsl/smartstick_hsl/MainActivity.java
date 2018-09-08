package com.hsl.smartstick_hsl;

// bluetoothのクラス
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.InputStream;
import java.io.OutputStream;
// レイアウト・Actibity系の準備
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
// UUID 通信方式
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothSocket mBtSocket; // BTソケット
    private InputStream mInput; // 出力ストリーム
    private OutputStream mOutput; // 出力ストリーム
    private Button btn_f, btn_1, btn_2, btn_3, btn_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BluetoothAdapter mBluetoothAdapter; // BTアダプタ
        BluetoothDevice mBtDevice; // BTデバイス

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ボタンのインスタンスを取得
        btn_f = findViewById(R.id.button_f);
        btn_1 = findViewById(R.id.button_1);
        btn_2 = findViewById(R.id.button_2);
        btn_3 = findViewById(R.id.button_3);
        btn_4 = findViewById(R.id.button_4);

        // BTの準備 --------------------------------------------------------------
        // BTアダプタのインスタンスを取得
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 相手先BTデバイスのインスタンスを取得(相手のデバイスを特定)
        mBtDevice = mBluetoothAdapter.getRemoteDevice("00:18:E5:04:06:5E"); //hc-05

//         BTソケットのインスタンスを取得。ソケット通信の準備
        try {
            // 接続に使用するプロファイルを指定
            mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ソケットを接続する(今回は出力用だけ)
        try {
            mBtSocket.connect();
            mOutput = mBtSocket.getOutputStream(); // 出力ストリームオブジェクトを得る
        } catch (IOException e) {
            e.printStackTrace();
        }
        //各ボタン押下時の挙動設定

        btn_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mInput = mBtSocket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                int bytes;
                try {
                    bytes = mInput.read(buffer);
                    String message = new String(buffer, 0, bytes);
                    Log.d("onClick", message);
                    String[] splitmessage = message.split("e");

                    String[] values = splitmessage[splitmessage.length - 2].split(",");

                    if (values[0].equals("0")){
                        btn_1.setText("-");
                    }else{
                        btn_1.setText(values[1]);
                    }

                    if (Float.parseFloat(values[3])>1) {
                        btn_f.setBackgroundColor(Color.rgb(255,100,100));
                    }else{
                        btn_f.setBackgroundColor(Color.rgb(18,21,93));
                    }

                    btn_2.setText(values[2]);
                    btn_3.setText(values[3] + " kcal");
                    btn_4.setText(values[4] + " steps");

                    Log.d("onClick", splitmessage[0]);
                } catch (IOException e) {

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ソケットを閉じる
        try {
            mBtSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}