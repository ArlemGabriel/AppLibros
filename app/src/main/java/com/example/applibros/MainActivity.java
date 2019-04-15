package com.example.applibros;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private final String api_key = "d9ffc65c50ba568511743b1180dedec3";
    TextView tempTextView;
    ArrayList<ArrayList<String>> authors = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> books = new ArrayList<ArrayList<String>>();
    ArrayList<String> listaLibros = new ArrayList<>();

    public void refreshClicked(View view){
        loadAuthors();
        loadBooks();
    }

    public void loadAuthors(){
        String url = "https://mysterious-depths-76086.herokuapp.com/authors.json";
        Log.i("url", url);
        DownloadTaskAuthors downloadTaskAuthors = new DownloadTaskAuthors();
        downloadTaskAuthors.execute(url);
    }
    public void loadBooks(){
        String url = "https://mysterious-depths-76086.herokuapp.com/books.json";
        Log.i("url", url);
        DownloadTaskBooks downloadTaskBooks = new DownloadTaskBooks();
        downloadTaskBooks.execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadAuthors();
        //tempTextView = findViewById(R.id.tempTextView);


        //loadWeather();

    }

    public class DownloadTaskAuthors extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = jsonArray.getJSONObject(i);
                    String id = json.getString("id");
                    String name = json.getString("name");
                    String lastName = json.getString("lastname");
                    poblarArregloAutores(id,name,lastName);
                }
                Log.i("AUTHORS",authors.toString());
                loadBooks();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public class DownloadTaskBooks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1){
                    char current = (char)data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = jsonArray.getJSONObject(i);
                    String idAuthor = json.getString("author_id");
                    String title = json.getString("title");
                    poblarArregloLibros(idAuthor,title);
                }
                Log.i("BOOKS",books.toString());
                poblarListaLibros();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public void poblarArregloAutores(String id,String name,String lastname){
        ArrayList<String> datosAutor= new ArrayList<String>();
        datosAutor.add(id);
        datosAutor.add(name);
        datosAutor.add(lastname);
        authors.add(datosAutor);
    }
    public void poblarArregloLibros(String idAuthor,String title){
        ArrayList<String> datosLibro= new ArrayList<String>();
        datosLibro.add(idAuthor);
        datosLibro.add(title);
        books.add(datosLibro);
    }

    public void poblarListaLibros(){
        int largoAutores = authors.size()-1;
        int largoLibros = books.size()-1;
        for(int i=0;i<=largoLibros;i++){
            for(int p=0;p<=largoAutores;p++){
                Log.i("i",String.valueOf(i));
                Log.i("p",String.valueOf(p));
                Log.i("idAuthor",authors.get(p).get(0));
                Log.i("idAuthorBook",books.get(i).get(0));
                String idAuthor = authors.get(p).get(0);
                String idAuthorBook = books.get(i).get(0);
                if(idAuthor==idAuthorBook){
                    String name = authors.get(p).get(1);
                    String lastname = authors.get(p).get(2);
                    String titleBook = books.get(i).get(1);
                    listaLibros.add(name+" "+lastname+"\n"+titleBook);
                    Log.i("ListaLibros:",listaLibros.toString());
                    break;
                }
            }
        }
        poblarListView();

    }
    public void poblarListView(){
        ListView listView = findViewById(R.id.lvLibros);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listaLibros);

        listView.setAdapter(arrayAdapter);

    }
}
