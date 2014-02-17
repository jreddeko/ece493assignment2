package com.ece493.assignment2;
import java.util.Stack;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;
import android.text.InputType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
	private boolean ImageChanged = false;
	private Stack<Bitmap> backUp;
	private static final int SELECT_PICTURE = 1;
	private static final int REQUEST_IMAGE_CAPTURE = 2;
	private GestureDetectorCompat mDetector; 
	private OnTouchListener mGestureListener;
	private GestureDetectorCompat mGestureDetector;
	private boolean mIsScrolling = false;
	protected float x;
	private float startX;
	private float startY;
	private int y;
	private int backupCount = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		mDetector = new GestureDetectorCompat(this, this);
		backUp = new Stack<Bitmap>();

		mGestureDetector = new GestureDetectorCompat(this, new SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(MotionEvent e) {

				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {

				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {

				return true;
			}
		});
		mGestureDetector.setOnDoubleTapListener(this);
		mGestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				}
				if(event.getAction() == MotionEvent.ACTION_UP) {
					if(mIsScrolling ) {
						mIsScrolling  = false;
						if(Math.abs(startY - y) > Math.abs(startX - x))
						{
							ImageWarp warp = new SwirlImageWarp(MainActivity.this, getBitmapFromImageView(), startY - y);
							new ApplyFilterTask().execute(warp);
						}
						else
						{
							ImageWarp warp = new RippleImageWarp(MainActivity.this, getBitmapFromImageView(), startX - x);
							new ApplyFilterTask().execute(warp);
						}
					};
				}

				return false;
			}
		};
		ImageView mImageView = (ImageView)findViewById(R.id.imageView1);
		mImageView.setOnTouchListener(mGestureListener);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuItem buttonSettings = menu.add("Save");  
		buttonSettings = menu.add("Backup Count");
		buttonSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		buttonSettings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() 
		{

			public boolean onMenuItemClick(MenuItem item) 
			{
				if(item.getTitle().equals("Save"))
				{
					savePopup();
				}
				if(item.getTitle().equals("Backup Count"))
				{
					backupCountPopup();
				}
				return false; 
			}
		});
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{

		if (resultCode == RESULT_OK) 
		{
			ImageView image = (ImageView) findViewById(R.id.imageView1);
			if (requestCode == SELECT_PICTURE) 
			{
				Uri selectedImageUri = data.getData();
				Bitmap bitmap = ImageHelper.getBitmap(image.getWidth(),image.getHeight(), getPath(selectedImageUri));
				setImage(bitmap);
				ImageChanged = false;
			}
			if (requestCode == REQUEST_IMAGE_CAPTURE) 
			{
				Bundle extras = data.getExtras();
				Bitmap bitmap = (Bitmap) extras.get("data");
				bitmap = Bitmap.createScaledBitmap(bitmap, image.getWidth(), image.getHeight(), true);
				setImage(bitmap);
				ImageChanged = false;;
			}
		}
	}
	
	public void buttonTakePicture(View v)
	{
		if(ImageChanged)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setMessage("You are about to lose all unsaved progress, would you like to save?").setTitle("Warning");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					savePopup();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					removeImage();
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (takePictureIntent.resolveActivity(getPackageManager()) != null) 
						startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		else
		{
			removeImage();
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) 
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}

	}

	public void buttonLoadImage(View v)
	{
		if(ImageChanged)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setMessage("You are about to lose all unsaved progress, would you like to save?").setTitle("Warning");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					savePopup();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					removeImage();
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent,
							"Select Picture"), SELECT_PICTURE);
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		else
		{
			removeImage();
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent,
					"Select Picture"), SELECT_PICTURE);
		}
	}

	@Override
	public void onBackPressed() {
		if(backUp.isEmpty())
		{
			if(this.getBitmapFromImageView()!=null)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(false);
				builder.setMessage("You are about to lose all unsaved progress, would you like to save?").setTitle("Warning");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						savePopup();
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						MainActivity.super.onBackPressed();					
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
			else
			{
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent,
						"Select Picture"), SELECT_PICTURE);
			}
		}
		else
		{
			setImage(this.backUp.pop());
			ImageChanged = true;
		}
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		Point p = scaleXY(e); 
		ImageWarp warp = new SphereImageWarp(MainActivity.this, getBitmapFromImageView(), p);
		new ApplyFilterTask().execute(warp);
		return true;
	}

	@Override 
	public boolean onTouchEvent(MotionEvent event)
	{ 
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{	
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		if(mIsScrolling == false)
		{
			startX = distanceX;
			startY = distanceY;
			y = 0;
			x = 0;
		}
		mIsScrolling = true;
		y += distanceY;
		x += distanceX;
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	// Private Methods
	
	private Point scaleXY(MotionEvent e)
	{
		ImageView view = (ImageView)findViewById(R.id.imageView1);
		int[] offset = new int[2];
		view.getLocationOnScreen(offset);
		float xRel = e.getRawX() - offset[0];
		float yRel = e.getRawY() - offset[1];

		float heightRatio =  (float)this.getBitmapFromImageView().getHeight() / (float) view.getHeight();
		float widthRatio = (float)this.getBitmapFromImageView().getWidth()/ (float) view.getWidth() ;

		xRel *= widthRatio;
		yRel *= 2*heightRatio;
		return new Point((int)xRel, (int)yRel);
	}

	private Bitmap getBitmapFromImageView()
	{
		ImageView im = (ImageView)findViewById(R.id.imageView1);
		if(im.getDrawable()==null)
			return null;
		return ((BitmapDrawable)im.getDrawable()).getBitmap();
	}

	private void savePopup()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		builder.setMessage("Enter filename for image").setTitle("Save");
		builder.setView(input);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				MediaStore.Images.Media.insertImage(getContentResolver(), getBitmapFromImageView(), input.getText().toString() , "");
				ImageChanged = false;
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void backupCountPopup()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setMessage("Enter max number of backups").setTitle("BackupCount");
		builder.setView(input);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				backupCount = Integer.parseInt(input.getText().toString());
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void setImage(Bitmap bitmap)
	{
		ImageView mImageView = (ImageView)findViewById(R.id.imageView1);
		mImageView.setImageBitmap(bitmap);
	}

	private void removeImage()
	{	

		ImageView im = (ImageView)findViewById(R.id.imageView1);
		if(im.getDrawable()==null)
			return;
		Drawable drawable = im.getDrawable();
		if(drawable instanceof BitmapDrawable)
		{
			BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
			bitmapDrawable.getBitmap().recycle();
		}
		backUp = new Stack<Bitmap>();
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

	private class ApplyFilterTask extends AsyncTask<ImageWarp, Void, Bitmap>
	{
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() 
		{
			while (backUp.size() >= backupCount) {
				backUp.remove(0);
	        }
			backUp.push(getBitmapFromImageView().copy(Bitmap.Config.ARGB_8888,false));
			dialog = ProgressDialog.show(MainActivity.this, "",
					"Loading...", false);
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			setImage(result);
			ImageChanged = true;
		}

		@Override
		protected Bitmap doInBackground(ImageWarp... params)
		{
			params[0].execute();
			return params[0].getImage();
		}
	}
}
