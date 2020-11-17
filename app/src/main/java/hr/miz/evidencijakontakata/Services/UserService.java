package hr.miz.evidencijakontakata.Services;

import hr.miz.evidencijakontakata.ErrorHandling.CustomError;
import hr.miz.evidencijakontakata.Models.DiagnosisModel;
import hr.miz.evidencijakontakata.Models.ExposureUrlModel;
import hr.miz.evidencijakontakata.Services.CallBackInterface.IHttpCallback;
import hr.miz.evidencijakontakata.Services.CallBackInterface.IResponseCallback;
import hr.miz.evidencijakontakata.Services.Client.ApiClient;
import hr.miz.evidencijakontakata.Services.Interface.IUserService;
import hr.miz.evidencijakontakata.Utilities.HttpUtilities;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserService {
    public static void diagnosisKeys(String authCode, DiagnosisModel input, final IResponseCallback callback) {
        IUserService iservice = ApiClient.getClient().create(IUserService.class);

        final Call<ResponseBody> call = iservice.diagnosisKeys(authCode, input);

        HttpUtilities httpUtilities = new HttpUtilities();
        httpUtilities.post(call, new IHttpCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }

            @Override
            public void onError(CustomError error) {
                callback.onError(error);
            }
        });
    }

    public static void getExposure(String url, final IResponseCallback callback) {
        IUserService iservice = ApiClient.getClient().create(IUserService.class);

        final Call<ResponseBody> call = iservice.getExposure(url);

        HttpUtilities httpUtilities = new HttpUtilities();
        httpUtilities.post(call, new IHttpCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }

            @Override
            public void onError(CustomError error) {

                callback.onError(error);
            }
        });
    }

    public static void getUrls(final IResponseCallback callback) {
        IUserService iservice = ApiClient.getClient().create(IUserService.class);

        final Call<ExposureUrlModel> call = iservice.getUrls(DiagnosisModel.consentToFederation());

        HttpUtilities httpUtilities = new HttpUtilities();
        httpUtilities.post(call, new IHttpCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }

            @Override
            public void onError(CustomError error) {
                callback.onError(error);
            }
        });
    }

    public static void checkGooglePlay(String url, final IResponseCallback callback) {
        IUserService iservice = ApiClient.getClient().create(IUserService.class);

        final Call<ResponseBody> call = iservice.checkGooglePlay(url);

        HttpUtilities httpUtilities = new HttpUtilities();
        httpUtilities.post(call, new IHttpCallback() {
            @Override
            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }

            @Override
            public void onError(CustomError error) {
                callback.onError(error);
            }
        });
    }
}
