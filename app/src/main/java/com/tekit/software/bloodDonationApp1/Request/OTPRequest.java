package com.tekit.software.bloodDonationApp1.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPRequest {
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;

    }


}
