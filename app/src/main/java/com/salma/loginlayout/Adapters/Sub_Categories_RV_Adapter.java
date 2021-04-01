package com.salma.loginlayout.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.salma.loginlayout.Interfaces.OnFavoritesClickListener;
import com.salma.loginlayout.Interfaces.OnSubCardClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Subcategory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Sub_Categories_RV_Adapter extends RecyclerView.Adapter<Sub_Categories_RV_Adapter.ViewH>{
    ArrayList<Subcategory> subcategories = new ArrayList<>();
    OnSubCardClickListener listener ;
    OnFavoritesClickListener favlistener ;

    public Sub_Categories_RV_Adapter(ArrayList<Subcategory> subcategories, OnSubCardClickListener listener, OnFavoritesClickListener favlistener) {
        this.subcategories = subcategories;
        this.listener = listener;
        this.favlistener = favlistener ;

    }

    @NonNull
    @Override
    public ViewH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_card_view, null, false);
        ViewH cat_view_holder = new ViewH(v);

        return cat_view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewH holder, final int position) {
        Subcategory subcat = subcategories.get(position);
        holder.name1.setText(subcat.getSub_category_name());
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
        if(subcat.getStar_state() == 0){
            holder.star.setChecked(false);
            holder.star.setBackgroundResource(R.drawable.ic_staroff);
        }
        else{
            holder.star.setChecked(true);
            holder.star.setBackgroundResource(R.drawable.ic_star);

        }



    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }



    class ViewH extends RecyclerView.ViewHolder{
        TextView name1;
        ImageView img1;
        ProgressBar pb;
        ToggleButton star ;

        public ViewH(@NonNull View itemView) {
            super(itemView);
           name1 = (TextView) itemView.findViewById(R.id.grid_text);
            img1 = (ImageView) itemView.findViewById(R.id.grid_image);
            pb = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            star= (ToggleButton)itemView.findViewById(R.id.favorate);

            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onitemClick(subcategories.get(getAdapterPosition()).getSub_category_name(),subcategories.get(getAdapterPosition()).getSub_category_image());
                }
            });

            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(subcategories.get(getAdapterPosition()).getStar_state()==0){
                         favlistener.onCheckStar(subcategories.get(getAdapterPosition()).getId(),
                                subcategories.get(getAdapterPosition()).getSub_category_name(),
                                subcategories.get(getAdapterPosition()).getSub_category_image(),
                                subcategories.get(getAdapterPosition()).getCategory_id(),
                                subcategories.get(getAdapterPosition()).getStar_state(),
                                subcategories.get(getAdapterPosition()).getPrivacy());


                            subcategories.get(getAdapterPosition()).setStar_state(1);
                            star.setBackgroundResource(R.drawable.ic_star);



                    }
                    else{
                       favlistener.onUncheckStar(subcategories.get(getAdapterPosition()).getId(),
                                subcategories.get(getAdapterPosition()).getSub_category_name(),
                                subcategories.get(getAdapterPosition()).getSub_category_image(),
                                subcategories.get(getAdapterPosition()).getCategory_id(),
                                subcategories.get(getAdapterPosition()).getStar_state(),
                                subcategories.get(getAdapterPosition()).getPrivacy());

                            subcategories.get(getAdapterPosition()).setStar_state(0);
                            star.setBackgroundResource(R.drawable.ic_staroff);
//                        star.setBackgroundResource(R.drawable.ic_staroff);
//                        subcategories.get(getAdapterPosition()).setStar_state(0);
//                        Snackbar.make(v,  subcategories.get(getAdapterPosition()).getSub_category_name()+ " removed from favorites", Snackbar.LENGTH_SHORT).setAnchorView(R.id.fab).show();

                    }
                }
            });




        }



    }
}
