package br.com.isgreen.archandroid.base

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.extension.*
import br.com.isgreen.archandroid.util.OnActivityResultCallback
import br.com.isgreen.archandroid.util.OnEventReceivedListener
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Created by Éverdes Soares on 08/22/2019.
 */

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VB : ViewBinding> : Fragment(), FragmentCompat {

    //    private val mPermissionHelper: PermissionHelper by lazy { PermissionHelperImpl(context) }
    private var mOnEventReceivedListener: OnEventReceivedListener? = { code, data ->
        onEventReceived(code, data)
    }

    private var mIsLayoutCreated = false
    private var mLayoutView: View? = null
    private var mViewBinding: ViewBinding? = null

    //    private var mPermissionResultCallback: PermissionResultCallback? = null

//    protected val permissionHelper
//        get() = mPermissionHelper
//
//    var permissionResultCallback
//        get() = mPermissionResultCallback
//        set(value) {
//            mPermissionResultCallback = value
//        }

    abstract val bindingInflater: (LayoutInflater) -> VB
    protected val binding: VB
        get() = mViewBinding as VB

    var onActivityResultCallback: OnActivityResultCallback? = null

    open fun onEventReceived(code: Int, data: Any?) {}

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        loadModule()
        initDefaultObservers()

        super.onCreate(savedInstanceState)

        baseActivity?.addOnEventReceivedListener(mOnEventReceivedListener)

        setHasOptionsMenu(true)
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mLayoutView == null) {
            mViewBinding = bindingInflater.invoke(inflater)
            mLayoutView = requireNotNull(mViewBinding).root
        } else {
            (mLayoutView?.parent as? ViewGroup)?.removeView(mLayoutView)
        }

        hideKeyboard()
        return mLayoutView
//        if (mLayoutView == null) {
//            mLayoutView = inflater.inflate(screenLayout, container, false)
//        } else {
//            (mLayoutView?.parent as? ViewGroup)?.removeView(mLayoutView)
//        }
//
//        hideKeyboard()
//
//        return mLayoutView
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
        onActivityResultCallback?.invoke(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        baseActivity?.removeOnEventReceivedListener(mOnEventReceivedListener)
        mOnEventReceivedListener = null
        onActivityResultCallback = null
    }
    //endregion Fragment

    //region Local
    private fun initDefaultObservers() {
        viewModel?.redirect?.observe(this, { destination ->
            val rootFragment = if (destination == R.id.splashFragment) getRootParent() else this
            navigate(destination, TransitionAnimation.FADE, null, true, rootFragment)
        })
        viewModel?.loading?.observe(this, { isLoading ->
            onLoadingChanged(isLoading)
        })
        viewModel?.message?.observe(this, { message ->
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

    private fun getRootParent(): Fragment {
        var rootParent = parentFragment

        while (rootParent?.parentFragment != null) {
            rootParent = rootParent.parentFragment
        }

        return rootParent ?: this
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
            if (isAtLeastR()) {
                if (toWhite) {
                    it.window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    it.window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                it.window.decorView.systemUiVisibility = if (
                    toWhite || Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                ) {
                    0
                } else {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }
    //endregion StatusBar

}