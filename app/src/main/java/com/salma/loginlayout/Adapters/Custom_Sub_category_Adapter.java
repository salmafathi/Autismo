package com.salma.loginlayout.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.salma.loginlayout.Interfaces.OnCustomAdapterAddClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.DatabaseAcess;
import com.salma.loginlayout.database.Subcategory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Sub_category_Adapter extends RecyclerView.Adapter<Custom_Sub_category_Adapter.ViewH> {

    ArrayList<Subcategory> customSubCategory = new ArrayList<>();
    OnCustomAdapterAddClickListener Addlistener ;
    DatabaseAcess db ;
    String categoryName ;
    int Maincategoryid;
    String MainCatName ;
    Context c ;

    public Custom_Sub_category_Adapter(Context c ,ArrayList<Subcategory> customSubCategory, OnCustomAdapterAddClickListener listener) {
        this.customSubCategory = customSubCategory ;
        this.Addlistener = listener ;
        this.c= c ;
    }

    @NonNull
    @Override
    public Custom_Sub_category_Adapter.ViewH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sub_card_view, null, false);
        Custom_Sub_category_Adapter.ViewH cat_view_holder = new Custom_Sub_category_Adapter.ViewH(v);
        return cat_view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Custom_Sub_category_Adapter.ViewH holder, int position) {

        db= DatabaseAcess.getInstance(c);
        db.open();
        categoryName = db.get_categorynamebycatid(customSubCategory.get(position).getCategory_id());
        Maincategoryid = db.get_MainCategoryIDbycatid(customSubCategory.get(position).getCategory_id());
        MainCatName = db.get_MainCategoryNamebymaincatid(Maincategoryid);
        db.close();

        Subcategory subcat = customSubCategory.get(position);
        holder.name1.setText(subcat.getSub_category_name());
        holder.name2.setText(categoryName+" | "+MainCatName);
        String uri = subcat.getSub_category_image();
        Picasso.get().load(uri).resize(150,150).centerInside().into(holder.img1,new Callback(){
            @Override
            public void onSuccess() {
                holder.img1.setVisibility(View.VISIBLE);
                holder.pb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {
                holder.img1.setVisibility(View.INVISIBLE);
                holder.pb.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return customSubCategory.size();
    }

    class ViewH extends RecyclerView.ViewHolder{
        TextView name1;
        TextView name2 ;
        ImageView img1;
        ProgressBar pb;
        Button Add ;
        public ViewH(@NonNull View itemView) {
            super(itemView);
            name1 = (TextView) itemView.findViewById(R.id.customText);
            name2 = (TextView) itemView.findViewById(R.id.Maincattext);
            img1 = (ImageView) itemView.findViewById(R.id.custom_image);
            pb = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            Add= (Button)itemView.findViewById(R.id.ADDbutton);

            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Addlistener.OnAddListner(customSubCategory.get(getAdapterPosition()).getId(),
                                             customSubCategory.get(getAdapterPosition()).getSub_category_name(),
                                              customSubCategory.get(getAdapterPosition()).getSub_category_image(),
                                                customSubCategory.get(getAdapterPosition()).getCategory_id(),
                                               customSubCategory.get(getAdapterPosition()).getStar_state(),
                                               customSubCategory.get(getAdapterPosition()).getPrivacy());

                    Snackbar.make(v, customSubCategory.get(getAdapterPosition()).getSub_category_name()+" added to "+categoryName, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            });

        }
    }


}
