package com.example.scrollex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrollex.wificonnect.WifiConnecter;
import com.example.scrollex.wificonnect.WifiDirectBoradcastReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayActivity extends AppCompatActivity {

    TextView txtMultiScoreA,txtMultiCountDown,txtMultiScoreB;
    EditText edMultiMessage;
    Button btnMultiSend;
    RecyclerView recyclerMultiView;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntenatFliter;

    static  final int MESSAGE_READ=1;
    MultiplayActivity.ServerClass serverClass;
    MultiplayActivity.ClientClass clientClass;
    MultiplayActivity.SendReceive sendReceive;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer_layout);

         allinitialize();
         listeners();

    }
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case MESSAGE_READ:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    Toast.makeText(getApplicationContext(),tempMsg,Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        }
    });



    private void allinitialize() {

        txtMultiCountDown=findViewById(R.id.txt_multicountdown);
        txtMultiScoreA=findViewById(R.id.txt_multiscoreA);
        txtMultiScoreB=findViewById(R.id.txt_multiscoreB);
        recyclerMultiView=findViewById(R.id.idMultiRecycle);
        btnMultiSend=findViewById(R.id.btnMultiSend);
        edMultiMessage=findViewById(R.id.edMultiMessage);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mIntenatFliter = new IntentFilter();
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    private void listeners() {


        btnMultiSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=edMultiMessage.getText().toString();
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                sendReceive.write(msg.getBytes());
            }
        });
    }

/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        registerReceiver(mReceiver,mIntenatFliter);
    }
*/

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(mReceiver,mIntenatFliter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    public  class  ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            super.run();
            try {
                serverSocket=new ServerSocket(8888);
                socket=serverSocket.accept();
                sendReceive=new MultiplayActivity.SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  class  SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public  SendReceive(Socket skt){
            socket=skt;
            try {
                inputStream = socket.getInputStream();
                outputStream=socket.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();

            byte[] buffer=new byte[1024];
            int bytes;

            while (socket!=null){
                try {
                    bytes=inputStream.read(buffer);

                    if(bytes>0){
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public  void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class ClientClass extends Thread{
        private Socket socket;
        String hostAdd;

        public ClientClass(InetAddress hostAddress) {
            this.socket = new Socket();
            this.hostAdd = hostAddress.getHostAddress();
        }

        @Override
        public void run() {
            super.run();

            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                sendReceive=new MultiplayActivity.SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
