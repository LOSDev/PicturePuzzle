package com.example.picpuzzle;

import java.lang.reflect.Field;

import com.example.picturepuzzle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Integer [] images;
	private Context myContext;
	private Bitmap[] cache;
	
	public ImageAdapter(Context c) {

		myContext = c;

		// Dynamically figure out which images we've imported
		// into the drawable folder, so we don't have to manually
		// type each image in to a fixed array.
		
		// obtain a list of all of the objects in the R.drawable class
		Field[] list = R.drawable.class.getFields();
 
		
		int count = 0, index = 0, j = list.length;

		// We first need to figure out how many of our images we have before
		// we can request the memory for an array of integers to hold their contents.

		// loop over all of the fields in the R.drawable class
		for(int i=0; i < j; i++)
			// if the name starts with img_ then we have one of our images!
			if(list[i].getName().startsWith("pic_")) count++;

		// We now know how many images we have. Reserve the memory for an 
		// array of integers with length 'count' and initialize our cache.
		images = new Integer[count];
		cache = new Bitmap[count];

		// Next, (unsafely) try to get the values of each of those fields
		// into the images array.
		try {
			for(int i=0; i < j; i++)
				if(list[i].getName().startsWith("pic_"))
					images[index++] = list[i].getInt(null);
		} catch(Exception e) {}
		// safer: catch IllegalArgumentException & IllegalAccessException

	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return images[arg0];
	}

	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView;
		if(convertView == null) {

			// create a new view
			imgView = new ImageView(myContext);
			imgView.setLayoutParams(new GridView.LayoutParams(100,100));

		} else {
	
			// recycle an old view (it might have old thumbs in it!)
			imgView = (ImageView) convertView;
	
		}
		if(cache[position] == null) {
			
			// create a new Bitmap that stores a resized
			// version of the image we want to display. 
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			Bitmap thumb = BitmapFactory.decodeResource(myContext.getResources(), images[position], options);

			// store the resized thumb in a cache so we don't have to re-generate it
			cache[position] = thumb;
		}
		imgView.setImageBitmap(cache[position]);
		return imgView;

	}

	
	
	
}
