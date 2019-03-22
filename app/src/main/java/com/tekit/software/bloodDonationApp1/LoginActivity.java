package com.tekit.software.bloodDonationApp1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tekit.software.bloodDonationApp1.Request.LoginRequest;
import com.tekit.software.bloodDonationApp1.Response.LoginResponse;
import com.tekit.software.bloodDonationApp1.Response.OTPResponse;
import com.tekit.software.bloodDonationApp1.RestApi.ApiClient;
import com.tekit.software.bloodDonationApp1.RestApi.ApiInterface;
import com.tekit.software.bloodDonationApp1.Utils.ValidationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse> {

    @BindView(R.id.tv_sign_up)
    TextView textViewSignUp;

    @BindView(R.id.et_applicant_mobile_no)
    EditText editTextApplicantMobileNo;

    @BindView(R.id.et_applicant_password)
    EditText editTextApplicationPassword;

    @BindView(R.id.checkbox)
    CheckBox checkBox;

    @BindView(R.id.tv_forget_pass)
    TextView textViewForgetPass;

    @BindView(R.id.btn_login)
    Button buttonLogin;


    private String mobileNo,password;
    private LoginRequest loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSignUpTextView();
        setClickListener();
    }

    private void setClickListener() {
        textViewForgetPass.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }


    private void setSignUpTextView() {
        String signUpText = getResources().getString(R.string.singup_text_signin);
        SpannableString spannableSignUpText = new SpannableString(signUpText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, MobileNoRegistrationActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableSignUpText.setSpan(clickableSpan, 23, signUpText.indexOf("now") - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignUp.setText(spannableSignUpText);
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        textViewSignUp.setHighlightColor(Color.TRANSPARENT);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_forget_pass:
                startActivity(new Intent(LoginActivity.this,MobileNoRegistrationActivity.class));
                break;
            case R.id.btn_login:
                if(checkValidation()){
                      login();
                }
                break;
        }
    }

    private boolean checkValidation(){
        mobileNo = editTextApplicantMobileNo.getText().toString().trim();
        password= editTextApplicationPassword.getText().toString().trim();
        if(!ValidationUtils.checkEmpty(mobileNo)){
            editTextApplicantMobileNo.setError(getResources().getString(R.string.err_msg_enter_phone_no));
            return false;
            //showToast("Please enter mobile no");
        }if(!ValidationUtils.mobileValidation(mobileNo)){
            editTextApplicantMobileNo.setError(getResources().getString(R.string.err_msg_enter_valid_mobile_no));
            return false;
            //showToast("Please enter valid mobile no");
        }if(!ValidationUtils.checkEmpty(password)){
            editTextApplicationPassword.setError(getResources().getString(R.string.err_msg_enter_password));
              return false;
        }if(!ValidationUtils.passwordValidation(password)){
            editTextApplicationPassword.setError(getResources().getString(R.string.err_msg_enter_valid_pass));
            return false;
        }
      return true;
    }


    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void login(){
        loginRequest = new LoginRequest();
        loginRequest.setPhone(mobileNo);
        loginRequest.setPassword(password);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> callClient = apiService.LoginUser(ApiClient.getHeader(), loginRequest);
        callClient.enqueue(this);
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof LoginResponse) {
                LoginResponse loginResponse= response.body();
                Toast.makeText(this,loginResponse.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {

    }
}
