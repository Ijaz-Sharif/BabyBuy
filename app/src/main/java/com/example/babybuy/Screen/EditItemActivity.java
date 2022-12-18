package com.example.babybuy.Screen;

import static com.example.babybuy.MainActivity.itemArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.babybuy.Database.DatabaseService;
import com.example.babybuy.Model.Item;
import com.example.babybuy.R;
import com.example.babybuy.Utils.Constant;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {
    Uri imgUri=null;
    EditText et_quantity,et_price,et_name;
    ImageView itemPic;
    Button btn_save;
    DatabaseService databaseService;
    private Dialog loadingDialog;
    Bitmap bitmap=null;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemPic=findViewById(R.id.foodPic);
        et_quantity=findViewById(R.id.et_quantity);
        et_price=findViewById(R.id.et_price);
        btn_save=findViewById(R.id.btn_save);
        et_name=findViewById(R.id.et_name);
        databaseService=new DatabaseService(this);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        index=getIntent().getIntExtra("index",-1);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {

                if(et_quantity.getText().toString().isEmpty()){
                    et_quantity.setError("required");
                }
                else if(et_name.getText().toString().isEmpty()){
                    et_name.setError("required");
                }
                else if(et_price.getText().toString().isEmpty()){
                    et_price.setError("required");
                }

                else {
                    loadingDialog.show();
                    if(bitmap==null){
                        databaseService.updateItem(new Item(et_name.getText().toString(),et_quantity.getText().toString(),et_price.getText().toString(),
                                Constant.getUserId(EditItemActivity.this)),Constant.getBytes(bitmap));
                    }
                    else {
                        databaseService.updateItem(new Item(et_name.getText().toString(),et_quantity.getText().toString(),et_price.getText().toString(),
                                Constant.getUserId(EditItemActivity.this)));
                    }

                    loadingDialog.dismiss();

                }
            }
        });


        itemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPicture();
            }
        });
    }
    @Override
    protected void onStart() {
        itemPic.setImageBitmap(Constant.getImages(itemArrayList.get(index).getImage()));

        et_quantity.setText(itemArrayList.get(index).getQuantity());
        et_name.setText(itemArrayList.get(index).getName());
        et_price.setText(itemArrayList.get(index).getPrice());
        super.onStart();
    }

    public void AddPicture() {
        // read image from gallery
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            imgUri  = data.getData();

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                itemPic.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}