package com.example.communicationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.communicationdemo.R;
import com.example.communicationdemo.entity.ChatEntity;

import java.util.List;

/**
 * 聊天adapter（模拟）
 */

public class ChatAdapter extends BaseAdapter {
    private List<ChatEntity> chatList;
    private Context context;

    public ChatAdapter(Context context, List<ChatEntity> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
            holder = new ViewHolder();
            holder.tv_m_count = (TextView) convertView.findViewById(R.id.count_my);
            holder.tv_m_name = (TextView) convertView.findViewById(R.id.name_my);
            holder.tv_other_name = (TextView) convertView.findViewById(R.id.name);
            holder.tv_other_count = (TextView) convertView.findViewById(R.id.count);
            holder.lay_other = (LinearLayout) convertView.findViewById(R.id.lay_other);
            holder.lay_my = (LinearLayout) convertView.findViewById(R.id.lay_my);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChatEntity chatEntity = chatList.get(position);
        if (chatEntity.geOther() == 1) {
            holder.lay_my.setVisibility(View.GONE);
            holder.lay_other.setVisibility(View.VISIBLE);
            holder.tv_other_count.setText(chatEntity.getCount());
            holder.tv_other_name.setText(chatEntity.getName());
        } else {
            holder.lay_my.setVisibility(View.VISIBLE);
            holder.lay_other.setVisibility(View.GONE);
            holder.tv_m_count.setText(chatEntity.getCount());
            holder.tv_m_name.setText(chatEntity.getName());
        }
        return convertView;
    }

    private static class ViewHolder {
        //other
        TextView tv_other_name;
        TextView tv_other_count;
        LinearLayout lay_other;
        //my
        TextView tv_m_name;
        TextView tv_m_count;
        LinearLayout lay_my;
    }
}
