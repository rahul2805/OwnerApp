package com.abc.templateownerapp.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.util.Pair;

import com.abc.templateownerapp.Network;
import com.abc.templateownerapp.utils.AsyncTaskResult;
import com.abc.templateownerapp.utils.NetworkResponse;
import com.abc.templateownerapp.utils.callback;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainTask {

    public static void getData(Context context, String endPoint, final callback<String> cb) {
        FetchTask fetchTask =  new FetchTask() {
            @Override
            protected void onPostExecute(AsyncTaskResult<NetworkResponse> asyncTaskResult) {
                if (asyncTaskResult.isError()) {
                    cb.onError(asyncTaskResult.getError());
                } else {
                    NetworkResponse response = asyncTaskResult.getResult();
                    Log.d("res", response.getResponseString());
                    if (response.getResponseCode() == 200) {
                        cb.onSucess(response.getResponseString());
                    } else {
                        cb.onError(new Exception("404"));
                    }
                }
            }
        };
        fetchTask.execute(new Pair<Context, String>(context, endPoint));
    }

    public static class FetchTask extends AsyncTask<Pair<Context, String>, Void, AsyncTaskResult<NetworkResponse>> {

        @Override
        protected AsyncTaskResult<NetworkResponse> doInBackground(Pair<Context, String>... data) {
            Context context = data[0].first;
            String endPoint = data[0].second;
            try {
                NetworkResponse tokenResponse = Network.verifyToken(User.getUserInstance().getToken());
                if (tokenResponse.getResponseCode() != 200) {
                    return  new AsyncTaskResult<>(new Exception("Invalid Token"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                NetworkResponse response = Network.makeCall(endPoint, null, Network.getAppId(context));
                return new AsyncTaskResult<>(response);
            } catch (IOException  e) {
                e.printStackTrace();
                return new AsyncTaskResult<>(e);
            }
        }
    }

    public static void addItem(Context context, JSONObject body, final callback<String> cb) {
        AddItemTask addItemTask =  new AddItemTask() {
            @Override
            protected void onPostExecute(AsyncTaskResult<NetworkResponse> asyncTaskResult) {
                if (asyncTaskResult.isError()) {
                    cb.onError(asyncTaskResult.getError());
                } else {
                    NetworkResponse response = asyncTaskResult.getResult();
                    Log.d("res", response.getResponseString());
                    if (response.getResponseCode() == 200) {
                        cb.onSucess(response.getResponseString());
                    } else {
                        cb.onError(new Exception("404"));
                    }
                }
            }
        };
        addItemTask.execute(new Pair<Context, JSONObject>(context, body));
    }

    public static class AddItemTask extends AsyncTask<Pair<Context, JSONObject>, Void, AsyncTaskResult<NetworkResponse>> {

        @Override
        protected AsyncTaskResult<NetworkResponse> doInBackground(Pair<Context, JSONObject>... data) {
            Context context = data[0].first;
            JSONObject jsonObject = data[0].second;
            try {
                NetworkResponse tokenResponse = Network.verifyToken(User.getUserInstance().getToken());
                if (tokenResponse.getResponseCode() != 200) {
                    return  new AsyncTaskResult<>(new Exception("Invalid Token"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                NetworkResponse response = Network.makeCall("app/addItem", body, Network.getAppId(context));
                return new AsyncTaskResult<>(response);
            } catch (IOException  e) {
                e.printStackTrace();
                return new AsyncTaskResult<>(e);
            }
        }
    }

    public static void deliverOrder(Context context, JSONObject body, final callback<String> cb) {
        DeliverOrderTask deliverOrderTask =  new DeliverOrderTask() {
            @Override
            protected void onPostExecute(AsyncTaskResult<NetworkResponse> asyncTaskResult) {
                if (asyncTaskResult.isError()) {
                    cb.onError(asyncTaskResult.getError());
                } else {
                    NetworkResponse response = asyncTaskResult.getResult();
                    Log.d("res", response.getResponseString());
                    if (response.getResponseCode() == 200) {
                        cb.onSucess(response.getResponseString());
                    } else {
                        cb.onError(new Exception("404"));
                    }
                }
            }
        };
        deliverOrderTask.execute(new Pair<Context, JSONObject>(context, body));
    }

    public static class DeliverOrderTask extends AsyncTask<Pair<Context, JSONObject>, Void, AsyncTaskResult<NetworkResponse>> {

        @Override
        protected AsyncTaskResult<NetworkResponse> doInBackground(Pair<Context, JSONObject>... data) {
            Context context = data[0].first;
            JSONObject jsonObject = data[0].second;
            try {
                NetworkResponse tokenResponse = Network.verifyToken(User.getUserInstance().getToken());
                if (tokenResponse.getResponseCode() != 200) {
                    return  new AsyncTaskResult<>(new Exception("Invalid Token"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                NetworkResponse response = Network.makeCall("app/deliverOrder", body, Network.getAppId(context));
                return new AsyncTaskResult<>(response);
            } catch (IOException  e) {
                e.printStackTrace();
                return new AsyncTaskResult<>(e);
            }
        }
    }


}
