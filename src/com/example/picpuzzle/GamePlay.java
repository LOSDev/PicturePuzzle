package com.example.picpuzzle;

import com.example.picturepuzzle.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class GamePlay extends Activity implements OnClickListener{
	
		
	int difficulty=2;
	int padding =2;
	static Bitmap[] images =null;
	
	static int width;
	static int height;
	static int blackpos;
	static Bitmap bmBlack;
	static TableLayout layout;
	static int moves;
	static int[] hashs;
	int size;
	static int resource=-1;
	static Bundle extras=null;
	//public static final int WAIT_TIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_play);
		
		/*
		 	if(resource!=-1) {
			Bitmap temp= createBitmap();
			previewToast(temp);
		}
		*/
		// retrieve the set of data from the Intent
		if(extras==null)extras = getIntent().getExtras();
		getPrefs();
    	
		size = difficulty * difficulty;
		
			
        //if Activity was paused, show images-Array
        if(images==null){
			setUpImages();
		}else{
			showImages();
		}
	}

	// Create the small Bitmaps out of the Full Image -> SHow the Preview
	private void setUpImages() {
		size = difficulty * difficulty;
		//KeepingTrack of the position of the black Tile
		blackpos=size-1;

        // retrieve the set of data from the Intent
        extras = getIntent().getExtras();

        // and retrieve the imageToDisplay ID from the extras bundle
       resource = (int)extras.getLong("imageToDisplay");
        
        
       
       //Make smaller Images to save memory
       	/*
       	BitmapFactory.Options options = new BitmapFactory.Options();
       	
		options.inSampleSize = 2;
		bmBlack = BitmapFactory.decodeResource(getResources(), R.drawable.black, options);
        Bitmap background = BitmapFactory.decodeResource(this.getResources(), resource, options);
        
        
        */
        Bitmap background= createBitmap();
        
        width = background.getWidth()/difficulty;
        height = background.getHeight()/difficulty;
        images = new Bitmap[size];
        
        hashs = new int[size];
       
        //create Tiles in order and saves the in "images"-Array 
        //create "hashs"-array to compare with "images"-array later
        for (int x=0; x < difficulty ; x++){
        	for (int y=0; y < difficulty ; y++){
        		int startx = x * width;
        		int starty = y * height;
        		Bitmap cropped = Bitmap.createBitmap(background, startx, starty, width, height);
        		int position = y*difficulty + x;
        		images[position] = cropped;
        		
        		hashs[position]=cropped.hashCode();
        	}
        }
        
        images[blackpos]  = bmBlack;
        
        hashs[blackpos] =bmBlack.hashCode();
        
        
       getTilesInStartingPosition();
       showImages();
       previewToast(background);
		
	}


	private Bitmap createBitmap() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		bmBlack = BitmapFactory.decodeResource(getResources(), R.drawable.black, options);
        return BitmapFactory.decodeResource(this.getResources(), resource, options);
       
	}

	private void getTilesInStartingPosition() {
		for (int i=0; i<size/2;i++){
			Bitmap temp = images[i];
			images[i]= images[size-2-i];
			images[size-2-i]=temp;
			
		}
		if(size%2==0){
			Bitmap temp = images[size-2];
			images[size-2]= images[size-3];
			images[size-3]=temp;
		}
	}

	//create a custom Dialog(prettier)/Toast(more stable) that disappears after 3 Seconds
	private void previewToast(Bitmap background) {
		
        ImageView t = new ImageView(this);
        t.setImageBitmap(background);
        Toast toast = new Toast(this);
        toast.setView(t);
        toast.show();
        
        /* Custom Dialog crashed sometimes
        
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(t);
        
        dialog.show();
        
        //start Thread that dismisses the Dialog after 3 seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, WAIT_TIME);
        */
	}

	//display the image-Array on screen in TableLayout
	private void showImages() {
		int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
		int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
		
		
		
		layout = (TableLayout)findViewById(R.id.MyTableView);
		layout.removeAllViews();

        for (int y=0; y<difficulty; y++) {
            TableRow tr = new TableRow(this);
            //tr.setPadding(padding, padding,padding,padding);
            for (int x=0; x<difficulty; x++) {
            	 //create the Bitmaps with OnClickListener
            	int position = y*difficulty + x;
                Bitmap teil = images[position];
                ImageView t = new ImageView(this);
                t.setImageBitmap(teil);
                t.setId(position);
                t.setOnClickListener(this);
                double ratio =(double) width/height;
                int tilewid = (screenWidth/difficulty)- (padding);
                double tilehei = tilewid/ratio;
                int tileh =(int) tilehei;
                if (tileh*difficulty+difficulty*padding>screenHeight){
                	
                	tileh=screenHeight/difficulty-difficulty;
                	double tilew =   tileh * ratio;
                	tilewid = (int) tilew;
                	
                	
                }
                View v = createbLine(padding, TableRow.LayoutParams.MATCH_PARENT);
    			
                tr.addView(t, tilewid,tileh);
                tr.addView(v);
               
            } 
            layout.addView(tr);
            View v = createbLine(TableRow.LayoutParams.MATCH_PARENT,padding);
			
			layout.addView(v);
            
        } // for
        
        
        super.setContentView(layout);
       
        boolean win = checkWin();
        if (win) {
        	Intent next = new Intent(this, YouWin.class);
        	next.putExtra("image", resource);
    		next.putExtra("moves", moves);
    		setPrefs();
    		moves=0;
    		images=null;
    		resource=-1;
    		startActivity(next);
    		finish();
        	
        }
		
	}

	


	private View createbLine(int i, int j) {
		View v = new View (this);
		v.setLayoutParams(new TableRow.LayoutParams(i, j));
		v.setBackgroundColor(Color.rgb(0, 0, 0));
		return v;
	}

	private boolean checkWin() {
		for (int i=0;i<size;i++){
			// for Android 3.1 an up -->    if(!images[i].sameAs(imagesCheck[i])) return false;
			int md = images[i].hashCode();
			
			int hashcode = hashs[i];
			if(md!=hashcode) return false;
		}
		return true;
		
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_play, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.easy:
			setDifficulty(3);
			setUpImages();
			return false;
		case R.id.advanced:
			setDifficulty(4);
			setUpImages();
			return false;
		case R.id.hard:
			setDifficulty(5);
			setUpImages();
			return false;
		case R.id.restart:
			setDifficulty(2);
			setUpImages();
			return false;
		case R.id.quit:
			images=null;
			extras=null;
			resource=-1;
			moves=0;
			finish();
			return false;
		}
		return true;
	}

	private void setDifficulty(int i) {
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefs.edit();
    	
    	//saves difficulty and number of moves
    	moves=0;
    	difficulty=i;
    	editor.putInt("difficulty", i);
    	
    	// we must commit the preferences or they won't be saved!
    	editor.commit();
	}


	@Override
	public void onClick(View v) {
		int pos = v.getId();
		if(pos==blackpos-1 && blackpos%difficulty!=0){
			switchTiles(pos);
		}
		else if(pos==blackpos+1 && pos%difficulty!=0){
			switchTiles(pos);
		}
		else if(pos==blackpos-difficulty){
			switchTiles(pos);
		}
		else if(pos==blackpos+difficulty){
			switchTiles(pos);
		}
	}

	//after Tile was Clicked images are switched/Position of black Tile and number of moves updated
	private void switchTiles(int pos) {
		moves++;
		
		images[blackpos]=images[pos];
		images[pos] = bmBlack;
		blackpos=pos;
		
		showImages();
		
	}

	public void onPause() {
    	// call the parent's onPause() method
    	super.onPause();
    	setPrefs();
    	
    }
	
	private void setPrefs() {
		// build our preferences object with our data to save
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefs.edit();
    	
    	//saves difficulty and number of moves
    	editor.putInt("moves", moves);
    	editor.putInt("difficulty", difficulty);
    	editor.putInt("image", resource);
    	// we must commit the preferences or they won't be saved!
    	editor.commit();
		
	}


	public void onResume() {
    	// call the parent's onPause() method
    	super.onResume();
    	getPrefs();
    		
    }


	private void getPrefs() {
SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    	
    	moves= prefs.getInt("moves",0);
    	difficulty=prefs.getInt("difficulty",2);
    	//resource = prefs.getInt("image", (int)extras.getLong("imageToDisplay")); 
	}
}
