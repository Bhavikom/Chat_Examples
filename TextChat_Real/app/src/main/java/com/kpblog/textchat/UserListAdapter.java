package com.kpblog.textchat;

/**
 * Created by Khushvinders on 15-Nov-16.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.kpblog.textchat.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    List<User> userList;
    private ImageLoader imageLoader;
    private Activity activity;

    public UserListAdapter(Activity activity, List<User> userList) {
        this.userList = userList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ((TextChatApplication)activity.getApplication()).getImageLoader();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
     final   ViewHolder viewHolder;
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.userlist_adapter_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.status = (NetworkImageView) vi.findViewById(R.id.status);
            viewHolder.userName = (TextView)vi.findViewById(R.id.userName);
            viewHolder.userLayout = (LinearLayout)vi.findViewById(R.id.userLayout);
            vi.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) vi.getTag();
        }
        viewHolder.userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chats.USER_TWO = userList.get(position).getUsername();
                ((MainActivity)activity).mViewPager.setCurrentItem(1);
            }
        });
        viewHolder.userName.setText(userList.get(position).getUsername());
        viewHolder.status.setImageUrl("http://"+activity.getString(R.string.server)+":9090/plugins/presence/status?jid="+userList.get(position).getUsername()+"@domain_name", imageLoader);
        return vi;
    }
    class ViewHolder{
        NetworkImageView status;
        TextView userName;
        LinearLayout userLayout;
    }
}