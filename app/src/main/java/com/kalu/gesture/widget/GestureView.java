package com.kalu.gesture.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.util.LinkedList;

/**
 * description: 手势预览
 * create by kalu on 2018/9/29 8:58
 * <p>
 * 0>>>1>>>2
 * 3>>>4>>>5
 * 6>>>7>>>8
 * </p>
 */
public final class GestureView extends View {

    // 手势
    private int mMoveX = -1, mMoveY = -1;
    // 半径外圆
    private int mRadiusOuter = 100;
    // 半径内圆
    private int mRadiusInner = 50;
    // 颜色圆环
    private int mColorOuter = 0x66FF5442;
    // 颜色选中
    private int mColorSelect = 0xffFF5442;
    // 颜色默认
    private int mColorNormal = 0xffCACCD6;
    // 画笔
    private final Paint mPaint = new Paint();
    // 数据
    private final int[] mData = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
    // 轨迹
    private final LinkedList<Integer> mLine = new LinkedList<>();

    /**********************************************************************************************/

    public GestureView(Context context) {
        this(context, null, 0);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(20);

        // 路径
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);

        // step1: 轨迹
        final Path path = new Path();
        int size = mLine.size();
        for (int i = 0; i < size; i += 2) {
            final int x = mLine.get(i);
            final int y = mLine.get(i + 1);
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }

            if (i == size - 2 && mMoveX != -1 && mMoveY != -1) {
                path.lineTo(mMoveX, mMoveY);
                mMoveX = -1;
                mMoveY = -1;
            }
        }
        canvas.drawPath(path, mPaint);

        // 内边距
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        // 视图宽度
        final int width = getWidth() - paddingRight - paddingLeft;
        // 视图高度
        final int height = getHeight() - paddingTop - paddingBottom;

        // 外圆宽度总和
        final int radiusOuterH = 6 * mRadiusOuter;
        // 外圆高度总和
        final int radiusOuterV = 6 * mRadiusOuter;

        // 容错提示
        if (radiusOuterH > width || radiusOuterV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusOuterH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusOuterV) / 2;

        // 循环
        for (int i = 0; i < 9; i++) {

            final int positionH = (i % 3);
            final int positionV = (i / 3);

            final int cx = mRadiusOuter + positionH * (spaceH + 2 * mRadiusOuter) + paddingLeft;
            final int cy = mRadiusOuter + positionV * (spaceV + 2 * mRadiusOuter) + paddingTop;
            Log.e("kalu", "onDraw ==> i =" + i + ", cx = " + cx + ", cy = " + cy);

            final int number = mData[i];
            if (number != -1) {

                // 选种颜色外圆
                mPaint.setColor(mColorOuter);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mRadiusOuter, mPaint);

                // 选种颜色内圆
                mPaint.setColor(mColorSelect);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mRadiusInner, mPaint);

                // 选中文字
//                mPaint.setColor(0xFF000000);
//                final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
//                final float font = fontMetrics.bottom - fontMetrics.top;
//                mPaint.setStyle(Paint.Style.FILL);
//                canvas.drawText(String.valueOf(number), cx, cy + font / 3, mPaint);
            } else {

                // 默认颜色
                mPaint.setColor(mColorNormal);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mRadiusInner, mPaint);
            }

            // 圆心
            mPaint.setColor(Color.BLACK);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, 5, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 内边距
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        // 视图宽度
        final int width = getWidth() - paddingRight - paddingLeft;
        // 视图高度
        final int height = getHeight() - paddingTop - paddingBottom;

        // 外圆宽度总和
        final int radiusOuterH = 6 * mRadiusOuter;
        // 外圆高度总和
        final int radiusOuterV = 6 * mRadiusOuter;

        // x
        final int x = (int) event.getX();
        // y
        final int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveX = -1;
                mMoveY = -1;
                checkDown(x, y, width, height, paddingLeft, paddingBottom, radiusOuterH, radiusOuterV);

                if (null != mOnGestureChangeListener) {
                    mOnGestureChangeListener.onChange(mData);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                checkMove(x, y, width, height, paddingLeft, paddingBottom, radiusOuterH, radiusOuterV);

                if (null != mOnGestureChangeListener) {
                    mOnGestureChangeListener.onChange(mData);
                }
                break;
            case MotionEvent.ACTION_UP:
                mMoveX = -1;
                mMoveY = -1;
                checkUp(x, y, width, height, paddingLeft, paddingBottom, radiusOuterH, radiusOuterV);

                if (null != mOnGestureChangeListener) {
                    mOnGestureChangeListener.onChange(mData);
                }
                break;
        }
        return true;
    }

    private final void checkDown(final int x, final int y, final int width, final int height, final int paddingLeft, final int paddingTop, final int radiusOuterH, final int radiusOuterV) {

        // 清空
        for (int i = 0; i < 9; i++) {
            mData[i] = -1;
        }
        mLine.clear();
        postInvalidate();

        // 间隙宽度水平
        final int spaceH = (width - radiusOuterH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusOuterV) / 2;

        // 寻找最大值
        int max = 0;
        for (int i = 0; i < 9; i++) {
            max = Math.max(max, mData[i]);
        }

        // 循环
        for (int i = 0; i < 9; i++) {

            final int positionH = (i % 3);
            final int positionV = (i / 3);

            final int cx = mRadiusOuter + positionH * (spaceH + 2 * mRadiusOuter) + paddingLeft;
            final int cy = mRadiusOuter + positionV * (spaceV + 2 * mRadiusOuter) + paddingTop;
            // Log.e("kalu", "onDraw[预览结果] ==> i =" + i + ", cx = " + cx + ", cy = " + cy);

            boolean inner = isInner(x, y, cx, cy);
            if (inner) {
                mData[i] = max + 1;

                mLine.add(cx);
                mLine.add(cy);

                postInvalidate();
                return;
            }
        }
    }

    private final void checkMove(final int x, final int y, final int width, final int height, final int paddingLeft, final int paddingTop, final int radiusOuterH, final int radiusOuterV) {

        final int size = mLine.size();
        if (size < 2)
            return;

        // 容错提示
        if (radiusOuterH > width || radiusOuterV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusOuterH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusOuterV) / 2;

        // 寻找最大值
        int max = 0;
        for (int i = 0; i < 9; i++) {
            max = Math.max(max, mData[i]);
        }

        // 循环
        for (int i = 0; i < 9; i++) {

            final int positionH = (i % 3);
            final int positionV = (i / 3);

            final int cx = mRadiusOuter + positionH * (spaceH + 2 * mRadiusOuter) + paddingLeft;
            final int cy = mRadiusOuter + positionV * (spaceV + 2 * mRadiusOuter) + paddingTop;
            Log.e("kaluww", "onDraw[预览结果] ==> i =" + i +
                    ", width = " + width +
                    ", height = " + height +
                    ", spaceH = " + spaceH +
                    ", spaceV = " + spaceV +
                    ", radius = " + mRadiusOuter +
                    ", cx = " + cx +
                    ", cy = " + cy);

            boolean inner = isInner(x, y, cx, cy);
            if (inner && mData[i] == -1) {
                mData[i] = max + 1;

                mLine.add(cx);
                mLine.add(cy);

                postInvalidate();
                return;
            }
        }

        mMoveX = x;
        mMoveY = y;
        postInvalidate();
    }

    private final void checkUp(final int x, final int y, final int width, final int height, final int paddingLeft, final int paddingTop, final int radiusOuterH, final int radiusOuterV) {

        final int size = mLine.size();
        if (size < 2)
            return;

        // 容错提示
        if (radiusOuterH > width || radiusOuterV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusOuterH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusOuterV) / 2;

        // 寻找最大值
        int max = 0;
        for (int i = 0; i < 9; i++) {
            max = Math.max(max, mData[i]);
        }

        // 循环
        for (int i = 0; i < 9; i++) {

            // Log.e("kalu", "onDraw[预览结果] ==> i =" + i + ", cx = " + cx + ", cy = " + cy);
            boolean inner = isInner(x, y, i, spaceH, spaceV, paddingLeft, paddingTop);
            if (inner) return;
        }
    }

    private final boolean isInner(int x, int y, int cx, int cy) {

        final int rangex = cx - x;
        final int rangey = cy - y;
        final double sqrt = Math.sqrt(rangex * rangex + rangey * rangey);
        return sqrt <= mRadiusOuter;
    }

    private final boolean isInner(int x, int y, int i, int spaceH, int spaceV, int paddingLeft, int paddingTop) {

        final int positionH = (i % 3);
        final int positionV = (i / 3);

        final int cx = mRadiusOuter + positionH * (spaceH + 2 * mRadiusOuter) + paddingLeft;
        final int cy = mRadiusOuter + positionV * (spaceV + 2 * mRadiusOuter) + paddingTop;

        final int rangex = cx - x;
        final int rangey = cy - y;
        final double sqrt = Math.sqrt(rangex * rangex + rangey * rangey);
        return sqrt <= mRadiusOuter;
    }

    /**********************************************************************************************/

    public interface OnGestureChangeListener {

        void onChange(int[] data);
    }

    private OnGestureChangeListener mOnGestureChangeListener;

    public void setOnGestureChangeListener(OnGestureChangeListener listener) {
        this.mOnGestureChangeListener = listener;
    }
}
