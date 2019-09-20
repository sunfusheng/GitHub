package com.sunfusheng.github.util;

import android.Manifest;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;


/**
 * @author sunfusheng on 2018/4/24.
 */
public class PermissionUtil {
    private static final Permissions ExternalStoragePermissions = Permissions.build(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    public static void checkAndRequestExternalStoragePermissions(CheckRequestPermissionsListener listener) {
        SoulPermission.getInstance().checkAndRequestPermissions(ExternalStoragePermissions, listener);
    }
}
