package com.akhterrasool.collegelibrary.server;

import com.android.volley.Response;

interface ResponseHandler<T> {

    Response.Listener<T> getResponseHandler();
}
