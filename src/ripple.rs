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
void ripple(int);
void XXX(int x, int y) {

	ripple(x);
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


void ripple(int param)
{
    for(int x = 0; x < width; x ++)
    {
        for(int y = 0; y < height; y++)
        {
            float nx = (float)y / 16;
            float ny = (float)x / 16;  
            float fx, fy;
            fx = (float) sin(nx);
            fy = (float) sin(ny);
            setPixelAt(x,y,getPixelAt(x + param * fx, y + 0 * fy));
        }
    }
}