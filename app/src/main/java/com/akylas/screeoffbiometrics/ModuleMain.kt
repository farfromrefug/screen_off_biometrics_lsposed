package com.akylas.screeoffbiometrics

import android.app.AndroidAppHelper
import android.content.Context
import android.os.PowerManager

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XposedHelpers.findClass;

val rootPackage = "com.android.systemui";
val TAG = "com.akylas.screeoffbiometrics";
class ModuleMain : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!lpparam.packageName.equals(rootPackage))
            return;
        val keyguardUpdateMonitorClass = findClass("com.android.keyguard.KeyguardUpdateMonitor", lpparam.classLoader)
        findMethod(keyguardUpdateMonitorClass) { name == "shouldListenForFingerprint" }
            .hookBefore {
                val ctx = AndroidAppHelper.currentApplication()
                val pm = ctx.getSystemService(Context.POWER_SERVICE) as PowerManager;
                if (!pm.isInteractive) {
                    it.result = false
                }
            }
    }
}
