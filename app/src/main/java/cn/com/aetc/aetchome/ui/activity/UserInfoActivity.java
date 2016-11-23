package cn.com.aetc.aetchome.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.aetc.aetchome.R;
import cn.com.aetc.aetchome.common.Constants;
import cn.com.aetc.aetchome.common.base.SwipeBackActivity;
import cn.com.aetc.aetchome.utils.MyUtils;
import cn.com.aetc.aetchome.utils.PrefserUtil;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by chenliang3 on 2016/8/3.
 */
public class UserInfoActivity extends SwipeBackActivity {

    @BindView(R.id.userinfo_scrollview)
    ScrollView scrollView;
    @BindView(R.id.userinfo_userhead)
    ImageView userHead;
    @BindView(R.id.userinfo_toolbar)
    Toolbar toolbar;

    private static final int PHOTO_ALBUM_REQUEST = 0x0001;
    private static final int PHOTO_CAMERA_REQUEST = 0x0002;
    private static final int PHOTO_REQUEST_CUT = 0x0003;
    private static final String USERHEAD = "aetcer";
    private File file;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    protected void initViews() {
        toolbar.setTitle("用户信息");
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        String tmpPath = PrefserUtil.getInstance(this).get(Constants.USER_HEAD_PATH, String.class, "");
        if (!TextUtils.isEmpty(tmpPath)){
            File tmpFile = new File(tmpPath);
            if (tmpFile.exists()){
                Glide.with(this)
                        .load(tmpFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .bitmapTransform(new RoundedCornersTransformation(this, 10, 0, RoundedCornersTransformation.CornerType.ALL))
                        .error(R.mipmap.ic_default)
                        .into(userHead);
            }else {
                // TODO: 2016/8/4 从服务器下载
            }
        }
    }

    @OnClick(R.id.userinfo_photo)
    public void photoClick(){
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照","打开相册")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index){
                            case 0:
                                choiceHeadFromCamera(USERHEAD+System.currentTimeMillis()+".png");
                                break;
                            case 1:
                                choiceHeadFromAlbum(USERHEAD+System.currentTimeMillis()+".png");
                                break;
                            default:
                                break;
                        }
                    }
                })
        .show();
    }

    //从系统相册中选取图片
    private void choiceHeadFromAlbum(String path){
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");

        String savePath = MyUtils.getSavePath(path+System.currentTimeMillis());
        file = new File(savePath);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        startActivityForResult(intent, PHOTO_ALBUM_REQUEST);
    }

    //拍照选取图片
    private void choiceHeadFromCamera(String path){
        String savePath = MyUtils.getSavePath(path+System.currentTimeMillis());
        file = new File(savePath);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("output", Uri.fromFile(file));
        startActivityForResult(intent, PHOTO_CAMERA_REQUEST);
    }

    //裁剪图片
    private void cutPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 4);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //递归删除文件夹下的所有文件
    private void delAllFiles(File root){
        File files[] = root.listFiles();
        if (files != null){
            for (File f : files){
                if (f.isDirectory()){
                    delAllFiles(f);
                    try {
                        f.delete();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    if (f.exists()){
                        delAllFiles(f);
                        try {
                            f.delete();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case PHOTO_ALBUM_REQUEST:
                    cutPhoto(data.getData());
                    break;
                case PHOTO_CAMERA_REQUEST:
                    cutPhoto(Uri.fromFile(file));
                    break;
                case PHOTO_REQUEST_CUT:
                    delAllFiles(new File(Environment.getExternalStorageDirectory().getPath() + MyUtils.SAVE_PATH));//删除文件夹下所有文件
                    Bitmap bitmap = data.getExtras().getParcelable("data");
                    file = MyUtils.bitmap2File(USERHEAD+System.currentTimeMillis()+".png", bitmap);
                    PrefserUtil.getInstance(this).put(Constants.USER_HEAD_PATH, file.getPath());
                    Glide.with(this)
                            .load(file)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .bitmapTransform(new RoundedCornersTransformation(this, 10, 0, RoundedCornersTransformation.CornerType.ALL))
                            .error(R.mipmap.ic_default)
                            .into(userHead);
                    break;
                default:
                    break;
            }
        }
    }
}
