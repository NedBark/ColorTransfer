package com.example.colortransfer.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ImageM
{
    public int width;
    public int height;

    private List<PixelF> pixels;

    public ImageM(int _width, int _height)
    {
        width=_width;
        height=_height;
        pixels=new ArrayList<PixelF>();
        for(int i=0; i<width*height;i++)
        {
            pixels.add(new PixelF());
        }
    }

    public ImageM(Bitmap img)
    {
        Bitmap resized=Bitmap.createScaledBitmap(img,img.getWidth()/4,img.getHeight()/4,false);
        width=resized.getWidth();
        height=resized.getHeight();
        pixels=new ArrayList<PixelF>();
        for(int i=0; i<resized.getHeight();i++)
        {
            for(int j=0;j<resized.getWidth();j++)
            {
                int pixel = resized.getPixel(j,i);
                PixelF tmp = new PixelF(Color.red(pixel),Color.green(pixel),Color.blue(pixel));
                pixels.add(tmp);
            }
            Log.d("Const:","Finished row: "+Integer.toString(i));
        }
        Log.d("Const:","Finished");
    }

    public PixelF getPixel(int row,int col)
    {
        int index = row * width + col;
        return pixels.get(index);
    }

    public Bitmap toBitmap()
    {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for(int i=0; i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                int R = (int)getPixel(i,j).getR();
                if(R>255) R=255;
                int G = (int)getPixel(i,j).getG();
                if(G>255) G=255;
                int B = (int)getPixel(i,j).getB();
                if(B>255) B=255;
                bmp.setPixel(j,i,Color.rgb(R,G,B));
            }
        }
        return bmp;
    }
}
