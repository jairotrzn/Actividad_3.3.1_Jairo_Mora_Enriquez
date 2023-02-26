package com.example.actividad_331_jairo_mora_enriquez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends BaseAdapter {
    private Context context;
    private ArrayList<Datos> listaDatos;

    public Adaptador(Context context, ArrayList<Datos> listaDatos) {
        super();
        this.context = context;
        this.listaDatos = listaDatos;
    }

    @Override
    public int getCount() {
        return listaDatos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       LayoutInflater inflater = LayoutInflater.from(context);
       View vista = inflater.inflate(R.layout.adaptador,parent,false);

       TextView iden = (TextView) vista.findViewById(R.id.identificador);
       TextView nombre = (TextView) vista.findViewById(R.id.datonombre);
       TextView apellido = (TextView) vista.findViewById(R.id.ediTextApellido);

       iden.setText(String.valueOf(position + 1));
       nombre.setText(listaDatos.get(position).getNombre());
       apellido.setText(listaDatos.get(position).getApellido());

       return vista;
    }
}
