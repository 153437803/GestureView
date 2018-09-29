package com.kalu.gesture.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import com.kalu.gesture.R;

/**
 * description: 手势结果
 * create by kalu on 2018/9/29 8:58
 * <p>
 * 0>>>1>>>2
 * 3>>>4>>>5
 * 6>>>7>>>8
 * </p>
 */
public final class GestureResultView extends View {

    // 半径
    private int mRadius = -1;
    // 颜色默认
    private int mColorNormal = 0xffCACCD6;
    // 颜色选中
    private int mColorSelect = 0xffFF5442;
    // 画笔
    private final Paint mPaint = new Paint();
    // 数据
    private final int[] mData = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};

    /**********************************************************************************************/

    public GestureResultView(Context context) {
        this(context, null, 0);
    }

    public GestureResultView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typed = null;

        try {

            typed = context.obtainStyledAttributes(attrs, R.styleable.GestureResultView);

            final int size = (int) getResources().getDisplayMetrics().density * 10;
            mRadius = typed.getInteger(R.styleable.GestureResultView_grv_radius, size);
            mColorNormal = typed.getColor(R.styleable.GestureResultView_grv_color_normal, 0xffCACCD6);
            mColorSelect = typed.getColor(R.styleable.GestureResultView_grv_color_select, 0xffFF5442);

            typed.recycle();

        } catch (Exception e) {

            if (null != typed) {
                typed.recycle();
            }
        }
    }

    /**********************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画笔
        mPaint.reset();
        mPaint.clearShadowLayer();
        mPaint.setAntiAlias(true);
        mPaint.setFakeBoldText(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(20);

        // 内边距
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        // 视图宽度
        final int width = getWidth() - paddingRight - paddingLeft;
        // 视图高度
        final int height = getHeight() - paddingTop - paddingBottom;

        // 圆点宽度总和
        final int radiusH = 6 * mRadius;
        // 圆点高度总和
        final int radiusV = 6 * mRadius;

        // 容错提示
        if (radiusH > width || radiusV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusV) / 2;

        // 循环
        for (int i = 0; i < 9; i++) {

            final int positionH = (i % 3);
            final int positionV = (i / 3);

            final float cx = mRadius + positionH * (spaceH + 2 * mRadius) + paddingLeft;
            final float cy = mRadius + positionV * (spaceV + 2 * mRadius) + paddingTop;
            // Log.e("kalu", "onDraw[预览结果] ==> i =" + i + ", cx = " + cx + ", cy = " + cy);

            final int number = mData[i];
            if (number != -1) {
                // 选种颜色
                mPaint.setColor(mColorSelect);
                canvas.drawCircle(cx, cy, mRadius, mPaint);

                // 选中文字
                mPaint.setColor(0xFF000000);
                final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                final float font = fontMetrics.bottom - fontMetrics.top;
                canvas.drawText(String.valueOf(number), cx, cy + font / 3, mPaint);
            } else {

                // 默认颜色
                mPaint.setColor(mColorNormal);
                canvas.drawCircle(cx, cy, mRadius, mPaint);
            }
        }
    }

    /**
     * 设置单个数据
     *
     * @param index
     * @param number
     */
    public final void setSelect(@IntRange(from = 0, to = 8) final int index, @IntRange(from = 1, to = 9) final int number) {

        for (int i = 0; i < 9; i++) {
            if (mData[i] == number) {
                throw new RuntimeException("已存在" + number);
            }
        }

        mData[index] = number;
        postInvalidate();
    }

    /**
     * 清空单个数据
     *
     * @param index
     */
    public final void clear(@IntRange(from = 0, to = 8) final int index) {
        mData[index] = -1;
        postInvalidate();
    }

    /**
     * 清空所有数据
     */
    public final void clearAll() {
        for (int i = 0; i < 9; i++) {
            mData[i] = -1;
        }
        postInvalidate();
    }
}
