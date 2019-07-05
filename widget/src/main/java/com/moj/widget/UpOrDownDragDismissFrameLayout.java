package com.moj.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author m5314
 *         Created by m5314 on 2017/6/02.
 */
public class UpOrDownDragDismissFrameLayout extends FrameLayout implements NestedScrollingParent {
  private static final int TOUCH_SLOP = 8;

  private float dragDismissDistance = Float.MAX_VALUE;
  private NestedScrollingParentHelper mNestedScrollingParentHelper;

  private float totalDrag;
  private boolean draggingDown = false;
  private boolean draggingUp = false;

  private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      update();
    }
  };

  private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
      if (mListener != null) {
        mListener.dismiss();
      }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
  };

  public UpOrDownDragDismissFrameLayout(Context context) {
    this(context, null, 0);
  }

  public UpOrDownDragDismissFrameLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public UpOrDownDragDismissFrameLayout(Context context, AttributeSet attrs,
                                        int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    final TypedArray a = getContext().obtainStyledAttributes(
      attrs, R.styleable.UpOrDownDragDismissFrameLayout, 0, 0);

    if (a.hasValue(R.styleable.UpOrDownDragDismissFrameLayout_dragDismissDistance)) {
      dragDismissDistance = a.getDimensionPixelSize(R.styleable
        .UpOrDownDragDismissFrameLayout_dragDismissDistance, 0);
    }

    a.recycle();
    mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
  }

  public void setListener(Listener listener) {
    this.mListener = listener;
  }

  @Override
  public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
    return true;
  }

  @Override
  public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    if (draggingDown && dy > 0 || draggingUp && dy < 0) {
      dragScale(dy);
      consumed[1] = dy;
    }
  }

  @Override
  public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                             int dxUnconsumed, int dyUnconsumed) {
    begin = true;
    dragScale(dyUnconsumed);
  }

  @Override
  public void onStopNestedScroll(View child) {
    if(totalDrag == 0){
      return;
    }
    Log.e("onStopNestedScView", "onStopNestedScroll");

    if (Math.abs(totalDrag) >= dragDismissDistance) {
      ObjectAnimator oa = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y,
        getTranslationY(), totalDrag < 0 ? getHeight() : -getHeight())
        .setDuration(200);
      oa.addUpdateListener(mAnimatorUpdateListener);
      oa.addListener(mAnimatorListener);
      oa.start();

    } else {
      ObjectAnimator oa = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0)
        .setDuration(200);
      oa.addUpdateListener(mAnimatorUpdateListener);
      oa.start();
      totalDrag = 0;
      draggingDown = draggingUp = false;
    }
  }

  @Override
  public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
    return false;
  }

  @Override
  public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
    return false;
  }

  @Override
  public void onNestedScrollAccepted(View child, View target, int axes) {
    mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
  }

  boolean begin = false;
  private void dragScale(int scroll) {
    if (scroll == 0) return;

    totalDrag += scroll;

    if(Math.abs(totalDrag) < TOUCH_SLOP && begin){
      return;
    }
    begin = false;
    if (scroll < 0 && !draggingUp && !draggingDown) {
        draggingDown = true;
    } else if (scroll > 0 && !draggingDown && !draggingUp) {
        draggingUp = true;
    }
    setTranslationY(-totalDrag);

    if ((draggingDown && totalDrag >= 0)
      || ((draggingUp) && totalDrag <= 0)) {
      totalDrag = 0;
      draggingDown = draggingUp = false;
      setTranslationY(0f);
      setTranslationX(0f);
    }
    update();
  }

  public void update() {
    if (mListener != null) {
      float a;
      a = Math.abs(getTranslationY()) / getHeight();
      mListener.onUpdate(1.0f * a);
    }
  }

  private Listener mListener;

  public interface Listener {
    void onUpdate(float percent);

    void dismiss();
  }
}
