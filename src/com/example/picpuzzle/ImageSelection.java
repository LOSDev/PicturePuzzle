package com.example.picpuzzle;

import com.example.picturepuzzle.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageSelection extends Activity implements OnItemClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_selection);
		
		GridView grid = (GridView) findViewById(R.id.gridview);
        	
		grid.setAdapter(new ImageAdapter(this));
			
		grid.setOnItemClickListener(this);
        	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_image_selection, menu);
		return true;
	}

	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		
		Intent i = new Intent(this, GamePlay.class);
		i.putExtra("imageToDisplay", id);
		startActivity(i);
		
		
	}
	
}
