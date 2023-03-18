package com.example.animation_temp

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BaseInterpolator
import android.view.animation.LinearInterpolator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _maxValueFlow1 = MutableStateFlow(0)
    val maxValueFlow1 = _maxValueFlow1.asStateFlow()
    private val _countUpFlow1: MutableStateFlow<Int> = MutableStateFlow(0)
    val countUpFlow1: StateFlow<Int> = _countUpFlow1.asStateFlow()

    private val _maxValueFlow2 = MutableStateFlow(0)
    val maxValueFlow2 = _maxValueFlow2.asStateFlow()
    private val _countUpFlow2: MutableStateFlow<Int> = MutableStateFlow(0)
    val countUpFlow2: StateFlow<Int> = _countUpFlow2.asStateFlow()

    fun fetch() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // ネットワーク通信を想定したdelay
                delay(1_000L)
                // response
                val maxValue1 = 50
                val maxValue2 = 50
                val currentValue1 = 45
                val currentValue2 = 45

                _maxValueFlow1.value = maxValue1
                _maxValueFlow2.value = maxValue2
                withContext(Dispatchers.Main) {
                    createAndStartCountUpAnimation(currentValue1, _countUpFlow1)
                    createAndStartCountUpAnimation(currentValue2, _countUpFlow2, AccelerateInterpolator())
                }
            }

        }
    }

    fun onClickReAnimate() {
        _maxValueFlow1.value = 0
        _maxValueFlow2.value = 0
        _countUpFlow1.value = 0
        _countUpFlow2.value = 0
        fetch()
    }

    private fun createAndStartCountUpAnimation(
        endValue: Int,
        countUpFlow: MutableStateFlow<Int>,
        interpolator: BaseInterpolator = LinearInterpolator()
    ) {
        ValueAnimator.ofInt(INITIAL_VALUE, endValue).apply {
            duration = ANIMATION_DURATION
            // アニメーションの線形非線形種類抜粋
            // LinearInterpolator : 線形
            // AccelerateInterpolator : 非線形　ゆっくり 〜 はやい
            // AccelerateDecelerateInterpolator : 非線形　はやい 〜 ゆっくり
            this.interpolator = interpolator
            addUpdateListener {
                countUpFlow.value = it.animatedValue as Int
            }
        }.start()
    }

    companion object {

        const val INITIAL_VALUE = 0
        const val ANIMATION_DURATION = 1_000L
    }
}