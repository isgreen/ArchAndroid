package br.com.isgreen.archandroid.base

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.extension.baseActivity
import br.com.isgreen.archandroid.extension.hideKeyboard
import br.com.isgreen.archandroid.extension.isAtLeastLollipop
import br.com.isgreen.archandroid.extension.isAtLeastMarshmallow
import br.com.isgreen.archandroid.util.OnActivityResultCallback
import br.com.isgreen.archandroid.util.OnEventReceivedListener
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

abstract class BaseFragment : Fragment() {

    enum class TransitionAnimation {
        TRANSLATE_FROM_RIGHT,
        TRANSLATE_FROM_LEFT,
        TRANSLATE_FROM_DOWN,
        TRANSLATE_FROM_UP,
        NO_ANIMATION,
        FADE
    }

    private val mHandler: Handler by lazy { Handler() }

    //    private val mPermissionHelper: PermissionHelper by lazy { PermissionHelperImpl(context) }
    private var mOnEventReceivedListener: OnEventReceivedListener? = { code, data ->
        onEventReceived(code, data)
    }

    private var mScreenLayout = -1
    private var mIsLayoutCreated = false
    private var mLayoutView: View? = null
    private var mBaseViewModel: BaseViewModel? = null

    //    private var mPermissionResultCallback: PermissionResultCallback? = null
    private var mOnActivityResultCallback: OnActivityResultCallback? = null

    abstract val module: Module?
    abstract val screenLayout: Int
    abstract val viewModel: BaseViewModel?

//    protected val permissionHelper
//        get() = mPermissionHelper
//
//    var permissionResultCallback
//        get() = mPermissionResultCallback
//        set(value) {
//            mPermissionResultCallback = value
//        }

    var onActivityResultCallback
        get() = mOnActivityResultCallback
        set(value) {
            mOnActivityResultCallback = value
        }


    abstract fun initView()
    abstract fun initObservers()
    abstract fun fetchInitialData()
    abstract fun showError(message: Any)
    abstract fun onLoadingChanged(isLoading: Boolean)
    open fun onEventReceived(code: Int, data: Any?) {}

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        loadModule()
        super.onCreate(savedInstanceState)

        mScreenLayout = screenLayout
        mBaseViewModel = viewModel
        baseActivity?.addOnEventReceivedListener(mOnEventReceivedListener)

        setHasOptionsMenu(true)
        setDefaultStatusBarColor()
        initDefaultObservers()
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mLayoutView == null) {
            mLayoutView = inflater.inflate(mScreenLayout, container, false)
        } else {
            (mLayoutView?.parent as? ViewGroup)?.removeView(mLayoutView)
        }

        hideKeyboard()

        return mLayoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!mIsLayoutCreated) {
            initView()
            fetchInitialData()
            mIsLayoutCreated = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            popBackStack()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        mPermissionResultCallback?.onPermissionResult(permissions, grantResults)
//        mPermissionResultCallback = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mOnActivityResultCallback?.invoke(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        baseActivity?.removeOnEventReceivedListener(mOnEventReceivedListener)
        mOnEventReceivedListener = null
        mOnActivityResultCallback = null
    }
    //endregion Fragment

    //region Local
    private fun initDefaultObservers() {
        mBaseViewModel?.redirect?.observe(this, Observer { destination ->
            val fragment = parentFragment?.parentFragment ?: this
            navigate(destination, TransitionAnimation.FADE, null, true, fragment)
            hideNavigationBottom()
        })
        mBaseViewModel?.loading?.observe(this, Observer { isLoading ->
            onLoadingChanged(isLoading)
        })
        mBaseViewModel?.message?.observe(this, Observer { message ->
            showError(message)
        })
    }

    private fun loadModule() {
        try {
            module?.let {
                unloadKoinModules(it)
                loadKoinModules(it)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    @SuppressLint("NewApi")
    fun changeLayout(layoutRoot: ConstraintLayout, @LayoutRes layoutRes: Int) {
        mHandler.postDelayed({
            mScreenLayout = layoutRes
            val constraintSet = ConstraintSet()
            constraintSet.clone(context, layoutRes)
            TransitionManager.beginDelayedTransition(layoutRoot)
            constraintSet.applyTo(layoutRoot)
        }, 100)
    }

    protected fun showNavigationBottom() {
        baseActivity?.changeNavigationVisibilityListener?.invoke(true)
    }

    protected fun hideNavigationBottom() {
        baseActivity?.changeNavigationVisibilityListener?.invoke(false)
    }

    protected fun sendEvent(code: Int, data: Any? = null) {
        baseActivity?.callOnEventReceived(code, data)
    }
    //endregion Local

    //region StatusBar
    private fun setDefaultStatusBarColor() {
        val themeStatusColor =
            if (isAtLeastMarshmallow()) getThemeStatusColor() else R.color.colorAccent
        changeStatusBarColor(themeStatusColor)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getThemeStatusColor(): Int {
        val outTypedValue = TypedValue()
        context?.theme?.resolveAttribute(R.attr.colorSurface, outTypedValue, true)
        return outTypedValue.data
    }

    protected fun changeStatusBarColor(color: Int) {
        activity?.let {
            if (isAtLeastMarshmallow()) {
                it.window.statusBarColor = color
                changeStatusBarIconsColor(color != Color.WHITE)
            } else if (isAtLeastLollipop()) {
                it.window.statusBarColor = color
            }
        }
    }

    protected fun changeStatusBarIconsColor(toWhite: Boolean) {
        activity?.let {
            it.window.decorView.systemUiVisibility = if (
                toWhite || Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            ) {
                0
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
    //endregion StatusBar

    //region Navigation
    fun navigate(
        directions: NavDirections,
        popUpTo: Int? = null,
        clearBackStack: Boolean? = false
    ) {
        val navController = NavHostFragment.findNavController(this)
        val destinationId = if (clearBackStack == true) navController.graph.id else popUpTo
        val options =
            buildOptions(TransitionAnimation.TRANSLATE_FROM_RIGHT, clearBackStack, destinationId)

        navController.navigate(directions, options)
    }

    fun navigate(
        directions: NavDirections,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
        popUpTo: Int? = null,
        clearBackStack: Boolean? = false
    ) {
        val navController = NavHostFragment.findNavController(this)
        val destinationId = if (clearBackStack == true) navController.graph.id else popUpTo
        val options = buildOptions(animation, clearBackStack, destinationId)

        navController.navigate(directions, options)
    }

    fun navigate(
        @IdRes resId: Int,
        clearBackStack: Boolean? = false,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT
    ) {
        navigate(resId, animation, null, clearBackStack, this)
    }

    //todo No futuro adicionar parâmetro destinationId,
    // para quando tiver que voltar de um Fragment para outro que não seja diretamente o anterior
    fun navigate(
        @IdRes resId: Int,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
        bundle: Bundle? = null,
        clearBackStack: Boolean? = false,
        fragment: Fragment
    ) {
        try {
            val navController = NavHostFragment.findNavController(fragment)
            val destinationId = if (clearBackStack == true) navController.graph.id else null
            val options = buildOptions(animation, clearBackStack, destinationId)
            navController.navigate(resId, bundle, options)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun navigate(
        view: View,
        @IdRes resId: Int,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
        clearBackStack: Boolean? = false
    ) {
        val navController = Navigation.findNavController(view)
        val destinationId = if (clearBackStack == true) navController.graph.id else null
        val options = buildOptions(animation, clearBackStack, destinationId)

        navController.navigate(resId, null, options)
    }

    fun popBackStack() {
        findNavController().popBackStack()
    }

    fun popUpTo(@IdRes destinationId: Int) {
        findNavController().popBackStack(destinationId, false)
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    private fun buildOptions(
        transitionAnimation: TransitionAnimation?,
        clearBackStack: Boolean?,
        @IdRes destinationId: Int?
    ): NavOptions {
        return navOptions {
            anim {
                when (transitionAnimation) {
                    TransitionAnimation.TRANSLATE_FROM_DOWN -> {
                        enter = R.anim.translate_slide_bottom_up
                        exit = R.anim.translate_no_change
                        popEnter = R.anim.translate_no_change
                        popExit = R.anim.translate_slide_bottom_down
                    }
                    TransitionAnimation.TRANSLATE_FROM_LEFT -> {
                        enter = R.anim.translate_right_enter
                        exit = R.anim.translate_right_exit
                        popEnter = R.anim.translate_left_enter
                        popExit = R.anim.translate_left_exit
                    }
                    TransitionAnimation.TRANSLATE_FROM_UP -> {
                        enter = R.anim.translate_slide_bottom_down
                        exit = R.anim.translate_no_change
                        popEnter = R.anim.translate_no_change
                        popExit = R.anim.translate_slide_bottom_up
                    }
                    TransitionAnimation.NO_ANIMATION -> {
                        enter = R.anim.translate_no_change
                        exit = R.anim.translate_no_change
                        popEnter = R.anim.translate_no_change
                        popExit = R.anim.translate_no_change
                    }
                    TransitionAnimation.FADE -> {
                        enter = R.anim.translate_fade_in
                        exit = R.anim.translate_fade_out
                        popEnter = R.anim.translate_fade_in
                        popExit = R.anim.translate_fade_out
                    }
                    else -> {
                        enter = R.anim.translate_left_enter
                        exit = R.anim.translate_left_exit
                        popEnter = R.anim.translate_right_enter
                        popExit = R.anim.translate_right_exit
                    }
                }
            }

            // Para limpar a pilha abaixo do fragment atual,
            // deve-se setar o 'destinationId' = navGraphId e 'inclusive' = true
            destinationId?.let {
                popUpTo(destinationId) {
                    inclusive = clearBackStack == true
                }
            }
        }
    }
    //endregion Navigation

}