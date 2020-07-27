package hr.miz.evidencijakontakata.Services.CallBackInterface;


import hr.miz.evidencijakontakata.ErrorHandling.CustomError;

public interface IHttpCallback {
    void onSuccess(Object string);
    void onError(CustomError error);
}
