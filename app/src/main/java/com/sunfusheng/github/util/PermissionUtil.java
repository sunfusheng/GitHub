package com.sunfusheng.github.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunfusheng on 2018/4/24.
 */
public class PermissionUtil {

    private static PermissionUtil permissionUtil;
    private List<String> permissionList;

    public static PermissionUtil getInstant() {
        if (permissionUtil == null) {
            permissionUtil = new PermissionUtil();
        }
        return permissionUtil;
    }

    private PermissionUtil() {
    }

    private OnPermissionCallback onPermissionCallback;

    public void requestPermission(Context context, String permission, OnPermissionCallback onPermissionCallback) {
        List<String> permissions = new ArrayList<>();
        permissions.add(permission);
        requestPermission(context, permissions, onPermissionCallback);
    }

    public void requestPermission(Context context, List<String> permissions, OnPermissionCallback onPermissionCallback) {
        this.onPermissionCallback = onPermissionCallback;

        permissionList = new ArrayList<>();
        permissionList.addAll(permissions);

        if (!isGranted(context, permissionList)) {
            PermissionActivity.start(context);
        } else {
            this.onPermissionCallback.onGranted();
        }
    }

    public static boolean isGranted(Context context, List<String> permissions) {
        for (String permission : permissions) {
            if (!isGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGranted(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    public void setOnPermissionCallback(OnPermissionCallback onPermissionCallback) {
        this.onPermissionCallback = onPermissionCallback;
    }

    public interface OnPermissionCallback {
        void onGranted();

        void onDenied();
    }

    public static class PermissionActivity extends Activity {

        private static final int REQUEST_CODE = 1000;

        public static void start(final Context context) {
            Intent starter = new Intent(context, PermissionActivity.class);
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(starter);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (isGranted(this, permissionUtil.permissionList)) {
                if (permissionUtil.onPermissionCallback != null) {
                    permissionUtil.onPermissionCallback.onGranted();
                }
            } else {
                String[] permissions = permissionUtil.permissionList.toArray(new String[permissionUtil.permissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == REQUEST_CODE && isGranted(this, permissionUtil.permissionList)) {
                if (permissionUtil.onPermissionCallback != null) {
                    permissionUtil.onPermissionCallback.onGranted();
                }
            } else {
                if (permissionUtil.onPermissionCallback != null) {
                    permissionUtil.onPermissionCallback.onDenied();
                }
            }
            finish();
        }
    }

}
