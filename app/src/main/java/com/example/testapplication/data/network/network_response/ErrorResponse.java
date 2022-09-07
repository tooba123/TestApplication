package com.example.testapplication.data.network.network_response;

public class ErrorResponse extends Response {

    private int code;
    private String message;

    public void setCode(int _code){
        code = _code;
    }

    public int getCode(){
        return code;
    }

    public void setMessage(String _message){
        message = _message;
    }

    public String getMessage() {
        return message;
    }
}
