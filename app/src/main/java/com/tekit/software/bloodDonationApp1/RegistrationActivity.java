package com.tekit.software.bloodDonationApp1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tekit.software.bloodDonationApp1.Request.RegistrationRequest;
import com.tekit.software.bloodDonationApp1.Response.RegistrationResponse;
import com.tekit.software.bloodDonationApp1.RestApi.ApiClient;
import com.tekit.software.bloodDonationApp1.RestApi.ApiInterface;
import com.tekit.software.bloodDonationApp1.Utils.SpinnerManager;
import com.tekit.software.bloodDonationApp1.Utils.ValidationUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tekit.software.bloodDonationApp1.UserLocationActivity.CITY;
import static com.tekit.software.bloodDonationApp1.UserLocationActivity.CURRENT_ADDRESS;
import static com.tekit.software.bloodDonationApp1.UserLocationActivity.DISTRICT;
import static com.tekit.software.bloodDonationApp1.UserLocationActivity.STATE;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, Callback<RegistrationResponse> {

    private static final String TAG = RegistrationActivity.class.getName();
    private static final int REQUEST_CODE_LOCATION = 1000;
    private static final int MIN_AGE = -18;
    private static final int MAX_AGE = -68;

    @BindView(R.id.et_first_name)
    EditText editTextFirstName;

    @BindView(R.id.et_last_name)
    EditText editTextLastName;

    @BindView(R.id.et_applicant_email)
    EditText editTextApplicationEmail;

    @BindView(R.id.et_applicant_password)
    EditText editTextApplicationPassword;

    @BindView(R.id.et_applicant_conf_password)
    EditText editTextConfPassword;

    @BindView(R.id.et_applicant_dob)
    TextView editTextApplicantDob;

    @BindView(R.id.radioSex)
    RadioGroup radioGroupSex;

    @BindView(R.id.radioMale)
    RadioButton radioButtonMale;

    @BindView(R.id.radioFemale)
    RadioButton radioButtonFemale;

    @BindView(R.id.sp_blood_group)
    Spinner spinnerBloodGroup;

    @BindView(R.id.btn_register)
    Button buttonRegister;

    @BindView(R.id.et_location)
    TextView editTextLocation;

    private String firstName, lastName, emailId, password, confPassword, dateOfBirth,city,state,district,bloodGroup;
    private int mMonth, mYear, mDay;
    private RegistrationRequest registrationRequest;
    private String[] bloodGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reginstraion);
        ButterKnife.bind(this);

        bloodGroupList = getResources().getStringArray(R.array.list_blood_group);
        setListener();


    }

    private void setListener() {
        buttonRegister.setOnClickListener(this);
        editTextApplicantDob.setOnClickListener(this);
        radioGroupSex.setOnCheckedChangeListener(this);
        editTextLocation.setOnClickListener(this);
        spinnerBloodGroup.setOnItemSelectedListener(this);
        spinnerBloodGroup.setAdapter(SpinnerManager.setSpinner(this, bloodGroupList));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (checkValidation()) {
                      registerUser();
                }
                break;
            case R.id.et_applicant_dob:
                openCalender();
                break;
            case R.id.et_location:
                openLocation();

        }
    }

    private void openLocation(){
        Intent intent = new Intent(this,UserLocationActivity.class);
        startActivityForResult(intent,REQUEST_CODE_LOCATION);
    }


    private boolean checkValidation() {
        firstName = editTextFirstName.getText().toString().trim();
        lastName = editTextLastName.getText().toString().trim();
        emailId = editTextApplicationEmail.getText().toString().trim();
        dateOfBirth = editTextApplicantDob.getText().toString().trim();
        password = editTextApplicationPassword.getText().toString().trim();
        confPassword = editTextConfPassword.getText().toString().trim();
        if (!ValidationUtils.checkEmpty(firstName)) {
            editTextFirstName.setError(getResources().getString(R.string.err_msg_enter_first_name));
            return false;
        }  if (!ValidationUtils.checkEmpty(lastName)) {
            editTextLastName.setError(getResources().getString(R.string.err_msg_enter_last_name));
            return false;
        }  if (!ValidationUtils.checkEmpty(emailId)) {
            editTextApplicationEmail.setError(getResources().getString(R.string.err_msg_enter_email));
            return false;
        } if (!ValidationUtils.validateEmail(emailId)) {
            editTextApplicationEmail.setError(getResources().getString(R.string.err_msg_enter_valid_email));
            return false;
        } if (!ValidationUtils.checkEmpty(password)) {
            editTextApplicationPassword.setError(getResources().getString(R.string.err_msg_enter_password));
            return false;
        } if (!ValidationUtils.passwordValidation(password)) {
            editTextApplicationPassword.setError(getResources().getString(R.string.err_msg_enter_valid_pass));
            return false;
        } if (!ValidationUtils.checkEmpty(confPassword)) {
            editTextConfPassword.setError(getResources().getString(R.string.err_msg_enter_conf_password));
            return false;
        } if (!ValidationUtils.passwordValidation(confPassword)) {
            editTextConfPassword.setError(getResources().getString(R.string.err_msg_enter_valid_pass));
            return false;
        } if (!password.equals(confPassword)) {
            editTextConfPassword.setError(getResources().getString(R.string.err_msg_pass_match));
            return false;
        } if (!ValidationUtils.checkEmpty(dateOfBirth)) {
            editTextApplicantDob.setError(getResources().getString(R.string.err_msg_enter_dob));
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void openCalender() {

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, MIN_AGE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker dateView, int year, int monthOfYear, int dayOfMonth) {
                String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                editTextApplicantDob.setText(date);


                //textViewTaskDueDate.setText(DateAndTimeUtil.dateToString(date));

            }
        }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePickerDialog.setTitle("");
        c.add(Calendar.YEAR,MAX_AGE);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {


    }

    private void registerUser(){
        registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstname(firstName);
        registrationRequest.setLastname(lastName);
        registrationRequest.setEmail(emailId);
        registrationRequest.setPassword(password);
        registrationRequest.setConfirmpassword(confPassword);
        registrationRequest.setDob(dateOfBirth);
        registrationRequest.setBlood(bloodGroup);
        registrationRequest.setDistrict(district);
        registrationRequest.setState(state);
        registrationRequest.setCity(city);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<RegistrationResponse> callClient = apiService.registrationUser(ApiClient.getHeaders(),registrationRequest);
        callClient.enqueue(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(resultCode == RESULT_OK)
            switch (requestCode){
                case REQUEST_CODE_LOCATION:

                    editTextLocation.setText(data.getStringExtra(CURRENT_ADDRESS));
                    city = data.getStringExtra(CITY);
                    state = data.getStringExtra(STATE);
                    district =data.getStringExtra(DISTRICT);

                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_blood_group:
                bloodGroup = bloodGroupList[position];
                showToast(bloodGroup);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
        switch (response.code()) {

            case 200: {
                if(response.body() instanceof  RegistrationResponse){

                }
                showToast("details have been saved");

                //downloadPDF();
                break;

            }
            case 403: {
                showToast("Problem at server site");
            }
        }
    }

    @Override
    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
        showToast(t.toString());

    }
}
