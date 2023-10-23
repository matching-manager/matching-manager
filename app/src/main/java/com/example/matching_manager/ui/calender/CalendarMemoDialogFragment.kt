import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.matching_manager.R
import com.example.matching_manager.databinding.CalendarMemoDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//import com.example.matching_manager.databinding.MemoDialogFragmentBinding

//import kotlinx.android.synthetic.main.memo_dialog_fragment.*

class CalendarMemoDialogFragment : BottomSheetDialogFragment() {

    private var memoSaveListener: ((String) -> Unit)? = null

    private lateinit var fragment: CalendarMemoDialogFragment
    private lateinit var binding: CalendarMemoDialogFragmentBinding

    //FragmentMemoDialogBinding

    companion object {
        const val REQUEST_KEY = "CalendarMemoDialogFragment"  // 다이얼로그 요청 키
        const val RESULT_KEY_TEXT = "memoText" // 결과 데이터 키
        const val RESULT_KEY_DATE = "selectedDate" // 날짜 데이터를 위한 상수 추가


        fun newInstance(): CalendarMemoDialogFragment {
            return CalendarMemoDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 다이얼로그 레이아웃을 설정합니다.
        return inflater.inflate(R.layout.calendar_memo_dialog_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = CalendarMemoDialogFragmentBinding.bind(view)
        //fragment = binding

        binding.btnSave.setOnClickListener {
            val memoText = binding.edtCalendarTxt.text.toString()




            // 메모 데이터를 부모 Fragment로 전달합니다.
            setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY_TEXT to memoText))


            dismiss() // 다이얼로그 닫기
        }

        binding.btnCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }
    }

    fun setMemoSaveListener(listener: (String) -> Unit) {
        memoSaveListener = listener
    }
}
