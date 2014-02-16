package com.ece493.assignment2;

import android.graphics.Bitmap;

public class Swirl {

	public static Bitmap test(Bitmap bm)
	{
		int width  = bm.getWidth();
        int height = bm.getHeight();
        double x0 = 0.5 * (width  - 1);
        double y0 = 0.5 * (height - 1);

        Bitmap bm2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
   

        // swirl
        for (int sx = 0; sx < width; sx++) {
            for (int sy = 0; sy < height; sy++) {
                double dx = sx - x0;
                double dy = sy - y0;
                double r = Math.sqrt(dx*dx + dy*dy);
                double angle = Math.PI / 256 * r;
                int tx = (int) (+dx * Math.cos(angle) - dy * Math.sin(angle) + x0);
                int ty = (int) (+dx * Math.sin(angle) + dy * Math.cos(angle) + y0);

                // plot pixel (sx, sy) the same color as (tx, ty) if it's in bounds
                if (tx >= 0 && tx < width && ty >= 0 && ty < height)
                    bm2.setPixel(sx, sy, bm.getPixel(tx, ty));
            }
        }

        return bm2;
	}
 

   
}
