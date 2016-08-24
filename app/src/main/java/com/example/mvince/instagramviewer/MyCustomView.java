/**
 * Created by taku on 16/08/24.
 */

package com.example.mvince.instagramviewer;

        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TabHost;
        import android.widget.TabHost.TabSpec;
        import android.widget.TextView;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.Color;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.content.Context;
        import android.util.AttributeSet;

public class MyCustomView extends View{

    private int width;
    private int height;
    private Bitmap image;

    public MyCustomView(Context context){
        super(context);

        // 背景色
        setBackgroundColor(Color.argb(80, 211, 222, 241));  //第一引数はalpha
        image = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        width = 230+image.getWidth();
        height = (120 > image.getHeight())?120:image.getHeight();
    }

    public MyCustomView(Context context, AttributeSet attrs){
        super(context,attrs);

        setBackgroundColor(Color.argb(80, 211, 222, 241));
        image = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        width = 230+image.getWidth();
        height = (120 > image.getHeight())?120:image.getHeight();
    }

    @Override
    public void onDraw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setTextSize(25);
//        canvas.drawRect(30, 30, 80, 120, p);
        canvas.drawText("InstaFlickr-Android",145,80,p);

        canvas.drawBitmap(image, 5, 10, p);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(width, height);     //// カスタムView全体の描画サイズを指定する
    }

  /*  @Override
    public void onLayout(int widthMeasureSpec,int heightMeasureSpec) {   //子Viewの位置を決める
        super.onLayout(left, top, right, bottom, changed);
        View view = findViewById(R.id.view_id);
        int vl = view.getLeft();  //super.onLayoutでViewのleft,top等には値が入っている
        int vt = view.getTop();
        int vr = view.getRight();
        int vb = view.getBottom();
        //ちょっと左にずらしますよ...
        vl -= 100;
        vr -= 100;
        view.layout(vl, vt, vr, vb);   //このViewだけ位置をずらす
    }*/
}