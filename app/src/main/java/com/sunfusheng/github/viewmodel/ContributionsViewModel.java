package com.sunfusheng.github.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.sunfusheng.github.Constants;
import com.sunfusheng.github.net.download.DownloadManager;
import com.sunfusheng.github.net.download.IDownloadListener;
import com.sunfusheng.github.net.download.ProgressResult;
import com.sunfusheng.github.util.PermissionUtil;

import java.io.File;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class ContributionsViewModel extends ViewModel {

    private final MutableLiveData<String> mParams = new MutableLiveData<>();

    public final LiveData<ProgressResult<String>> liveData =
            Transformations.switchMap(mParams, this::downloadContributionsFile);

    public void request(String username) {
        mParams.setValue(username);
    }

    public static String getContributionsFilePath(String username) {
        return Constants.CacheDir.CONTRIBUTION.getPath() + File.separator + username + ".html";
    }

    private LiveData<ProgressResult<String>> downloadContributionsFile(String username) {
        MutableLiveData<ProgressResult<String>> mutableLiveData = new MutableLiveData<>();

        String filePath = getContributionsFilePath(username);
        File file = new File(filePath);
        if (file.exists()) {
            mutableLiveData.setValue(ProgressResult.success(username));
        }

        PermissionUtil.checkAndRequestExternalStoragePermissions(new CheckRequestPermissionsListener() {
            @Override
            public void onAllPermissionOk(Permission[] allPermissions) {
                File dir = Constants.CacheDir.CONTRIBUTION;
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                DownloadManager.instance().download("https://github.com/users/" + username + "/contributions", filePath,
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
            public void onPermissionDenied(Permission[] refusedPermissions) {
                mutableLiveData.setValue(ProgressResult.error("请打开读写权限！"));
            }
        });
        return mutableLiveData;
    }
}
