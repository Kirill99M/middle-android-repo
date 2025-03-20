package com.example.androidpracticumcustomview.ui.theme

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.getSize
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.example.androidpracticumcustomview.R

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var transparentAnimationDuration: Long = 2000

    var movementAnimationDuration: Long = 5000

    private val nonAnimatedChildren = mutableSetOf<View>()

    init {
        setWillNotDraw(true)
        context.withStyledAttributes(attrs, R.styleable.CustomContainer) {
            transparentAnimationDuration = getInt(
                R.styleable.CustomContainer_customContainer_transparent_animation_duration,
                2000
            ).toLong()
            movementAnimationDuration = getInt(
                R.styleable.CustomContainer_customContainer_movement_animation_duration,
                5000
            ).toLong()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = getSize(widthMeasureSpec)
        val parentHeight = getSize(heightMeasureSpec)
        setMeasuredDimension(parentWidth, parentHeight)
        children.forEach { child ->
            measureChild(child, UNSPECIFIED, UNSPECIFIED)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        children.forEach { child ->
            child.layout(
                (measuredWidth - child.measuredWidth) / 2,
                (measuredHeight - child.measuredHeight) / 2,
                (measuredWidth + child.measuredWidth) / 2,
                (measuredHeight + child.measuredHeight) / 2
            )
        }
        nonAnimatedChildren.forEach { child ->
            animateItem(child)
            nonAnimatedChildren.remove(child)
        }
    }

    override fun addView(child: View) {
        if (childCount > 2) {
            removeView(child)
            throw IllegalStateException("Нельзя добавлять больше 2ух элементов")
        }
        child.alpha = 0f
        nonAnimatedChildren.add(child)
        super.addView(child)
    }

    private fun getAnimatePosition(
        layoutHeight: Int,
        childHeight: Int,
        isFirstElement: Boolean
    ): Float {
        return if (isFirstElement) {
            -((layoutHeight - childHeight) / 2).toFloat()
        } else {
            (((layoutHeight - childHeight) / 2)).toFloat()
        }
    }

    private fun animateItem(item: View) {
        val movementAnimator = ObjectAnimator.ofFloat(
            item, "translationY", getAnimatePosition(
                layoutHeight = height,
                childHeight = item.height,
                isFirstElement = children.first() == item
            )
        ).apply {
            duration = movementAnimationDuration
        }
        val itemAnimatorSet = AnimatorSet()
        val transparencyAnimator = ObjectAnimator.ofFloat(
            item, "alpha", 1f
        ).apply {
            duration = transparentAnimationDuration
        }
        itemAnimatorSet.playTogether(
            movementAnimator,
            transparencyAnimator
        )
        itemAnimatorSet.start()
    }
}