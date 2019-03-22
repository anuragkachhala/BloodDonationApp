package com.tekit.software.bloodDonationApp1;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tekit.software.bloodDonationApp1.Request.OTPRequest;
import com.tekit.software.bloodDonationApp1.Response.OTPResponse;
import com.tekit.software.bloodDonationApp1.RestApi.ApiClient;
import com.tekit.software.bloodDonationApp1.RestApi.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements KeyBoardLayout.OnNumberClickListener, KeyBoardLayout.OnDeleteClickListener, View.OnClickListener, Callback<OTPResponse> {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.pass)
    TextView textViewPass;

    @BindView(R.id.editTextone)
    TextView textViewOne;
    @BindView(R.id.editTexttwo)
    TextView textViewTwo;

    @BindView(R.id.editTextthree)
    TextView textViewThree;

    @BindView(R.id.editTextfour)
    TextView textViewFour;

    @BindView(R.id.tv_count_down)
    TextView textViewCountDown;


    @BindView(R.id.msg_verification_code)
    TextView textViewMsgVerificationCode;

    @BindView(R.id.tv_resend_otp)
    TextView textViewResendOTP;

    private KeyBoardLayout keyBoardLayout;

    private int count = 0;
    private String otp, mobileNumber,insertedOTP;
    private Intent intent;
    private OTPRequest otpRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        intent = getIntent();
        otp = intent.getStringExtra("OTP");
        mobileNumber = intent.getStringExtra("Mobile No.");
        keyBoardLayout = new KeyBoardLayout(this, this, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(keyBoardLayout);
        recyclerView.setHasFixedSize(true);

        textViewMsgVerificationCode.setText(getResources().getString(R.string.msg_enter_verification_code) + " " + mobileNumber);
        textViewResendOTP.setOnClickListener(this);
        setCountDown();


    }

    private void setCountDown() {

        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;//convert to seconds
                long minutes = seconds / 60;//convert to minutes

                if (minutes > 0)//if we have minutes, then there might be some remainder seconds
                    seconds = seconds % 60;//seconds can be between 0-60, so we use the % operator to get the remainder

                String time = formatNumber(minutes) + ":" +
                        formatNumber(seconds);
                textViewCountDown.setText(getResources().getString(R.string.msg_code_expire) + " ( " + time + " )");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                textViewResendOTP.setVisibility(View.VISIBLE);
                textViewCountDown.setText("Pin Expired!");
            }

        }.start();

    }

    private String formatNumber(long value) {
        if (value < 10)
            return "0" + value;
        return value + "";
    }

    @Override
    public void onNumberClicked(int keyValue) {
        String pass = textViewPass.getText().toString() + keyValue;
        textViewPass.setText(pass);
        if (pass.length() == 1) {
            keyBoardLayout.setPinLength(pass.length());
            keyBoardLayout.notifyItemChanged(keyBoardLayout.getItemCount() - 1);
        }
        if (count == 0) {
            textViewOne.setText(String.valueOf(keyValue));
        }
        if (count == 1) {
            textViewTwo.setText(String.valueOf(keyValue));
        }
        if (count == 2) {
            textViewThree.setText(String.valueOf(keyValue));
        }
        if (count == 3) {
            textViewFour.setText(String.valueOf(keyValue));
           insertedOTP = textViewOne.getText().toString()+textViewTwo.getText().toString().trim()+textViewThree.getText().toString()+textViewFour.getText().toString().trim();
           if(otp.substring(0,4).equals(insertedOTP)){
               Toast.makeText(this,"Valid otp",Toast.LENGTH_SHORT).show();
               startActivity(new Intent(this,RegistrationActivity.class));
           }else {
               Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show();
           }


        }
        if (count < 4) {
            count++;
        }

        Log.e("count", "inAdd : " + count);

    }

    @Override
    public void onDeleteClicked() {
        String pass = method(textViewPass.getText().toString());
        textViewPass.setText(pass);

        if (pass.length() == 0 || pass.isEmpty()) {
            keyBoardLayout.setPinLength(pass.length());
            keyBoardLayout.notifyItemChanged(keyBoardLayout.getItemCount() - 1);

        }
        if (count > 0) {
            count--;
        }

        if (count == 0) {
            textViewOne.setText("");

        }
        if (count == 1) {
            textViewTwo.setText("");
        }
        if (count == 2) {
            textViewThree.setText("");
        }
        if (count == 3) {
            textViewFour.setText("");
        }


        Log.e("count", "inDelete : " + count);
    }


    public String method(String str) {
        if (str != null && !str.isEmpty()) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    public void onDeleteLongClicked() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_resend_otp:
                sendOTP();
                break;
        }
    }

    private void sendOTP(){
        otpRequest = new OTPRequest();
        otpRequest.setPhone(mobileNumber);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<OTPResponse> callClient = apiService.authenticateUser(ApiClient.getHeader(), otpRequest);
        callClient.enqueue(this);


    }

    @Override
    public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof OTPResponse) {
                OTPResponse otpResponse = response.body();
                Toast.makeText(this, String.valueOf(otpResponse.getData()), Toast.LENGTH_LONG).show();
                otp = String.valueOf(otpResponse.getData());
            }

        }

    }

    @Override
    public void onFailure(Call<OTPResponse> call, Throwable t) {

    }
}
