package com.proyect.utb.restaurantesutb.como_restaurante.insertar_comida;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.Validator;
import com.proyect.utb.restaurantesutb.MySingleton;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.comidas;
import com.proyect.utb.restaurantesutb.como_restaurante.comidas_res.comidas_res;
import com.proyect.utb.restaurantesutb.como_restaurante.pedidos.res_pedidos;
import com.proyect.utb.restaurantesutb.registrar.registrar;
import com.proyect.utb.restaurantesutb.tipo_cuenta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class res_insertarcomida extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nav_nombre,nav_email;
    CircleImageView nav_imagen;
    EditText nombre;
    EditText descripcion;
    EditText precio;
    Button foto;
    private CircleImageView img;
    private LinearLayout relativeLayout;
    private final String APP_DIRECTORIO ="fotos/";
    private final String MEDIA_DIRECTORIO =APP_DIRECTORIO+"misfotos/";
    String mpath="";
    Bitmap myBitmap_img=null;
    final int MY_PERMISSION=100,PHOTO_CODE=200,SELECT_PICTURE=300;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_insertarcomida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nombre= (EditText) findViewById(R.id.activity_insertarcomida_nombre);
        descripcion= (EditText) findViewById(R.id.activity_insertarcomida_descripcion);
        precio= (EditText) findViewById(R.id.activity_insertarcomida_precio);
        img= (CircleImageView) findViewById(R.id.activity_insertarcomida_imagen);
        foto= (Button) findViewById(R.id.activity_insertarcomida_foto);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opciones();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               add_comida();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
datos_restaurante();
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void add_comida(){
        final ProgressDialog progressDialog=new ProgressDialog(res_insertarcomida.this);
        progressDialog.setMessage("Registrando...");
        progressDialog.show();

        final String myBase64Image = encodeToBase64(myBitmap_img, Bitmap.CompressFormat.JPEG, 100);
        final String name=nombre.getText().toString();
        final String des=descripcion.getText().toString();
        final String precio1=precio.getText().toString();
        final String fecha=getDateTime();
        //
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url ="https://myservidor.000webhostapp.com/comidas/api/subir_fotos.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        comidas com=new comidas(name,fecha+".jpg",des,precio1,nav_email.getText().toString(),root.child("comida").push().getKey());

                        root.child("comidas").child(root.child("comida").push().getKey()).setValue(com);
                        progressDialog.dismiss();
                        nombre.setText("");
                        descripcion.setText("");
                        precio.setText("");
                        img.setImageBitmap(null);
                        Toast.makeText(res_insertarcomida.this, "subida exitosa", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error","hay un error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("tipo","2");
                params.put("nombre_imagen",fecha);
                params.put("imagen",myBase64Image);
                return params;
            }
        };
// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        //
        //


    }

    private void datos_restaurante(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_imagen= (CircleImageView) headerView.findViewById(R.id.activity_principal_clientes_header_imagen);
        nav_nombre = (TextView) headerView.findViewById(R.id.activity_principal_clientes_header_nombre);
        nav_email= (TextView) headerView.findViewById(R.id.activity_principal_clientes_header_email);

        SharedPreferences prefs =
                getSharedPreferences("cuenta", Context.MODE_PRIVATE);
        final String correo = prefs.getString("email", "1");
        root.child("restaurantes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot n : dataSnapshot.getChildren()){

                    String email1=n.child("email").getValue().toString().trim();
                    if(email1.equals(correo.trim())) {

                        nav_nombre.setText(n.child("nombre").getValue().toString());
                        nav_email.setText(email1);
                        Rect rect=new Rect(nav_imagen.getLeft(),nav_imagen.getTop(),nav_imagen.getRight(),nav_imagen.getBottom());
                        ImageRequest request = new ImageRequest("https://myservidor.000webhostapp.com/comidas/fotos_res/"+n.child("imagen").getValue().toString(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        nav_imagen.setImageBitmap(bitmap);
                                    }
                                }, 0, 0, null,
                                new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                });
// Access the RequestQueue through your singleton class.
                        request.setShouldCache(false);

                        MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        if (image==null){
            return "";
        }
        else{


            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            image.compress(compressFormat, quality, byteArrayOS);
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        }
    }

    private void opciones() {
        final CharSequence[] option={"Tomar foto","Elegir de galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(res_insertarcomida.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        opencamara();
                        break;
                    case 1:
                        Intent i=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(i.createChooser(i,"elige una opcion"), SELECT_PICTURE );
                        break;
                    default:
                        dialog.dismiss();
                        break;

                }
            }


        });
        builder.show();
    }

    private void opencamara() {
        File file=new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORIO);
        boolean directorio_exist=file.exists();
        if(!directorio_exist){
            directorio_exist=  file.mkdirs();

        }
        if(directorio_exist){
            long timestamp= System.currentTimeMillis()/1000;
            String nameimg=""+timestamp+".jpg";

            mpath=Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORIO+File.separator+nameimg;
            File file1=new File(mpath);
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
            startActivityForResult(intent,PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path",mpath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mpath=savedInstanceState.getString("file_path");
    }
    public Bitmap resize(Bitmap image) {

        int width = image.getWidth();
        int height = image.getHeight();
        Log.i("imagen normal",width+" - "+height);
        int finalWidth = 0;
        int finalHeight = 0;

        if(width>3500&&width<4500){
            finalWidth = width-((width/2)+(width/3));
            finalHeight=height-((height/2)+(height/3));
        }
        else if(width>2500&&width<=3499){
            finalWidth = width-((width/2)+(width/4));
            finalHeight=height-((height/2)+(height/4));
        }
        else if(width>1700&&width<=2499){
            finalWidth = width-((width/2)+(width/5));
            finalHeight=height-((height/2)+(height/5));
        }
        else if(width>1001&&width<1699){
            finalWidth = width-((width/2)+(width/6));
            finalHeight=height-((height/2)+(height/6));
        }
        else if(width>800 && width<1000){
            finalWidth = width-(width/3);
            finalHeight=height-(height/3);
        }
        else if(width<799){
            finalWidth = width;
            finalHeight=height;
        }
        else {

        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,new String[]{mpath},null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage","Scanned"+path+":");
                            Log.i("ExternalStorage","->Uri= "+uri);
                        }
                    });
                    myBitmap_img= BitmapFactory.decodeFile(mpath);
                    myBitmap_img=resize(myBitmap_img);
                    img.setImageBitmap(myBitmap_img);

                    break;
                case SELECT_PICTURE:
                    Uri uri=data.getData();
                    try {
                        myBitmap_img=MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        myBitmap_img=resize(myBitmap_img);
                        img.setImageBitmap(myBitmap_img);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private boolean myRequesStoragePermission() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||(shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(relativeLayout,"Los permisos son necesarios para poder usar la aplicacion",Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},MY_PERMISSION);
                }
            }).show();
        }else {    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},MY_PERMISSION);
        }
        return false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSION){
            if(grantResults.length==2&&grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos Aceptados", Toast.LENGTH_SHORT).show();
                foto.setEnabled(true);
            }else {
                showExplanation();
            }
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder=new AlertDialog.Builder(res_insertarcomida.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Necesitas aceptar los permisos para usas esta app");
        builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("cuenta", Context.MODE_PRIVATE);
            settings.edit().clear().commit();
            Intent intent=new Intent(res_insertarcomida.this,tipo_cuenta.class);
            startActivity(intent);

            res_pedidos res=new res_pedidos();
            comidas_res com=new comidas_res();
            com.comidasres.finish();
            res.respedidos.finish();
            finish();
        } else if (id == R.id.nav_pedidos) {
            res_pedidos res=new res_pedidos();

            res.respedidos.finish();

            Intent intent=new Intent(res_insertarcomida.this,res_pedidos.class);
            startActivity(intent);
            comidas_res com=new comidas_res();
            com.comidasres.finish();

            finish();

        } else if (id == R.id.nav_comidas) {
            comidas_res com=new comidas_res();
            com.comidasres.finish();
            Intent intent=new Intent(res_insertarcomida.this,comidas_res.class);
            startActivity(intent);
            res_pedidos res=new res_pedidos();

            res.respedidos.finish();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
