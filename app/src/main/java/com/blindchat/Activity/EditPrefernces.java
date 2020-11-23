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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

public class EditPrefernces extends BaseActivity implements View.OnClickListener
{
    String interestx="";
    CircleImageView profile_picture;
    ImageView upload_pic;
    String mediaPath;
    Button signUp;
    RadioGroup interest;

    @Override
    public void initialize(Bundle save) {
        setTitle("Update Profile");
        profile_picture = (CircleImageView) findViewById(R.id.profilePic);
        upload_pic = (ImageView) findViewById(R.id.upload_pic);

        signUp = (Button) findViewById(R.id.registerBtn);
        interest=(RadioGroup) findViewById(R.id.interest);

        signUp.setOnClickListener(this);
        profile_picture.setOnClickListener(this);
        upload_pic.setOnClickListener(this);
    }

    @Override
    public int getActivityLayout() {
        return R.layout.edit_preferences;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
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
        if (ContextCompat.checkSelfPermission(EditPrefernces.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditPrefernces.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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

    private void registerUser() {

        int selectinterest=interest.getCheckedRadioButtonId();



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


        if(mediaPath!=null && !mediaPath.equalsIgnoreCase(""))
        {
            uploadFile();
        }
        else
        {
            //register(string_name,string_email,string_phone,string_age,"");

            updateProfile("");
        }



    }

    private void updateProfile(String mediax)
    {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("name=").append(interestx);
            stringBuilder.append("&photo=").append(mediax);
            stringBuilder.append("&id=").append(AppPref.getInstance().getUSERID());

            String content = stringBuilder.toString();

            ApiConsumer apiConsumer = new ApiConsumer(EditPrefernces.this, AppUrl.EDIT_PROFILE, 1, content, true, "loading ...", new ApiResponse() {
                @Override
                public void getApiResponse(String responseData, int serviceCounter) {
                    try {
                        Log.d("responseData", responseData);
                        JSONObject jsonObject = new JSONObject(responseData);

                        String msg = jsonObject.getString("message");
                        //AppPref.getInstance().set(edt_name.getText().toString());
                        toastMessage(msg);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            apiConsumer.execute();
        } catch (Exception e) {
            toastMessage("Some Error Occurred, Please Try again");
            e.printStackTrace();
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
     //                   register(name.getText().toString(),email.getText().toString(),phone.getText().toString(),age.getText().toString(),serverResponse.getMessage());

                        AppPref.getInstance().setPHOTO(serverResponse.getMessage());
                        updateProfile(serverResponse.getMessage());


                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditPrefernces.this,"Some Error Occurred",Toast.LENGTH_LONG).show();
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
}
