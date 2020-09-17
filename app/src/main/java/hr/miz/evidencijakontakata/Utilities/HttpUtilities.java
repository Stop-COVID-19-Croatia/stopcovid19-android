package hr.miz.evidencijakontakata.Utilities;

import hr.miz.evidencijakontakata.R;

import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;
import hr.miz.evidencijakontakata.ErrorHandling.CustomError;
import hr.miz.evidencijakontakata.Services.CallBackInterface.IHttpCallback;

import java.io.IOException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpUtilities {
    private String error;
    private int errorCode;
    private static Call<?> call;

    public <T extends Object> void post(Call<T> call, final IHttpCallback callback) {

        if (!InternetConnectivityUtil.isNetworkAvailable(CroatiaExposureNotificationApp.getInstance())) {
            callback.onError(new CustomError(666, CroatiaExposureNotificationApp.getInstance().getResources().getString(R.string.no_internet_connection)));
            return;
        }
        HttpUtilities.call = call;
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                HttpUtilities.call = null;
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else try {
                    errorCode = response.code();
                    error = response.errorBody().string();
                    callback.onError(new CustomError(errorCode, error));
                } catch (IOException e) {
                    callback.onError(new CustomError(errorCode, error));

                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                HttpUtilities.call = null;
                if (!call.isCanceled()) {
                    callback.onError(new CustomError(500, t.getMessage()));
                }
            }
        });
    }


}
