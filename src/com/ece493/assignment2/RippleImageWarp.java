package com.ece493.assignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

public class RippleImageWarp extends ImageWarp
{
	private float param;
	public RippleImageWarp(Context context, Bitmap image, float param)
	{
		super(context, image);
		this.param = param/2;
	}
	
	@Override
	public void execute()
	{
		Log.d("ImageWarp", "Running RippleImageWarp");
		this.rs();
	}

	private void rs()
	{
		RenderScript tRS = RenderScript.create(getContext());
		Allocation tInAllocation = Allocation.createFromBitmap(tRS, super.getImage(), Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);
		Allocation tOutAllocation = Allocation.createTyped(tRS, tInAllocation.getType());
		ScriptC_ripple tScript = new ScriptC_ripple(tRS, getContext().getResources(), R.raw.ripple);
		tScript.set_width(super.getImage().getWidth());
		tScript.set_height(super.getImage().getHeight());
		tScript.bind_input(tInAllocation);
		tScript.bind_output(tOutAllocation);
		tScript.invoke_XXX((int)param,50);
		tOutAllocation.copyTo(super.getImage());
	}

}
