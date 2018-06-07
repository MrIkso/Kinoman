package ru.ratanov.kinoman.model.views

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import ru.ratanov.kinoman.model.utils.QueryPreferences

class TopItemView(context: Context) : ImageView(context) {

    private val SPANS_COUNT = QueryPreferences.getStoredQuery(context, "number_of_spans", "2").toInt()
    private val PADDING = 1

    init {
        scaleType = ScaleType.CENTER_CROP
        layoutParams = LinearLayout.LayoutParams(measuredWidth, measuredHeight).apply {
            setPadding(1, 1, 1, 1)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val parentWidth = (parent as View).measuredWidth

        val preferredWidth = (parentWidth / SPANS_COUNT) - PADDING * 2
        val preferredHeight = (preferredWidth * 1.5).toInt()

        val width = MeasureSpec.makeMeasureSpec(preferredWidth, MeasureSpec.EXACTLY)
        val height = MeasureSpec.makeMeasureSpec(preferredHeight, MeasureSpec.EXACTLY)

        setMeasuredDimension(width, height)
    }
}