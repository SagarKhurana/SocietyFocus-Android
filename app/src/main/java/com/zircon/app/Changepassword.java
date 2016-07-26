package com.zircon.app;

import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zircon.app.model.User;
import com.zircon.app.model.response.BaseResponse;
import com.zircon.app.ui.common.activity.nonav.BaseABNoNavActivity;
import com.zircon.app.utils.AuthCallBack;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.Log;
import com.zircon.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Changepassword extends BaseABNoNavActivity {

    EditText mOldPassView;
    EditText mNewPassView;
    EditText mConfirmNewPassView;

    Button mChangePasswordButton;
    String oldPass;
    String newPass;
    String newPassConfirm;

    User user;

    @Override
    protected View.OnClickListener getFABClickListener() {
        return null;
    }

    @Override
    protected int getContentViewResID() {
        return R.layout.activity_changepassword;
    }

    @Override
    protected void initViews() {
        setTitle("Change Password");

        mChangePasswordButton= (Button) findViewById(R.id.changepassword);
        mOldPassView= (EditText) findViewById(R.id.oldpassword);
        mNewPassView= (EditText) findViewById(R.id.newpassword);
        mConfirmNewPassView= (EditText) findViewById(R.id.confirmpassword);
        user= SessionManager.getLoggedInUser();
        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInputValid()){
                    Log.e("ChangePassword ", "Clicked!!");

                    Call<BaseResponse> call= HTTP.getAPI().modifyPassword(SessionManager.getToken(),oldPass,newPass,user.email);
                    call.enqueue(new AuthCallBack<BaseResponse>() {
                                     @Override
                                     protected void onAuthError() {

                                     }

                                     @Override
                                     protected void parseSuccessResponse(Response<BaseResponse> response) {
//                                         if (response.isSuccess() && response.body() != null && response.body().body != null) {
//                                         }
                                     }

                                     @Override
                                     public void onFailure(Throwable t) {

                                     }
                                 });
                    /*call.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Response<BaseResponse> response) {
                            if (response.isSuccess()) {
                                Toast.makeText(Changepassword.this, "Message: "+response.message()+" BodyMessage: "+response.body().message, Toast.LENGTH_SHORT).show();
                                Log.e("ChangePassword ", "Message: "+response.message()+" BodyMessage: "+response.body().message);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("ChangePassword ","Error: "+t.getMessage());
                        }
                    });*/
                }
            }
        });
    }
    public boolean isInputValid(){
        boolean isValid = true;
        oldPass=mOldPassView.getText().toString().trim();
        newPass=mNewPassView.getText().toString().trim();
        newPassConfirm=mConfirmNewPassView.getText().toString().trim();
        if(TextUtils.isEmpty(oldPass))
        {
            isValid=false;
            mOldPassView.setError("This field is necessary");
        }
        else if(TextUtils.isEmpty(newPass)){
            isValid=false;
            mNewPassView.setError("This field is necessary");
        }
        else if(TextUtils.isEmpty(newPassConfirm)){
            isValid=false;
            mConfirmNewPassView.setError("This field is necessary");
        }
        else if(!TextUtils.equals(newPass,newPassConfirm)){
            isValid=false;
            mConfirmNewPassView.setError("Does not match new password");
        }
        else if(TextUtils.equals(newPass,oldPass)){
            isValid=false;
            mNewPassView.setError("Old and New Password cannot be same");
        }
        else if(TextUtils.getTrimmedLength(newPass)<2){
            isValid=false;
            mNewPassView.setError("Cannot be less than 2 characters");
        }
        return isValid;
    }
}
