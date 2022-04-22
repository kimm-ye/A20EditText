package com.kosmo.a20edittext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    
    //Logcat을 사용하기 위한 태그 설정 (검색하기 위해 태그 설정)
    private static final String TAG = "KOSMO";

    TextView textView1;

    EditText etId;
    EditText etPwd;
    EditText etYear;
    EditText etMonth;
    EditText etDay;

    String sId;
    String sPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);

        etId = findViewById(R.id.etId);
        etPwd = findViewById(R.id.etPwd);
        etYear = findViewById(R.id.etYear);
        etMonth = findViewById(R.id.etMonth);
        etDay = findViewById(R.id.etDay);

        //비밀번호 입력상자에 텍스트가 변경될때의 리스너를 부착한 후 Watcher를 연결한다.
        etPwd.addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {

        String beforeText;

        //텍스트가 변경되는 중 호출되는 메서드
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //입력전 텍스트를 얻어와서 로그켓에 출력한다.
            beforeText = s.toString();
            Log.d(TAG, "beforeTextChanged : "+beforeText);
        }

        //텍스트 변경 중
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            byte[] bytes = null;
            try{
                //한글깨짐처리를 위한 케릭셋 설정
                //bytes = str.toString().getBytes("KSC5601");
                bytes = s.toString().getBytes("8859_1");
                //입력한 텍스트의 길이를 얻어옴
                int strCount = bytes.length;
                //텍스트 뷰에 설정을 줘서 몇바이트 입력되고 있는지 확인가능
                textView1.setText(strCount+" / 8바이트");
            }
            catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        //텍스트가 변경된 이후...
        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();
            Log.d(TAG, "afterTextChanged : "+ str);
            try{
                byte[] strBytes = str.getBytes("8859_1");
                if(strBytes.length > 8){
                    etPwd.setText(beforeText);
                    /*
                    글자수가 8자를 넘었을때 텍스트를 지정한만큼 블럭으로 지정하여 더이상
                    입력할 수 없도록 만들어준다.
                    setSelection(시작, 끝) 형태로 지정한다.
                     */
                    etPwd.setSelection(0,beforeText.length()-1);
                    //etPwd.setSelection(beforeText.length()-9, beforeText.length()-1); //8글자가 넘어가면 블럭으로 지정한다.
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    //키보드 버튼 내리기 버튼
    public void onKeydownClicked(View v){
        //하단의 명령어를 쓰면 키보드가 내려감
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //로그인 버튼을 누를 경우 간단한 폼값 체크
    public void onLoginClicked(View v){
        //입력상자에 입력된 텍스트를 얻어와서 문자열로 변경한다.
        sId = etId.getText().toString();
        sPwd = etPwd.getText().toString();

        //3글자 이하로 입력된 경우 경고창을 띄운다.
        if(sId.length() < 3){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("아이디를 입력해주세요")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("닫기", null)
                    .show();
            //포커스 이동(JS의 focus()함수와 동일하다.)
            etId.requestFocus();
            return;
        }
        else if (sPwd.length() < 5){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("비밀번호를 정확히 입력해주세요")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("닫기", null)
                    .show();
            etPwd.requestFocus();
            return;
        }
    }
}