/**
 * Created by taku on 16/08/23.
 */

package com.example.mvince.instagramviewer;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.AttributeSet;

public class TileLayout extends ViewGroup {

    public TileLayout(Context context) {
        this(context, null);
    }

    public TileLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TileLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 子Viewの大きさを決定
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // このViewGroupに指定されているレイアウトのモードを取得する
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            // レイアウトモードがEXACTLY以外のときはエラーにする
            throw new IllegalStateException("Must measure with an exact width");
        }

        // このViewGroupに割り当てられているサイズを取得する
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // このViewGroupのサイズをセットする
        setMeasuredDimension(widthSize, heightSize);

//        子Viewの設定。
//        まずはサイズの指定。

        // padding分を差し引いて親の幅と高さを求める
        int width = widthSize - getPaddingLeft() - getPaddingRight();
        int height = heightSize - getPaddingTop() - getPaddingBottom();

        int childWidth1;
        int childHeight1;

        int childWidth2;
        int childHeight2;

        int childWidth34;
        int childHeight34;

        // 2番目の子ビューの幅 = 高さ = 親の幅 * 2 / 3
        childWidth2 = childHeight2 = width * 2 / 3;

        // 1番目の子ビューの幅 = 親の幅
        childWidth1 = width;
        // 1番目の子ビューの高さ = 親の高さ - 2番目の子ビューの幅
        childHeight1 = height - childHeight2;

        // 3,4番目の子ビューの幅 = 親の幅 - 2番目の子ビューの幅
        childWidth34 = width - childWidth2;
        // 3,4番目の子ビューの高さ = 2番目の子ビューの高さ / 2
        childHeight34 = childHeight2 / 2;

//        すべての子Viewに大して、大きさを設定。

        // ViewGroup配下のすべての子Viewの数取得
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child
                    .getLayoutParams();

            int childWidth = 0;
            int childHeight = 0;
            if (i == 0) {
                // 1番目の子ビュー
                childWidth = childWidth1 - params.leftMargin
                        - params.rightMargin;
                childHeight = childHeight1 - params.topMargin
                        - params.bottomMargin;
            } else if (i == 1) {
                // 2番目の子ビュー
                childWidth = childWidth2 - params.leftMargin
                        - params.rightMargin;
                childHeight = childHeight2 - params.topMargin
                        - params.bottomMargin;
            } else if (i < 4) {
                // 3,4番目の子ビュー
                childWidth = childWidth34 - params.leftMargin
                        - params.rightMargin;
                childHeight = childHeight34 - params.topMargin
                        - params.bottomMargin;
            } else {
                // 5番目以降の子ビューのサイズは0（無視する）
                childWidth = 0;
                childHeight = 0;
            }

//         子Viewに大してmeasure()を実行。

            // 子ビューに対してmeasure()を呼んでサイズを指定する
            // TileLayoutでは子ビューのlayout_heightやlayout_widthの指定に関係なく
            // 決まったサイズで配置するのでMeasureSpec.EXACTLYにする
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth,
                    MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childHeight, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            child.requestLayout();
        }
    }

    // 子Viewの位置を決定
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        各子Viewの画面サイズ取得。
        // padding分を差し引いて子ビュー用の領域を求める
        int width = (r - l) - getPaddingLeft() - getPaddingRight();
        int height = (b - t) - getPaddingTop() - getPaddingBottom();

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = left + width;
        int bottom = top + height;

        // 1つめは画面幅、2つめ以降は正方形
        // 2つめは横幅の2/3
        int childWidth2 = width * 2 / 3;
        // 1つめは画面高さから、二つ目の高さを引いただけの高さ
        int childHeight1 = height - childWidth2;
        // 3、４つめは2つめの高さの半分
        int childHeight34 = childWidth2 / 2;

//        layoutを使って各子Viewを配置

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i > 3) {
                // 5番目以降の子ビューは配置しない（無視する）
                break;
            }
            View child = getChildAt(i);

            switch (i) {
                case 0: {
                    // 1番目の子ビュー
                    childLayout(child, left, top, right, top + childHeight1);
                    break;
                }
                case 1: {
                    // 2番目のビュー
                    childLayout(child, left, top + childHeight1,
                            left + childWidth2, bottom);
                    break;
                }
                case 2: {
                    // 3番目のビュー
                    childLayout(child, left + childWidth2, top + childHeight1,
                            right, top + childHeight1 + childHeight34);
                    break;
                }
                case 3: {
                    // 4番目のビュー
                    childLayout(child, left + childWidth2, top + childHeight1
                            + childHeight34, right, bottom);
                    break;
                }
            }
        }
    }

    private void childLayout(View child, int l, int t, int r, int b) {
        // マージン分だけずらして配置する
//        TileLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();

        LayoutParams params = (LayoutParams) child.getLayoutParams();

        child.layout(l + params.leftMargin, t + params.topMargin, r
                - params.rightMargin, b - params.bottomMargin);
        child.requestLayout();
    }
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }
    }
}