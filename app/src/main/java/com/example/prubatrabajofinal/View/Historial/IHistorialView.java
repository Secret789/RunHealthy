package com.example.prubatrabajofinal.View.Historial;

import com.example.prubatrabajofinal.Model.Historial.TrayectoriaModel;
import com.example.prubatrabajofinal.Model.Historial.UbicacionModel;

import java.util.ArrayList;

public interface IHistorialView {
    public void updateData(TrayectoriaModel tm);
    public void updateMapa(ArrayList<UbicacionModel> ubi);
}
