package com.example.a23_hf069

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FAQPersonalFragment : Fragment() {

    private lateinit var allFAQList: MutableList<FAQItem>
    private lateinit var faqMenuMap: HashMap<String, List<FAQItem>>
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var faqAdapter: FAQAdapter
    private var selectedMenu: String = "전체"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_f_a_q_personal, container, false)

        // 메뉴별 질문 리스트 생성
        faqMenuMap = linkedMapOf(
            "이력서" to listOf(
                FAQItem("이력서 1", "답변 1"),
                FAQItem("이력서 2", "답변 2"),
                // 이력서에 해당하는 나머지 FAQ 아이템들 추가
            ),
            "채용공고" to listOf(
                FAQItem("채용공고 1", "답변 1"),
                FAQItem("채용공고 2", "답변 2"),
                // 채용공고에 해당하는 나머지 FAQ 아이템들 추가
            ),
            "커뮤니티" to listOf(
                FAQItem("커뮤니티 1", "답변 1"),
                FAQItem("커뮤니티 2", "답변 2"),
                // 커뮤니티에 해당하는 나머지 FAQ 아이템들 추가
            ),
            "마이페이지" to listOf(
                FAQItem("마이페이지 1", "답변 1"),
                FAQItem("마이페이지 2", "답변 2"),
                // 마이페이지에 해당하는 나머지 FAQ 아이템들 추가
            ),
            "기타 문의" to listOf(
                FAQItem("기타 문의 1", "답변 1"),
                FAQItem("기타 문의 2", "답변 2"),
                // 기타 문의에 해당하는 나머지 FAQ 아이템들 추가
            )
        )

        // 전체 질문 리스트 생성
        allFAQList = mutableListOf()
        for (faqList in faqMenuMap.values) {
            allFAQList.addAll(faqList)
        }

        // "전체" 메뉴를 추가
        val menuItems = mutableListOf<String>()
        menuItems.add("전체")
        menuItems.addAll(faqMenuMap.keys)

        // Horizontal RecyclerView 초기화 (메뉴가 가로로 정렬되도록)
        val menuRecyclerView: RecyclerView = rootView.findViewById(R.id.menuRecyclerView)
        menuAdapter = MenuAdapter(menuItems, object : MenuAdapter.OnItemClickListener {
            override fun onItemClick(menu: String) {
                selectedMenu = menu
                if (selectedMenu == "전체") {
                    faqAdapter.updateData(allFAQList)
                } else {
                    faqAdapter.updateData(faqMenuMap[selectedMenu] ?: emptyList())
                }
            }
        })
        menuRecyclerView.adapter = menuAdapter
        menuRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        // RecyclerView 초기화
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerView)
        faqAdapter = FAQAdapter(allFAQList)
        recyclerView.adapter = faqAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        // 구분선 추가
        val dividerColor = Color.parseColor("#E6E6E6")
        val itemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        val dividerDrawable: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.divider_line)
        dividerDrawable?.let {
            it.setTint(dividerColor) // 구분선 Drawable에 색상을 설정합니다.
            itemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(itemDecoration)

        // 처음에 "전체" 메뉴를 선택한 상태로 보이도록 설정
        menuRecyclerView.post {
            menuRecyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()
        }

        return rootView
    }

    // RecyclerView 클릭 리스너를 위한 클래스 추가
    class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnItemClickListener) :
        RecyclerView.OnItemTouchListener {

        interface OnItemClickListener {
            fun onItemClick(view: View, position: Int)
            fun onLongItemClick(view: View?, position: Int)
        }

        private var mGestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    listener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            }
        })

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val childView = rv.findChildViewUnder(e.x, e.y)
            if (childView != null && mGestureDetector.onTouchEvent(e)) {
                listener.onItemClick(childView, rv.getChildAdapterPosition(childView))
                return true
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

    // MenuAdapter 클래스를 FAQPersonalFragment 내부에 정의
    class MenuAdapter(
        private val menuList: List<String>,
        private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

        interface OnItemClickListener {
            fun onItemClick(menu: String)
        }

        private var selectedPosition = 0 // 선택된 메뉴의 포지션을 기억하는 변수

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.menu_item_layout, parent, false)
            return MenuViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
            val currentMenu = menuList[position]
            holder.menuText.text = currentMenu

            // 선택된 메뉴의 스타일 변경
            if (selectedPosition == position) {
                holder.menuText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.selectedMenuColor))
                holder.menuText.setTypeface(null, Typeface.BOLD)
            } else {
                holder.menuText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.defaultMenuColor))
                holder.menuText.setTypeface(null, Typeface.NORMAL)
            }

            holder.itemView.setOnClickListener {
                // 선택된 메뉴의 스타일 변경
                notifyItemChanged(selectedPosition)
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition)

                listener.onItemClick(currentMenu)
            }
        }

        override fun getItemCount() = menuList.size

        // 메뉴 요소의 레이아웃에 대한 ViewHolder 클래스 정의
        class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val menuText: TextView = itemView.findViewById(R.id.menuText)
        }
    }
}
