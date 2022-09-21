package com.atom.android.muvik.customview


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.atom.android.muvik.R


class SwagPoints : View {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context):super(context){
        init(context, null)
    }



    var INVALID_VALUE = -1
    val MAX = 100
    val MIN = 0

    /**
     * Offset = -90 indicates that the progress starts from 12 o'clock.
     */
    private val ANGLE_OFFSET = -60

    /**
     * The current points value.
     */
    private var mPoints = MIN

    /**
     * The min value of progress value.
     */
    private var mMin = MIN

    /**
     * The Maximum value that this SeekArc can be set to
     */
    private var mMax = MAX

    /**
     * The increment/decrement value for each movement of progress.
     */
    private var mStep = 10

    /**
     * The Drawable for the seek arc thumbnail
     */
    private var mIndicatorIcon: Drawable? = null


    private var mProgressWidth = 12
    private var mArcWidth = 12
    private var mClockwise = true
    private var mEnabled = true

    //
    // internal variables
    //
    //
    // internal variables
    //
    /**
     * The counts of point update to determine whether to change previous progress.
     */
    private var mUpdateTimes = 0
    private var mPreviousProgress = -1f
    private var mCurrentProgress = 0f

    /**
     * Determine whether reach max of point.
     */
    private var isMax = false

    /**
     * Determine whether reach min of point.
     */
    private var isMin = false

    private var mArcRadius = 0
    private val mArcRect = RectF()
    private var mArcPaint: Paint = Paint()

    private var mProgressSweep = 0f
    private var mProgressPaint: Paint= Paint()

    private var mTextSize = 72f
    private var mTextPaint: Paint = Paint()
    private val mTextRect: Rect = Rect()

    private var mTranslateX = 0
    private var mTranslateY = 0

    // the (x, y) coordinator of indicator icon
    private var mIndicatorIconX = 0
    private var mIndicatorIconY = 0

    /**
     * The current touch angle of arc.
     */
    private var mTouchAngle = 0.0
    private var mOnSwagPointsChangeListener: OnSwagPointsChangeListener? = null


    private fun init(context: Context, attrs: AttributeSet?) {
        val density = resources.displayMetrics.density

        // Defaults, may need to link this into theme settings
        var arcColor = ContextCompat.getColor(context, R.color.purple_500)
        var progressColor = ContextCompat.getColor(context, R.color.purple_200)
        var textColor = ContextCompat.getColor(context, R.color.purple_700)
        mProgressWidth = (mProgressWidth * density).toInt()
        mArcWidth = (mArcWidth * density).toInt()
        mTextSize = (mTextSize * density).toInt().toFloat()
        mIndicatorIcon = ContextCompat.getDrawable(context, R.drawable.ic_home_black_24dp)
        if (attrs != null) {
            // Attribute initialization
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SwagPoints, 0, 0
            )
            val indicatorIcon = a.getDrawable(R.styleable.SwagPoints_indicatorIcon)
            if (indicatorIcon != null) mIndicatorIcon = indicatorIcon
            val indicatorIconHalfWidth = mIndicatorIcon!!.intrinsicWidth / 2
            val indicatorIconHalfHeight = mIndicatorIcon!!.intrinsicHeight / 2
            mIndicatorIcon!!.setBounds(
                -indicatorIconHalfWidth, -indicatorIconHalfHeight, indicatorIconHalfWidth,
                indicatorIconHalfHeight
            )
            mPoints = a.getInteger(R.styleable.SwagPoints_points, mPoints)
            mMin = a.getInteger(R.styleable.SwagPoints_min, mMin)
            mMax = a.getInteger(R.styleable.SwagPoints_max, mMax)
            mStep = a.getInteger(R.styleable.SwagPoints_step, mStep)
            mProgressWidth =
                a.getDimension(R.styleable.SwagPoints_progressWidth, mProgressWidth.toFloat())
                    .toInt()
            progressColor = a.getColor(R.styleable.SwagPoints_progressColor, progressColor)
            mArcWidth = a.getDimension(R.styleable.SwagPoints_arcWidth, mArcWidth.toFloat()).toInt()
            arcColor = a.getColor(R.styleable.SwagPoints_arcColor, arcColor)
            mTextSize = a.getDimension(R.styleable.SwagPoints_textSize, mTextSize).toInt().toFloat()
            textColor = a.getColor(R.styleable.SwagPoints_textColor, textColor)
            mClockwise = a.getBoolean(
                R.styleable.SwagPoints_clockwise,
                mClockwise
            )
            mEnabled = a.getBoolean(R.styleable.SwagPoints_enabled, mEnabled)
            a.recycle()
        }


        fun valuePerDegree(): Float {
            return  (mMax) / 360.0f as Float
        }

        // range check
        mPoints = if (mPoints > mMax) mMax else mPoints
        mPoints = if (mPoints < mMin) mMin else mPoints
        mProgressSweep = mPoints.toFloat() / valuePerDegree()
        mArcPaint = Paint()
        mArcPaint.setColor(arcColor)
        mArcPaint.setAntiAlias(true)
        mArcPaint.setStyle(Paint.Style.STROKE)
        mArcPaint.setStrokeWidth(mArcWidth.toFloat())
        mProgressPaint = Paint()
        mProgressPaint.setColor(progressColor)
        mProgressPaint.setAntiAlias(true)
        mProgressPaint.setStyle(Paint.Style.STROKE)
        mProgressPaint.setStrokeWidth(mProgressWidth.toFloat())
        mTextPaint = Paint()
        mTextPaint.setColor(textColor)
        mTextPaint.setAntiAlias(true)
        mTextPaint.setStyle(Paint.Style.FILL)
        mTextPaint.setTextSize(mTextSize)


    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val min = Math.min(width, height)
        mTranslateX = (width * 0.5f).toInt()
        mTranslateY = (height * 0.5f).toInt()
        val arcDiameter = min - paddingLeft
        mArcRadius = arcDiameter / 2
        val top = (height / 2 - arcDiameter / 2).toFloat()
        val left = (width / 2 - arcDiameter / 2).toFloat()
        mArcRect[left, top, left + arcDiameter] = top + arcDiameter
        updateIndicatorIconPosition()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        if (!mClockwise) {
            canvas.scale(-1F, 1F, mArcRect.centerX(), mArcRect.centerY())
        }

        // draw the text
        val textPoint = mPoints.toString()
        mTextPaint.getTextBounds(textPoint, 0, textPoint.length, mTextRect)
        // center the text
        val xPos: Int = canvas.getWidth() / 2 - mTextRect.width() / 2
        val yPos = (mArcRect.centerY() - (mTextPaint.descent() + mTextPaint.ascent()) / 2).toInt()
        //		Log.d("onDraw", String.valueOf(mPoints));
        canvas.drawText(mPoints.toString(), xPos.toFloat(), yPos.toFloat(), mTextPaint)

        // draw the arc and progress
        //canvas.drawArc(mArcRect, ANGLE_OFFSET.toFloat(), 360F, true, mArcPaint)
        canvas.drawArc(mArcRect, ANGLE_OFFSET.toFloat(), mProgressSweep -30,false, mProgressPaint)
        if (mEnabled) {
            // draw the indicator icon
            canvas.translate((mTranslateX - mIndicatorIconX).toFloat(),
                (mTranslateY - mIndicatorIconY).toFloat()
            )
            mIndicatorIcon!!.draw(canvas)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mEnabled) {
            // 阻止父View去攔截onTouchEvent()事件，確保touch事件可以正確傳遞到此層View。
            this.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> mOnSwagPointsChangeListener?.onStartTrackingTouch(
                    this
                )
                MotionEvent.ACTION_MOVE -> updateOnTouch(event)
                MotionEvent.ACTION_UP -> {
                    mOnSwagPointsChangeListener?.onStopTrackingTouch(
                        this
                    )
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    mOnSwagPointsChangeListener?.onStopTrackingTouch(
                        this
                    )
                    isPressed = false
                    this.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return true
        }
        return false
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (mIndicatorIcon != null && mIndicatorIcon!!.isStateful) {
            val state = drawableState
            mIndicatorIcon!!.state = state
        }
        invalidate()
    }

    private fun updateOnTouch(event: MotionEvent) {
        isPressed = true
        mTouchAngle = convertTouchEventPointToAngle(event.x, event.y)
        val progress = convertAngleToProgress(mTouchAngle)
        updateProgress(progress, true)
    }

    private fun convertTouchEventPointToAngle(xPos: Float, yPos: Float): Double {
        // transform touch coordinate into component coordinate
        var x = xPos - mTranslateX
        val y = yPos - mTranslateY
        x = if (mClockwise) x else -x
        var angle = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
        angle = if (angle < 0) angle + 360 else angle
        //		System.out.printf("(%f, %f) %f\n", x, y, angle);
        return angle
    }

    private fun convertAngleToProgress(angle: Double): Int {
        return Math.round(valuePerDegree() * angle).toInt()
    }

    private fun valuePerDegree(): Float {
        return mMax.toFloat() / 360.0f
    }

    private fun updateIndicatorIconPosition() {
        val thumbAngle = (mProgressSweep + 90).toInt()
        mIndicatorIconX = (mArcRadius * Math.cos(Math.toRadians(thumbAngle.toDouble()))).toInt()
        mIndicatorIconY = (mArcRadius * Math.sin(Math.toRadians(thumbAngle.toDouble()))).toInt()
    }


    private fun updateProgress(progress: Int, fromUser: Boolean) {

        // detect points change closed to max or min
        var progress = progress
        val maxDetectValue = (mMax.toDouble() * 0.95).toInt()
        val minDetectValue = (mMax.toDouble() * 0.05).toInt() + mMin
        //		System.out.printf("(%d, %d) / (%d, %d)\n", mMax, mMin, maxDetectValue, minDetectValue);
        mUpdateTimes++
        if (progress == INVALID_VALUE) {
            return
        }

        // avoid accidentally touch to become max from original point
        // 避免在靠近原點點到直接變成最大值
        if (progress > maxDetectValue && mPreviousProgress === INVALID_VALUE.toFloat()) {
//			System.out.printf("Skip (%d) %.0f -> %.0f %s\n",
//					progress, mPreviousProgress, mCurrentProgress, isMax ? "Max" : "");
            return
        }


        // record previous and current progress change
        // 紀錄目前和前一個進度變化
        if (mUpdateTimes == 1) {
            mCurrentProgress = progress.toFloat()
        } else {
            mPreviousProgress = mCurrentProgress
            mCurrentProgress = progress.toFloat()
        }

//		if (mPreviousProgress != mCurrentProgress)
//			System.out.printf("Progress (%d)(%f) %.0f -> %.0f (%s, %s)\n",
//					progress, mTouchAngle,
//					mPreviousProgress, mCurrentProgress,
//					isMax ? "Max" : "",
//					isMin ? "Min" : "");

        // 不能直接拿progress來做step
        mPoints = progress - progress % mStep
        /**
         * Determine whether reach max or min to lock point update event.
         *
         * When reaching max, the progress will drop from max (or maxDetectPoints ~ max
         * to min (or min ~ minDetectPoints) and vice versa.
         *
         * If reach max or min, stop increasing / decreasing to avoid exceeding the max / min.
         */
        // 判斷超過最大值或最小值，最大最小值不重複判斷
        // 用數值範圍判斷預防轉太快直接略過最大最小值。
        // progress變化可能從98 -> 0/1 or 0/1 -> 98/97，而不會過0或100
        if (mUpdateTimes > 1 && !isMin && !isMax) {
            if (mPreviousProgress >= maxDetectValue && mCurrentProgress <= minDetectValue && mPreviousProgress > mCurrentProgress) {
                isMax = true
                progress = mMax
                mPoints = mMax
                //				System.out.println("Reach Max " + progress);
                if (mOnSwagPointsChangeListener != null) {
                    mOnSwagPointsChangeListener!!
                        .onPointsChanged(this, progress, fromUser)
                    return
                }
            } else if (mCurrentProgress >= maxDetectValue && mPreviousProgress <= minDetectValue && mCurrentProgress > mPreviousProgress || mCurrentProgress <= mMin) {
                isMin = true
                progress = mMin
                mPoints = mMin
                //				Log.d("Reach", "Reach Min " + progress);
                if (mOnSwagPointsChangeListener != null) {
                    mOnSwagPointsChangeListener!!
                        .onPointsChanged(this, progress, fromUser)
                    return
                }
            }
            invalidate()
        } else {

            // Detect whether decreasing from max or increasing from min, to unlock the update event.
            // Make sure to check in detect range only.
            if (isMax and (mCurrentProgress < mPreviousProgress) && mCurrentProgress >= maxDetectValue) {
//				System.out.println("Unlock max");
                isMax = false
            }
            if (isMin
                && mPreviousProgress < mCurrentProgress
                && mPreviousProgress <= minDetectValue && mCurrentProgress <= minDetectValue && mPoints >= mMin
            ) {
//				Log.d("Unlock", String.format("Unlock min %.0f, %.0f\n", mPreviousProgress, mCurrentProgress));
                isMin = false
            }
        }
        if (!isMax && !isMin) {
            progress = if (progress > mMax) mMax else progress
            progress = if (progress < mMin) mMin else progress
            if (mOnSwagPointsChangeListener != null) {
                progress = progress - progress % mStep
                mOnSwagPointsChangeListener!!
                    .onPointsChanged(this, progress, fromUser)
            }
            mProgressSweep = progress.toFloat() / valuePerDegree()
            //			if (mPreviousProgress != mCurrentProgress)
//				System.out.printf("-- %d, %d, %f\n", progress, mPoints, mProgressSweep);
            updateIndicatorIconPosition()
            invalidate()
        }
    }

    interface OnSwagPointsChangeListener {
        /**
         * Notification that the point value has changed.
         *
         * @param swagPoints The SwagPoints view whose value has changed
         * @param points     The current point value.
         * @param fromUser   True if the point change was triggered by the user.
         */
        fun onPointsChanged(swagPoints: SwagPoints?, points: Int, fromUser: Boolean)
        fun onStartTrackingTouch(swagPoints: SwagPoints?)
        fun onStopTrackingTouch(swagPoints: SwagPoints?)
    }

    fun setPoints(points: Int) {
        var points = points
        points = if (points > mMax) mMax else points
        points = if (points < mMin) mMin else points
        updateProgress(points, false)
    }

    fun getPoints(): Int {
        return mPoints
    }

    fun getProgressWidth(): Int {
        return mProgressWidth
    }

    fun setProgressWidth(mProgressWidth: Int) {
        this.mProgressWidth = mProgressWidth
        mProgressPaint.strokeWidth = mProgressWidth.toFloat()
    }

    fun getArcWidth(): Int {
        return mArcWidth
    }

    fun setArcWidth(mArcWidth: Int) {
        this.mArcWidth = mArcWidth
        mArcPaint.strokeWidth = mArcWidth.toFloat()
    }

    fun setClockwise(isClockwise: Boolean) {
        mClockwise = isClockwise
    }

    fun isClockwise(): Boolean {
        return mClockwise
    }

    override fun isEnabled(): Boolean {
        return mEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        mEnabled = enabled
    }

    fun getProgressColor(): Int {
        return mProgressPaint.color
    }

    fun setProgressColor(color: Int) {
        mProgressPaint.color = color
        invalidate()
    }

    fun getArcColor(): Int {
        return mArcPaint.color
    }

    fun setArcColor(color: Int) {
        mArcPaint.color = color
        invalidate()
    }

    fun setTextColor(textColor: Int) {
        mTextPaint.color = textColor
        invalidate()
    }

    fun setTextSize(textSize: Float) {
        mTextSize = textSize
        mTextPaint.textSize = mTextSize
        invalidate()
    }

    fun getMax(): Int {
        return mMax
    }

    fun setMax(mMax: Int) {
        require(mMax > mMin) { "Max should not be less than min." }
        this.mMax = mMax
    }

    fun getMin(): Int {
        return mMin
    }

    fun setMin(min: Int) {
        require(mMax > mMin) { "Min should not be greater than max." }
        mMin = min
    }

    fun getStep(): Int {
        return mStep
    }

    fun setStep(step: Int) {
        mStep = step
    }

    fun setOnSwagPointsChangeListener(onSwagPointsChangeListener: OnSwagPointsChangeListener) {
        mOnSwagPointsChangeListener = onSwagPointsChangeListener
    }

}