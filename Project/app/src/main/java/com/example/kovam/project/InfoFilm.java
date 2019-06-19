package com.example.kovam.project;

public class InfoFilm {

    int id;
    String uriImage = "";
    String NameFilm = "";
    int idFilm;

    public void SetInfoFilm(int id, String uri, String name, int idFilm)
    {
        this.id = id;
        this.uriImage = uri;
        this.NameFilm = name;
        this.idFilm = idFilm;
    }

    public void SetID(int id)
    {
        this.id = id;
    }

    public void SetUri(String uri)
    {
        this.uriImage = uri;
    }

    public void SetName(String name)
    {
        this.NameFilm = name;
    }

    public void SetIDFilm(int id)
    {
        this.idFilm = id;
    }

    public int GetID()
    {
        return id;
    }

    public String GetUri()
    {
        return uriImage;
    }

    public String GetName()
    {
        return NameFilm;
    }

    public int GetIDFilm()
    {
        return idFilm;
    }
}
