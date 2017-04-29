/*********************************************************************
 * Chat server: accept chat messages from clients.
 * <p>
 * Sender name and GPS coordinates are encoded
 * in the messages, and stripped off upon receipt.
 * <p>
 * Copyright (c) 2017 Stevens Institute of Technology
 **********************************************************************/
package edu.stevens.cs522.chatserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ChatServer extends AppCompatActivity implements OnClickListener {

    final static public String TAG = ChatServer.class.getCanonicalName();

    /*
     * Socket used both for sending and receiving
     */
    private DatagramSocket serverSocket;

    /*
     * True as long as we don't get socket errors
     */
    private boolean socketOK = true;

    /*
     * TODO: Declare a listview for messages, and an adapter for displaying messages.
     */
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> messages;

    Button next;

    /*
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        int memo[] = new int[5 + 1];
        int way = climb_Stairs(0, 5, memo);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.msgList);
        messages = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(adapter);

        /**
         * Let's be clear, this is a HACK to allow you to do network communication on the main thread.
         * This WILL cause an ANR, and is only provided to simplify the pedagogy.  We will see how to do
         * this right in a future assignment (using a Service managing background threads).
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            /*
			 * Get port information from the resources.
			 */
            int port = Integer.parseInt(this.getString(R.string.app_port));
            serverSocket = new DatagramSocket(port);
        } catch (Exception e) {
            Log.e(TAG, "Cannot open socket" + e.getMessage());
            return;
        }

		/*
		 * TODO: Initialize the UI.
		 */
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        try {

            serverSocket.receive(receivePacket);
            Log.i(TAG, "Received a packet");

            InetAddress sourceIPAddress = receivePacket.getAddress();
            Log.i(TAG, "Source IP Address: " + sourceIPAddress);

                /*
                 * TODO: Extract sender and receiver from message and display.
                 */
            receiveData = receivePacket.getData();
            String message = new String(receiveData, "UTF-8");
            messages.add(message);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {

            Log.e(TAG, "Problems receiving packet: " + e.getMessage());
            socketOK = false;
        }

    }

    /*
     * Close the socket before exiting application
     */
    public void closeSocket() {
        serverSocket.close();
    }

    /*
     * If the socket is OK, then it's running
     */
    boolean socketIsOK() {
        return socketOK;
    }

    public int climbStairs(int n) {
        int memo[] = new int[n + 1];
        return climb_Stairs(0, n, memo);
    }
    public int climb_Stairs(int i, int n, int memo[]) {
        if (i > n) {
            return 0;
        }
        if (i == n) {
            return 1;
        }
        if (memo[i] > 0) {
            return memo[i];
        }
        memo[i] = climb_Stairs(i + 1, n, memo) + climb_Stairs(i + 2, n, memo);
        return memo[i];
    }


}