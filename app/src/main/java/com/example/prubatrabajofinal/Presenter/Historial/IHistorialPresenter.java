package com.example.prubatrabajofinal.Presenter.Historial;

import com.example.prubatrabajofinal.Model.Historial.TrayectoriaModel;
import com.example.prubatrabajofinal.Model.Historial.UbicacionModel;

import java.util.List;

public interface IHistorialPresenter {
    public TrayectoriaModel ObtenerTrayectoria(int id, String URL);
    public List<UbicacionModel> ObtenerUbicaciones(int id, String URL);
}
