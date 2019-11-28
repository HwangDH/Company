package com.example.company;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class SendInfo extends Activity {
    AlertDialog alertDialog;
    Button btn1;

    //카메라 변수
    private final int PICK_FROM_CAMERA1 = 2;
    private final int PICK_FROM_CAMERA2 = 101;
    private final int PICK_FROM_CAMERA3 = 102;
    private final int PICK_FROM_CAMERA4 = 103;
    private final int PICK_FROM_CAMERA5 = 104;
    private final int PICK_FROM_CAMERA6 = 105;

    //갤러리 변수
    private final int GET_GALLERY_IMAGE1 = 200;
    private final int GET_GALLERY_IMAGE2 = 201;
    private final int GET_GALLERY_IMAGE3 = 202;
    private final int GET_GALLERY_IMAGE4 = 203;
    private final int GET_GALLERY_IMAGE5 = 204;
    private final int GET_GALLERY_IMAGE6 = 205;

    private ImageView img1, img2, img3, img4, img5, img6;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_info);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        img6 = (ImageView) findViewById(R.id.img6);

        btn1 = (Button) findViewById(R.id.btn1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(SendInfo.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(SendInfo.this).create();

                alertDialog.setTitle("확인");
                alertDialog.setMessage("보내시겠습니까?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SendInfo.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheck(GET_GALLERY_IMAGE1, PICK_FROM_CAMERA1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheck(GET_GALLERY_IMAGE2, PICK_FROM_CAMERA2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheck(GET_GALLERY_IMAGE3, PICK_FROM_CAMERA3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheck(GET_GALLERY_IMAGE4, PICK_FROM_CAMERA4);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheck(GET_GALLERY_IMAGE5, PICK_FROM_CAMERA5);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCheck(GET_GALLERY_IMAGE6, PICK_FROM_CAMERA6);
            }
        });
    }

    public void onCheck(final int gallery, final int camera) {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction(camera);
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction(gallery);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(SendInfo.this)
                .setTitle("이미지 불러오는 방법 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    private void setImage(int number) {
        if (number == GET_GALLERY_IMAGE1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            img1.setImageBitmap(originalBm);
        } else if (number == GET_GALLERY_IMAGE2) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            img2.setImageBitmap(originalBm);
        } else if (number == GET_GALLERY_IMAGE3) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            img3.setImageBitmap(originalBm);
        } else if (number == GET_GALLERY_IMAGE4) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            img4.setImageBitmap(originalBm);
        } else if (number == GET_GALLERY_IMAGE5) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            img5.setImageBitmap(originalBm);
        } else if (number == GET_GALLERY_IMAGE6) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            img6.setImageBitmap(originalBm);
        }
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }

    //카메라 불러오는 함수
    public void doTakePhotoAction(int camera) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
            Uri photoUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, camera);
        }
    }

    //갤러리에서 이미지 가져오는 함수
    public void doTakeAlbumAction(int gallery) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, gallery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage(requestCode);
            img1.setImageURI(photoUri);
        }
        else if (requestCode == GET_GALLERY_IMAGE2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage(requestCode);
            img2.setImageURI(photoUri);
        }
        else if (requestCode == GET_GALLERY_IMAGE3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage(requestCode);
            img3.setImageURI(photoUri);
        }
        else if (requestCode == GET_GALLERY_IMAGE4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage(requestCode);
            img4.setImageURI(photoUri);
        }
        else if (requestCode == GET_GALLERY_IMAGE5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage(requestCode);
            img5.setImageURI(photoUri);
        }
        else if (requestCode == GET_GALLERY_IMAGE6 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            setImage(requestCode);
            img6.setImageURI(photoUri);
        }
        else if (requestCode == PICK_FROM_CAMERA1) {
            setImage(requestCode);
        } else if (requestCode == PICK_FROM_CAMERA1) {
            setImage(requestCode);;
        } else if (requestCode == PICK_FROM_CAMERA1) {
            setImage(requestCode);
        } else if (requestCode == PICK_FROM_CAMERA1) {
            setImage(requestCode);
        } else if (requestCode == PICK_FROM_CAMERA1) {
            setImage(requestCode);
        } else if (requestCode == PICK_FROM_CAMERA1) {
            setImage(requestCode);
        }
    }
}
/*
    public void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp"+String.valueOf(System.currentTimeMillis())+".jpg";
        mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void doTakeAlbumAction(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode!=RESULT_OK){
            return;
        }

        switch (requestCode){
            case PICK_FROM_CAMERA: {
                mlmageCaptureUri = data.getData();
                Log.d("SmartWheel", mlmageCaptureUri.getPath().toString());
            }
            case PICK_FROM_ALBUM:{
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri, "image/*");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }
            case CROP_FROM_IMAGE:{
                if(resultCode!=RESULT_OK){
                    return;
                }

                final Bundle extras = data.getExtras();

                String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel/"+System.currentTimeMillis()+".jpg";

                if(extras!=null){
                    Bitmap photo = extras.getParcelable("data");
                    img1.setImageBitmap(photo);
                    storeCropImage(photo, filepath);
                    absolutePath = filepath;
                    break;
                }

                File f = new File(mlmageCaptureUri.getPath());
                if(f.exists()){
                    f.delete();
                }
            }
        }
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.img1){
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드 할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }
    }

    public void storeCropImage(Bitmap bitmap, String filepath){

    }
    */
