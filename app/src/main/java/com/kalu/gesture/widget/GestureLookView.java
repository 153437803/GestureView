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
public final class GestureLookView extends View {

    // 半径外圆
    private int mRadiusOuter = 80;
    // 半径内圆
    private int mRadiusInner = 40;
    // 颜色圆环
    private int mColorOuter = 0x66FF5442;
    // 颜色选中
    private int mColorSelect = 0xffFF5442;
    // 颜色默认
    private int mColorNormal = 0xffCACCD6;
    // 路径
    private final Path mPath = new Path();
    // 画笔
    private final Paint mPaint = new Paint();
    // 数据
    private final int[] mData = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
    // 路径
    private final LinkedList<Integer> mPaths = new LinkedList<>();

    /**********************************************************************************************/

    public GestureLookView(Context context) {
        this(context, null, 0);
    }

    public GestureLookView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLookView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mPath.reset();
        for (int i = 0; i < mPaths.size(); i += 2) {
            final int x = mPaths.get(i);
            final int y = mPaths.get(i + 1);
            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
        }
        canvas.drawPath(mPath, mPaint);

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
        final int radiusH = 6 * mRadiusInner;
        // 圆点高度总和
        final int radiusV = 6 * mRadiusInner;

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

            final float cx = mRadiusInner + positionH * (spaceH + 2 * mRadiusInner) + paddingLeft;
            final float cy = mRadiusInner + positionV * (spaceV + 2 * mRadiusInner) + paddingTop;
            // Log.e("kalu", "onDraw[预览结果] ==> i =" + i + ", cx = " + cx + ", cy = " + cy);

            final int number = mData[i];
            if (number != -1) {

                // 选种颜色外圆
                mPaint.setColor(mColorSelect);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mRadiusInner, mPaint);

                // 选种颜色内圆
                mPaint.setColor(mColorSelect);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mRadiusInner, mPaint);

                // 选中文字
                mPaint.setColor(0xFF000000);
                final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                final float font = fontMetrics.bottom - fontMetrics.top;
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(String.valueOf(number), cx, cy + font / 3, mPaint);
            } else {

                // 默认颜色
                mPaint.setColor(mColorNormal);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mRadiusInner, mPaint);
            }
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
        // x
        final int x = (int) event.getX();
        // y
        final int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                checkDown(x, y, width, height, paddingLeft, paddingBottom);
                break;
            case MotionEvent.ACTION_MOVE:
                checkMove(x, y, width, height, paddingLeft, paddingBottom);
                break;
            case MotionEvent.ACTION_UP:
                checkUp(x, y, width, height, paddingLeft, paddingBottom);
                break;
        }
        return true;
    }

    private final void checkMove(final int x, final int y, final int width, final int height, final int paddingLeft, final int paddingTop) {

        Log.e("kalu", "checkMove ==> size = " + mPaths.size());
        if (mPaths.size() < 2)
            return;

        // 圆点宽度总和
        final int radiusH = 6 * mRadiusInner;
        // 圆点高度总和
        final int radiusV = 6 * mRadiusInner;

        // 容错提示
        if (radiusH > width || radiusV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusV) / 2;

        // 寻找最大值
        int max = 0;
        for (int i = 0; i < 9; i++) {
            max = Math.max(max, mData[i]);
        }

        // 循环
        for (int i = 0; i < 9; i++) {

            final int positionH = (i % 3);
            final int positionV = (i / 3);

            final int cx = mRadiusInner + positionH * (spaceH + 2 * mRadiusInner) + paddingLeft;
            final int cy = mRadiusInner + positionV * (spaceV + 2 * mRadiusInner) + paddingTop;
            // Log.e("kalu", "onDraw[预览结果] ==> i =" + i + ", cx = " + cx + ", cy = " + cy);

            boolean inner = isInner(x, y, cx, cy);
            if (inner && mData[i] == -1) {
                mData[i] = max + 1;
                mPaths.add(cx);
                mPaths.add(cy);
                postInvalidate();
                return;
            }
        }

        if (mPaths.size() > 4) {
            mPaths.removeLast();
            mPaths.removeLast();
        }

        mPaths.add(x);
        mPaths.add(y);
        postInvalidate();
    }

    private final void checkDown(final int x, final int y, final int width, final int height, final int paddingLeft, final int paddingTop) {

        // 清空
        mPath.reset();
        mPaths.clear();
        postInvalidate();

        // 圆点宽度总和
        final int radiusH = 6 * mRadiusInner;
        // 圆点高度总和
        final int radiusV = 6 * mRadiusInner;

        // 容错提示
        if (radiusH > width || radiusV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusV) / 2;

        // 寻找最大值
        int max = 0;
        for (int i = 0; i < 9; i++) {
            max = Math.max(max, mData[i]);
        }

        // 循环
        for (int i = 0; i < 9; i++) {

            final int positionH = (i % 3);
            final int positionV = (i / 3);

            final int cx = mRadiusInner + positionH * (spaceH + 2 * mRadiusInner) + paddingLeft;
            final int cy = mRadiusInner + positionV * (spaceV + 2 * mRadiusInner) + paddingTop;
            // Log.e("kalu", "onDraw[预览结果] ==> i =" + i + ", cx = " + cx + ", cy = " + cy);

            boolean inner = isInner(x, y, cx, cy);
            if (inner) {
                mData[i] = max + 1;
                mPaths.add(cx);
                mPaths.add(cy);
                postInvalidate();
                return;
            }
        }
    }

    private final void checkUp(final int x, final int y, final int width, final int height, final int paddingLeft, final int paddingTop) {

        Log.e("kalu", "checkUp ==> size = " + mPaths.size());
        if (mPaths.size() < 2)
            return;

        // 圆点宽度总和
        final int radiusH = 6 * mRadiusInner;
        // 圆点高度总和
        final int radiusV = 6 * mRadiusInner;

        // 容错提示
        if (radiusH > width || radiusV > height) {
            throw new RuntimeException("圆点宽度总和不能大于视图宽度");
        }

        // 间隙宽度水平
        final int spaceH = (width - radiusH) / 2;
        // 间隙宽度垂直
        final int spaceV = (height - radiusV) / 2;

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

        mPaths.removeLast();
        mPaths.removeLast();
        postInvalidate();
    }

    private final boolean isInner(int x, int y, int cx, int cy) {
        final int left = cx - mRadiusInner;
        final int right = cx + mRadiusInner;
        final int top = cy - mRadiusInner;
        final int bottom = cy + mRadiusInner;

        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        } else {
            return false;
        }
    }

    private final boolean isInner(int x, int y, int i, int spaceH, int spaceV, int paddingLeft, int paddingTop) {

        final int positionH = (i % 3);
        final int positionV = (i / 3);

        final int cx = mRadiusInner + positionH * (spaceH + 2 * mRadiusInner) + paddingLeft;
        final int cy = mRadiusInner + positionV * (spaceV + 2 * mRadiusInner) + paddingTop;

        final int left = cx - mRadiusInner;
        final int right = cx + mRadiusInner;
        final int top = cy - mRadiusInner;
        final int bottom = cy + mRadiusInner;

        if (x >= left && x <= right && y >= top && y <= bottom) {
            return true;
        } else {
            return false;
        }
    }

    /**********************************************************************************************/

    public interface OnGestureChangeListener {

        void onChange(int[] data);

        void onError(String message);
    }

    private OnGestureChangeListener mOnGestureChangeListener;

    public void setOnGestureChangeListener(OnGestureChangeListener listener) {
        this.mOnGestureChangeListener = listener;
    }
}
