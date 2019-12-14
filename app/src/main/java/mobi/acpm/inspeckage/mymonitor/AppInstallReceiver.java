package mobi.acpm.inspeckage.mymonitor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.NotYetConnectedException;

import mobi.acpm.inspeckage.Module;
import mobi.acpm.inspeckage.util.Config;
import mobi.acpm.inspeckage.util.PackageDetail;

public class AppInstallReceiver extends BroadcastReceiver {
    private PackageDetail pd;
    private SharedPreferences mPrefs;

    private void loadSelectedApp(Context context, String pkg) {

        mPrefs = context.getSharedPreferences(Module.PREFS, context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mPrefs.edit();
        //this put has to come before the PackageDetail
        edit.putString(Config.SP_PACKAGE, pkg);

        pd = new PackageDetail(context, pkg);

        edit.putBoolean(Config.SP_HAS_W_PERMISSION, false);
        if (pd.getRequestedPermissions().contains("android.permission.WRITE_EXTERNAL_STORAGE") &&
                Build.VERSION.SDK_INT < 23) {
            edit.putBoolean(Config.SP_HAS_W_PERMISSION, true);
        }

        edit.putString(Config.SP_APP_NAME, pd.getAppName());
        edit.putString(Config.SP_APP_ICON_BASE64, pd.getIconBase64());
        edit.putString(Config.SP_PROCESS_NAME, pd.getProcessName());
        edit.putString(Config.SP_APP_VERSION, pd.getVersion());
        edit.putString(Config.SP_DEBUGGABLE, pd.isDebuggable());
        edit.putString(Config.SP_ALLOW_BACKUP, pd.allowBackup());
        edit.putString(Config.SP_APK_DIR, pd.getApkDir());
        edit.putString(Config.SP_UID, pd.getUID());
        edit.putString(Config.SP_GIDS, pd.getGIDs());
        edit.putString(Config.SP_DATA_DIR, pd.getDataDir());

        edit.putString(Config.SP_REQ_PERMISSIONS, pd.getRequestedPermissions());
        edit.putString(Config.SP_APP_PERMISSIONS, pd.getAppPermissions());

        edit.putString(Config.SP_EXP_ACTIVITIES, pd.getExportedActivities());
        edit.putString(Config.SP_N_EXP_ACTIVITIES, pd.getNonExportedActivities());

        edit.putString(Config.SP_EXP_SERVICES, pd.getExportedServices());
        edit.putString(Config.SP_N_EXP_SERVICES, pd.getNonExportedServices());

        edit.putString(Config.SP_EXP_BROADCAST, pd.getExportedBroadcastReceivers());
        edit.putString(Config.SP_N_EXP_BROADCAST, pd.getNonExportedBroadcastReceivers());

        edit.putString(Config.SP_EXP_PROVIDER, pd.getExportedContentProvider());
        edit.putString(Config.SP_N_EXP_PROVIDER, pd.getNonExportedContentProvider());

        edit.putString(Config.SP_SHARED_LIB, pd.getSharedLibraries());

        edit.putBoolean(Config.SP_APP_IS_RUNNING, false);
        edit.putString(Config.SP_DATA_DIR_TREE, "");

        //test
        //edit.putString(Config.SP_REPLACE_SP, "limitEventUsage,true");

        edit.apply();

        //resolve this problem
        if (pd.getRequestedPermissions().contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
            pd.extractInfoToFile();
        }

    }

    private static void echoDataToFile(String packageName){
        try {
            String cmd = "echo " + packageName +" > /data/local/tmp/monitor_package";
            System.out.println(cmd);

            Process logProcess = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));

            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            logProcess.destroy();

        } catch (IOException | NotYetConnectedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            echoDataToFile(packageName);
            loadSelectedApp(context, packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Toast.makeText(context, "卸载成功"+packageName, Toast.LENGTH_LONG).show();
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Toast.makeText(context, "替换成功"+packageName, Toast.LENGTH_LONG).show();
        }


    }

}
