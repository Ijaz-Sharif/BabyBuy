package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babybuy.Database.DatabaseService;
import com.example.babybuy.Model.Item;
import com.example.babybuy.Screen.AccountActivity;
import com.example.babybuy.Screen.AddItemActivity;
import com.example.babybuy.Screen.EditItemActivity;
import com.example.babybuy.Screen.PurchaseListActivity;
import com.example.babybuy.Utils.Constant;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
   public static ArrayList<Item> itemArrayList =new ArrayList<Item>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    DatabaseService databaseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


// below method is use to add swipe to delete method for item of recycler view.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Do you want to delete?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                databaseService.Del(itemArrayList.get(viewHolder.getAdapterPosition()).getItemId());
                                getData();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               getData();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        }).attachToRecyclerView(recyclerView);
       // below method is use to add swipe to mark purchased method for item of recycler view.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Do you want to mark Purchase?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                databaseService.update(itemArrayList.get(viewHolder.getAdapterPosition()).getItemId());
                                getData();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getData();
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        }).attachToRecyclerView(recyclerView);







    }
    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }
    public void getData(){
        loadingDialog.show();
        itemArrayList=databaseService.getItemData(Constant.getUserId(this));
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
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {



            holder.image.setImageBitmap(Constant.getImages(itemArrayList.get(position).getImage()));

            holder.quantity.setText("Item Quantity :"+itemArrayList.get(position).getQuantity());
            holder.name.setText("Name :\n"+itemArrayList.get(position).getName());
            holder.price.setText("Price :\n"+itemArrayList.get(position).getPrice()+" $");
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final CharSequence[] options = {"Update","Send Message", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Update")) {
                                      startActivity(new Intent(MainActivity.this, EditItemActivity.class)
                                      .putExtra("index",position));
                                dialog.dismiss();
                            } if (options[item].equals("Send Message")) {
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                String message ="Item name"+itemArrayList.get(position).getName()+"\n Item Quantity"+itemArrayList.get(position).getQuantity()
                                        +"\nItem Price"+itemArrayList.get(position).getPrice();
                                sendIntent.putExtra("sms_body", message);
                                sendIntent.setType("vnd.android-dir/mms-sms");
                                startActivity(sendIntent);
                                dialog.dismiss();
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }

                        }
                    });
                    builder.show();

                }
            });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logoutUser:
                Constant.setUserLoginStatus(MainActivity.this,false);

                startActivity(new Intent(MainActivity.this, AccountActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case  R.id.viewlist:
                startActivity(new Intent(MainActivity.this, PurchaseListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItem(View view) {
        startActivity(new Intent(MainActivity.this, AddItemActivity.class));
    }
}