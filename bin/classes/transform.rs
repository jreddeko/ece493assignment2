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
void bulge();
void swirl2();
void ripple();
void sphere();
void XXX(int x, int y) {

	swirl();
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

void swirl()
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
            float angle = M_PI / 1000 * r;
            int tx = (int) (+dx * cos(angle) - dy * sin(angle) + x0);
            int ty = (int) (+dx * sin(angle) + dy * cos(angle) + y0);
            if (tx >= 0 && tx < width && ty >= 0 && ty < height)
                    setPixelAt(sx, sy, getPixelAt(tx, ty));
        }
    }
}

void swirl2()
{
    
    float fDegree = 0.5;

    float midX = width/2;
    float midY = height/2;

    float theta, radius;
    float newX, newY;
    
    int dx,dy;

    for (int x = 0; x < width; x++)
    {
        for (int y = 0; y < height; y++)
        {
            float trueX = x - midX;
            float trueY = y - midY;
            theta = atan2(trueY,trueX);

            radius = sqrt(trueX*trueX + trueY*trueY);

            newX = midX + (radius * cos(theta + fDegree * radius));
            if (newX > 0 && newX < width)
            {

                dx = (int)newX;
            }
            else
                dx = x;

            newY = midY + (radius * sin(theta + fDegree * radius));
            if (newY > 0 && newY < height)
            {

                dy = (int)newY;
            }
            else
                dy = y;
                
            setPixelAt(x,y,getPixelAt(dx,dy));
        }
    }
}

void ripple()
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
            setPixelAt(x,y,getPixelAt(x + 5 * fx, y + 0 * fy));
        }
    }
}

void sphere()
{
    float centreX = 100;
    float centreY = 100;
    float a = 500;
    float b = 500;
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
void bulge()
{

    // bulge
    for (int sx = 0; sx < width; sx++) 
    {
        for (int sy = 0; sy < height; sy++)
        {
            float r = sqrt(pow((float)sx - width/2,2) + pow((float)sy - height/2,2));
            float angle = atan2((float)sx-width/2, (float)sy-height/2);
            float rnx = pow(r,2.5)/width/2;
            float rny = pow(r,2.5)/height/2;
            setPixelAt(sx,sy,getPixelAt(rnx*cos(angle) + width/2, rny
            *sin(angle) + height/2));
        }
    }
}