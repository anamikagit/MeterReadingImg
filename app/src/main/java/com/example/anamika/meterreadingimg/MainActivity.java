package com.example.anamika.meterreadingimg;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    Button meter_image;
    String formattedDate, temp;

    ApiInterface apiService = RestClient.getClient(RestClient.baseUrl).create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meter_image = findViewById(R.id.meter_reading);
        meter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraMethod();
            }
        });
    }

    public void cameraMethod() {
        Intent cameraIntent = new
                Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            System.out.println("Bitmap" + photo);
            BitMapToString(photo);
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeBytes(b);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
        formattedDate = df.format(c.getTime());
        System.out.println("InsideCamera");
        SendingImage();
        return temp;
    }
    public void SendingImage(){

        Call<List<ImageServerResponce>> call = apiService.sendingImage(temp);

        call.enqueue(new Callback<List<ImageServerResponce>>() {

            @Override
            public void onResponse(Call<List<ImageServerResponce>> call, Response<List<ImageServerResponce>> response) {

                List<ImageServerResponce> imageServerResponces = response.body();
                if(imageServerResponces != null && imageServerResponces.size()>0){
                    ImageServerResponce imageServerResponce = imageServerResponces.get(0);
                    Toast.makeText(MainActivity.this,"Image sent Successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Error Sending image",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ImageServerResponce>> call, Throwable t) {

            }
        });
    }
}
