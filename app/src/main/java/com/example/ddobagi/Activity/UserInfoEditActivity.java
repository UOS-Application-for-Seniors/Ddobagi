package com.example.ddobagi.Activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.example.ddobagi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoEditActivity extends AppCompatActivity {

    EditText UserPassword, UserPasswordCheck, address1, address2, userName ,userBirthYear ,userBirthMonth , userBirthDay, NOKNameEditText, NOKPhoneNumber, NOKNotificationDays;
    Button save_Btn, duplicate_check;
    int educationlevel = 0;
    TextView idCheckResult, passCheckResult;
    boolean isValidID = false, isValidPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        duplicate_check = findViewById(R.id.btn_duplicate);
        idCheckResult = findViewById(R.id.id_check_result);
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
                String name, id, password, year, month, day, add1, add2, NOKName, NOKPhone, NOKnotifi, userBirthDate, address;
                name = userName.getText().toString();
                password = UserPassword.getText().toString();
                year = userBirthYear.getText().toString();
                month = userBirthMonth.getText().toString();
                day = userBirthDay.getText().toString();
                add1 = address1.getText().toString();
                add2 = address2.getText().toString();
                //edulevel
                NOKName = NOKNameEditText.getText().toString();
                NOKPhone = NOKPhoneNumber.getText().toString();
                NOKnotifi = NOKNotificationDays.getText().toString();

                if(name.equals("")){
                    makeToast("성명을 입력해주세요");
                    return;
                }
                else if(!isValidID){
                    makeToast("중복되지 않은 아이디를 입력하고 중복검사 버튼을 눌러주세요");
                    return;
                }
                else if(!isValidPass){
                    makeToast("비밀번호를 확인해주세요");
                    return;
                }
                else if(year.equals("") || month.equals("") || day.equals("")){
                    makeToast("출생년도를 모두 입력해주세요");
                    return;
                }
                else if(add1.equals("") || add2.equals("")){
                    makeToast("주소를 모두 입력해주세요");
                    return;
                }
                else if(NOKName.equals("")){
                    makeToast("보호자 성명을 입력해주세요");
                    return;
                }
                else if(NOKPhone.equals("")){
                    makeToast("보호자 연락처를 입력해주세요");
                    return;
                }
                else if(NOKnotifi.equals("")){
                    makeToast("알림 송신 주기를 입력해주세요");
                    return;
                }

                userBirthDate = year+"-"+month+"-"+day;
                address = add1+","+add2;
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("name",name);
                    jsonObject.put("password",password);
                    jsonObject.put("userBirthDate", userBirthDate);
                    jsonObject.put("Address", address);
                    jsonObject.put("userEducationLevel", educationlevel);
                    jsonObject.put("NOKName", NOKName);
                    jsonObject.put("NOKPhoneNumber", NOKPhone);
                    jsonObject.put("NOKNotificationDays", Integer.parseInt(NOKnotifi));
                    jsonArray.put(jsonObject);
                    Log.i("jsonString", jsonObject.toString());
                }catch(Exception e){
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Communication.registerUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(UserInfoEditActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
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
                        );
                Communication.requestQueue.add(jsonObjectRequest);

                };

        });

    }
    private void makeToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}