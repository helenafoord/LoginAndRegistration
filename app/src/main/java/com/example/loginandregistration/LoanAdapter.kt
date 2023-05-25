package com.example.loginandregistration

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import java.lang.RuntimeException

class LoanAdapter(private var dataSet: MutableList<Loan>, private val context: Activity):
    RecyclerView.Adapter<LoanAdapter.ViewHolder>(){
        companion object{
            const val TAG = "LoanAdapter"
        }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewBorrower: TextView
        val textViewAmount: TextView
        val layout: ConstraintLayout

        init{
            textViewBorrower= view.findViewById(R.id.textView_itemLoan_borrower)
            textViewAmount = view.findViewById(R.id.textView_itemloan_amount)
            layout = view.findViewById(R.id.item_loan_layout)

        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder:ViewHolder, position: Int){

        //val loan = loanList[position]
        val context = viewHolder.layout.context
        viewHolder.textViewBorrower.text = dataSet[position].lendee
        viewHolder.textViewAmount.text = dataSet[position].balanceRemaining().toString()
        viewHolder.layout.isLongClickable = true
        viewHolder.layout.setOnLongClickListener{
            val popMenu = PopupMenu(context, viewHolder.textViewBorrower)
            popMenu.inflate(R.menu.menu_loan_detail)
            popMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_loanList_delete -> {
                        deleteFromBackendless(position)
                        dataSet.removeAt(position)
                        notifyItemRemoved(position)
                        true
                    }
                    else -> true
                }
            }
            popMenu.show()
            true

        }
        viewHolder.layout.setOnClickListener {
            when(context){
                is LoanListActivity -> context.onLoanItemClicked(dataSet[position])
                else -> throw RuntimeException("Unreachable")
            }
        }

    }


    private fun deleteFromBackendless(position: Int){
        Log.d(TAG, "deleteFromBackendless: Deleting ${dataSet[position]}")
        Backendless.Data.of(Loan::class.java).remove(dataSet[position], object: AsyncCallback<Long>{
            override fun handleFault(fault: BackendlessFault?) {
                Log.d(TAG, "handleFault: Couldn't Delete")
                Log.d(TAG, "handleFault: $fault")
            }

            override fun handleResponse(response: Long?) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount() = dataSet.size
}