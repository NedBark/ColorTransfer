package com.example.colortransfer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.colortransfer.utils.ImageM;
import com.example.colortransfer.utils.ImageCalculations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;

    private Bitmap image1;
    private Bitmap image2;
    private Bitmap image3;

    private static final int CAMERA_REQUEST1 = 1888;
    private static final int CAMERA_REQUEST2 = 1889;
    private static final int MY_CAMERA_PERMISSION_CODE = 101;
    private static final int FS_PERMISSION_CODE = 102;

    private String imagePath1;
    private String imagePath2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView1=findViewById(R.id.inputPicture1);
        mImageView2=findViewById(R.id.inputPicture2);
        mImageView3=findViewById(R.id.resultPicture);

        //Init inp pictures

        mImageView1.setBackgroundColor(0);
        mImageView2.setBackgroundColor(0);
        mImageView3.setBackgroundColor(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void captureImage1(View view)
    {
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FS_PERMISSION_CODE);
        }
        else if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            String fileName = "Photo1";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try{
                File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                imagePath1 = imageFile.getAbsolutePath();

                Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.colortransfer.fileprovider", imageFile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST1);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void captureImage2(View view)
    {
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FS_PERMISSION_CODE);
        }
        else if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {

            String fileName = "Photo2";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try{
                File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                imagePath2 = imageFile.getAbsolutePath();

                Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.colortransfer.fileprovider", imageFile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("image_path", imagePath2);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST2);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public void combineImages(View view)
    {
        /*
        ImageM imgRGB=new ImageM(image1);
        ImageM RGB_LSM = ImageCalculations.RGBtoLMS(imgRGB);
        ImageM LMS_LAB = ImageCalculations.LMStoLAB(RGB_LSM);
        ImageM LAB_LSM=ImageCalculations.LABtoLMS(LMS_LAB);
        ImageM LSM_RGB=ImageCalculations.LMStoRGB(LAB_LSM);
        */

        ImageM img1=new ImageM(image1);
        ImageM img2=new ImageM(image2);

        ImageM LMS_img1 = ImageCalculations.RGBtoLMS(img1);
        ImageM LAB_img1 = ImageCalculations.LMStoLAB(LMS_img1);

        ImageM LMS_img2 = ImageCalculations.RGBtoLMS(img2);
        ImageM LAB_img2 = ImageCalculations.LMStoLAB(LMS_img2);

        double[] means_img1 =new double[3];
        double[] means_img2 =new double[3];

        double[] std_img1 =new double[3];
        double[] std_img2 =new double[3];

        means_img1[0]=ImageCalculations.getChannelMean(LAB_img1,0);
        means_img1[1]=ImageCalculations.getChannelMean(LAB_img1,1);
        means_img1[2]=ImageCalculations.getChannelMean(LAB_img1,2);
        means_img2[0]=ImageCalculations.getChannelMean(LAB_img2,0);
        means_img2[1]=ImageCalculations.getChannelMean(LAB_img2,1);
        means_img2[2]=ImageCalculations.getChannelMean(LAB_img2,2);

        std_img1[0]=ImageCalculations.getStdDev(LAB_img1,means_img1[0],0);
        std_img1[1]=ImageCalculations.getStdDev(LAB_img1,means_img1[1],1);
        std_img1[2]=ImageCalculations.getStdDev(LAB_img1,means_img1[2],2);
        std_img2[0]=ImageCalculations.getStdDev(LAB_img2,means_img2[0],0);
        std_img2[1]=ImageCalculations.getStdDev(LAB_img2,means_img2[1],1);
        std_img2[2]=ImageCalculations.getStdDev(LAB_img2,means_img2[2],2);

        LAB_img2=ImageCalculations.normLAB(LAB_img2,means_img2,std_img2,std_img1);
        LAB_img2=ImageCalculations.addMean(LAB_img2,means_img1);

        ImageM LMS_img3 = ImageCalculations.LABtoLMS(LAB_img2);
        ImageM img3 = ImageCalculations.LMStoRGB(LMS_img3);

        image3=img3.toBitmap();
        mImageView3.setImageBitmap(image3);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String file_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File dir = new File(file_path);
        if(!dir.exists()) dir.mkdirs();
        Date date = new Date();
        File file = new File(dir, "Transfered_"+sdf.format(new Timestamp(date.getTime())).toString() + ".png");
        try
        {
            FileOutputStream fOut = new FileOutputStream(file);
            image3.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            Toast.makeText(this, "Image saved.", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for(File fl:storageDirectory.listFiles())
        {
            fl.delete();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == FS_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Files permission granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Files permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==CAMERA_REQUEST1)
            {
                image1= BitmapFactory.decodeFile(imagePath1);
                mImageView1.setImageBitmap(image1);
            }
            else if(requestCode==CAMERA_REQUEST2)
            {
                image2=BitmapFactory.decodeFile(imagePath2);
                mImageView2.setImageBitmap(image2);
            }
        }
    }
}
