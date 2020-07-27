package hr.miz.evidencijakontakata.ErrorHandling;

public class CustomError extends Exception {
    public int errorCode;
    public String errorMessage;

    public CustomError(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CustomError(String errorMessage) {
        this.errorCode = 9999;
        this.errorMessage = errorMessage;
    }
}
