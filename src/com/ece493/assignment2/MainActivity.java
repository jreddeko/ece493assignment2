package com.ece493.assignment2;
import java.io.FileNotFoundException;
import java.io.IOException;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
//import android.renderscript.*;
import android.support.v8.renderscript.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity
{

	private Bitmap bm;
	private static final int SELECT_PICTURE = 1;
	private static final int REQUEST_IMAGE_CAPTURE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem buttonSettings = menu.add("Settings");  
		buttonSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		buttonSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class); 
				MainActivity.this.startActivity(intent);
				return false; 
			}
		});
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) 
		{
	        ImageView mImageView = (ImageView)findViewById(R.id.imageView1);
			if (requestCode == SELECT_PICTURE) 
			{
				System.out.println("SELECT PICTURE");
				Uri selectedImageUri = data.getData();

				try
				{
					bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
			
			        rs();
			        mImageView.setImageBitmap(bitmap);
			
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (requestCode == REQUEST_IMAGE_CAPTURE) 
			{
				System.out.println("REQUEST_IMAGE_CAPTURE");
		        Bundle extras = data.getExtras();
		        Bitmap imageBitmap = (Bitmap) extras.get("data");
		        bitmap = imageBitmap;
		        rs();
		        mImageView.setImageBitmap(bitmap);
		    }
			
		}
	}
	Bitmap bitmap;
	public void rs()
	{
		RenderScript tRS = RenderScript.create(this);

		Allocation tInAllocation = Allocation.createFromBitmap(tRS, bitmap,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
		Allocation tOutAllocation = Allocation.createTyped(tRS, tInAllocation.getType());
		ScriptC_transform tScript = new ScriptC_transform(tRS, getResources(), R.raw.transform);
		tScript.set_width(bitmap.getWidth());
		tScript.set_height(bitmap.getHeight());
		tScript.bind_input(tInAllocation);
		tScript.bind_output(tOutAllocation);
		tScript.invoke_XXX(50,50);
		tOutAllocation.copyTo(bitmap);


	}
	public void buttonProcessImage(View v)
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	    
	}

	public void buttonLoadImage(View v)
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,
				"Select Picture"), SELECT_PICTURE);
	}

	@SuppressWarnings("deprecation")
	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}
	
	private class ApplyFilterTask extends AsyncTask<Void, Void, int[][]>
	{
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(MainActivity.this, "",
					"Loading...", false);
		}

		@Override
		protected void onPostExecute(int[][] result)
		{
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			ImageView im = (ImageView)findViewById(R.id.imageView1);
			im.setImageBitmap(bitmap);
		}


		@Override
		protected int[][] doInBackground(Void... params)
		{
			rs();
			return null;
		}}
}
