package com.kalu.gesture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public final class MainActivity extends AppCompatActivity {

//    private GestureDrawView gestureDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
