package hr.miz.evidencijakontakata.Services.Interface;

import hr.miz.evidencijakontakata.Models.DiagnosisModel;
import hr.miz.evidencijakontakata.Models.ExposureUrlModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IUserService {
    @POST("submission/diagnosis-keys")
    Call<ResponseBody> diagnosisKeys(@Header ("authorization-code") String authCode, @Body DiagnosisModel input);

    @GET
    Call<ResponseBody> getExposure(@Url String url);

    @GET("submission/diagnosis-key-file-urls")
    Call<ExposureUrlModel> getUrls();

    @GET
    Call<ResponseBody> checkGooglePlay(@Url String googlePlayUrl);
}
