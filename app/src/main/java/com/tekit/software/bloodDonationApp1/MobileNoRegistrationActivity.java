package com.tekit.software.bloodDonationApp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class MobileNoRegistrationActivity extends AppCompatActivity implements View.OnClickListener, Callback<OTPResponse> {


    @BindView(R.id.et_phone_number)
    EditText editTextPhoneNumber;

    @BindView(R.id.iv_next)
    ImageView imageViewNext;

    private OTPRequest otpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no_registration);
        ButterKnife.bind(this);
        setClickListener();
    }

    private void setClickListener() {
        imageViewNext.setOnClickListener(this);
            }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_next:
                if (editTextPhoneNumber.getText().toString().isEmpty() || editTextPhoneNumber.getText().toString().length() < 10) {
                    editTextPhoneNumber.setText(getResources().getString(R.string.err_msg_enter_valid_mobile_no));
                    //Toast.makeText(this,"Mobile No. is not valid ",Toast.LENGTH_SHORT).show();
                } else {
                    otpRequest = new OTPRequest();
                    otpRequest.setPhone(editTextPhoneNumber.getText().toString().trim());
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<OTPResponse> callClient = apiService.authenticateUser(ApiClient.getHeader(), otpRequest);
                    callClient.enqueue(this);



                   /* startActivity(new Intent(this, OTPActivity.class));
                    finish();
*/
                }
                break;
        }
    }

    @Override
    public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
        int statusCode = response.code();
        if (statusCode == 200) {

            if (response.body() instanceof OTPResponse) {
                OTPResponse otpResponse = response.body();
                Toast.makeText(this, String.valueOf(otpResponse.getData()), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, OTPActivity.class);
                intent.putExtra("OTP",String.valueOf(otpResponse.getData()));
                intent.putExtra("Mobile No.",editTextPhoneNumber.getText().toString().trim());
                startActivity(intent);
                finish();
            }

        }

    }

    @Override
    public void onFailure(Call<OTPResponse> call, Throwable t) {

    }
}
