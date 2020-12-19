package com.example.prubatrabajofinal.Presenter.Historial;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.prubatrabajofinal.Model.Historial.TrayectoriaModel;
import com.example.prubatrabajofinal.Model.Historial.UbicacionModel;
import com.example.prubatrabajofinal.View.Historial.IHistorialView;
import com.example.prubatrabajofinal.View.Usuario.IUsuarioView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialPresenterCompl implements IHistorialPresenter{
    IHistorialView iHistorialView;
    View v;
    TrayectoriaModel trayectoriaModel;
    ArrayList<UbicacionModel> lista;
    public HistorialPresenterCompl(IHistorialView iHistorialView, View v){
        this.iHistorialView=iHistorialView;
        this.v=v;
    }
    public TrayectoriaModel ObtenerTrayectoria(int id, String URL){
        final ProgressDialog progressBar = new ProgressDialog(v.getContext());
        progressBar.setMessage("Cargando...");
        progressBar.show();

        JsonArrayRequest jasonArrayRequest =new JsonArrayRequest(URL+"?TraId="+Integer.toString(id), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        SimpleDateFormat formatOri = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        SimpleDateFormat formatFec = new SimpleDateFormat("dd/MM/yy");
                        SimpleDateFormat formatHor = new SimpleDateFormat("HH:mm:ss");

                        Date d1=new Date();
                        Date d2=new Date();
                        Date d3=new Date();

                        try {
                            d1= formatOri.parse(jsonObject.getString("TraFec"));
                            d2= formatOri.parse(jsonObject.getString("TraHorIni"));
                            d3= formatOri.parse(jsonObject.getString("TraHorFin"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long diff = Math.abs(d2.getTime() - d3.getTime());
                        long diffDays = diff / ( 60 * 1000);


                        trayectoriaModel=new TrayectoriaModel(
                                jsonObject.getInt("TraId"),
                                formatFec.format(d1),
                                formatHor.format(d2),
                                formatHor.format(d3),
                                Long.toString(diffDays)
                        );

                    } catch (JSONException e) {
                        //Log.e("myTag", e.getMessage());
                        progressBar.dismiss();
                        Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                iHistorialView.updateData(trayectoriaModel);
                progressBar.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(v.getContext(),"Error de conexion"+error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(jasonArrayRequest);
        return trayectoriaModel;
    }
    public List<UbicacionModel> ObtenerUbicaciones(int id, String URL){
        final ProgressDialog progressBar = new ProgressDialog(v.getContext());
        progressBar.setMessage("Cargando...");
        progressBar.show();

        lista =new ArrayList<>();
        JsonArrayRequest jasonArrayRequest =new JsonArrayRequest(URL+"?UbiTraId="+Integer.toString(id), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        lista.add(new UbicacionModel(
                                jsonObject.getInt("UbiId"),
                                jsonObject.getString("UbiFec"),
                                jsonObject.getString("UbiLat"),
                                jsonObject.getString("UbiLon"),
                                jsonObject.getString("UbiVel"))
                        );

                    } catch (JSONException e) {
                        //Log.e("myTag", e.getMessage());
                        progressBar.dismiss();
                        Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                iHistorialView.updateMapa(lista);
                progressBar.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("myTag","Error de conexion");
                progressBar.dismiss();
                Toast.makeText(v.getContext(),"Error de conexion",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(jasonArrayRequest);
        return lista;
    }
}
