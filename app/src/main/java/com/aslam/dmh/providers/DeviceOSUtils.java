package com.aslam.dmh.providers;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by Aslam on 20-06-29.
 */

public class DeviceOSUtils {

    public static void setDeviceOwner(String componentName) {
        try {
            Class WangPosManagerClass = Class.forName("android.os.WangPosManager");
            Object WangPosManager = WangPosManagerClass.newInstance();
            Method method = WangPosManagerClass.getMethod("setDeviceOwner", String.class);
            method.invoke(WangPosManager, componentName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setGrantSecuritySettings(String componentName) {
        try {
            Class WangPosManagerClass = Class.forName("android.os.WangPosManager");
            Object WangPosManager = WangPosManagerClass.newInstance();
            Method method = WangPosManagerClass.getMethod("setGrantSecuritySettings", String.class);
            method.invoke(WangPosManager, componentName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Install apk without not notify user If return true, uninstall application success (Install
     * application is a time-consuming operation.It cannot be executed in the UI thread.)
     *
     * @param appPath is apk path. appPath is not empty
     */
    public static boolean silentInstall(final String appPath) {
        try {
            File file = new File(appPath);
            file.setReadable(true, false);
            file.setWritable(true, false);
            Class WangPosManagerClass = Class.forName("android.os.WangPosManager");
            Object WangPosManager = WangPosManagerClass.newInstance();
            Method method = WangPosManagerClass.getMethod("silentInstall", String.class);
            boolean result = (boolean) method.invoke(WangPosManager, appPath);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Uninstall apk without not notify user. If return true, uninstall application success (Uninstall
     * application is a time-consuming operation. It cannot be executed in the UI thread.)
     *
     * @param packageName is application package name. packageName is not empty
     */
    public static boolean silentUninstall(String packageName) {
        try {
            Class WangPosManagerClass = Class.forName("android.os.WangPosManager");
            Object WangPosManager = WangPosManagerClass.newInstance();
            Method method = WangPosManagerClass.getMethod("silentUninstall", String.class);
            boolean result = (boolean) method.invoke(WangPosManager, packageName);
            return result;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }
}