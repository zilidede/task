package com.zl.task.vo.http;


public class HttpVO {
    private String url; //url
    private String method; // get 或 post
    private HttpResponse response; // 返回数据
    private HttpRequest request; // 请求数据

    public HttpVO() {
    }


    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
