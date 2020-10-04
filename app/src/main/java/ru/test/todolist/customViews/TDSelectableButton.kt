package ru.test.todolist.customViews

import android.animation.AnimatorInflater
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.selectable_button_view.view.*
import ru.test.todolist.R
import ru.test.todolist.utils.CommonUtils

class TDSelectableButton(context: Context, attrs: AttributeSet?) : TDLinearLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    var isSelect: Boolean = false

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        isSelect = selected
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (selected) {
                outlineSpotShadowColor = resources.getColor(R.color.mainShadow, context.theme)
                tv_title.setTextColor(resources.getColor(R.color.selectableButtonColorSelected))
            } else {
                outlineSpotShadowColor = resources.getColor(R.color.blackShadowAlpha, context.theme)
                tv_title.setTextColor(resources.getColor(R.color.selectableButtonColorUnselected))
            }
        }
    }

    override fun initViewComponents(view: View?) {
        orientation = LinearLayout.HORIZONTAL
        isClickable = true
        setPadding(CommonUtils.convertDpToPixels(12F), CommonUtils.convertDpToPixels(12F), CommonUtils.convertDpToPixels(12F), CommonUtils.convertDpToPixels(12F))
        background = resources.getDrawable(R.drawable.selectable_button_background, context.theme)
        stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.drawable.press_button_animation)
        elevation = CommonUtils.convertDpToPixels(2F).toFloat()
    }

    fun setParams(title: String?) {
        tv_title.text = title
    }

    override fun getCustomViewLayoutId(): Int {
        return R.layout.selectable_button_view
    }
}