package com.kalu.gesture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kalu.gesture.widget.GestureResultView;
import com.kalu.gesture.widget.GestureView;

import java.util.Arrays;

public final class MainActivity extends AppCompatActivity {

//    private GestureDrawView gestureDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final GestureView view1 = findViewById(R.id.gd_gesture_draw);
        final GestureResultView view2 = findViewById(R.id.gr_gesture_result);

        view1.setOnGestureChangeListener(new GestureView.OnGestureChangeListener() {
            @Override
            public void onChange(int[] data) {
                Log.e("up", Arrays.toString(data));
                view2.setDate(data);
            }
        });
//
//        gestureDrawView = findViewById(R.id.gd_gesture_draw);
//        final GestureResultView gestureResultView = findViewById(R.id.gr_gesture_result);
//
//        //手势绘制监听
//        gestureDrawView.setOnGestureDrawListener(new GestureDrawView.OnGestureDrawListener() {
//            @Override
//            public void gestureDraw(List<GestureCircleView> checkedList) {
//                //设置手势绘制选中节点，显示手势绘制结果
//                gestureResultView.setCheckedList(checkedList);
//            }
//        });
//
//        //手势绘制错误监听
//        gestureDrawView.setOnGestureErrorListener(new GestureDrawView.OnGestureErrorListener() {
//            @Override
//            public void gestureError() {
//                //清除手势绘制结果
//                gestureResultView.clearResult();
//            }
//        });
    }
}
