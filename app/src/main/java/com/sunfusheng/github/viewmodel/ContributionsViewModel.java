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

import java.io.File;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class ContributionsViewModel extends ViewModel {

    private final MutableLiveData<String> params = new MutableLiveData();

    public final LiveData<ProgressResult<String>> liveData =
            Transformations.switchMap(params, this::downloadContributionsFile);

    public void setRequestParams(String username) {
        params.setValue(username);
    }

    private String getContributionsFilePath(String username) {
        return Constants.FileDir.CONTRIBUTIONS + username + "_contributions.html";
    }

    private LiveData<ProgressResult<String>> downloadContributionsFile(String username) {
        MutableLiveData<ProgressResult<String>> mutableLiveData = new MutableLiveData<>();

        String filePath = getContributionsFilePath(username);
        File file = new File(filePath);
        if (file.exists()) {
            mutableLiveData.setValue(ProgressResult.success(username));
        }

        PermissionUtil.getInstant().requestPermission(AppUtil.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.OnPermissionCallback() {
            @Override
            public void onGranted() {
                File dir = new File(Constants.FileDir.CONTRIBUTIONS);
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
