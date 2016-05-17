package pl.iticity.dbfds.controller;


public class JsonResponse {

    private String message;
    private boolean success;

    public static JsonResponse instance(boolean success, String message){
        return new JsonResponse(message,success);
    }

    private JsonResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
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
