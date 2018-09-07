package com.hsl.smartstick_hsl;

// bluetoothのクラス
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.io.InputStream;
import java.io.OutputStream;
// レイアウト・Actibity系の準備
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
// UUID 通信方式
import java.util.UUID;
public class MainActivity extends AppCompatActivity {
    //    private BluetoothAdapter mBluetoothAdapter; // BTアダプタ
//    private BluetoothDevice mBtDevice; // BTデバイス
    private BluetoothSocket mBtSocket; // BTソケット
    private InputStream mInput; // 出力ストリーム
    private OutputStream mOutput; // 出力ストリーム
    //    private Button btn_f, btn_b, btn_l, btn_r, btn_c; // 送信ボタン
    private Button btn_1, btn_2, btn_3, btn_4;
    //    private TextView txt1, txt_debug;
    private int count=0;
//    private String state1="---", state2="---", stateConnect_bef, stateConnect_aft, ConType; // try-except分岐のデバッグ用
//    private BluetoothServerSocket mBtServerSockets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BluetoothAdapter mBluetoothAdapter; // BTアダプタ
        BluetoothDevice mBtDevice; // BTデバイス
        Button btn_f; // 送信ボタン
        TextView txt_debug;
        String state1, state2, ConType; // try-except分岐のデバッグ用


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ボタンのインスタンスを取得
        btn_f = findViewById(R.id.button_f);
        btn_1 = findViewById(R.id.button_1);
        btn_2 = findViewById(R.id.button_2);
        btn_3 = findViewById(R.id.button_3);
        btn_4 = findViewById(R.id.button_4);
        //txt_debug = findViewById(R.id.debugTxt);
        // BTの準備 --------------------------------------------------------------
        // BTアダプタのインスタンスを取得
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 相手先BTデバイスのインスタンスを取得(相手のデバイスを特定)
        mBtDevice = mBluetoothAdapter.getRemoteDevice("30:AE:A4:3A:12:16");
        // BTソケットのインスタンスを取得。ソケット通信の準備
        try {
            // 接続に使用するプロファイルを指定
            mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

            state1 = "OK";
        } catch (IOException e) {
            e.printStackTrace();
            state1 = "NG";
        }

        ConType = String.valueOf(mBtSocket.getConnectionType());

        // ソケットを接続する(今回は出力用だけ)
        try {
            mBtSocket.connect();
            mOutput = mBtSocket.getOutputStream(); // 出力ストリームオブジェクトを得る
            state2 = "OK";

        } catch (IOException e) {
            e.printStackTrace();
            state2 = "NG";
        }

        //各ボタン押下時の挙動設定
        //
        btn_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mInput = mBtSocket.getInputStream();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                int bytes;
                try {
                    bytes = mInput.read(buffer);
                    String message = new String(buffer, 0, bytes);
                    Log.d("onClick", message);
                    String[] splitmessage = message.split("e");

                    //String message2 = message.substring(message.length() - 16);
                    btn_1.setText(splitmessage[splitmessage.length - 1]);
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