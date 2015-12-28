package com.dx.identificationcard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText mCardNum;

    private TextView mAllNum;

    private final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1,
            6, 3, 7, 9, 10, 5, 8, 4, 2};

    // 18位身份证中最后一位校验码
    private final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7',
            '6', '5', '4', '3', '2'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCardNum = (EditText) findViewById(R.id.card_num);
        mAllNum = (TextView) findViewById(R.id.all_num);
    }

    public void onCalculation(View view) {
        String card = mCardNum.getText().toString();
        if (!isCard(card)) {
            Toast.makeText(this, "身份证的号码格式不对", Toast.LENGTH_SHORT).show();
            return;
        }

        card += calculateVerifyCode(card);
        mAllNum.setText(card);
    }


    private static boolean isCard(String code) {
        Pattern pattern = Pattern
                .compile("(\\d{17})");
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) {
            return false;
        }

        String birthdayPart = code.substring(6, 14);
        if (birthdayPart.compareTo("1900") < 0) {
            return false;
        }
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = simpleDate.parse(birthdayPart);
            if (date.getTime() > System.currentTimeMillis()) {
                return false;
            }
            return birthdayPart.equals(simpleDate.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    private static char calculateVerifyCode(CharSequence cardNumber) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char ch = cardNumber.charAt(i);
            sum += ((int) (ch - '0')) * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

}
