package com.example.vkex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    ListView listr;
    ArrayList<String> texter;
    private String[] scope = new String[]{VKScope.GROUPS, VKScope.FRIENDS};
    private Button showMsg, hum;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        System.out.println(Arrays.asList(fingerprints));

        VKSdk.login(this, scope);
        showMsg = (Button) findViewById(R.id.button);
        showMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 20));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        VKApiGetDialogResponse getMessagesResponse = (VKApiGetDialogResponse) response.parsedModel;

                        VKList<VKApiDialog> list_mes_vk = getMessagesResponse.items;


                        ArrayList<String> list_mes = new ArrayList<>();
                        ArrayList<String> list_users = new ArrayList<>();

                        for (VKApiDialog msg : list_mes_vk) {
                            list_users.add(String.valueOf(msg.message.user_id));
                            list_mes.add(msg.message.body);
                        }
                        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list_mes);
                        listr.setAdapter(new CustomAdapter(MainActivity.this, list_users, list_mes, list_mes_vk));
                    }
                });
            }
        });


        texter = new ArrayList<>();
        hum = (Button) findViewById(R.id.buttonGroup);
        hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final VKRequest request = new VKApiGroups().getById(VKParameters.from("group_ids", "jumoreski"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);


                        VKList list_vk = (VKList) response.parsedModel;


                        try {
                            VKRequest request1 = new VKApiWall()
                                    .get(VKParameters.from(VKApiConst.OWNER_ID, "-" + list_vk.get(0).fields.getInt("id"), VKApiConst.COUNT, 100));
                            request1.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    super.onComplete(response);
                                    try {
                                        JSONObject jsonObject = (JSONObject) response.json.get("response");
                                        JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject post = (JSONObject) jsonArray.get(i);
                                            texter.add(post.getString("text"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, texter);
                            listr.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }

        });
    }

            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
                    @Override
                    public void onResult(VKAccessToken res) {
// Пользователь успешно авторизовался
                        Toast.makeText(getApplicationContext(), "ACcceeee", Toast.LENGTH_SHORT).show();
                        listr = (ListView) findViewById(R.id.List);
                        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name", "last_name"));
                        request.executeWithListener(new VKRequest.VKRequestListener() {
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);
                                VKList listv = (VKList) response.parsedModel;
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, listv);
                                listr.setAdapter(arrayAdapter);
                            }
                        });

                    }

                    @Override
                    public void onError(VKError error) {

                    }


                })) ;
            }


}







