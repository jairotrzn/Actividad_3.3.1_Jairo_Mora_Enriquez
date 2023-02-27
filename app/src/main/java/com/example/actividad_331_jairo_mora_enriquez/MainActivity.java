package com.example.actividad_331_jairo_mora_enriquez;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnAñadir,btnMostrar,btnEliminar,btnModificar;
    private EditText txtNombre,txtApellido;
    private ListView listaContactos;
    private static   ArrayList<Datos> datos;
    private String identificador, nombre,apellido;
    private ContactosDB miBd;
    private static final String DB_NAME = "ContactosDB.db";
    private static final int VERSION_ACTUAL = 1;
    SQLiteDatabase db;

    private  Datos datos1;
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

        datos.clear();
        View header = getLayoutInflater().inflate(R.layout.cabecera,null);
        listaContactos.addHeaderView(header);
        Adaptador miAdaptador = new Adaptador(this,datos);
        listaContactos.setAdapter(miAdaptador);


        miBd = new ContactosDB(getApplicationContext(),DB_NAME,null,VERSION_ACTUAL);
        db = miBd.getWritableDatabase();

        this.btnAñadir.setOnClickListener(this);
        this.btnMostrar.setOnClickListener(this);
        this.btnEliminar.setOnClickListener(this);
        this.btnModificar.setOnClickListener(this);

        this.listaContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                datos1 = datos.get(position - 1 );
               txtNombre.setText(datos1.getNombre().toString());
               txtApellido.setText(datos1.getApellido().toString());
            }
        });
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
                save(nombre,apellido);
                break;

            case  R.id.btnMostrar:
                printContacts();
                break;

            case  R.id.btnEliminar:
                showDeleteDialog();
                break;

            case  R.id.btnModificar:
                showMofiifieDialog();
                break;


        }
    }

    private void showMofiifieDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ATENCION");
        builder.setMessage("¿Desea modificar el registro?");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreActual = datos1.getNombre();
                String nombreModificado = txtNombre.getText().toString();
                String apellido = txtApellido.getText().toString();
                upDate(nombreModificado,apellido,nombreActual);
                Toast.makeText(getApplicationContext(),"Contacto modificado",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("¿Cancelar?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }

    private void upDate(String nombreModificado, String apellido, String nombreActual) {
        String[] args = new String[] {nombreModificado,apellido,nombreActual};
        db.execSQL("UPDATE Contactos SET nombre =? , apellido=? WHERE nombre=?",args);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ADVERTENCIA");
        builder.setMessage("¿Desea eliminar el registro?");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = txtNombre.getText().toString();
                delete(name);
                Toast.makeText(getApplicationContext(),"Contacto eliminado " + name,Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog cuadro = builder.create();
        cuadro.show();
    }
    private void delete(String nombre){
        String[] args = new String[] {nombre};
        db.execSQL("DELETE FROM Contactos WHERE nombre=?",args);

    }
    private void save(String nombre, String apellido) {
        if(!nombre.isEmpty() && !apellido.isEmpty()){
            String[] args = new String[] {nombre};
            Cursor cursor= db.rawQuery("SELECT nombre FROM Contactos WHERE nombre=?",args);
            if (cursor.moveToFirst()) {
                Toast.makeText(getApplicationContext(), "Ya existe este usuario", Toast.LENGTH_SHORT).show();

            }else{
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("nombre",nombre);
                nuevoRegistro.put("apellido", apellido);
                db.insert("Contactos",null,nuevoRegistro);
                Toast.makeText(getApplicationContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }else{
            Toast.makeText(getApplicationContext(), "Es obligatorio rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Datos> getAll(){

        Cursor miCursor = db.rawQuery("SELECT nombre,apellido FROM Contactos",null);
        if(miCursor.moveToNext()){
          do{
              String nombreU = miCursor.getString(0);
              String apelldoU = miCursor.getString(1);
              datos.add(new Datos(nombreU,apelldoU));
          }while (miCursor.moveToNext());
        }else{
            Toast.makeText(getApplicationContext(), "No hay contactos en la BD", Toast.LENGTH_SHORT).show();
        }
        miCursor.close();
        return datos;
    }

    private void printContacts(){
        datos.clear();
        Adaptador miAdaptador = new Adaptador(this,getAll());
        listaContactos.setAdapter(miAdaptador);


    }

}