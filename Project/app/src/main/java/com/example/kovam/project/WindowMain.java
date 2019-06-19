package com.example.kovam.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WindowMain extends AppCompatActivity{

    String check = "";
    String MyNaFilm = "";
    public int height;
    public int count = 0;
    Thread StartToken;
    InfoFilm[] If;
    RelativeLayout[] RL;
    ImageView[] img;
    TextView[] TV;
    GridLayout dl;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_window);

        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();

        check = getIntent().getStringExtra("Name");
        MyNaFilm = getIntent().getStringExtra("NameFilm");

        CreateTopFilms();
    }

    public void ClickMenuButt(View v)
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

    public void ClickSearchButt(View v)
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

    public void ClickExitButt(View v)
    {
        Intent intent = new Intent(WindowMain.this, MainActivity.class);
        startActivity(intent);
    }

    public void ClickMainButt(View v)
    {
        RemoveAllView();
        Intent intent = new Intent(WindowMain.this, WindowMain.class);
        startActivity(intent);
    }

    public void GoSearch(View v)
    {
        RemoveAllView();
        Intent intent = new Intent(WindowMain.this, WindowMain.class);
        EditText et = findViewById(R.id.EnterNameFilm);
        intent.putExtra("Name", "Search");
        intent.putExtra("NameFilm", et.getText().toString());
        startActivity(intent);
    }

    public void CreateTopFilms() {
        if (check.equals("Top")) {
            Runnable TaskSearchTop = () -> {
                try {
                    URL myurlTop = new URL("https://api.themoviedb.org/3/movie/popular?api_key=b9a390d8cec1db186c60520b2614c392&language=ru-RU&page=1");

                    URLConnection myUrlConTop = myurlTop.openConnection();
                    myUrlConTop.connect();

                    GetResponse(myUrlConTop);
                } catch (IOException ex) {
                }
            };

            StartToken = new Thread(TaskSearchTop);
            StartToken.start();
        }

        if (check.equals("Search")) {
            Runnable TaskSearchTop = () -> {
                try
                {
                    String MyNameFilm = URLEncoder.encode(MyNaFilm, "utf-8");
                    URL myurlTop = new URL("https://api.themoviedb.org/3/search/movie?api_key=b9a390d8cec1db186c60520b2614c392&language=ru-RU&page=1&query=" + MyNameFilm);
                    URLConnection myUrlConTop = myurlTop.openConnection();
                    myUrlConTop.connect();

                    GetResponse(myUrlConTop);
                } catch (IOException ex) {
                }
            };

            StartToken = new Thread(TaskSearchTop);
            StartToken.start();
        }

        dl = findViewById(R.id.TopFilm);
        SystemClock.sleep(5000);
        for (int i = 0; i < RL.length; i++) {
            RL[i] = new RelativeLayout(this);
            img[i] = new ImageView(this);
            TV[i] = new TextView(this);
            RelativeLayoutObject(RL[i]);
            ImageViewObject(img[i]);
            TextView(TV[i], If[i].GetName());
            RL[i].addView(img[i]);
            RL[i].addView(TV[i]);
            dl.addView(RL[i]);
            new DownloadImageTask(img[i]).execute("https://image.tmdb.org/t/p/w600_and_h900_bestv2" + If[i].GetUri()); }
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
            JSONArray arraJson = dataJsonObj.getJSONArray("results");

            If = new InfoFilm[arraJson.length()];
            RL = new RelativeLayout[arraJson.length()];
            img = new ImageView[arraJson.length()];
            TV = new TextView[arraJson.length()];

            for(int i = 0; i < arraJson.length(); i++)
            {
                JSONObject secarrJson = arraJson.getJSONObject(i);
                If[i] = new InfoFilm();
                If[i].SetInfoFilm(i, secarrJson.getString("poster_path"), secarrJson.getString("title"), Integer.parseInt(secarrJson.getString("id")));
            }
        }
        catch(MalformedURLException ex) {
        }
        catch (IOException ex) {
        }
        catch (JSONException ex) {
        }
    }

    public void RelativeLayoutObject(RelativeLayout rl)
    {
        rl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height / 3));
    }

    public void ImageViewObject(ImageView img)
    {
        img.setBackgroundColor(Color.rgb(0,0,0));
        img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height / 3));
        img.setId(count);
        count++;
        img.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v){
                if(v == img)
                {
                    for(int i = 0; i < If.length; i++) {
                        if (img.getId() == If[i].GetID())
                        {
                            RemoveAllView();
                            Intent intent = new Intent(WindowMain.this, FilmDetails.class);
                            intent.putExtra("id", If[i].GetIDFilm());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public void TextView(TextView tv, String text)
    {
        RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        tParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        tv.setText(text);
        tv.setTextSize(30);
        tv.setBackgroundColor(Color.rgb(100,100,100));
        tv.setPadding(0,15,0,15);
        tv.setTextColor(Color.rgb(0,0,0));
        tv.setAlpha((float)0.7);

        tv.setLayoutParams(tParams);
    }

    public void RemoveAllView()
    {
        for(int i = 0; i < img.length; i++)
        {
            ((ViewGroup) RL[i].getParent()).removeView(RL[i]);
            ((ViewGroup) img[i].getParent()).removeView(img[i]);
            ((ViewGroup) TV[i].getParent()).removeView(TV[i]);
        }

        dl.removeAllViews();
    }
}
