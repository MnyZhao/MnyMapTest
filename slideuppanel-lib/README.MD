#SlidingUpPanelLayout  

[借鉴自此处github](https://github.com/umano/AndroidSlidingUpPanel)

## 使用方法
```text
包含com.sothree.slidinguppanel.SlidingUpPanelLayout在活动布局中的根元素。
布局必须gravity设置为top或bottom。
确保它有两个child。第一个child是你的主要布局。第二个child是您向上滑动面板的布局。
主布局应设置宽度和高度match_parent。
滑动布局应当具有的宽度设定为match_parent和高度设置为match_parent，wrap_content或最大desireable高度。如果要将高度定义为屏幕的感知，请将其设置为match_parent并定义layout_weight滑动视图的属性。
默认情况下，整个面板将充当拖动区域，并将拦截点击和拖动事件。您可以使用setDragView方法或umanoDragView属性将拖动区域限制为特定视图。
```
```xml
<com.sothree.slidinguppanel.SlidingUpPanelLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:sothree="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/sliding_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="bottom"
    sothree:umanoDragView="@+id/dragView"
    sothree:umanoOverlay="true"
    sothree:umanoPanelHeight="40dp"
    sothree:umanoParallaxOffset="100dp"
    sothree:umanoScrollableView="@+id/list"
    sothree:umanoShadowHeight="4dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:text="helloworld" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/darker_gray"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:gravity="center"
            android:text="上拉显示更多" />
    </LinearLayout>

</com.sothree.slidinguppanel.SlidingUpPanelLayout>

```
```java
为了与ActionBar顺利交互，请确保在您的样式中windowActionBarOverlay设置true：

<style name="AppTheme">
    <item name="android:windowActionBarOverlay">true</item>
</style>
但是，在这种情况下，您可能需要在主要布局中添加上边距?android:attr/actionBarSize 或?attr/actionBarSize支持较旧的API版本。
```
#####必须存在两个child 多了也不行少了也不行
#####第一个child 是主要显示内容 应该为填充
#####第二个child 是上拉显示区域 也可以填充

### 属性一览

* 如果您使用自定义umanoDragView，则面板将通过单击事件传递到主布局。制作第二个布局clickable以防止这种情况。
* 您可以使用setPanelHeight方法或umanoPanelHeight属性更改面板高度。*显示默认高度
* 如果要隐藏滑动面板上方的阴影，请将shadowHeight属性设置为0。
* 使用setEnabled(false)完全禁用滑动面板（包括触摸和滑动编程）
* 使用setTouchEnabled(false)以禁用面板的触摸响应（拖动和点击），你仍然可以控制面板编程
* 使用getPanelState来获得当前面板状态
* 使用setPanelState设置当前面板状态
* 您可以通过设置umanoParallaxOffset属性为主视图添加视差*既向上拖出第二个布局时第一个布局位移的距离。
* 您可以在屏幕中间设置一个锚点，setAnchorPoint以允许面板的中间展开状态（类似于Google地图）。
* 您可以设置a PanelSlideListener监视有关滑动窗格的事件。
* 您还可以通过更改layout_gravity布局的属性使面板从顶部滑动top。
* 您可以通过设置umanoScrollInterpolator属性为面板移动提供滚动插补器。例如，如果您想要面板的反弹或过冲效果。
* 默认情况下，面板会推送主要内容。您可以使用setOverlayed方法或umanoOverlay属性覆盖主要内容。如果您想使滑动布局半透明，这非常有用。您还可以设置umanoClipPanel为false以使面板在非叠加模式下透明。
* 默认情况下，当面板向上滑动时，主要内容会变暗。您可以通过更改来更改暗淡的颜色umanoFadeColor。将其设置为"@android:color/transparent"完全消除调光。
###setPanelState 设置当前面版状态
```text
        EXPANDED,//扩展
        COLLAPSED,//关闭
        ANCHORED,//锚点  设置锚点setAnchorPoint(float f) f的值在0-1 之间 指 占用屏幕的比例
        HIDDEN,//隐藏
        DRAGGING//拖拽
```
###setFadeOnClickListener
* 为点亮的主视图部分提供单击。 如果面板处于折叠或隐藏位置，则不会触发侦听器。 如果未提供on click侦听器，则灰色区域上的单击将传递到主布局。
###当点击返回 控件未关闭时 重写onBackPressed方法并根据状态选择关闭
```java
    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
```
### .addPanelSlideListener 添加状态变化监听