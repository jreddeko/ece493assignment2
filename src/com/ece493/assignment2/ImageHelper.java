package com.ece493.assignment2;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;


public class ImageHelper
{
	public static Integer MAX_FILTER_SIZE = 50;

	/**
	 * Creates a 2d array of pixels from a bitmap
	 * @param bitmap
	 * @return
	 */
	public static int[][] getPixels(Bitmap bitmap)
	{
		int[][] pixels = new int[bitmap.getHeight()][bitmap.getWidth()];
		for(int i = 0; i < bitmap.getHeight();i++)
		{
			for(int j = 0; j < bitmap.getWidth();j++)
			{
				int p = bitmap.getPixel(j, i);
				pixels[i][j] = p;
			}
		}
		return pixels;
	}

	/**
	 * Creates a bitmap from a 2d array of pixels
	 * @param n
	 * @return
	 */
	public static Bitmap setPixels(int [][] n)
	{
		Bitmap bm = Bitmap.createBitmap(n[0].length,n.length,Config.ARGB_8888);
		for (int i = 0; i < n.length; i++) {
			for (int j = 0; j < n[i].length; j++) {
				bm.setPixel(j, i, n[i][j]);
			}
		}
		return bm;
	}

	/**
	 * Creates and returns a bitmap from the image found at path.
	 * @param w
	 * @param h
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmap(int w, int h, String path) 
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calcBitmapSize(options, w, h);
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(path, options);
		MAX_FILTER_SIZE = bm.getWidth() > bm.getHeight() ? bm.getHeight() : bm.getWidth();
		return bm;
	}

	/**
	 * Helps to calculate appropriate  size of image
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calcBitmapSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

}
