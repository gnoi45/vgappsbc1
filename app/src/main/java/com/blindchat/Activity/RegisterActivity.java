package com.blindchat.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.blindchat.R;
import com.blindchat.Utility.ApiConfig;
import com.blindchat.Utility.ApiConsumer;
import com.blindchat.Utility.ApiResponse;
import com.blindchat.Utility.AppConfig;
import com.blindchat.Utility.AppPref;
import com.blindchat.Utility.AppUrl;
import com.blindchat.Utility.ServerResponse;

import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, ApiResponse {

    EditText name, phone, email, age;
    RadioButton male,female,trans;
    Button signUp;
    RadioGroup group,interest;
    CircleImageView profile_picture;
    ImageView upload_pic;
    String mediaPath;

    String sexx="";
    String interestx="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        profile_picture = (CircleImageView) findViewById(R.id.profilePic);
        upload_pic = (ImageView) findViewById(R.id.upload_pic);


        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);

        group = (RadioGroup) findViewById(R.id.group);
        interest=(RadioGroup) findViewById(R.id.interest);
        male = (RadioButton) findViewById(R.id.maleRadio);
        female = (RadioButton) findViewById(R.id.femaleRadio);
        trans = (RadioButton) findViewById(R.id.transRadio);

        signUp = (Button) findViewById(R.id.registerBtn);

        signUp.setOnClickListener(this);
        profile_picture.setOnClickListener(this);
        upload_pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                registerUser();
                break;
            case R.id.profilePic:
            case R.id.upload_pic:
                getProfilePic();
                break;
        }
    }

    private void getProfilePic() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            pickImage();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            // When an Image is picked
//            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
//
//                // Get the Image from data
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                assert cursor != null;
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                mediaPath = cursor.getString(columnIndex);
//
////                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////                Bitmap bitmap = decodeFile(new File(mediaPath),600,200);
////                FileOutputStream out = new FileOutputStream(mediaPath);
////                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                // Set the Image in ImageView for Previewing the Media
////                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
//                //     imgView.setImageBitmap(bitmap);
//                cursor.close();
//
//     //           uploadFile();
//            }
//        } catch (Exception e) {
//
//        }
//    }

    private void registerUser() {
        String string_name = name.getText().toString();
        String string_phone = phone.getText().toString();
        String string_email = email.getText().toString();
        String string_age = age.getText().toString();
        int selectedId = group.getCheckedRadioButtonId();
        int selectinterest=interest.getCheckedRadioButtonId();


        switch (selectedId)
        {
            case R.id.maleRadio:
                sexx="MALE";
                break;
            case R.id.femaleRadio:
                sexx="FEMALE";
                break;
            case R.id.transRadio:
                sexx="SHEMALE";
                break;
        }


        switch(selectinterest)
        {
            case R.id.malecheck:
                interestx="MALE";
                break;
            case R.id.femalecheck:
                interestx="FEMALE";
                break;
            case R.id.bothcheck:
                interestx="BOTH";
                break;
        }

        if(string_name.equalsIgnoreCase(""))
        {
            name.setError("Please Enter Name");
            return;
        }
        else if(string_email.equalsIgnoreCase(""))
        {
            email.setError("Please Enter Email Id");
            return;
        }
        else if(string_phone.equalsIgnoreCase(""))
        {
            phone.setError("Please Enter Phone Number");
            return;
        }
        else if(string_age.equalsIgnoreCase(""))
        {
            age.setError("Please Enter Age");
            return;
        }

        if(mediaPath!=null && !mediaPath.equalsIgnoreCase(""))
        {
            uploadFile();
        }
        else
        {
            register(string_name,string_email,string_phone,string_age,"");
        }



    }

    private void register(String string_name,String string_email,String string_phone,String string_age,String image)
    {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("name=").append(string_name);
        stringBuilder.append("&email=").append(string_email);
        stringBuilder.append("&mobile=").append(string_phone);
        stringBuilder.append("&age=").append(string_age);
        stringBuilder.append("&gender=").append(sexx+"");
        stringBuilder.append("&interest=").append(interestx+"");
        stringBuilder.append("&photo=").append(image);
        stringBuilder.append("&token=").append(AppPref.getInstance().getTOKEN());

        String content=stringBuilder.toString();


        ApiConsumer apiConsumer=new ApiConsumer(this, AppUrl.REGISTER_URL,0,content,true,"loading ...",this);
        apiConsumer.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            try {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                profile_picture.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {

        File file = new File(mediaPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        //socket.emit("messagedetection", AppPref.getInstance().getUSERID()+";"+to_id,"image;"+serverResponse.getMessage());

                        //sendmsg(serverResponse.getMessage(),"image");
                        register(name.getText().toString(),email.getText().toString(),phone.getText().toString(),age.getText().toString(),serverResponse.getMessage());



                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this,"Some Error Occurred",Toast.LENGTH_LONG).show();
                    assert serverResponse != null;
                    if(serverResponse!=null)
                        Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.v("failed",t.getMessage());
            }
        });
    }

    @Override
    public void getApiResponse(String responseData, int serviceCounter) {
        try {
            Log.d("responsedata",responseData);
            JSONObject jsonObject=new JSONObject(responseData);

            String id=jsonObject.getString("id");
            String name=jsonObject.getString("name");
            String mobile=jsonObject.getString("mobile");
            String email=jsonObject.getString("email");
            String photo=jsonObject.getString("photo");

            //String points=jsonObject.getString("blind_credit");

            AppPref.getInstance().setUSERID(id);
            AppPref.getInstance().setEMAIL(email);
            AppPref.getInstance().setMOBILE(mobile);
            AppPref.getInstance().setNAME(name);
            AppPref.getInstance().setUNIQID("BC000"+id);
            AppPref.getInstance().setPHOTO(photo);
            AppPref.getInstance().setPOINTS("100");

            Intent intent=new Intent(this, OtpActivity.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}