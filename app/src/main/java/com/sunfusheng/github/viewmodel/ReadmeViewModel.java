package com.sunfusheng.github.viewmodel;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.net.download.DownloadManager;
import com.sunfusheng.github.net.download.IDownloadListener;
import com.sunfusheng.github.net.download.ProgressResult;
import com.sunfusheng.github.util.AppUtil;
import com.sunfusheng.github.util.PermissionUtil;
import com.sunfusheng.github.util.SdCardUtil;

import java.io.File;

/**
 * @author by sunfusheng on 2018/11/19
 */
public class ReadmeViewModel extends ViewModel {

    public static final String README_DIR = "readme";

    private final MutableLiveData<String> params = new MutableLiveData();

    public final LiveData<ProgressResult<String>> liveData =
            Transformations.switchMap(params, this::downloadReadmeFile);

    public void setRequestParams(String repoFullName) {
        params.setValue(repoFullName);
    }

    public static String getReadmeFilePath(String username) {
        return SdCardUtil.getDiskCacheDir(README_DIR).getPath() + File.separator + username + "_readme.html";
    }

    public static String getRemoteReadmePath(String repoFullName) {
        return Constants.BASE_WEB_PAGE_URL + repoFullName;
    }

//    public static String getRemoteReadmePath(String repoFullName) {
//        return "https://raw.githubusercontent.com/" + repoFullName + "/master/README.md";
//    }

    private LiveData<ProgressResult<String>> downloadReadmeFile(String repoFullName) {
        String username = repoFullName.split("/")[0];
        MutableLiveData<ProgressResult<String>> mutableLiveData = new MutableLiveData<>();

        String filePath = getReadmeFilePath(username);
        File file = new File(filePath);
        if (file.exists()) {
            mutableLiveData.setValue(ProgressResult.success(username));
        }

        PermissionUtil.getInstant().requestPermission(AppUtil.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.OnPermissionCallback() {
            @Override
            public void onGranted() {
                File dir = SdCardUtil.getDiskCacheDir(README_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                DownloadManager.instance().download(getRemoteReadmePath(repoFullName), filePath,
                        new IDownloadListener() {
                            @Override
                            public void onStart() {
                                mutableLiveData.setValue(ProgressResult.start());
                            }

                            @Override
                            public void onSuccess(File file) {
                                mutableLiveData.setValue(ProgressResult.success(username));
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                mutableLiveData.setValue(ProgressResult.error(e));
                            }

                            @Override
                            public void onProgress(long bytesTransferred, long totalBytes, int percentage) {
                                mutableLiveData.setValue(ProgressResult.progress(percentage));
                            }
                        });
            }

            @Override
            public void onDenied() {
                mutableLiveData.setValue(ProgressResult.error("请打开读写权限！"));
            }
        });

        return mutableLiveData;
    }
}
