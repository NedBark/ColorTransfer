package com.example.colortransfer.utils;

import android.util.Log;

public class ImageCalculations {
    public static ImageM RGBtoLMS(ImageM img)
    {
        Log.d("IC: ","RGBtoLMS");
        ImageM LMS=new ImageM(img.width,img.height);
        for(int i=0;i<img.height;i++)
        {
            for(int j=0;j<img.width;j++)
            {
                PixelF tmp = img.getPixel(i,j);
                double initR=tmp.getR();
                double initG=tmp.getG();
                double initB=tmp.getB();
                //
                double l= 0.3811f * initR + 0.5783f * initG + 0.0402f * initB;
                double m= 0.1967f * initR + 0.7244f * initG + 0.0782f * initB;
                double s= 0.0241f * initR + 0.1288f * initG + 0.8444f * initB;

                double L = Math.log10(l);
                double M = Math.log10(m);
                double S = Math.log10(s);
                //
                LMS.getPixel(i,j).setR(L);
                LMS.getPixel(i,j).setG(M);
                LMS.getPixel(i,j).setB(S);
            }
        }
        return LMS;
    }

    public  static ImageM LMStoLAB(ImageM LMS)
    {
        Log.d("IC: ","LMStoLAB");
        ImageM LAB=new ImageM(LMS.width,LMS.height);
        for(int i=0;i<LMS.height;i++)
        {
            for(int j=0;j<LMS.width;j++)
            {
                PixelF tmp = LMS.getPixel(i,j);
                double L=tmp.getR();
                double M=tmp.getG();
                double S=tmp.getB();
                //
                double l= Math.sqrt(3)/3 * L + Math.sqrt(3)/3 * M + Math.sqrt(3)/3 * S;
                double a= Math.sqrt(6)/6 * L + Math.sqrt(6)/6 * M - Math.sqrt(6)/3 * S;
                double b= Math.sqrt(2)/2 * L - Math.sqrt(2)/2 * M + 0 * S;
                //
                LAB.getPixel(i,j).setR(l);
                LAB.getPixel(i,j).setG(a);
                LAB.getPixel(i,j).setB(b);
            }
        }
        return LAB;
    }

    public static ImageM LABtoLMS(ImageM LAB)
    {
        Log.d("IC: ","LABtoLMS");
        ImageM LMS=new ImageM(LAB.width,LAB.height);
        for(int i=0;i<LAB.height;i++)
        {
            for(int j=0;j<LAB.width;j++)
            {
                PixelF tmp = LAB.getPixel(i,j);
                double L=tmp.getR();
                double A=tmp.getG();
                double B=tmp.getB();
                //
                double _L= Math.sqrt(3)/3 * L + Math.sqrt(6)/6 * A + Math.sqrt(2)/2 * B;
                double _M= Math.sqrt(3)/3 * L + Math.sqrt(6)/6 * A - Math.sqrt(2)/2 * B;
                double _S= Math.sqrt(3)/3 * L - Math.sqrt(6)/3 * A + 0 * B;
                //
                LMS.getPixel(i,j).setR(Math.pow(10,_L));
                LMS.getPixel(i,j).setG(Math.pow(10,_M));
                LMS.getPixel(i,j).setB(Math.pow(10,_S));
            }
        }
        return LMS;
    }

    public  static  ImageM LMStoRGB(ImageM LMS)
    {
        Log.d("IC: ","LMStoRGB");
        ImageM RGB=new ImageM(LMS.width,LMS.height);
        for(int i=0;i<LMS.height;i++)
        {
            for (int j = 0; j < LMS.width; j++)
            {
                PixelF tmp = LMS.getPixel(i,j);
                double l =tmp.getR();
                double m =tmp.getG();
                double s =tmp.getB();

                double R= 4.4679 * l - 3.5873 * m + 0.1193 * s;
                double G= -1.2186 * l + 2.3809 * m - 0.1624 * s;
                double B= 0.0497 * l - 0.2439 * m + 1.2045 * s;

                RGB.getPixel(i,j).setR(R);
                RGB.getPixel(i,j).setG(G);
                RGB.getPixel(i,j).setB(B);
            }
        }
        return RGB;
    }

    public static double getChannelMean(ImageM img, int channel)
    {
        Log.d("IC: ","getChannelMean");
        double mean=0.0;
        for(int i =0 ; i<img.height;i++)
        {
            for(int j=0;j<img.width;j++)
            {
                if(channel==0) mean+=img.getPixel(i,j).getR();
                else if(channel==1) mean+=img.getPixel(i,j).getG();
                else if(channel==2) mean+=img.getPixel(i,j).getB();
            }
        }
        return  mean/(img.width*img.height);
    }

    public static double getStdDev(ImageM img, double mean, int channel)
    {
        Log.d("IC: ","getStdDev");
        double std=0.0;
        for(int i =0 ; i<img.height;i++)
        {
            for(int j=0;j<img.width;j++)
            {
                if(channel==0) std+=Math.pow(img.getPixel(i,j).getR()-mean,2);
                else if(channel==1) std+=Math.pow(img.getPixel(i,j).getG()-mean,2);
                else if(channel==2) std+=Math.pow(img.getPixel(i,j).getB()-mean,2);
            }
        }
        return  Math.sqrt(std/(img.width*img.height));
    }

    public static ImageM normLAB(ImageM LAB, double[] means, double[] stdT, double[] stdS)
    {
        Log.d("IC: ","normLAB");
        ImageM nLAB = new ImageM(LAB.width,LAB.height);
        for(int i =0 ; i<LAB.height;i++)
        {
            for(int j=0;j<LAB.width;j++)
            {
                double l=LAB.getPixel(i,j).getR()-means[0];
                double a=LAB.getPixel(i,j).getG()-means[1];
                double b=LAB.getPixel(i,j).getB()-means[2];
                l=stdT[0]*l/stdS[0];
                a=stdT[1]*a/stdS[1];
                b=stdT[2]*b/stdS[2];

                nLAB.getPixel(i,j).setR(l);
                nLAB.getPixel(i,j).setG(a);
                nLAB.getPixel(i,j).setB(b);
            }
        }
        return  nLAB;
    }

    public static ImageM addMean(ImageM img,double[] means)
    {
        Log.d("IC: ","addMean");
        ImageM nImg = new ImageM(img.width,img.height);
        for (int i = 0; i < img.height; i++)
        {
            for (int j = 0; j < img.width; j++)
            {
                nImg.getPixel(i, j).setR(img.getPixel(i, j).getR() + means[0]);
                nImg.getPixel(i, j).setG(img.getPixel(i, j).getG() + means[1]);
                nImg.getPixel(i, j).setB(img.getPixel(i, j).getB() + means[2]);
            }
        }
        return  nImg;
    }
}