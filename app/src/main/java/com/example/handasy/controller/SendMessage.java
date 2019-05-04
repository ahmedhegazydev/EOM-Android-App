package com.example.handasy.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.otsdc.sdk.OTSRestClient;
import com.otsdc.sdk.model.messages.MessageResponse;
import com.otsdc.sdk.resources.IMessageResource;

/**
 * Created by ahmed on 4/1/2017.
 */

public class SendMessage extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... params) {

        OTSRestClient client = new OTSRestClient("gfBGml_t4YQMaJpy79G8aEZn4PLA9k");
        IMessageResource messageResource = client.getMessageResource();


        //Send Message 1
        MessageResponse sendResponse = null;
        /*try {
            sendResponse = messageResource.send(new MessageRequest("00966" + params[0].substring(1), params[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        if (sendResponse == null) {
            return "fail";
        } else if (sendResponse.isSuccess()) {
            Log.d("SendResponse:", "" + sendResponse);
            return "done";
            //Do something
        } else {
            Log.d("SendResponse Fail:", sendResponse.getErrorCode() + "-" + sendResponse.getMessage());
            return "fail";
        }

    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public SendMessage(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
