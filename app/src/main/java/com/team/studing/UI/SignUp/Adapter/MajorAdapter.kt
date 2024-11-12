import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.LoginActivity
import com.team.studing.databinding.RowUniversityBinding

class MajorAdapter(
    private var loginActivity: LoginActivity,
    private var majorList: List<String>,
    private var query: String
) :
    RecyclerView.Adapter<MajorAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newList: List<String>, inputQuery: String) {
        majorList = newList
        query = inputQuery
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val university = majorList[position]

        // 검색어와 일치하는 부분을 파란색으로 강조
        Log.d("##", "query : ${query}")
        val spannable = SpannableString(university)
        val startIndex = university.indexOf(query, ignoreCase = true)
        if (startIndex >= 0) {
            // 텍스트 색상 설정
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#5175EC")),
                startIndex,
                startIndex + query.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 텍스트 볼드체 설정
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                startIndex + query.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        holder.majorName.text = spannable
    }

    override fun getItemCount() = majorList.size

    inner class ViewHolder(val binding: RowUniversityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val majorName = binding.textViewRowUniversity

        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
                true
            }
        }
    }
}