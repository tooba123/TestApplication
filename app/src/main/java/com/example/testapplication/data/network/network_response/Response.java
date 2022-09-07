package com.example.testapplication.data.network.network_response;

public class Response {

    public static class Type{

        public final static  int SUCCESS = 1;
        public final static int ERROR = 0;
    }
    private int type;

    public Response(){

    }
    public void setType(int _type){
        this.type = _type;
    }

    public int getType() {
        return type;
    }
}
