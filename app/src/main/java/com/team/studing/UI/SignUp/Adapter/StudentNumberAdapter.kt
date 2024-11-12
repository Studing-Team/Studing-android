import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.databinding.RowStudentNumberBinding

class StudentNumberAdapter(
    private var loginActivity: LoginActivity,
    private var studentNumberList: List<String>,
    private var selectedNum: Int
) :
    RecyclerView.Adapter<StudentNumberAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newList: List<String>, inputSelectedNum: Int) {
        studentNumberList = newList
        selectedNum = inputSelectedNum
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowStudentNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentNum = studentNumberList[position]

        holder.textViewNumber.text = studentNum

        // 이전에 선택된 부분 색상 변경
        if (selectedNum != -1 && selectedNum == position) {
            holder.layout.run {
                setBackgroundResource(R.color.primary_10)
                setBackgroundColor(resources.getColor(R.color.primary_10))
            }
            holder.textViewNumber.run {
                setTextColor(resources.getColor(R.color.primary_50))
                setTextAppearance(R.style.BodyAdd)
            }
        }
    }

    override fun getItemCount() = studentNumberList.size

    inner class ViewHolder(val binding: RowStudentNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textViewNumber = binding.textViewStudentNumber
        val layout = binding.layoutStudentNumber

        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)
                true
            }
        }
    }
}