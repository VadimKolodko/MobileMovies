package com.example.kovam.project;

public class ContainerDetailsFilm {

    String Overview = "";
    String uriImage = "";
    String NameFilm = "";

    public void SetDetails(String over, String uri, String name)
    {
        this.Overview = over;
        this.uriImage = uri;
        this.NameFilm = name;
    }

    public String GetOver()
    {
        return Overview;
    }

    public String GetUri()
    {
        return uriImage;
    }

    public String GetName()
    {
        return NameFilm;
    }
}
