package com.akhterrasool.collegelibrary.util;


import com.akhterrasool.collegelibrary.app.App;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Client {

    private static RequestQueue requestQueue;

    public static void send(Request request) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(App.getContext());
        }

        requestQueue.add(request);
    }
}
