package com.ece493.assignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

public class SwirlImageWarp extends ImageWarp
{
	private float param;
	public SwirlImageWarp(Context context, Bitmap image, float param)
	{
		super(context, image);
		this.param = param > 0 ? image.getWidth() - param : -1 * image.getWidth() - param;
		System.out.println(param);
	}
	
	@Override
	public void execute()
	{
		Log.d("ImageWarp", "Running SwirlImageWarp");
		this.rs();
	}

	private void rs()
	{
		RenderScript tRS = RenderScript.create(getContext());
		Allocation tInAllocation = Allocation.createFromBitmap(tRS, super.getImage(), Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
		Allocation tOutAllocation = Allocation.createTyped(tRS, tInAllocation.getType());
		ScriptC_swirl tScript = new ScriptC_swirl(tRS, getContext().getResources(), R.raw.swirl);
		tScript.set_width(super.getImage().getWidth());
		tScript.set_height(super.getImage().getHeight());
		tScript.bind_input(tInAllocation);
		tScript.bind_output(tOutAllocation);
		tScript.invoke_XXX((int)param,50);
		tOutAllocation.copyTo(super.getImage());
	}
}
