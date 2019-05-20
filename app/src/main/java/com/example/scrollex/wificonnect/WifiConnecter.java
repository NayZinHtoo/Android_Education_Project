package com.example.scrollex.wificonnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scrollex.MultiplayActivity;
import com.example.scrollex.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WifiConnecter extends AppCompatActivity {

    Button btnWifiOnOff,btnWifiDiscover,btnSend,btnSend1;
    ListView listView;
    TextView textInfo,txtMessage;
    EditText ed_message,ed_message1;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntenatFliter;

    List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    static  final int MESSAGE_READ=1;
    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;

    LinearLayoutCompat lOut,lbutton,nameinput;
    LinearLayout listlayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wificonnect_layout);

        initialize();
        allListener();

    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case MESSAGE_READ:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    txtMessage.setText(tempMsg);
                    break;
            }
            return true;
        }
    });




    private void allListener() {


        btnWifiOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(false);
                    btnWifiOnOff.setText("ON");

                }else {
                    wifiManager.setWifiEnabled(true);
                    btnWifiOnOff.setText("OFF");
                }
            }
        });

        btnWifiDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Discovery Success",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(getApplicationContext(),"Discovery Fail",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device=deviceArray[position];
                WifiP2pConfig config=new WifiP2pConfig();
                config.deviceAddress=device.deviceAddress;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Connect To"+ device.deviceName,Toast.LENGTH_LONG).show();
                        lOut.setVisibility(View.VISIBLE);
                        lbutton.setVisibility(View.GONE);
                        listlayout.setVisibility(View.GONE);
                       /* Intent intent=new Intent(getApplicationContext(), MultiplayActivity.class);
                        startActivity(intent);*/

                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),ed_message.getText().toString(),Toast.LENGTH_LONG).show();
                   nameinput.setVisibility(View.GONE);
              /*  Intent intent=new Intent(getApplicationContext(), MultiplayActivity.class);
                  startActivity(intent);*/
            }
        });
        btnSend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),ed_message1.getText().toString(),Toast.LENGTH_LONG).show();
                String msg=ed_message1.getText().toString();
                sendReceive.write(msg.getBytes());
              /*  Intent intent=new Intent(getApplicationContext(), MultiplayActivity.class);
                startActivity(intent);*/
            }
        });
    }

    private void initialize() {
        btnWifiDiscover = findViewById(R.id.id_wifidiscover);
        btnWifiOnOff = findViewById(R.id.id_onoff);
        listView = findViewById(R.id.listView);
        textInfo=findViewById(R.id.txtInfo);
        txtMessage=findViewById(R.id.txt_message);

        btnSend=findViewById(R.id.btnMessageSend);
        ed_message=findViewById(R.id.ed_sendmessage);
        btnSend1=findViewById(R.id.btnMessageSend1);
        ed_message1=findViewById(R.id.ed_sendmessage1);

        lOut=findViewById(R.id.name_inputlayout);
        lbutton=findViewById(R.id.idbutton);
        listlayout=findViewById(R.id.idlayoutlistview);
        nameinput=findViewById(R.id.name_inputlayout);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBoradcastReceiver(mManager, mChannel, this);

        mIntenatFliter = new IntentFilter();
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntenatFliter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerslist) {
            if (!peerslist.getDeviceList().equals(peers)) {

                peers.clear();
                peers.addAll(peerslist.getDeviceList());

                deviceNameArray=new String[peerslist.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peerslist.getDeviceList().size()];
                int index=0;

                for(WifiP2pDevice device: peerslist.getDeviceList()){
                    deviceNameArray[index]=device.deviceName;
                    deviceArray[index]=device;
                    index++;
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,deviceNameArray);
                listView.setAdapter(adapter);
            }
            if(peers.size()==0){
                Toast.makeText(getApplicationContext(),"No Device Found",Toast.LENGTH_LONG).show();
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress=info.groupOwnerAddress;

            if(info.groupFormed && info.isGroupOwner){
                textInfo.setText("Host");
                serverClass=new ServerClass();
                serverClass.start();
            }else {
                textInfo.setText("Client");
                clientClass=new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        registerReceiver(mReceiver,mIntenatFliter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(mReceiver,mIntenatFliter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerReceiver(mReceiver,mIntenatFliter);
       // unregisterReceiver(mReceiver);
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
                sendReceive=new SendReceive(socket);
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
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
