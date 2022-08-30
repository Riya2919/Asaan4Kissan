package com.example.asaan4kissan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asaan4kissan.ml.LiteModelPlantDiseaseDefault1;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity6 extends AppCompatActivity {
    Button cam, gallery, predict;
    TextView result;
    ImageView imageView;
    Bitmap bitmap;
    int imageSize = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        getPermission();

        String[] Labels= new String[1001];
        int cnt = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("Labels.txt")));
            String line=bufferedReader.readLine();
            while(line!=null){
                Labels[cnt]=line;
                cnt++;
                line=bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cam = findViewById(R.id.button4);
        gallery = findViewById(R.id.button);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        predict = findViewById(R.id.button2);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });
    gallery.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 10);

        }
    });
    predict.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                LiteModelPlantDiseaseDefault1 model = LiteModelPlantDiseaseDefault1.newInstance(MainActivity6.this);

                // Creates inputs for reference.
                Log.d("shape", TensorImage.fromBitmap(bitmap).getBuffer().toString());
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                ByteBuffer byteBuffer = bitmapToByteBuffer(bitmap, 224,224);
                inputFeature0.loadBuffer(byteBuffer);



                // Runs model inference and gets result.
                LiteModelPlantDiseaseDefault1.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                result.setText(Labels[getMax(outputFeature0.getFloatArray())]+"");

                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                // TODO Handle the exception
            }

        }
    });
    }
    public ByteBuffer bitmapToByteBuffer(Bitmap image, int width, int height) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * width * height * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        // get 1D array of width * height pixels in image
        int[] intValues = new int[width * height];
        image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
        int pixel = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int val = intValues[pixel++]; // RGB
                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
            }
        }
        return byteBuffer;
    }
    int getMax(float[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {

                if (arr[i] > arr[max]) max = i;

        }
        return max;
    }


    void getPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity6.this, new String[]{Manifest.permission.CAMERA},11);

        }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @Nullable String[] permissions, @Nullable int[] grantResults) {
        if(requestCode == 11){
            if(grantResults.length>0){
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 10){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 12){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
