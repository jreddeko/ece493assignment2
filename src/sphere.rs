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
void sphere(int,int);
void XXX(int x, int y) {

	sphere(x,y);
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


void sphere(int x, int y)
{
    float centreX = x;
    float centreY = y;
    float a = width/8;
    float b = height/8;
    float a2 = a*a;
    float b2 = b*b;
    float refractionIndex = 1.5;
    for(int x = 0; x < width; x++)
    {
        for(int y = 0; y < height; y++)
        {
            float dx = x-centreX;
            float dy = y-centreY;
            float x2 = dx*dx;
            float y2 = dy*dy;
            if (y2 >= (b2 - (b2*x2)/a2)) 
            {
                setPixelAt(x,y,getPixelAt(x, y));
            } 
            else 
            {
                float rRefraction = 1.0f / refractionIndex;

                float z = (float)sqrt((1.0f - x2/a2 - y2/b2) * (a*b));
                float z2 = z*z;

                float xAngle = (float)acos(dx / sqrt(x2+z2));
                float angle1 = M_PI/2 - xAngle;
                float angle2 = (float)asin(sin(angle1)*rRefraction);
                angle2 = M_PI/2 - xAngle - angle2;
                float yAngle = (float)acos(dy / sqrt(y2+z2));
                angle1 = M_PI/2 - yAngle;
                angle2 = (float)asin(sin(angle1)*rRefraction);
                angle2 = M_PI/2 - yAngle - angle2;
                setPixelAt(x,y,getPixelAt(x - (float)tan(angle2)*z, y - (float)tan(angle2)*z));
            }
        }
    }

}
