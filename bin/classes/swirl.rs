#pragma version(1)
#pragma rs java_package_name(com.ece493.assignment2)
#include "rs_cl.rsh"
#include "rs_types.rsh"

uchar4* input;
uchar4* output;
int width;
int height;

static uchar4 getPixelAt(int, int);
void setPixelAt(int, int, uchar4);
void XXX();
void swirl();
void XXX(int x, int y) {

	swirl(x);
}



//a convenience method to clamp getting pixels into the image
static uchar4 getPixelAt(int x, int y) {
	if(y>=height) y = height-1;
	if(y<0) y = 0;
	if(x>=width) x = width-1;
	if(x<0) x = 0;
	return input[y*width + x];
}

//take care of setting x,y on the 1d-array representing the bitmap
void setPixelAt(int x, int y, uchar4 pixel) {
	output[y*width + x] = pixel;
}

void swirl(int param)
{
    double x0 = 0.5 * (width  - 1);
    double y0 = 0.5 * (height - 1);


    // swirl
    for (int sx = 0; sx < width; sx++) 
    {
        for (int sy = 0; sy < height; sy++) 
        {
            float dx = sx - x0;
            float dy = sy - y0;

            float r = sqrt(dx*dx + dy*dy);
            float angle = M_PI/param * r;

            int tx = (int) (+dx * cos(angle) - dy * sin(angle) + x0);
            int ty = (int) (+dx * sin(angle) + dy * cos(angle) + y0);
            if (tx >= 0 && tx < width && ty >= 0 && ty < height)
                    setPixelAt(sx, sy, getPixelAt(tx, ty));
        }
    }
}

