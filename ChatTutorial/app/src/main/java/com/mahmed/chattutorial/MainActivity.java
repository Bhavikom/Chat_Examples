package com.mahmed.chattutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ArrayList<MessagesData> mMessagesData = new ArrayList<>();
    private AbstractXMPPConnection mConnection;
    public static final String TAG = MainActivity.class.getSimpleName();
    private EditText sendMessageEt;
    private Button sendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv);
        mAdapter = new Adapter(mMessagesData);
        sendMessageEt = findViewById(R.id.sendMessageEt);
        sendBtn = findViewById(R.id.send);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this,manager.getOrientation());

        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        // i have cancel gradle build because i have not called a connection function sorry

        setConnection();


        //i have supposed you have already install openfire server and create user inside it ..for test purposes

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageSend = sendMessageEt.getText().toString();
                if(messageSend.length() > 0 ){

                    //Entitiy bare jid consits of username to whom you want to send message and domain of server
                    //to check domain of server follow me

                    sendMessage(messageSend,"aryan@192.168.56.1");
                }
            }
        });

     // i haved already connected spark to openfire
        //now again i connected for test purposes
    }

    private void sendMessage(String messageSend, String entityBareId) {

        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(entityBareId);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        if(mConnection != null) {

            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            Chat chat = chatManager.chatWith(jid);
            Message newMessage = new Message();
            newMessage.setBody(messageSend);

            try {
                chat.send(newMessage);

                MessagesData data = new MessagesData("send",messageSend);
                mMessagesData.add(data);
                mAdapter = new Adapter(mMessagesData);
                mRecyclerView.setAdapter(mAdapter);




            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setConnection(){
        // Create the configuration for this new connection

        //this function or code given in official documention give an error in openfire run locally to solve this error
        //first off firewall
        //then follow my steps

       new Thread(){
           @Override
           public void run() {



        InetAddress addr = null;
        try {

            // inter your ip4address now checking it
            addr = InetAddress.getByName("192.168.56.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        HostnameVerifier verifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        };
        DomainBareJid serviceName = null;
        try {
            serviceName = JidCreate.domainBareFrom("192.168.56.1");
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()

                .setUsernameAndPassword("aryan","9624300677")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setXmppDomain(serviceName)
                .setHostnameVerifier(verifier)
                .setHostAddress(addr)
                .setDebuggerEnabled(true)
                .build();
        mConnection = new XMPPTCPConnection(config);


        try {
            mConnection.connect();
            // all these proceedure also thrown error if you does not seperate this thread now we seperate thread create
            mConnection.login();

            if(mConnection.isAuthenticated() && mConnection.isConnected()){
                //now send message and receive message code here

                Log.e(TAG, "run: auth done and connected successfully" );
                ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                chatManager.addListener(new IncomingChatMessageListener() {
                    @Override
                   public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                        Log.e(TAG,"New message from " + from + ": " + message.getBody());

                        MessagesData data = new MessagesData("received",message.getBody().toString());
                        mMessagesData.add(data);

                        //now update recyler view
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //this ui thread important otherwise error occur

                                mAdapter = new Adapter(mMessagesData);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        });
                    }
                });

            }




        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
           }
       } .start();

    }

    //thanx for watching ..i have tested this app for online device samscung c5 sorry emulator not working
}
