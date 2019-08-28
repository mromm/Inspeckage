package mobi.acpm.inspeckage.unpack;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import mobi.acpm.inspeckage.ui.MainActivity;

class MyHooker extends XC_MethodHook{
    private String packageName;
    private XC_LoadPackage.LoadPackageParam param;

    public MyHooker(String packageName, XC_LoadPackage.LoadPackageParam param) {
        this.packageName = packageName;
        this.param = param;
    }

    public MyHooker() {
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        Class cls = (Class) param.getResult();
        if (cls == null) {
            return;
        }
        String name = cls.getName();
        XposedBridge.log("当前类名：" + name);
        byte[] bArr =(byte[]) UnPack.Dex_getBytes.invoke(UnPack.getDex.invoke(cls));
        if (bArr == null) {
            XposedBridge.log("数据为空：返回");
            return;
        }

        XposedBridge.log("开始写数据");
        String dex_path = "/data/data/" + packageName + "/" + packageName + "_" + bArr.length + ".dex";
        XposedBridge.log(dex_path);
        File file = new File(dex_path);
        if (file.exists()) {
            return;
        }
        FIO.writeByte(bArr, file.getAbsolutePath());

    }

}


public class UnPack {


    static Class Dex;
    static Method Dex_getBytes;
    static Method getDex;
    XSharedPreferences shared;


    public static UnPack instance;
    public static final String DIR = "dir";
    public static final String KEY = "packagename";
    List apps;
    ActionBar bar;
    public static final String h = "Ubb6o57-M2ziSR9K3Jy7c7rohXGWVyh0";
    PackageManager manager;
    public static final String s = "FDex2(1.1)\nFDex升级版-By FormatFa格式化法\n\n所需环境:\n1.安卓4.4以上\n2.root\n3.Xposed框架\n4.输出哪看软件提示\n\n安卓7.0测试通过";
    SharedPreferences sp;

    public UnPack() {
        this.getDex = (Method)null;
        instance = this;
    }


    public static void initRefect() {
        Class v4 = null;
        try {
            Dex = Class.forName("com.android.dex.Dex");
            Dex_getBytes = Dex.getDeclaredMethod("getBytes");
            if((h.endsWith("0")) && s.length() == 91) {
                try {
                    v4 = Class.forName("java.lang.Class");
                }
                catch(ClassNotFoundException v3_1) {
                    v3_1.printStackTrace();
                }
                getDex = v4.getDeclaredMethod("getDex");
            }
        }
        catch(Exception v3) {
            XposedBridge.log(v3.toString());
        }
    }
    public static void Hooking(XC_LoadPackage.LoadPackageParam arg18, String packName){
        Class v11;
        initRefect();
        if(packName == null || packName.equals("")) {
            XposedBridge.log("没有指定apk,请打开模块选择要脱dex的apk");
        }
        else if(!arg18.packageName.equals(packName)) {
        }
        else {
            XposedBridge.log(new StringBuffer().append(packName).append(" has hook").toString());
            ClassLoader v6 = arg18.classLoader;

            XposedHelpers.findAndHookMethod("java.lang.ClassLoader", v6,"loadClass", String.class, Boolean.TYPE, new MyHooker(packName, arg18));
        }
    }
}
