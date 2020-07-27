package hr.miz.evidencijakontakata.Services.Client;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hr.miz.evidencijakontakata.CroatiaExposureNotificationApp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        PersistentCookieJar persistentCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(CroatiaExposureNotificationApp.getInstance()));
        ClearableCookieJar cookieJar = persistentCookieJar;

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder();

                        request = builder.build();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(CroatiaExposureNotificationApp.globalEnvironment.globalURL)
                .addConverterFactory(GsonConverterFactory.create(getCustomGson()))
                .client(client)
                .build();


        return retrofit;
    }

    private static Gson getCustomGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
}
