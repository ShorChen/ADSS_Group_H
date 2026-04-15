package Suppliers.Domain.Service;

public class Response<T> {
    private final T data;
    private final String errorMessage;
    private final boolean success;

    public Response(T data) {
        this.data = data;
        errorMessage = null;
        success = true;
    }

    public Response(String errorMessage) {
        data = null;
        this.errorMessage = errorMessage;
        success = false;
    }

    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public T getData() { return data; }
}