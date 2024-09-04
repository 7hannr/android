package com.example.restaurantmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;

public class RegisterImageFragment extends Fragment {
    FoodVO food = new FoodVO();
    EditText description;
    ImageView image;
    String strFile = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_image, container, false);
        description = view.findViewById(R.id.description);
        image = view.findViewById(R.id.image);

        food = (FoodVO) getArguments().getSerializable("food");
        description.setText(food.getDescription());
        description.setEnabled(false);

        if (food.getImage() != null) {
            image.setImageBitmap(BitmapFactory.decodeFile(food.getImage()));
        }

        view.findViewById(R.id.prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", food);
                RegisterActivity registerActivity = (RegisterActivity) getActivity();
                registerActivity.replaceFragment("input", bundle);
            }
        });

        // 카메라 또는 앨범 버튼을 클릭한 경우
        view.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box = new AlertDialog.Builder(getActivity());
                box.setItems(new String[]{"앨범", "카메라"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) { // 앨범
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultAlbum.launch(intent);
                        } else { // 카메라
                            try {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                File file = File.createTempFile("img_", ".jpg", storageDir);
                                strFile = file.getAbsolutePath();
                                Uri uriFile = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);
                                activityResultCamera.launch(intent);
                            } catch (Exception e) {
                                System.out.println("카메라 오류: " + e.toString());
                            }
                        }
                    }
                });
                box.show();
            }
        });

        // 완료 버튼을 클릭한 경우
        view.findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strFile.equals("") || food.getName().equals("")) {
                    Toast.makeText(getActivity(), "상호명과 이미지를 선택하세요!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder box = new AlertDialog.Builder(getActivity());
                    box.setTitle("질의");
                    box.setMessage("등록 내용을 저장하실래요?");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FoodHelper helper = new FoodHelper(getContext());
                            FoodDAO dao = new FoodDAO();
                            dao.foodInsert(helper, food);
                            getActivity().finish();
                        }
                    });
                    box.setNegativeButton("아니오", null);
                    box.show();
                }
            }
        });

        return view;
    }

    // 카메라로 촬영한 이미지를 처리
    ActivityResultLauncher<Intent> activityResultCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        image.setImageBitmap(BitmapFactory.decodeFile(strFile));
                        food.setImage(strFile);
                    }
                }
            }
    );

    // 앨범에서 선택한 이미지를 처리
    ActivityResultLauncher<Intent> activityResultAlbum = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Uri selectedImageUri = result.getData().getData();
                        Cursor cursor = getContext().getContentResolver().query(selectedImageUri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            strFile = cursor.getString(index);
                            image.setImageBitmap(BitmapFactory.decodeFile(strFile));
                            food.setImage(strFile);
                            cursor.close();
                        }
                    }
                }
            }
    );
}
