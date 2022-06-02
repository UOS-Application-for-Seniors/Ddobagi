package com.example.ddobagi.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.GifLoader;
import com.example.ddobagi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoEditActivity extends AppCompatActivity {

    EditText userName, UserPassword, UserPasswordCheck, address1, address2, userBirthYear ,userBirthMonth , userBirthDay, NOKNameEditText, NOKPhoneNumber, NOKNotificationDays;
    Button save_Btn;
    int educationlevel = 0;
    TextView passCheckResult;
    boolean isValidID = false, isValidPass = false;
    SharedPreferences share;

    TextView initSkipGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        UserPassword = findViewById(R.id.userPassword);
        UserPasswordCheck = findViewById(R.id.userPasswordCheck);
        passCheckResult = findViewById(R.id.pass_check_result);
        save_Btn = findViewById(R.id.btn_save);
        address1 = findViewById(R.id.userAdress1);
        address2 = findViewById(R.id.userAdress2);
        userName = findViewById(R.id.userName);
        Spinner userEducationLevel = (Spinner) findViewById(R.id.userEdu);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.education_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        userEducationLevel.setAdapter(adapter);
        userBirthYear = findViewById(R.id.userBirthYear);
        userBirthMonth = findViewById(R.id.userBirthMonth);
        userBirthDay = findViewById(R.id.userBirthDay);
        NOKNameEditText = findViewById(R.id.NOKName);
        NOKPhoneNumber = findViewById(R.id.NOKNumber);
        NOKNotificationDays = findViewById(R.id.NOKnotifi);

        share = getSharedPreferences("PREF", MODE_PRIVATE);

        initSkipGif = findViewById(R.id.init_skip_gif_btn);
        initSkipGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GifLoader.initSkipGif(share);
                SharedPreferences.Editor editor = share.edit();
                editor.putString("notRecommend", "");
                editor.commit();
                makeToast("이제 입력 예시를 다시 볼 수 있습니다");
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str1, str2;
                str1 = UserPassword.getText().toString();
                str2 = UserPasswordCheck.getText().toString();
                if(!str1.equals("") && !str2.equals("")){
                    if(str1.equals(str2)){
                        passCheckResult.setText("비밀번호가 일치합니다.");
                        isValidPass = true;
                    }
                    else{
                        passCheckResult.setText("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                        isValidPass = false;
                    }
                }
                else if(str1.equals("") && str2.equals("")){
                    passCheckResult.setText("");
                    isValidPass = false;
                }
                else if(str1.equals("") || str2.equals("")){
                    passCheckResult.setText("비밀번호를 모두 입력해주세요.");
                    isValidPass = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        UserPassword.addTextChangedListener(textWatcher);
        UserPasswordCheck.addTextChangedListener(textWatcher);

        userEducationLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                educationlevel = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                educationlevel = 0;
            }
        });

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, password, passwordCheck, year, month, day, add1, add2, NOKName, NOKPhone, NOKnotifi, userBirthDate, address;
                name = userName.getText().toString();
                password = UserPassword.getText().toString();
                passwordCheck = UserPasswordCheck.getText().toString();
                year = userBirthYear.getText().toString();
                month = userBirthMonth.getText().toString();
                day = userBirthDay.getText().toString();
                add1 = address1.getText().toString();
                add2 = address2.getText().toString();
                //edulevel
                NOKName = NOKNameEditText.getText().toString();
                NOKPhone = NOKPhoneNumber.getText().toString();
                NOKnotifi = NOKNotificationDays.getText().toString();


                if(!password.equals("") || !passwordCheck.equals("")){
                    if(!isValidPass){
                        makeToast("비밀번호를 확인해주세요");
                        return;
                    }
                }
                else if(!year.equals("") || !month.equals("") || !day.equals("")){
                    if(!year.equals("") && !month.equals("") && !day.equals("")){

                    }
                    else{
                        makeToast("출생년도를 모두 입력해주세요");
                        return;
                    }
                }
                else if(!add1.equals("") || !add2.equals("")){
                    if(!add1.equals("") && !add2.equals("")){

                    }
                    else{
                        makeToast("주소를 모두 입력해주세요");
                        return;
                    }
                }
                else if(!NOKPhone.equals("")){
                    if(NOKPhone.length() != 11){
                        makeToast("전화번호 11자리를 모두 입력해주세요");
                        return;
                    }
                }
//                else if(name.equals("") && password.equals("") && year.equals("") && add1.equals("") && NOKName.equals("") && NOKPhone.equals("") && NOKnotifi.equals("")){
//                    makeToast("변경할 정보가 없습니다");
//                    return;
//                }
                //최종학력


                userBirthDate = year+"-"+month+"-"+day;



                address = add1+","+add2;


//                JSONArray jsonArray = new JSONArray();
//                JSONObject jsonObject = new JSONObject();
//                try{
//                    if(!name.equals("")){
//                        jsonObject.put("name",name);
//                    }
//                    if(!password.equals("")){
//                        jsonObject.put("password",password);
//                    }
//                    if(!userBirthDate.equals("--")){
//                        jsonObject.put("userBirthDate", userBirthDate);
//                    }
//                    if(!address.equals(",")){
//                        jsonObject.put("Address", address);
//                    }
//
//                    jsonObject.put("userEducationLevel", educationlevel);
//
//                    if(!NOKName.equals("")){
//                        jsonObject.put("NOKName", NOKName);
//                    }
//                    if(!NOKPhone.equals("")){
//                        jsonObject.put("NOKPhoneNumber", NOKPhone);
//                    }
//                    if(!NOKnotifi.equals("")){
//                        jsonObject.put("NOKNotificationDays", Integer.parseInt(NOKnotifi));
//                    }
//                    jsonArray.put(jsonObject);
//                    Log.i("jsonString", jsonObject.toString());
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
                StringRequest request = new StringRequest(Request.Method.POST, Communication.userInfoUpdate,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(UserInfoEditActivity.this, "정보 수정 완료", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Error.Response", error.toString());
                                String json = null;
                                NetworkResponse response = error.networkResponse;
                                if(response != null && response.data != null){
                                    switch(response.statusCode){
                                        case 400:
                                            json = new String(response.data);
                                            System.out.println(json);
                                            break;
                                    }
                                    //Additional cases
                                }
                            }
                        }
                        ){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                if(!name.equals("")){
                                    params.put("name", name);
                                }
                                if(!password.equals("")){
                                    params.put("password", password);
                                }
                                if(!userBirthDate.equals("--")){
                                    params.put("userBirthDate", userBirthDate);
                                }
                                if(!address.equals(",")){
                                    params.put("Address", address);
                                }

                                params.put("userEducationLevel", Integer.toString(educationlevel));

                                if(!NOKName.equals("")){
                                    params.put("NOKName",NOKName);
                                }
                                if(!NOKPhone.equals("")){
                                    params.put("NOKPhoneNumber",NOKPhone);
                                }
                                if(!NOKnotifi.equals("")){
                                    params.put("NOKNotificationDays", NOKnotifi);
                                }
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                SharedPreferences share = getSharedPreferences("PREF", MODE_PRIVATE);
                                params.put("Authorization", "Bearer " + share.getString("Access_token", ""));

                                return params;
                        }
                };
                        Communication.requestQueue.add(request);
                };

        });

    }

    private void makeToast(String str){
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layout));
        TextView textView = layout.findViewById(R.id.text);
        textView.setText(str);

        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}