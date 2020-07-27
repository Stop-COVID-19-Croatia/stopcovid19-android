package hr.miz.evidencijakontakata.Services.CallBackInterface;


import hr.miz.evidencijakontakata.ErrorHandling.CustomError;

public interface IResponseCallback<T> {
    void onSuccess(T response);
    void onError(CustomError exception);
}
