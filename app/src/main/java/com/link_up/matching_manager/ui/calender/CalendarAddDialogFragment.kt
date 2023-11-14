import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.link_up.matching_manager.databinding.CalendarAddDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.Calendar

class CalendarAddDialogFragment : BottomSheetDialogFragment() {

    private var _binding: CalendarAddDialogFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ADD_REQUEST_KEY = "add_request_key"  // ADD 다이얼로그 요청 키
        const val ADD_RESULT_KEY_TEXT = "add_result_key_text" // ADD 결과 데이터 키
        const val ADD_RESULT_KEY_PLACE = "add_result_key_place" // ADD 장소 데이터 키
        const val ADD_RESULT_KEY_SCHEDULE = "add_result_key_schedule" // ADD 날짜 데이터 키
        const val ADD_RESULT_KEY_YEAR = "add_result_key_year"
        const val ADD_RESULT_KEY_MONTH = "add_result_key_month"
        const val ADD_RESULT_KEY_DAY = "add_result_key_day"

        fun newInstance(): CalendarAddDialogFragment {
            return CalendarAddDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarAddDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initView() = with(binding) {

        btnSave.setOnClickListener {
            val memoText = edtCalendarAddDialogMemo.text.toString()
            val memoPlace = edtCalendarAddDialogPlace.text.toString()

            if (materialCalendarCalendarAddView.selectedDate != null && memoText.isNotBlank() && memoPlace.isNotBlank()) {
                val memoYear = materialCalendarCalendarAddView.selectedDate.year
                val memoMonth = materialCalendarCalendarAddView.selectedDate.month
                val memoDay = materialCalendarCalendarAddView.selectedDate.day

                // 메모 데이터를 부모 Fragment로 전달합니다.
                setFragmentResult(
                    ADD_REQUEST_KEY,
                    bundleOf(
                        ADD_RESULT_KEY_TEXT to memoText,
                        ADD_RESULT_KEY_PLACE to memoPlace,
                        ADD_RESULT_KEY_YEAR to memoYear,
                        ADD_RESULT_KEY_MONTH to memoMonth,
                        ADD_RESULT_KEY_DAY to memoDay,
                    )
                )
                dismiss() // 다이얼로그 닫기
            } else {
                Toast.makeText(requireContext(), "날짜와 빈칸을 모두 채워주세요!", Toast.LENGTH_SHORT).show()
                btnSave.isEnabled = true
            }
        }
        btnCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



