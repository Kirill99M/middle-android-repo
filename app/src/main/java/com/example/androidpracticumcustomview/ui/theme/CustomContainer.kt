package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import androidx.core.view.children
import com.example.androidpracticumcustomview.R
import androidx.core.content.withStyledAttributes

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

    var transparentAnimationDuration: Long = 5000

    var movementAnimationDuration: Long = 5000

    init {
        setWillNotDraw(true)
        context.withStyledAttributes(attrs, R.styleable.CustomContainer) {
            transparentAnimationDuration = getInt(
                R.styleable.CustomContainer_customContainer_transparent_animation_duration,
                5000
            ).toLong()
            movementAnimationDuration = getInt(
                R.styleable.CustomContainer_customContainer_movement_animation_duration,
                5000
            ).toLong()
        }
    }

    override fun addView(child: View) {
        child.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        child.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (childCount > 2) {
                    removeView(child)
                    throw IllegalStateException("Нельзя добавлять больше 2ух элементов")
                }
                child.alpha = 0f
                child.animate().alpha(1f).setDuration(transparentAnimationDuration)
                    .withStartAction {
                        child.animate().translationY(
                            getAnimatePosition(
                                layoutHeight = height,
                                childHeight = child.height,
                                isFirstElement = children.first() == child
                            )
                        ).duration = movementAnimationDuration
                    }
                child.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        )
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
}