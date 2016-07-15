package sctek.cn.ysbracelet.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import sctek.cn.ysbracelet.R;
import sctek.cn.ysbracelet.ble.BleUtils;
import sctek.cn.ysbracelet.device.DeviceInformation;
import sctek.cn.ysbracelet.devicedata.YsData;
import sctek.cn.ysbracelet.fragments.HomeFragment;
import sctek.cn.ysbracelet.http.XmlNodes;
import sctek.cn.ysbracelet.http.YsHttpConnection;
import sctek.cn.ysbracelet.thread.HttpConnectionWorker;
import sctek.cn.ysbracelet.uiwidget.CircleImageView;
import sctek.cn.ysbracelet.user.UserManagerUtils;
import sctek.cn.ysbracelet.user.YsUser;
import sctek.cn.ysbracelet.utils.DialogUtils;
import sctek.cn.ysbracelet.utils.ImageCropUtils;
import sctek.cn.ysbracelet.utils.UrlUtils;

public class EditorDeviceInfoActivity extends AppCompatActivity {

    private final static String TAG = EditorDeviceInfoActivity.class.getSimpleName();

    private TextView titleTv;
    private View actionBarV;
    private ImageButton backIb;
    private ImageButton actionIb;

    private EditText nameEt;
    private Spinner sexSp;
    private Spinner ageSp;
    private Spinner heightSp;
    private Spinner weightSP;

    private String deviceId;

    private CircleImageView gravatarCiv;

    private final int CROP_PICTRURE_MSG = 1;
    private final int UPLOAD_PICTRUE_MSG =5;
    private final int SAVE_PHOTO_IMAGE = 6;
    private final int REQUEST_TAKE_PICTURE = 1, REQUEST_LOCAL_PICTURE = 2
            , REQUEST_CROP_PICTURE = 3;
    private final int SHOW_UPDATE_PHOTO = 3;

    private String imagePath;
    private String cropImagePath;

    private View selectPhotoFullV;
    private View selectPhotoV;
    private View progressV;

    private TextView takePictureTv;
    private TextView localPhotoTv;

    private Animation selectPhotoAniOut;
    private Animation selectPhotoAniIn;

    private Bitmap imageBitmap;

    private DisplayImageOptions displayImageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_device_info);

        deviceId = getIntent().getStringExtra(HomeFragment.EXTR_DEVICE_ID);
        imagePath = Environment.getExternalStorageDirectory().getPath() + "/imageTemp.png";
        cropImagePath = Environment.getExternalStorageDirectory().getPath() + "/imageCropTemp.png";
        Log.e(TAG, imagePath);

        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gravatar_stub)
                .showImageOnFail(R.drawable.gravatar_stub)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.gravatar_stub)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        initElement();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        Uri uri = null;
        if(requestCode == REQUEST_LOCAL_PICTURE) {
            if(resultCode == RESULT_OK) {
                uri = data.getData();
            }
        }
        else if(requestCode == REQUEST_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                uri = Uri.fromFile(new File(imagePath));
            }
        }
        else if(requestCode == REQUEST_CROP_PICTURE) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if(bundle != null) {
                    imageBitmap = bundle.getParcelable("data");
                    mHandler.obtainMessage(UPLOAD_PICTRUE_MSG, imageBitmap).sendToTarget();
                }
            }
        }
        if(uri == null)
            return;
        Log.e(TAG, uri.toString());
        mHandler.obtainMessage(CROP_PICTRURE_MSG, uri).sendToTarget();
    }

    private void initElement() {

        actionBarV = findViewById(R.id.action_bar_rl);
        titleTv = (TextView)findViewById(R.id.title_tv);
        backIb = (ImageButton)findViewById(R.id.nav_back_ib);
        actionIb = (ImageButton)findViewById(R.id.action_ib);

        DeviceInformation device = YsUser.getInstance().getDevice(deviceId);

        titleTv.setText(device.name);
        actionIb.setVisibility(View.VISIBLE);
        actionIb.setImageResource(R.drawable.ic_delete_selector);
        actionIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmDialog();
            }
        });
        backIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameEt = (EditText)findViewById(R.id.name_et);
        sexSp = (Spinner)findViewById(R.id.sex_sp);
        ageSp = (Spinner)findViewById(R.id.age_sp);
        heightSp = (Spinner)findViewById(R.id.height_sp);
        weightSP = (Spinner)findViewById(R.id.weigth_sp);

        nameEt.setText(device.name);

        if(device.sex.equals("Female")) {
            sexSp.setSelection(0);
        }
        else {
            sexSp.setSelection(1);
        }
        ageSp.setSelection(device.age -1);
        heightSp.setSelection(device.height - 75);
        weightSP.setSelection(device.weight - 10);

        selectPhotoFullV = findViewById(R.id.select_photo_fullscreen_rl);
        selectPhotoV = findViewById(R.id.select_photo_rl);
        progressV = findViewById(R.id.uploading_photo_progress);
        gravatarCiv = (CircleImageView)findViewById(R.id.gravatar_civ);

        ImageLoader.getInstance().displayImage(YsUser.getInstance().getDevice(deviceId).getImagePath(),
                gravatarCiv, displayImageOptions);

        takePictureTv = (TextView)findViewById(R.id.take_picture_tv);
        localPhotoTv = (TextView)findViewById(R.id.local_picture_tv);

        takePictureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoFullV.setVisibility(View.GONE);
                File image = new File(imagePath);
                if(image.exists()) {
                    image.delete();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(intent, REQUEST_TAKE_PICTURE);
            }
        });

        localPhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoFullV.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQUEST_LOCAL_PICTURE);
            }
        });

    }

    public void showEditPhotoLayout(View view) {
        selectPhotoFullV.setVisibility(View.VISIBLE);
        selectPhotoAniIn = AnimationUtils.loadAnimation(
                this, R.anim.search_layout_in_from_down);
        selectPhotoFullV.startAnimation(selectPhotoAniIn);
    }

    public void hideEditPhotoLayout(View view) {
        selectPhotoAniOut = AnimationUtils.loadAnimation(
                this, R.anim.search_layout_out_from_up);
        selectPhotoFullV.startAnimation(selectPhotoAniOut);
        selectPhotoAniOut
                .setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        // TODO Auto-generated method stub
                        selectPhotoFullV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    public void showDeleteConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_device_msg);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserManagerUtils.delDevice(YsUser.getInstance().getName(), deviceId, new HttpConnectionWorker.ConnectionWorkListener() {
                    @Override
                    public void onWorkDone(int resCode) {
                        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                            DeviceInformation device = YsUser.getInstance().getDevice(deviceId);
                            YsUser.getInstance().deleteDevice(device, getContentResolver());
                            setResult(HomeFragment.RESULT_CODE_DELETE_OK);
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            onBackPressed();
                        }
                        else {
                            DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.delete_device_fail);
                        }
                    }

                    @Override
                    public void onResult(YsData result) {

                    }

                    @Override
                    public void onError(Exception e) {
                        DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.delete_device_fail);
                    }
                });
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void onSaveButtonClicked(View v) {

        if(BleUtils.DEBUG) Log.e(TAG, "onSaveButtonClicked");

        final String name = nameEt.getText().toString();
        final String sex = sexSp.getSelectedItem().toString();
        String ageStr = ageSp.getSelectedItem().toString();
        String heightStr = heightSp.getSelectedItem().toString();
        String weightStr = weightSP.getSelectedItem().toString();

        if(!isValidName(name)) {
            nameEt.requestFocus();
            return;
        }

        final int age = getAge(ageStr);
        final int height = getHeight(heightStr);
        final int weight = getWeight(weightStr);

        if(!infoChanged(name, sex, age, height, weight)) {
            onBackPressed();
            return;
        }

        UserManagerUtils.updateDeviceInfo(YsUser.getInstance().getName(), deviceId, name, sex, age, height, weight
                , new HttpConnectionWorker.ConnectionWorkListener() {
            @Override
            public void onWorkDone(int resCode) {

                if(resCode == XmlNodes.RESPONSE_CODE_FAIL) {
                    DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.update_deviceinfo_fail);
                }
                else if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                    DeviceInformation device = YsUser.getInstance().getDevice(deviceId);
                    device.name = name;
                    device.sex = sex;
                    device.age = age;
                    device.height = height;
                    device.weight = weight;
                    device.update(getContentResolver());
                    setResult(HomeFragment.RESULT_CODE_EDIT_OK);
                }
                onBackPressed();
            }

            @Override
            public void onResult(YsData result) {
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.update_deviceinfo_fail);
                onBackPressed();
            }
        });

    }

    private int getAge(String str) {
        String temp[] = str.split(" ");
        return Integer.parseInt(temp[0]);
    }

    private int getHeight(String str) {
        return Integer.parseInt(str.replace("cm", ""));
    }

    private int getWeight(String str) {
        return Integer.parseInt(str.replace("kg", ""));
    }

    private boolean isValidName(String name) {
        if(TextUtils.isEmpty(name))
            return false;
        if (name.length() > 20) {
            DialogUtils.makeToast(this, R.string.name_too_long);
            return false;
        }
        return true;
    }

    private boolean infoChanged(String name, String sex, int age ,int height, int weight) {
        Log.e(TAG, name + " " + sex + " " + age + " " + height + " " + weight);
        DeviceInformation device = YsUser.getInstance().getDevice(deviceId);
        Log.e(TAG, device.name + " " + device.sex + " " + device.age + " " + device.height + " " + device.weight);
        if (!device.name.equals(name) || !device.sex.equals(sex) || age != device.age
                || device.height != height || device.weight != weight)
            return true;
        return false;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == CROP_PICTRURE_MSG) {
                Uri uri = (Uri)msg.obj;
                Intent intent = new Intent("com.android.camera.action.CROP");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    String url= ImageCropUtils.getPath(EditorDeviceInfoActivity.this,uri);
                    Log.e(TAG,"url:" + url);
                    intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
                }else{
                    Log.e(TAG,"url:" + uri.getPath());
                    intent.setDataAndType(uri, "image/*");
                }
                intent.putExtra("crop", true);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 100);
                intent.putExtra("outputY", 100);
                File cropImage = new File(cropImagePath);
                if(cropImage.exists()) {
                    cropImage.delete();
                }
                intent.putExtra("scale", true);
                intent.putExtra("noFaceDetection", true);
                intent.putExtra("return-data", true);
                intent.putExtra("output", Uri.fromFile(cropImage));
                startActivityForResult(intent, REQUEST_CROP_PICTURE);
            }
            else if(msg.what == UPLOAD_PICTRUE_MSG) {
                Bitmap image = (Bitmap)msg.obj;
                String params = UrlUtils.compositeUploadImageParams(YsUser.getInstance().getName(), deviceId, image);
                String url = UrlUtils.UPLOAD_IMAGE_URL;
                YsHttpConnection connection = new YsHttpConnection(url, YsHttpConnection.METHOD_POST, params);
                new HttpConnectionWorker(connection, new HttpConnectionWorker.ConnectionWorkListener() {
                    @Override
                    public void onWorkDone(int resCode) {
                        progressV.setVisibility(View.GONE);
                        if(resCode == XmlNodes.RESPONSE_CODE_SUCCESS) {
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().getMemoryCache();
                            ImageLoader.getInstance().displayImage(YsUser.getInstance().getDevice(deviceId).getImagePath(),
                                    gravatarCiv, displayImageOptions);
                            setResult(HomeFragment.RESULT_CODE_EDIT_OK);
                        }
                        else {
                            DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.upload_image_fail);
                        }
                    }

                    @Override
                    public void onResult(YsData result) {

                    }

                    @Override
                    public void onError(Exception e) {
                        progressV.setVisibility(View.GONE);
                        DialogUtils.makeToast(EditorDeviceInfoActivity.this, R.string.upload_image_fail);
                    }
                }).start();
                progressV.setVisibility(View.VISIBLE);
            }
        }
    };

}
