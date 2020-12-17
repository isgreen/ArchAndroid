package br.com.isgreen.archandroid.screen.pullrequest.option

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import br.com.isgreen.archandroid.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_pull_request_option.*

class PullRequestOptionFragment : BottomSheetDialogFragment() {

    companion object {
//        private const val TASK_EXTRA_KEY = "task"

        fun newInstance() = PullRequestOptionFragment().apply {
//            arguments = bundleOf(TASK_EXTRA_KEY to task)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pull_request_option, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val task = arguments?.getParcelable<Task>(TASK_EXTRA_KEY)
    }
}