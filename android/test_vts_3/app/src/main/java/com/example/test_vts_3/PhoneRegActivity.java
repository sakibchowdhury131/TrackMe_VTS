package com.example.test_vts_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class PhoneRegActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private CountryCodePicker countryCodePicker;
    private Button sendOPTBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_reg);


        countryCodePicker = findViewById(R.id.countryCodePickerID);
        phoneNumber = findViewById(R.id.phoneRegID);
        sendOPTBtn = findViewById(R.id.sendOTPbtnID);



        sendOPTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = countryCodePicker.getSelectedCountryCode().toString();
                String number = phoneNumber.getText().toString();

                String validNumber = "+"+code+number;


                if (number.isEmpty()){
                    phoneNumber.setError("Phone Number Required");
                    phoneNumber.requestFocus();
                    return;
                }
                if (number.length()!=10){
                    phoneNumber.setError("Phone Number Must be 10 digits long");
                    phoneNumber.requestFocus();
                    return;
                }


                Intent intent = new Intent (PhoneRegActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", validNumber);
                startActivity(intent);

            }
        });



        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarID);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleBackButton();
            }
        });
    }

    private void HandleBackButton() {
        startActivity(new Intent(PhoneRegActivity.this, MainActivity.class));
    }
}