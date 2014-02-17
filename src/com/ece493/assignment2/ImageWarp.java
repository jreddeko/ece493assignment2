package com.ece493.assignment2;

import android.content.Context;
import android.graphics.Bitmap;

public class ImageWarp
{
	private Bitmap image;
	private Bitmap imageBackup;
	private Context context;
	
	public ImageWarp(Context context, Bitmap image)
	{
		this.image = image;
		this.imageBackup = Bitmap.createBitmap(image);
		this.setContext(context);
	}
	
	public Bitmap getImage()
	{
		return this.image;
	}
	
	public Bitmap getBackup()
	{
		return this.imageBackup;
	}
	
	public void execute()
	{
	}

	public Context getContext()
	{
		return context;
	}

	public void setContext(Context context)
	{
		this.context = context;
	}

}
