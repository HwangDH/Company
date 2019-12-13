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
import android.graphics.Matrix;
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


import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    private String userChoosenTask;
    Uri[] imageUri  = new Uri[6];
    String [] imagePath = new String[6];
    private ImageView img1, img2, img3, img4, img5, img6;
    CognitoCachingCredentialsProvider credentialsProvider;
    File [] f = new File [6];
    AmazonS3 s3;
    TransferUtility transferUtility;

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

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:d99c73f5-b1fe-4ed7-a454-4f5b5446cf04", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        transferUtility = new TransferUtility(s3, getApplicationContext());

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
                        for(int i = 0; i<6; i++) {
                            TransferObserver observer = transferUtility.upload(
                                    "s3testdh",
                                    f[i].getName(),
                                    f[i]
                            );
                        }
                        Intent intent = new Intent(SendInfo.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(GET_GALLERY_IMAGE1);
                //   onCheck(GET_GALLERY_IMAGE1, PICK_FROM_CAMERA1);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(GET_GALLERY_IMAGE2);
                //   onCheck(GET_GALLERY_IMAGE2, PICK_FROM_CAMERA2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(GET_GALLERY_IMAGE3);
                //  onCheck(GET_GALLERY_IMAGE3, PICK_FROM_CAMERA3);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(GET_GALLERY_IMAGE4);
                // onCheck(GET_GALLERY_IMAGE4, PICK_FROM_CAMERA4);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(GET_GALLERY_IMAGE5);
                //  onCheck(GET_GALLERY_IMAGE5, PICK_FROM_CAMERA5);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(GET_GALLERY_IMAGE6);
                //   onCheck(GET_GALLERY_IMAGE6, PICK_FROM_CAMERA6);
            }
        });
    }

    private void selectImage(final int imgNum) {
        Log.d(TAG, "select Image");
        final CharSequence[] items = {"촬영하기", "사진 가져오기",
                "취소"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진가져오기");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // boolean result = Utility.checkPermission(getApplicationContext());

                if (items[item].equals("촬영하기")) {
                    userChoosenTask = "촬영하기";
                    //if (result)
                    //cameraIntent();

                } else if (items[item].equals("사진 가져오기")) {
                    userChoosenTask = "사진 가져오기";
                    //if (result)
                    galleryIntent(imgNum);

                } else if (items[item].equals("취소")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    private void galleryIntent(int imgNum) {
        Log.d(TAG, "Gallery Intent");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), imgNum);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_GALLERY_IMAGE1) {
                Log.d(TAG, "onActivityResult, GET_GALLERY_IMAGE1");
                Bitmap bm = null;
                imageUri[0] = data.getData();
                Log.d(TAG, Build.VERSION.SDK_INT + "");
                imagePath[0] = getRealPathFromURI(imageUri[0]);
                try {
                    bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                f[0] = new File(imagePath[0]);
                System.out.println(imagePath[0]);
                img1.setImageBitmap(bm);
                //onSelectFromGalleryResult(data, GET_GALLERY_IMAGE1);
            }
            else if (requestCode == GET_GALLERY_IMAGE2){
                Log.d(TAG, "onActivityResult, GET_GALLERY_IMAGE2");
                Bitmap bm = null;
                imageUri[1] = data.getData();
                Log.d(TAG, Build.VERSION.SDK_INT + "");
                imagePath[1] = getRealPathFromURI(imageUri[1]);
                try {
                    bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                f[1] = new File(imagePath[1]);
                img2.setImageBitmap(bm);
                //onSelectFromGalleryResult(data, GET_GALLERY_IMAGE2);
            }
            else if(requestCode == GET_GALLERY_IMAGE3){
                Log.d(TAG, "onActivityResult, GET_GALLERY_IMAGE3");
                Bitmap bm = null;
                imageUri[2] = data.getData();
                Log.d(TAG, Build.VERSION.SDK_INT + "");
                imagePath[2] = getRealPathFromURI(imageUri[2]);
                try {
                    bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                f[2] = new File(imagePath[2]);
                img3.setImageBitmap(bm);
                //onSelectFromGalleryResult(data, GET_GALLERY_IMAGE3);
            }
            else if(requestCode == GET_GALLERY_IMAGE4){
                Log.d(TAG, "onActivityResult, GET_GALLERY_IMAGE4");
                Bitmap bm = null;
                imageUri[3] = data.getData();
                Log.d(TAG, Build.VERSION.SDK_INT + "");
                imagePath[3] = getRealPathFromURI(imageUri[3]);
                try {
                    bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                f[3] = new File(imagePath[3]);
                img4.setImageBitmap(bm);
                //onSelectFromGalleryResult(data, GET_GALLERY_IMAGE4);
            }
            else if(requestCode == GET_GALLERY_IMAGE5){
                Log.d(TAG, "onActivityResult, GET_GALLERY_IMAGE5");
                Bitmap bm = null;
                imageUri[4] = data.getData();
                Log.d(TAG, Build.VERSION.SDK_INT + "");
                imagePath[4] = getRealPathFromURI(imageUri[4]);
                try {
                    bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                f[4] = new File(imagePath[4]);
                img5.setImageBitmap(bm);
                //onSelectFromGalleryResult(data, GET_GALLERY_IMAGE5);
            }
            else if(requestCode == GET_GALLERY_IMAGE6){
                Log.d(TAG, "onActivityResult, GET_GALLERY_IMAGE6");
                Bitmap bm = null;
                imageUri[5] = data.getData();
                Log.d(TAG, Build.VERSION.SDK_INT + "");
                imagePath[5] = getRealPathFromURI(imageUri[5]);
                try {
                    bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                f[5] = new File(imagePath[5]);
                img6.setImageBitmap(bm);
                //onSelectFromGalleryResult(data, GET_GALLERY_IMAGE6);
            }

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor;
        String result ="";
        String[] proj = {MediaStore.Images.Media.DATA};
        cursor = getContentResolver().query(contentURI, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
        // f[0] =new File (cursor.getString(column_index));
        return result;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
// create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if (Build.VERSION.SDK_INT <= 19) {
//matrix.postRotate(90);
        }
// recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public File SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/MobileCard");

        if (!myDir.exists()) {
            Log.d("SaveImage", "non exists : " + myDir);
            myDir.mkdirs();
        }

        long now = System.currentTimeMillis();
        String fname = now + ".jpg";
        File file = new File(myDir, fname);

        if (file.exists()) {
            Log.d("SaveImage", "file exists");
            file.delete();
        } else {
            Log.d("SaveImage", "file non exists");
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Log.d("SaveImage", "file save");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
}
