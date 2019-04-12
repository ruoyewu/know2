package com.wuruoye.know.util.base.permission;


import androidx.annotation.NonNull;

/**
 * @Created : wuruoye
 * @Date : 2018/3/22.
 * @Decription :
 */

public interface IWPermission {
    interface OnWPermissionListener {
        void onPermissionResult(int requestCode, @NonNull String[] permissions,
                                @NonNull int[] grantResult);
    }

    void requestPermission(String[] permissions, int requestCode, OnWPermissionListener listener);

    boolean isGranted(String[] permissions);
}
