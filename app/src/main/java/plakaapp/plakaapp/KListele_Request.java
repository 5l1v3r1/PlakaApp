package plakaapp.plakaapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Simulakra on 28.10.2017.
 */

public class KListele_Request {
    private static KListele_Request mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private KListele_Request(Context context)
    {
        mCtx=context;
        requestQueue =  getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized KListele_Request getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new KListele_Request(context);
        }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request)
    {
        requestQueue.add(request);
    }
}

