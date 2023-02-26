package com.example.actividad_331_jairo_mora_enriquez;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Button btnAñadir,btnMostrar,btnEliminar,btnModificar;
    private EditText txtNombre,txtApellido;
    private ListView listaContactos;
    private static   ArrayList<Datos> datos;
    private String identificador, nombre,apellido;
    private ContactosDB miBd;
    private static final String DB_NAME = "ContactosDB.db";
    private static final int VERSION_ACTUAL = 1;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnAñadir = (Button) findViewById(R.id.btnAñador);
        this.btnMostrar = (Button) findViewById(R.id.btnMostrar);
        this.btnEliminar = (Button) findViewById(R.id.btnEliminar);
        this.btnModificar = (Button) findViewById(R.id.btnModificar);
        this.listaContactos = (ListView) findViewById(R.id.listaContactos);
        this.txtNombre = (EditText) findViewById(R.id.editextName);
        this.txtApellido = (EditText)findViewById(R.id.ediTextApellido);

        this.datos = new ArrayList<>();
        printContacts();

        miBd = new ContactosDB(getApplicationContext(),DB_NAME,null,VERSION_ACTUAL);
        db = miBd.getWritableDatabase();

        this.btnAñadir.setOnClickListener(this);
        this.btnMostrar.setOnClickListener(this);
        this.btnEliminar.setOnClickListener(this);
        this.btnModificar.setOnClickListener(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.btnAñador:
                this.nombre = txtNombre.getText().toString();
                this.apellido = txtApellido.getText().toString();
//                if(!nombre.isEmpty() && !apellido.isEmpty()){
//                    String[] args = new String[] {nombre};
//                    Cursor fila= db.rawQuery("SELECT user FROM Usuarios WHERE user=?",args);
//
//                    if (fila.moveToFirst()) {
//                        Toast.makeText(getApplicationContext(), "Ya existe este usuario", Toast.LENGTH_SHORT).show();
//
//                    }else{
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("nombre",nombre);
                        nuevoRegistro.put("apellido", apellido);
                        db.insert("Contactos",null,nuevoRegistro);
                        Toast.makeText(getApplicationContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
//                    }
//                    fila.close();
//                }else{
//                    Toast.makeText(getApplicationContext(), "Es obligatorio rellenar todos los campos", Toast.LENGTH_SHORT).show();
//                }

                break;

            case  R.id.btnMostrar:
                datos.clear(); //limpiar el array para evitar duplicados
                datos.addAll(getAll()); //agregar los nuevos contactos
                ((BaseAdapter) listaContactos.getAdapter()).notifyDataSetChanged(); //notificar al adaptador que los datos cambiaron
                break;

            case  R.id.btnEliminar:
                Toast.makeText(getApplicationContext(),"Contacto eliminado",Toast.LENGTH_SHORT).show();
                break;

            case  R.id.btnModificar:
                Toast.makeText(getApplicationContext(),"Contacto modificado",Toast.LENGTH_SHORT).show();
                break;


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private ArrayList<Datos> getAll(){
        datos.add(new Datos("pepe","nuevo"));
        Cursor miCursor = db.rawQuery("SELECT nombre,apellido FROM Contactos",null);
        if(miCursor.moveToNext()){
          do{
              String nombreU = miCursor.getString(0);
              String apelldoU = miCursor.getString(1);
              datos.add(new Datos(nombreU,apelldoU));
          }while (miCursor.moveToNext());
        }
        miCursor.close();
        return datos;
    }

    private void printContacts(){

        View header = getLayoutInflater().inflate(R.layout.cabecera,null);
        Adaptador miAdaptador = new Adaptador(this,datos);
        listaContactos.setAdapter(miAdaptador);
        listaContactos.addHeaderView(header);

    }

}