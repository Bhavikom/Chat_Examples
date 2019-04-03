package com.kpblog.textchat;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kpblog.textchat.model.Example;
import com.kpblog.textchat.model.User;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class UserList extends Fragment implements OnClickListener {

    List<User> users = new ArrayList<>();
    ListView userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userlist_layout, container, false);
        userList = (ListView)view.findViewById(R.id.userList);
        getUsrList();
        return view;
    }

    RequestQueue requestQueue;
    UserListAdapter adapter;
    public void getUsrList(){
        requestQueue = Volley.newRequestQueue(getActivity());
        try {
            StringRequest strReq = new StringRequest(Request.Method.GET,"http://"+ getString(R.string.server)+":9090/plugins/restapi/v1/users", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  System.out.println("Response : "+response);
                Example userResponse = new Gson().fromJson(response, Example.class);
                users.clear();
                users.addAll(userResponse.getUsers());
                adapter = new UserListAdapter(getActivity(), users);
                userList.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "YWoKRxv1C14yLTG0");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    5000000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(strReq);
    }
    catch (Exception e){
        e.printStackTrace();
    }
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}