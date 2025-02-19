package kv.vension.fastframe.core

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * ========================================================================
 * 作 者：Vension
 * 主 页：Github: https://github.com/Vension
 * 日 期：2019/7/15 10:48
 *   ______       _____ _______ /\/|_____ _____            __  __ ______
 *  |  ____/\    / ____|__   __|/\/  ____|  __ \     /\   |  \/  |  ____|
 *  | |__ /  \  | (___    | |     | |__  | |__) |   /  \  | \  / | |__
 *  |  __/ /\ \  \___ \   | |     |  __| |  _  /   / /\ \ | |\/| |  __|
 *  | | / ____ \ ____) |  | |     | |    | | \ \  / ____ \| |  | | |____
 *  |_|/_/    \_\_____/   |_|     |_|    |_|  \_\/_/    \_\_|  |_|______|
 *
 * Take advantage of youth and toss about !
 * ------------------------------------------------------------------------
 * 描述：页面公共接口
 * ========================================================================
 */
interface IAppCompatPage {

    fun getPageFragmentManager(): FragmentManager

    /**
     * 是否显示通用toolBar
     *
     * @return true 默认显示
     */
    fun showToolBar(): Boolean = true

    /**
     * toolbar是否覆盖在内容区上方
     *
     * @return false 不覆盖  true 覆盖
     */
    fun isToolbarCover(): Boolean = false

    /**
     * 请求数据前是否检查网络连接
     *
     * @return false 默认不检查
     */
    fun isCheckNet(): Boolean = true

    /**
     * 是否需要显示 TipView
     */
    fun enableNetworkTip(): Boolean = false

    /**
     *  获取bundle 中的数据
     */
    fun getBundleExtras() : Bundle

    /**
     * 是否使用 EventBus
     */
    fun useEventBus(): Boolean = true

    /**
     * 延时
     */
    fun postDelay(delayMillis: Long, block: () -> Unit)

    fun startProxyActivity(vClass: Class<out Fragment>)
    fun startProxyActivity(vClass: Class<out Fragment>, view: View?)
    fun startProxyActivity(vClass: Class<out Fragment>, bundle: Bundle)
    fun startProxyActivity(vClass: Class<out Fragment>, bundle: Bundle, view: View?)
    fun startProxyActivityForResult(vClass: Class<out Fragment>, requestCode: Int)
    fun startProxyActivityForResult(vClass: Class<out Fragment>, view: View?, requestCode: Int)
    fun startProxyActivityForResult(vClass: Class<out Fragment>, bundle: Bundle, requestCode: Int)
    fun startProxyActivityForResult(vClass: Class<out Fragment>, bundle: Bundle, view: View?, requestCode: Int)

}