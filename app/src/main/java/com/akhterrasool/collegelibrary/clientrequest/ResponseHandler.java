package com.akhterrasool.collegelibrary.clientrequest;

import com.android.volley.Response;

interface ResponseHandler<T> {

    Response.Listener<T> getResponseHandler();
}
