package com.example.picpuzzle;

import com.example.picturepuzzle.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YouWin extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_you_win);
		Bundle test = getIntent().getExtras();

        // retrieve the image and the number of moves from the extras bundle
        int bild = (int)test.getInt("image");
        int mo = (int)test.getInt("moves");
        
        
        //insert the image
        ImageView img = (ImageView)findViewById(R.id.image);
        Bitmap background = BitmapFactory.decodeResource(this.getResources(), bild);
        img.setImageBitmap(background);
        
        
        //insert moves into TextView
        String text = String.format(
        	    getString(R.string.message),
        	    mo);
        TextView txt = (TextView)findViewById(R.id.label);
        txt.setText(text);
        
        
        // Set Up Button to go back to Image Selection
        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               finish();
            }
        });
        //copyright info
        String author = getString(R.string.author);
    	Toast.makeText(this, author, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_you_win, menu);
		return true;
	}
	public void onPause() {
		super.onPause();
		
	
	}
}
