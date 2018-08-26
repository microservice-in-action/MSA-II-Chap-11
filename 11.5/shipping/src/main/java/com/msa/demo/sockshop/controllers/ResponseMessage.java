package com.msa.demo.sockshop.controllers;

public class ResponseMessage
{
    //The flag which indicate wheher http mehod is success or not
    private boolean success;

    //The content of http response
    private String body;

    //The status code of http response
    private int statusCode;

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }
}
