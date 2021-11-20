package com.example.duan1_nhom5;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.LogRecord;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private List<DienThoai> dsm;
    private List<DienThoai> dsm1;
    private Context c;
    DienThoai dienThoai;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    public SearchAdapter(Context c, ArrayList<DienThoai> dsm) {
        this.dsm = dsm;
        this.dsm1 = dsm;
        this.c = c;
    }


    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View view = inflater.inflate(R.layout.search_item, parent, false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchViewHolder holder, int position) {
        DienThoai lg = dsm.get(position);
        holder.tendt.setText(""+lg.getTen());
        holder.giadt.setText("Giá : "+lg.getGiaTien());
        holder.chitiet.setText(""+lg.getChiTiet());
        byte[] manghinh = Base64.getDecoder().decode(lg.getLinkAnh());
        Bitmap bm = BitmapFactory.decodeByteArray(manghinh,0, manghinh.length);
        holder.anhdt.setImageBitmap(bm);

    }

    @Override
    public int getItemCount() {
        return dsm.size();
    }




    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView tendt,giadt,chitiet;
        ImageView anhdt;

        public SearchViewHolder(View view) {
            super(view);

            tendt =view.findViewById(R.id.tencim);
            giadt = view.findViewById(R.id.tiencim);
            chitiet =view.findViewById(R.id.ctcim);
            anhdt = view.findViewById(R.id.cim);


        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                if (keyword.isEmpty()){
                    dsm = dsm1;
                }else {
                    List<DienThoai> list = new ArrayList<>();
                    for (DienThoai thoai:dsm1){
                        if (thoai.getTen().toLowerCase().contains(keyword.toLowerCase())){
                            list.add(thoai);
                        }
                    }
                        dsm = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dsm;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                dsm = (List<DienThoai>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
