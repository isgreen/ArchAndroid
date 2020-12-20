package br.com.isgreen.archandroid.screen.pullrequest.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.isgreen.archandroid.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_pull_request_option.*

class PullRequestOptionFragment : BottomSheetDialogFragment() {

    var onMergeClickListener: (() -> Unit)? = null
    var onApproveClickListener: (() -> Unit)? = null
    var onDeclineClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pull_request_option, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        txtMerge?.setOnClickListener {
            onMergeClickListener?.invoke()
            dismiss()
        }
        txtApprove?.setOnClickListener {
            onApproveClickListener?.invoke()
            dismiss()
        }
        txtDecline?.setOnClickListener {
            onDeclineClickListener?.invoke()
            dismiss()
        }
    }
}