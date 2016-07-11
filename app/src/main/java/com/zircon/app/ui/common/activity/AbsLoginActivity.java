package com.zircon.app.ui.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.zircon.app.model.LoginCredentials;
import com.zircon.app.model.response.LoginResponse;
import com.zircon.app.ui.home.MainNavActivity;
import com.zircon.app.utils.API;
import com.zircon.app.utils.HTTP;
import com.zircon.app.utils.Log;
import com.zircon.app.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by jikoobaruah on 21/01/16.
 */
public abstract class AbsLoginActivity extends AppCompatActivity {

    protected abstract void showProgress(boolean show);

    protected abstract void showLoginError(String errormessage);

    protected Call<LoginResponse> mLoginCall;

    protected Call<LoginResponse> mFBLoginCall;

    private String FBAccessToken;
    private com.zircon.app.model.LoginCredentials loginCredentials;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    Intent intent;
    protected void fblogin(String FBAccessToken) {
        this.FBAccessToken = FBAccessToken;
//        showProgress(true);
        API api = HTTP.getAPI();
        if (api == null) {
            //TODO handle no inernet scenario;
            return;
        }
        mFBLoginCall = HTTP.getAPI().fblogin(FBAccessToken);
        mFBLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
//                showProgress(false);
                mFBLoginCall = null;
                if (response.isSuccess()) {
                    SessionManager.setFBLoggedInUser(response.body().body.userDetails.user, response.body().body.token, response.body().body.society);
                    intent = new Intent(AbsLoginActivity.this, MainNavActivity.class);
                    if (getIntent().getIntExtra("requestcode", -1) == AbsBaseActivity.REQUEST_LOGIN) {
                        setResult(RESULT_OK);
                    } else {
                        startActivity(intent);
                    }
                    finish();
                    Log.e("TEMP",response.body().body.userDetails.toString());

                } else {
                    Log.e("TEMP",response.message());

//                    showLoginError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
//                showProgress(false);
                mFBLoginCall = null;
                Log.e(getClass().getSimpleName(),t.getMessage());

//                showLoginError(t.getMessage());
            }
        });
    }

    protected void login(final LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
        showProgress(true);
        API api = HTTP.getAPI();
        if (api == null) {
            //TODO handle no inernet scenario;
            return;
        }
        mLoginCall = HTTP.getAPI().login(loginCredentials.societyId, loginCredentials.userName, loginCredentials.password, Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID));
        mLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response) {
                showProgress(false);
                mLoginCall = null;
                if (response.isSuccess()) {
                    SessionManager.setLoggedInUser(response.body().body.userDetails.user, loginCredentials, response.body().body.token, response.body().body.society);
                    Intent intent = new Intent(AbsLoginActivity.this, MainNavActivity.class);
                    if (getIntent().getIntExtra("requestcode", -1) == AbsBaseActivity.REQUEST_LOGIN) {
                        setResult(RESULT_OK);
                    } else {
                        startActivity(intent);
                    }
                    finish();
                } else {
                    showLoginError(response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showProgress(false);
                mLoginCall = null;
                showLoginError(t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginCall != null)
            mLoginCall.cancel();
        mLoginCall = null;
        showProgress(false);
    }
}
