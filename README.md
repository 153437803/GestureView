# GestureUnlockView
手势密码解锁View


![image](https://github.com/xiesuichao/GestureUnlockView/raw/master/image/1.png)
![image](https://github.com/xiesuichao/GestureUnlockView/raw/master/image/2.png)


        //手势绘制监听
        gestureDrawView.setOnGestureDrawListener(new GestureDrawView.OnGestureDrawListener() {
            @Override
            public void gestureDraw(List<GestureCircle> checkedList) {
                //设置手势绘制选中节点，显示手势绘制结果
                gestureResultView.setCheckedList(checkedList);
            }
        });

        //手势绘制错误监听
        gestureDrawView.setOnGestureErrorListener(new GestureDrawView.OnGestureErrorListener() {
            @Override
            public void gestureError() {
                //清除手势绘制结果
                gestureResultView.clearResult();
            }
        });


    <!--手势绘制View-->
    <declare-styleable name="GestureDrawView">
        <!--外圆半径、颜色、水平间距、垂直间距-->
        <attr name="gdOuterRadius" format="integer"/>
        <attr name="gdOuterCheckCol" format="color"/>
        <attr name="gdOuterHorizontalSpace" format="integer"/>
        <attr name="gdOuterVerticalSpace" format="integer"/>
        <!--内圆半径、未选中颜色、选中颜色、连线颜色、连线宽度-->
        <attr name="gdInnerRadius" format="integer"/>
        <attr name="gdInnerNormalCol" format="color"/>
        <attr name="gdInnerCheckedCol" format="color"/>
        <attr name="gdLineCol" format="color"/>
        <attr name="gdLineWidth" format="integer"/>
    </declare-styleable>

    <!--手势绘制结果View-->
    <declare-styleable name="GestureResultView">
        <!--半径、未选中颜色、选中颜色、水平间距、垂直间距-->
        <attr name="grNormalRadius" format="integer"/>
        <attr name="grNormalCol" format="color"/>
        <attr name="grCheckedCol" format="color"/>
        <attr name="grHorizontalSpace" format="integer"/>
        <attr name="grVerticalSpace" format="integer"/>
    </declare-styleable>
    
    
    
