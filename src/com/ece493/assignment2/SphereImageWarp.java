package com.ece493.assignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

public class SphereImageWarp extends ImageWarp
{
	private Point point;
	public SphereImageWarp(Context context, Bitmap image, Point point)
	{
		super(context, image);
		this.point = point;
	}

	@Override
	public void execute()
	{
		Log.d("ImageWarp", "Running SphereImageWarp");
		this.rs();
	}
	public void rs()
	{
		System.out.println("X = ");
		System.out.println(point.x);
		System.out.println("Y = " + Float.toString(point.y));
		RenderScript tRS = RenderScript.create(getContext());
		Allocation tInAllocation = Allocation.createFromBitmap(tRS, super.getImage(), Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
		Allocation tOutAllocation = Allocation.createTyped(tRS, tInAllocation.getType());
		ScriptC_sphere tScript = new ScriptC_sphere(tRS, getContext().getResources(), R.raw.sphere);
		tScript.set_width(super.getImage().getWidth());
		tScript.set_height(super.getImage().getHeight());
		tScript.bind_input(tInAllocation);
		tScript.bind_output(tOutAllocation);
		tScript.invoke_XXX(point.x,point.y);
		tOutAllocation.copyTo(super.getImage());
	}


}
