package pl.iticity.dbfds.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class JsonResponse {

    private String message;
    private boolean success;
    private String exceptionName;

    public static JsonResponse fromException(Exception exception){
        return new JsonResponse(exception.getMessage(),false,exception.getClass().getName());
    }

    public static JsonResponse success(String message){
        return new JsonResponse(message,true,null);
    }

    private JsonResponse(String message, boolean success, String exceptionName) {
        this.message = message;
        this.success = success;
        this.exceptionName = exceptionName;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
