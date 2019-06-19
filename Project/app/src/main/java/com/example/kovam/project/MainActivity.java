package com.example.kovam.project;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    public String SessionID = "";
    public String Token = "";
    private boolean bool = false;
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if(!isOnline(this))
        {
            GridLayout gridCon = findViewById(R.id.GridConn);
            gridCon.setVisibility(View.VISIBLE);
        }

        CreateToken();
    }

    public void EnterButt(View v)
    {
        if(bool) {
            bool = false;
            CreateSession();
        }
    }

    public void CreateToken()
    {
        Runnable TokenConn = () -> {
            try{
                URL myUrlToken = new URL("https://api.themoviedb.org/3/authentication/token/new?api_key=b9a390d8cec1db186c60520b2614c392");

                URLConnection myUrlConTok = myUrlToken.openConnection();
                myUrlConTok.connect();

                GetResponse(myUrlConTok, "request_token");
                Token = result;
                }
                catch(MalformedURLException ex) {
                }
                catch (IOException ex) {
                } };

        Thread StartToken = new Thread(TokenConn);
        StartToken.start();
    }

    public void CreateSession()
    {
        Runnable TaskCreateSession = () -> {
            try{
                EditText User = findViewById(R.id.Login);
                EditText Pass = findViewById(R.id.Password);
                String AutUrl = "https://api.themoviedb.org/3/authentication/token/validate_with_login?api_key=b9a390d8cec1db186c60520b2614c392";
                String SesUrl = "https://api.themoviedb.org/3/authentication/session/new?api_key=b9a390d8cec1db186c60520b2614c392";
                URL myurlAut = new URL(AutUrl + "&request_token=" + Token + "&username=" + User.getText().toString() + "&password=" + Pass.getText().toString());
                URL myurlSes = new URL(SesUrl + "&request_token=" + Token);

                URLConnection myUrlConAut = myurlAut.openConnection();
                myUrlConAut.connect();
                GetResponse(myUrlConAut, "request_token");

                if(bool) {
                    bool = false;
                    URLConnection myUrlConSes = myurlSes.openConnection();
                    myUrlConSes.connect();
                    GetResponse(myUrlConSes, "session_id");
                    SessionID = result;

                    if(bool)
                    {
                        bool = false;

                        Intent intent = new Intent(MainActivity.this, WindowMain.class);
                        intent.putExtra("Name", "Top");
                        intent.putExtra("NameFilm", "NoFilm");
                        startActivity(intent);
                    }
                }
            }
            catch(MalformedURLException ex) {
            }
            catch (IOException ex) {
            }
            };

        Thread StartToken = new Thread(TaskCreateSession);
        StartToken.start();
    }

    public void GetResponse(URLConnection url, String look)
    {
        try {
            InputStream inputStream = url.getInputStream();
            StringBuffer Buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader((inputStream)));
            String line;
            while ((line = reader.readLine()) != null) {
                Buffer.append(line);
            }

            String ResulJson = Buffer.toString();

            JSONObject dataJsonObj = new JSONObject(ResulJson);

            result = dataJsonObj.getString(look);
            bool = dataJsonObj.getBoolean("success");
        }
        catch(MalformedURLException ex) {
        }
        catch (IOException ex) {
        }
        catch (JSONException ex) {
        }
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
