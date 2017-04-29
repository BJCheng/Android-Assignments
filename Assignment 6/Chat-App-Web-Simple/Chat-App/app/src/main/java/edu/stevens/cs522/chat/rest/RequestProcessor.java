package edu.stevens.cs522.chat.rest;

import android.content.Context;

/**
 * Created by dduggan.
 */

public class RequestProcessor {

    private Context context;

    private RestMethod restMethod;

    public RequestProcessor(Context context) {
        this.context = context;
        this.restMethod =  new RestMethod(context);
    }

    public Response process(Request request) {
        return request.process(this);
    }

    public Response perform(RegisterRequest registerRequest) {
        return restMethod.perform(registerRequest);
    }

    public Response perform(PostMessageRequest postMessageRequest) {
        return restMethod.perform(postMessageRequest);
    }

}
