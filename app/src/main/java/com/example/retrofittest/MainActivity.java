package com.example.retrofittest;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText nameEdt, refEdt;
    private Button saveBtn, imageBtn;
    private ImageView image;

    String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdt = findViewById(R.id.name);
        refEdt = findViewById(R.id.reference);

        saveBtn = findViewById(R.id.save);
        imageBtn = findViewById(R.id.select_image);

        image = findViewById(R.id.imageview);

        clickListeners();
    }

    private void clickListeners() {

    imageBtn.setOnClickListener(view -> {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 10);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    });

    saveBtn.setOnClickListener(view -> {
        addCustomer(nameEdt.getText().toString(), refEdt.getText().toString());
    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10 && resultCode== Activity.RESULT_OK) {
            Uri uri = data.getData();
            Context context = MainActivity.this;

            path = RealPathUtil.getRealPath(context, uri);

            Bitmap bitmap = BitmapFactory.decodeFile(path);

            image.setImageBitmap(bitmap);

        }
    }

    public void addCustomer(String name, String ref) {
        String BASE_URL = "https://arbol-mid.herokuapp.com/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody cus_name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody cus_ref = RequestBody.create(MediaType.parse("multipart/form-data"), ref);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<AddCustomerRes> call = apiService.addCustomer(body, cus_name, cus_ref);

        call.enqueue(new Callback<AddCustomerRes>() {
            @Override
            public void onResponse(Call<AddCustomerRes> call, Response<AddCustomerRes> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().toString().equals("200")) {
                        Toast.makeText(getApplicationContext(), "Customer added", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "omg :(", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddCustomerRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}