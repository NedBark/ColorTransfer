package com.example.colortransfer.utils;

public class PixelF {
    private double R;
    private double G;
    private double B;
    public PixelF()
    {
        R=0;
        G=0;
        B=0;
    }
    public PixelF(double valR, double valG, double valB)
    {
        this.R=valR;
        this.G=valG;
        this.B=valB;
    }

    public void setR(double newR)
    {
        R=newR;
    }

    public void setG(double newG)
    {
        G=newG;
    }

    public void setB(double newB)
    {
        B=newB;
    }

    public double getR()
    {
        return R;
    }

    public double getG()
    {
        return G;
    }

    public double getB()
    {
        return B;
    }

    public String getPixel()
    {
        return "["+Double.toString(R)+";"+Double.toString(G)+";"+Double.toString(B)+"]";
    }
}
