package com.meizu.powertesttool.powerdataupdate;

import android.net.Uri;

/**
 * Created by wangwen1 on 16-4-25.
 */
public class PermissionStore {
    public static class PackageStore{
        public static final String AUTHORITY = "com.meizu.safe.alphame.provider";
        public static final String TABLE_NAME = "PkgNameIndex";
        public static final String PATH = "PkgNameIndex";
        public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);

        public static final String PKG_NAME = "pkgName";
        public static final String LEVEL_VALUE = "levelValue";
        public static final String TYPE_VALUE = "typeValue";
        public static final String OPERATION_VALUE = "operationValue";
        public static final String USER_SETTING_VALUE = "userSettingValue";
    }
}
