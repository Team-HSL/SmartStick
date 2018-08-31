package com.hsl.smartstick_hsl;

// bluetoothのクラス
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
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
    private BluetoothAdapter mBluetoothAdapter; // BTアダプタ
    private BluetoothDevice mBtDevice; // BTデバイス
    private BluetoothSocket mBtSocket; // BTソケット
    private InputStream mInput; // 出力ストリーム
    private OutputStream mOutput; // 出力ストリーム
    private Button btn_f, btn_b, btn_l, btn_r, btn_c; // 送信ボタン
    private TextView txt1, txt_debug;
    private int count=0;
    private String state1="---", state2="---", stateConnect_bef, stateConnect_aft, ConType; // try-except分岐のデバッグ用
    private BluetoothServerSocket mBtServerSockets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ボタンのインスタンスを取得
        btn_f = findViewById(R.id.button_f);
        btn_b = findViewById(R.id.button_b);
        btn_l = findViewById(R.id.button_l);
        btn_r = findViewById(R.id.button_r);
        btn_c = findViewById(R.id.button_count);
//        txt1 = findViewById(R.id.Bluetoothtext);
        txt_debug = findViewById(R.id.debugTxt);
        // BTの準備 --------------------------------------------------------------
        // BTアダプタのインスタンスを取得
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 相手先BTデバイスのインスタンスを取得(相手のデバイスを特定)
        mBtDevice = mBluetoothAdapter.getRemoteDevice("30:AE:A4:3A:12:16");
        // BTソケットのインスタンスを取得。ソケット通信の準備
        try {
//            //connect()の前にsocketのaccept
//            mBtServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(
//                    this.getPackageName(), UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//            mBtSocket = mBtServerSocket.accept();

            // 接続に使用するプロファイルを指定
            mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//                    UUID.fromString("00001400-0000-1000-8000-00805F9B34FB"));

            state1 = "OK";
//            txt1.setText("socket instance ok");
        } catch (IOException e) {
            e.printStackTrace();
            state1 = "NG";
            //            txt1.setText("socket instance NG!");
        }

        ConType = String.valueOf(mBtSocket.getConnectionType());

        // ソケットを接続する(今回は出力用だけ)
        try {
            mBtSocket.connect();

            mInput = mBtSocket.getInputStream();
            mOutput = mBtSocket.getOutputStream(); // 出力ストリームオブジェクトを得る
            state2 = "OK";

        } catch (IOException e) {
            e.printStackTrace();
            state2 = "NG";
        }
        txt_debug.setText("device name :" + mBtDevice.getName() + "(" + mBtDevice.getAddress() + ")\n"
                + "socket instance :" + state1 + "\n"
                + "socket connection Type :" + ConType + "\n"
                + "socket connect :" + state2 + "\n"
        );
        //各ボタン押下時の挙動設定
        //
        btn_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] buffer = new byte[1024];
                int bytes;
                while (true) {
                    //btn_c.setText("start!");
                    try {

                        //btn_c.setText("start!");
                        //Read from the InputStream
                        bytes = mInput.read(buffer);
                        String message = new String(buffer, 0, bytes);
                        Log.d("onClick", message);
                        try {
                            Thread.sleep(10000);
                        }catch(InterruptedException e) {
                        e.printStackTrace();
                        }

                        btn_c.setText(message);
                        //btn_c.setText(String.valueOf(bytes));
                    } catch (IOException e) {
                        break;
                    }

                }
            }
        });
        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    mOutput.write('b');
                    count += 1;
                    btn_c.setText(String.valueOf(count));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btn_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                long time = System.currentTimeMillis();
                try {
                    mOutput.write('l');
                    count += 1;
                    btn_c.setText(String.valueOf(count));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btn_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                long time = System.currentTimeMillis();
                try {
                    mOutput.write('r');
                    count += 1;
                    btn_c.setText(String.valueOf(count));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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