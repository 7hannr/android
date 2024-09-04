package com.example.restaurantmanagement;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class FoodListFragment extends Fragment {
    FoodListAdapter adapter = new FoodListAdapter();
    List<FoodVO> array = new ArrayList<>();
    StaggeredGridLayoutManager manager;
    int type = 1;
    FoodDAO dao = new FoodDAO();
    FoodHelper helper;

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    array = dao.list(helper, false);
                    adapter.notifyDataSetChanged();
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        helper = new FoodHelper(getContext());
        array = dao.list(helper, false);

        RecyclerView list = view.findViewById(R.id.list_food);
        list.setAdapter(adapter);
        manager = new StaggeredGridLayoutManager(type, StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        ImageView imgType = view.findViewById(R.id.type);
        view.findViewById(R.id.type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    imgType.setImageResource(R.drawable.ic_list2);
                    type = 2;
                } else {
                    imgType.setImageResource(R.drawable.ic_list);
                    type = 1;
                }
                manager = new StaggeredGridLayoutManager(type, StaggeredGridLayoutManager.VERTICAL);
                list.setLayoutManager(manager);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        array=dao.list(helper, false);
        adapter.notifyDataSetChanged();
    } //onResume
} //FoodListFragment


// RecyclerView 어댑터 클래스 정의
    private class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_food, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            FoodVO vo = array.get(position);
            holder.image.setImageBitmap(BitmapFactory.decodeFile(vo.getImage()));
            holder.name.setText(vo.getName());
            holder.description.setText(vo.getDescription());

            if (vo.getKeep() == 1) {
                holder.keep.setImageResource(R.drawable.ic_keep_on);
            } else {
                holder.keep.setImageResource(R.drawable.ic_keep_off);
            }

            holder.keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder box = new AlertDialog.Builder(getActivity());
                    box.setTitle("질의");

                    if (vo.getKeep() == 1) {
                        box.setMessage("즐겨찾기를 취소하실래요?");
                        box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.keepUpdate(helper, vo.getId(), 0);
                                array = dao.list(helper, false);
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        box.setMessage("즐겨찾기에 등록하실래요?");
                        box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dao.keepUpdate(helper, vo.getId(), 1);
                                array = dao.list(helper, false);
                                notifyDataSetChanged();
                            }
                        });
                    }

                    box.setNegativeButton("아니오", null);
                    box.show();
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FoodInfoActivity.class);
                    intent.putExtra("id", vo.getId());
                    activityResult.launch(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image, keep;
            TextView name, description;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
                description = itemView.findViewById(R.id.description);
                keep = itemView.findViewById(R.id.keep);
            }
        }
    }
}
