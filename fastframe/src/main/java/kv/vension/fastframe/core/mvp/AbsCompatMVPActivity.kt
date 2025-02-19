package kv.vension.fastframe.core.mvp

import android.os.Bundle
import android.widget.Toast
import kv.vension.fastframe.core.AbsCompatActivity


/**
 * ========================================================================
 * 作 者：Vension
 * 主 页：Github: https://github.com/Vension
 * 日 期：2019/8/23 14:25
 *   ______       _____ _______ /\/|_____ _____            __  __ ______
 *  |  ____/\    / ____|__   __|/\/  ____|  __ \     /\   |  \/  |  ____|
 *  | |__ /  \  | (___    | |     | |__  | |__) |   /  \  | \  / | |__
 *  |  __/ /\ \  \___ \   | |     |  __| |  _  /   / /\ \ | |\/| |  __|
 *  | | / ____ \ ____) |  | |     | |    | | \ \  / ____ \| |  | | |____
 *  |_|/_/    \_\_____/   |_|     |_|    |_|  \_\/_/    \_\_|  |_|______|
 *
 * Take advantage of youth and toss about !
 * ------------------------------------------------------------------------
 * 描述：带 MVP 的 Activity - 基类
 *
 * Kotlin 泛型中的 in 和 out
 * 如果你的类是将泛型作为内部方法的返回，那么可以用 out:因为其主要是产生（produce）指定泛型对象
 * interface Production<out T> {
 *    fun produce(): T
 *  }
 * 如果你的类是将泛型对象作为函数的参数，那么可以用 in,因为其主要是消费指定泛型对象
 * interface Consumer<in T> {
 *    fun consume(item: T)
 *   }
 *
 * Invariant(不变)
 * 如果既将泛型作为函数参数，又将泛型作为函数的输出，那就既不用 in 或 out。
 * interface ProductionConsumer<T> {
 *    fun produce(): T
 *     fun consume(item: T)
 *  }
 * ========================================================================
 */
abstract class AbsCompatMVPActivity<in V : IView,P : IPresenter<V>> : AbsCompatActivity(), IView {

    /*
   *   kotlin 懒加载，在第一次使用Presenter时初始化，这种设计是针对一个View只针对一个Presenter。
   *   多个Presenter的情况此处不应该使用泛型
   */
    protected val mPresenter: P by lazy {
        createPresenter()
    }

    /**
     *  创建Presenter
     *
     *  @return P
     */
    abstract fun createPresenter(): P


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view和presenter解绑
        mPresenter?.let {
            it.attachView(this as V)
        }
    }


    /**
     * 释放一些资源
     */
    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.let {
            it.detachView()
        }
    }


    override fun showProgressDialog() {
        if (!mLoadingDialog.isShowing){
            mLoadingDialog.show()
        }
    }

    override fun dismissProgressDialog() {
        if (mLoadingDialog.isShowing){
            mLoadingDialog.dismiss()
        }
    }

    override fun showLoading() {
        actMultiStateLayout?.let {
            if (it.getViewStatus() != it.STATUS_LOADING){
                it.showLoading()
            }
        }
    }


    override fun showContent() {
        actMultiStateLayout?.let {
            if (it.getViewStatus() != it.STATUS_CONTENT){
                it.showContent()
            }
        }
    }


    override fun showEmpty() {
        actMultiStateLayout?.let {
            if (it.getViewStatus() != it.STATUS_EMPTY){
                it.showEmpty()
            }
        }
    }

    override fun showNoNetwork() {
        actMultiStateLayout?.let {
            if (it.getViewStatus() != it.STATUS_NO_NETWORK){
                it.showNoNetwork()
            }
        }
    }

    override fun showError() {
        actMultiStateLayout?.let {
            if (it.getViewStatus() != it.STATUS_ERROR){
                it.showError()
            }
        }
    }


    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}