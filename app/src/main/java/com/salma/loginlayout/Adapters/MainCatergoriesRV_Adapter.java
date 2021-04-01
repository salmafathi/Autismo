package com.salma.loginlayout.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.salma.loginlayout.Interfaces.OnMainCardClickListener;
import com.salma.loginlayout.Interfaces.OnSubCardClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.activites.CustomSubCategories;
import com.salma.loginlayout.database.Main_Categories;
import com.salma.loginlayout.database.Subcategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainCatergoriesRV_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Main_Categories> maincategories;
    OnMainCardClickListener listener ;
    CardView card;
    CardView card2 ;
    Context c ;

    public MainCatergoriesRV_Adapter(ArrayList<Main_Categories> maincategories, OnMainCardClickListener listener, Context c) {
        this.maincategories = maincategories;
        this.listener = listener;
        this.c = c ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType == 0 ) {
                  View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view, null, false);
                  MainCatergoriesRV_Adapter.ViewH0 maincat_view_holder = new MainCatergoriesRV_Adapter.ViewH0(v);
                  return maincat_view_holder;
        }

         else{
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view_lastitem, null, false);
                MainCatergoriesRV_Adapter.ViewH2 maincat_view_holder2 = new MainCatergoriesRV_Adapter.ViewH2(v2);
                return maincat_view_holder2;
        }
   }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == 0){
            Main_Categories maincat = maincategories.get(position);
            ViewH0 holder0 = (ViewH0) holder;
            holder0.maincategory_name.setText(maincat.getMain_category_name());
            holder0.maincategory_icon.setImageResource(c.getResources().getIdentifier(maincat.getMain_category_icon(), "drawable", c.getPackageName()));
        }
        else{

             ViewH2  holder2 = (ViewH2) holder;
             holder2.maincategorypus_icon.setImageResource(c.getResources().getIdentifier("ic_plus", "drawable", c.getPackageName()));
        }

    }


    @Override
    public int getItemCount() {
        return maincategories.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==5){
            return 2 ;
        }
        else{
            return 0 ;
        }
    }

    class ViewH0 extends RecyclerView.ViewHolder{
        TextView maincategory_name;
        ImageView maincategory_icon;

        public ViewH0(@NonNull View itemView) {
            super(itemView);
            maincategory_name = (TextView) itemView.findViewById(R.id.main_category_name);
            maincategory_icon = (ImageView) itemView.findViewById(R.id.icon_image);
            card = itemView.findViewById(R.id.mcard);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMainCarditemClick(maincategories.get(getAdapterPosition()).getId());
                }
            });

        }



    }


    class ViewH2 extends RecyclerView.ViewHolder{
        ImageView maincategorypus_icon;
        public ViewH2(@NonNull View itemView) {
            super(itemView);
            maincategorypus_icon = (ImageView) itemView.findViewById(R.id.icon2_image);
            card2 = itemView.findViewById(R.id.mcard2);
            card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(c.getApplicationContext(), CustomSubCategories.class);
                    c.getApplicationContext().startActivity(in);
                }
            });

        }



    }


    public static final Uri getUriToResource(Context context, int resId)
            throws Resources.NotFoundException {
        Resources res = context.getResources();
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        return resUri;
    }
}
