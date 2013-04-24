package com.example.mytranslator;


import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;

import android.view.Menu;
import android.view.View;

import android.widget.TextView;


public class MyMainActivity extends Activity {

		
	public void onClickTranslate(View view){
		
		//startActivity(new Intent("com.example.mynetworking.TranslationActivity"));
	
		Intent intent = new Intent(MyMainActivity.this, TranslationActivity.class);
		TextView txtInfo = (TextView)findViewById(R.id.editText1);
		intent.putExtra("wordToTranslate", txtInfo.getText().toString());
		
	    startActivity(intent);
		
	}
	
	public void onClickEditText(View view){
		     TextView text = (TextView)findViewById(R.id.editText1);
		     text.setText("");
	}
	
	
	////////////////////////////////////////////////////////////////////////////////  Default ////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_networking);
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_networking, menu);
        return true;
    }
}
