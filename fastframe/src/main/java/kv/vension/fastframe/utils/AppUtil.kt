package kv.vension.fastframe.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import java.security.MessageDigest


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/5/22 12:00
 * 描  述：APP 相关的工具类
 * ========================================================
 */

object AppUtil {

    /**
     * 判断当前应用是否是debug模式
     */
    fun isApkInDebug(context: Context): Boolean {
        try {
            val info = context.applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            return false
        }

    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    fun getAppVersionName(context: Context): String {
        var verName = ""
        try {
            val packageName = context.packageName
            verName = context.packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    /**
     * 得到软件版本号
     *
     * @param context 上下文
     * @return 当前版本Code
     */
    fun getAppVersionCode(context: Context): Int {
        var verCode = -1
        try {
            val packageName = context.packageName
            verCode = context.packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verCode
    }

    /**
     * 获取应用运行的最大内存
     *
     * @return 最大内存
     */
    val maxMemory: Long
        get() = Runtime.getRuntime().maxMemory() / 1024


    /**
     * 获取应用签名
     *
     * @param context 上下文
     * @param pkgName 包名
     * @return 返回应用的签名
     */
    @SuppressLint("PackageManagerGetSignatures")
    fun getSign(context: Context, pkgName: String): String? {
        return try {
            @SuppressLint("PackageManagerGetSignatures")
            val pis = context.packageManager.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES)
            hexDigest(pis.signatures[0].toByteArray())
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     * @return 32位签名字符串
     */
    private fun hexDigest(paramArrayOfByte: ByteArray): String {
        val hexDigits = charArrayOf(48.toChar(), 49.toChar(), 50.toChar(), 51.toChar(), 52.toChar(), 53.toChar(), 54.toChar(), 55.toChar(), 56.toChar(), 57.toChar(), 97.toChar(), 98.toChar(), 99.toChar(), 100.toChar(), 101.toChar(), 102.toChar())
        try {
            val localMessageDigest = MessageDigest.getInstance("MD5")
            localMessageDigest.update(paramArrayOfByte)
            val arrayOfByte = localMessageDigest.digest()
            val arrayOfChar = CharArray(32)
            var i = 0
            var j = 0
            while (true) {
                if (i >= 16) {
                    return String(arrayOfChar)
                }
                val k = arrayOfByte[i].toInt()
                arrayOfChar[j] = hexDigits[0xF and k.ushr(4)]
                arrayOfChar[++j] = hexDigits[k and 0xF]
                i++
                j++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }


    /**
     * 获取设备的可用内存大小
     *
     * @param context 应用上下文对象context
     * @return 当前内存大小
     */
    fun getDeviceUsableMemory(context: Context): Int {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        // 返回当前系统的可用内存
        return (mi.availMem / (1024 * 1024)).toInt()
    }


    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    val sdkVersion: Int
        get() = android.os.Build.VERSION.SDK_INT

    fun exit() {
        // 杀死进程
        android.os.Process.killProcess(android.os.Process.myPid())
        // 退出程序
        System.exit(0)
        // 通知系统回收
        System.gc()
    }


}