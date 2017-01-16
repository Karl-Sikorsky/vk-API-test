package com.example.vkex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;

/**
 * Created by ПОДАРУНКОВИЙ on 15.01.2017.
 */
public class CustomAdapter extends BaseAdapter {
    private ArrayList<String > users,messages;
    private VKList<VKApiDialog> dialogs;
    Context context;
    public CustomAdapter(Context context, ArrayList<String> users, ArrayList<String> messages, VKList<VKApiDialog> dialogs){
        this.users=users;
        this.messages=messages;
        this.context = context;
        this.dialogs=dialogs;

    }



    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SetData setData = new SetData();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_list_view,null);
        setData.user_name = (TextView) view.findViewById(R.id.userView);
        setData.msg = (TextView) view.findViewById(R.id.msgView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("message.send", VKParameters.from(VKApiConst.USER_ID,dialogs.get(position).message.user_id, VKApiConst.MESSAGE,"test msg"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        setData.msg.setText("SEND");
                    }
                });
            }
        });
        return view;
    }
}
class SetData{
     TextView user_name,msg;
}
