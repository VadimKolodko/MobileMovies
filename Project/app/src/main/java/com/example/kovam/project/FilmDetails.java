package com.example.kovam.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FilmDetails extends AppCompatActivity {

    private int idFilm;
    private ContainerDetailsFilm CDF;
    Thread StartToken;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.details_film);

        GetDetails();
        addObjectOnWindow();
    }

    public void GetDetails()
    {
        idFilm = getIntent().getIntExtra("id", idFilm);

        Runnable TaskDetails = () -> {
            try{
                URL myurlTop = new URL("https://api.themoviedb.org/3/movie/" + idFilm + "?api_key=b9a390d8cec1db186c60520b2614c392&language=ru-RU");

                URLConnection myUrlConTop = myurlTop.openConnection();
                myUrlConTop.connect();

                GetResponse(myUrlConTop);
            }
            catch (IOException ex) {
            }
        };

        StartToken = new Thread(TaskDetails);
        StartToken.start();
    }

    public void GetResponse(URLConnection url)
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
            JSONObject conteiner = dataJsonObj.getJSONObject("belongs_to_collection");
            CDF = new ContainerDetailsFilm();

            CDF.SetDetails(dataJsonObj.getString("overview"), dataJsonObj.getString("poster_path"), conteiner.getString("name"));
        }
        catch(MalformedURLException ex) {
        }
        catch (IOException ex) {
        }
        catch (JSONException ex) {
        }
    }

    public void addObjectOnWindow()
    {
        SystemClock.sleep(5000);

        ImageView iv = findViewById(R.id.TableImage);
        TextView tv = findViewById(R.id.NameFilm);
        TextView tvv = findViewById(R.id.DetailsFilm);

        new DownloadImageDetails(iv).execute("https://image.tmdb.org/t/p/w600_and_h900_bestv2" + CDF.GetUri());
        tv.setText(CDF.GetName());
        tvv.setText(CDF.GetOver());
    }

    public void ClickMenuButtDet(View v)
    {
        GridLayout mygrid = findViewById(R.id.MyMenu);

        if(mygrid.getVisibility() == v.INVISIBLE)
        {
            mygrid.setVisibility(v.VISIBLE);
        }
        else
        {
            mygrid.setVisibility(v.INVISIBLE);
        }
    }

    public void ClickSerButtDet(View v)
    {
        GridLayout mygrid = findViewById(R.id.MySearch);

        if(mygrid.getVisibility() == v.INVISIBLE)
        {
            mygrid.setVisibility(v.VISIBLE);
        }
        else
        {
            mygrid.setVisibility(v.INVISIBLE);
        }
    }

    public void ClickExitButtDet(View v)
    {
        Intent intent = new Intent(FilmDetails.this, MainActivity.class);
        startActivity(intent);
    }

    public void ClickMainButtDet(View v)
    {
        Intent intent = new Intent(FilmDetails.this, WindowMain.class);
        intent.putExtra("Name", "Top");
        intent.putExtra("NameFilm", "NoFilm");
        startActivity(intent);
    }
}
