package com.example.duan1_nhom5;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class ChiTietDienThoaiFragment extends Fragment {

    TextView ten,gia,chitiet,sotien,noidungchitiet,soluong;
    ImageView anhct,cong,tru;
    Button muahang;
    ArrayList<BinhLuan> dsbl = new ArrayList<BinhLuan>();
    EditText nhapcmt;
    Button cmt;
    String key;
    String uid;
    int so = 1;
    BinhLuanAdapter adapter;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ArrayList<DienThoai> dsm = new ArrayList<DienThoai>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChiTietDienThoaiFragment() {
        // Required empty public constructor
    }

    public static ChiTietDienThoaiFragment newInstance(String param1, String param2) {
        ChiTietDienThoaiFragment fragment = new ChiTietDienThoaiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rvcmt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        nhapcmt = view.findViewById(R.id.nhapbinhluan);
        cmt = view.findViewById(R.id.btnbinhluan);
        ten = view.findViewById(R.id.tv_name_chitiet_dienthoai);
        sotien = view.findViewById(R.id.tv_tien_chitiet_dienthoai);
        noidungchitiet = view.findViewById(R.id.tv_chitiet_dienthoai);
        muahang = view.findViewById(R.id.btn_muangay);
        soluong = view.findViewById(R.id.soluong);
        cong = view.findViewById(R.id.cong);




        tru = view.findViewById(R.id.tru);

        anhct=view.findViewById(R.id.img_chitiet_dienthoai);

        Bundle bundle = this.getArguments();
        String ten1 = bundle.getString("name");
        int gia = bundle.getInt("gia");
        String cht = bundle.getString("chitiet");
        String anh = bundle.getString("anh");
        int daban = bundle.getInt("daban");
        key = bundle.getString("keydt");
        byte[] manghinh = Base64.getDecoder().decode(anh);
        Bitmap bm = BitmapFactory.decodeByteArray(manghinh,0, manghinh.length);
        anhct.setImageBitmap(bm);
        sotien.setText(""+gia);
        ten.setText(ten1);
        FirebaseRecyclerOptions<BinhLuan> options =
                new FirebaseRecyclerOptions.Builder<BinhLuan>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("DienThoai").child(key).child("BinhLuan")
                                , BinhLuan.class)
                        .build();
        adapter = new BinhLuanAdapter(options);
        recyclerView.setAdapter(adapter);

        noidungchitiet.setText(cht);

        cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                uid = databaseReference.push().getKey();
                SharedPreferences sharedPref = getActivity().getSharedPreferences("ThongTin", MODE_PRIVATE);
                String email = sharedPref.getString("email","");
                String noidung = nhapcmt.getText().toString();
                String ngay = String.valueOf(System.currentTimeMillis());
                BinhLuan binhLuan = new BinhLuan(uid,email,noidung,ngay);

                databaseReference.child("DienThoai").child(key).child("BinhLuan").child(uid).setValue(binhLuan).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        nhapcmt.setText("");
                    }
                });
            }
        });

        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                so = so+1;
                soluong.setText(so+"");
                int sl = Integer.parseInt(soluong.getText().toString());
                int tinhtong = gia * sl;
                sotien.setText(tinhtong+"");
            }
        });
        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                so = so-1;
                soluong.setText(""+so);
                int sl = Integer.parseInt(soluong.getText().toString());
                int tinhtong = gia * sl;
                sotien.setText(tinhtong+"");
            }
        });


        muahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tengh = ten.getText().toString();
                int giagh = Integer.parseInt(sotien.getText().toString());
                int sogh = Integer.parseInt(soluong.getText().toString());
                byte[] anh=ImageView_To_Byte(anhct);

                String chuoianh = Base64.getEncoder().encodeToString(anh);
                GioHang gioHang = new GioHang(tengh,giagh,sogh,chuoianh,gia,key,daban);

                String uid = firebaseAuth.getInstance().getCurrentUser().getUid();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("GioHang").child(uid).push().setValue(gioHang);
                Fragment fragment = new GioHangFragment();
                FragmentManager fmgr = getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                FragmentTransaction ft = fmgr.beginTransaction();
                ft.replace(R.id.nav_host_fragment_content_main, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }
    public byte[] ImageView_To_Byte(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    private void getlist(){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("DienThoai").child(key+"").child("BinhLuan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    BinhLuan binhLuan = dataSnapshot.getValue(BinhLuan.class);
                    dsbl.add(binhLuan);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sanpham, container, false);
    }
}