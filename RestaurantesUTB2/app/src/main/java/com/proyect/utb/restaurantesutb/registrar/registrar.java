package com.proyect.utb.restaurantesutb.registrar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.prmja.http.prmja_com;
import com.proyect.utb.restaurantesutb.MySingleton;
import com.proyect.utb.restaurantesutb.R;
import com.proyect.utb.restaurantesutb.clases.restaurante;
import com.proyect.utb.restaurantesutb.login.login;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class registrar extends AppCompatActivity implements Validator.ValidationListener{

    private Button select_img,enviar;
    @NotEmpty(message = "Escriba algun nombre para el restaurante" )
    private EditText nombre;
    @NotEmpty(message = "Escriba alguna descripcion" )
    EditText descripcion;
    @Password(min = 4, scheme = Password.Scheme.ANY, message = "minimo 4 caracteres")
    EditText contra;
    @Email(message= "Escriba un email valido" )
    EditText email;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private CircleImageView img;
    private LinearLayout relativeLayout;
    Validator validator;
    private final String APP_DIRECTORIO ="fotos/";
    private final String MEDIA_DIRECTORIO =APP_DIRECTORIO+"misfotos/";
    String mpath="";
    Bitmap myBitmap_img=null;
   final int MY_PERMISSION=100,PHOTO_CODE=200,SELECT_PICTURE=300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        //
        select_img= (Button) findViewById(R.id.button_select_imagen);
        nombre= (EditText) findViewById(R.id.editText_nombre);
        descripcion= (EditText) findViewById(R.id.editText_descripcion);
        contra= (EditText) findViewById(R.id.editText_contraseña);
        email= (EditText) findViewById(R.id.editText_email);
        img= (CircleImageView) findViewById(R.id.imagen_select);
        relativeLayout= (LinearLayout) findViewById(R.id.view);
        enviar= (Button) findViewById(R.id.button_enviar);
        validator = new Validator(this);
        validator.setValidationListener(this);
        //
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

            }
        });
        //
        if(myRequesStoragePermission())select_img.setEnabled(true);
        else select_img.setEnabled(false);

        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opciones();
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
        final AlertDialog.Builder builder=new AlertDialog.Builder(registrar.this);
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
            select_img.setEnabled(true);
        }else {
            showExplanation();
        }
    }
    }

    private void showExplanation() {
        AlertDialog.Builder builder=new AlertDialog.Builder(registrar.this);
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
    String res="";

    @Override
    public void onValidationSucceeded() {
        final ProgressDialog progressDialog=new ProgressDialog(registrar.this);
        progressDialog.setMessage("Registrando...");
        progressDialog.show();

        final String myBase64Image = encodeToBase64(myBitmap_img, Bitmap.CompressFormat.JPEG, 100);
        final String name=nombre.getText().toString();
        final String des=descripcion.getText().toString();
        final String pass=contra.getText().toString();
        final String ema=email.getText().toString();
        //
        MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url ="https://myservidor.000webhostapp.com/comidas/api/subir_fotos.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        restaurante restaurante1=new restaurante(name,ema+".jpg",des,ema,pass);

                        root.child("restaurantes").child(root.child("retaurantes").push().getKey()).setValue(restaurante1);
                        progressDialog.dismiss();
                        nombre.setText("");
                        descripcion.setText("");
                        email.setText("");
                        contra.setText("");
                        img.setImageBitmap(null);
                        Toast.makeText(registrar.this, "Registro exitoso ahora Inicia Sesión", Toast.LENGTH_SHORT).show();
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
                params.put("tipo","3");
                params.put("nombre_imagen",ema);
                params.put("imagen",myBase64Image);
                return params;
            }
        };
// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        //
        //

    }



    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors)
        {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
            else
            {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
