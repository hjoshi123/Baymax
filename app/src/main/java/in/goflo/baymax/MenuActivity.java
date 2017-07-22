package in.goflo.baymax;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

public class MenuActivity extends AppCompatActivity {
    final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            for (String key : b.keySet()) {
                String value = b.getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
            setContentView(R.layout.activity_main_fcm);
            TextView doctorName = (TextView) findViewById(R.id.doctor);
            TextView distance = (TextView) findViewById(R.id.distance);
            TextView address = (TextView) findViewById(R.id.address);
            doctorName.setText(b.getString("name"));
            distance.setText(b.getString("distance"));
            address.setText(b.getString("address"));
        }else{
            setContentView(R.layout.activity_menu);
        }

    }

    public void imageCapture(View v){
        startActivity(new Intent(MenuActivity.this,UploadPhotoActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.updateToken:
                String token = FirebaseInstanceId.getInstance().getToken();
                if(token!= null){
                    Log.v(TAG,"Token: "+token);
                    sendRegistrationToServer(token);
                }
                break;
        }
        return true;
    }
    private void sendRegistrationToServer(String refreshedToken) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.goflo.in:5000/token?token="+refreshedToken;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        // Display the first 500 characters of the response string.
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MenuActivity.this, "Token update failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
