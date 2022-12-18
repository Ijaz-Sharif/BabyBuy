package com.example.babybuy.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babybuy.Database.DatabaseService;
import com.example.babybuy.MainActivity;
import com.example.babybuy.Model.Item;
import com.example.babybuy.R;
import com.example.babybuy.Utils.Constant;

import java.util.ArrayList;

public class PurchaseListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Item> itemArrayList =new ArrayList<Item>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list);
        databaseService=new DatabaseService(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }
    public void getData(){
        loadingDialog.show();
        // get food record on the the base of user id
        itemArrayList=databaseService.getPurchaseItemData(Constant.getUserId(this));
        // call the adapter class
        arrayAdapter=new ArrayAdapter();
        recyclerView.setAdapter(arrayAdapter);
        loadingDialog.dismiss();
    }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(PurchaseListActivity.this).inflate(R.layout.item_list,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.image.setImageBitmap(Constant.getImages(itemArrayList.get(position).getImage()));

            holder.quantity.setText("Item Quantity :"+itemArrayList.get(position).getQuantity());
            holder.name.setText("Name :\n"+itemArrayList.get(position).getName());
            holder.price.setText("Price :\n"+itemArrayList.get(position).getPrice()+" $");

        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView quantity,name,price;
            CardView cardView;
            ImageView image;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                price=itemView.findViewById(R.id.price);
                quantity=itemView.findViewById(R.id.quantity);
                cardView=itemView.findViewById(R.id.cardView);
                image=itemView.findViewById(R.id.image);
            }
        }
    }
}