package com.example.ddobagi.Activity;


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
import com.example.ddobagi.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText UserID, UserPassword, UserPasswordCheck, address1, address2, userName,userBirthYear,userBirthMonth, userBirthDay, NOKNameEditText, NOKPhoneNumber, NOKNotificationDays;
    Button save_Btn, duplicate_check;
    int educationlevel = 0;
    TextView idCheckResult, passCheckResult;
    boolean isValidID = false, isValidPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        duplicate_check = findViewById(R.id.btn_duplicate);
        UserID = findViewById(R.id.userID);
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

        UserID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idCheckResult.setText("???????????? ????????? ???????????????.");
                isValidID = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        passCheckResult.setText("??????????????? ???????????????.");
                        isValidPass = true;
                    }
                    else{
                        passCheckResult.setText("??????????????? ???????????? ????????????. ?????? ??????????????????.");
                        isValidPass = false;
                    }
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

        duplicate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputID = UserID.getText().toString();
                if(inputID.equals("")){
                    return;
                }

                StringRequest request = new StringRequest(Request.Method.POST, Communication.idCheckUrl, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(Integer.parseInt(s) != 0){
                            idCheckResult.setText("????????? ??????????????????.");
                            isValidID = false;
                            //Toast.makeText(RegistrationActivity.this, "????????? ??????????????????", Toast.LENGTH_LONG).show();
                        }
                        else{
                            idCheckResult.setText("???????????? ??? ????????????.");
                            isValidID = true;
                            //Toast.makeText(RegistrationActivity.this, "???????????? ??? ????????????.", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        makeToast("????????? ??????????????????: "+volleyError);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", inputID);
                        // ?????? ??????

                        return params;
                    }
                };

                Communication.requestQueue.add(request);
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
                boolean NOK = false;
                name = userName.getText().toString().trim();
                id = UserID.getText().toString().trim();
                password = UserPassword.getText().toString().trim();
                year = userBirthYear.getText().toString().trim();
                month = userBirthMonth.getText().toString().trim();
                day = userBirthDay.getText().toString().trim();
                add1 = address1.getText().toString().trim();
                add2 = address2.getText().toString().trim();
                //edulevel
                NOKName = NOKNameEditText.getText().toString().trim();
                NOKPhone = NOKPhoneNumber.getText().toString().trim();
                NOKnotifi = NOKNotificationDays.getText().toString().trim();

                if(name.equals("")){
                    makeToast("????????? ??????????????????");
                    return;
                }
                else if(!isValidID){
                    makeToast("???????????? ?????? ???????????? ???????????? ???????????? ????????? ???????????????");
                    return;
                }
                else if(!isValidPass){
                    makeToast("??????????????? ??????????????????");
                    return;
                }
                else if(year.equals("") || month.equals("") || day.equals("")){
                    makeToast("??????????????? ?????? ??????????????????");
                    return;
                }
                else if(add1.equals("") || add2.equals("")){
                    makeToast("????????? ?????? ??????????????????");
                    return;
                }
                else if(!NOKName.equals("") || !NOKPhone.equals("") || !NOKnotifi.equals("")){
                    if(!NOKName.equals("") && !NOKPhone.equals("") && !NOKnotifi.equals("")){
                        NOK = true;
                    }
                    else{
                        makeToast("????????? ????????? ??????????????? ?????? ???????????????");
                        return;
                    }
                }

                userBirthDate = year+"-"+month+"-"+day;
                address = add1+","+add2;
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("name",name);
                    jsonObject.put("id", id);
                    jsonObject.put("password",password);
                    jsonObject.put("userBirthDate", userBirthDate);
                    jsonObject.put("address", address);
                    jsonObject.put("userEducationLevel", educationlevel);
                    if(NOK){
                        jsonObject.put("NOKName", NOKName);
                        jsonObject.put("NOKPhoneNumber", NOKPhone);
                        jsonObject.put("NOKNotificationDays", Integer.parseInt(NOKnotifi));
                    }
                    jsonArray.put(jsonObject);
                    Log.i("jsonString", jsonObject.toString());
                }catch(Exception e){
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Communication.registerUrl, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(RegistrationActivity.this, "???????????? ??????", Toast.LENGTH_LONG).show();
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