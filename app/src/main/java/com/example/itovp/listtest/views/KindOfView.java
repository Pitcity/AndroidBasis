package com.example.itovp.listtest.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.itovp.listtest.R;

/**
 * Created by itovp on 20.02.2017.
 */

public class KindOfView extends View {

	public enum Thickness {
		thin, middle, thick
	}

	float dx, dy;

	Paint mPaintCircle = new Paint(), mPaintLine = new Paint();

	public KindOfView(Context ctx) {
		super(ctx);
	}

	public KindOfView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KindOfView, 0, 0);
		mPaintCircle.setColor(a.getColor(R.styleable.KindOfView_circleColor, 0x10000000));
		mPaintLine.setColor(a.getColor(R.styleable.KindOfView_lineColor, 0x10000000));
		mPaintLine.setStrokeWidth(a.getInteger(R.styleable.KindOfView_lineWidth, 1));
	}

	public void setCircleColor(int color) {
		mPaintCircle.setColor(color);

		invalidate();
	}

	public void setLineThickness(Thickness thickness) {
		switch (thickness) {
			case thin:
				mPaintLine.setStrokeWidth(1);

			case middle:
				mPaintLine.setStrokeWidth(5);

			case thick:
				mPaintLine.setStrokeWidth(10);
		}
		invalidate();
	}

	public void setLineColor(int color) {
		mPaintLine.setColor(color);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawLine(0, 0, 200, 200, mPaintLine);
		canvas.drawCircle(100, 100, 50, mPaintCircle);

	}

	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		this.setVisibility(INVISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLayoutParams();
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				dx = (event.getRawX() - layoutParams.leftMargin);
				dy = (event.getRawY() - layoutParams.topMargin);
				break;
			case MotionEvent.ACTION_MOVE:
				layoutParams.leftMargin = (int) (event.getRawX() - dx);
				layoutParams.topMargin = (int) (event.getRawY() - dy);
				this.setLayoutParams(layoutParams);
				break;
			case MotionEvent.ACTION_UP:
				if (event.getRawX() == dx + layoutParams.leftMargin && event.getRawY() == dy + layoutParams.topMargin) {
					Animation fadeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade);
					this.startAnimation(fadeAnimation);
				}
		}
		return true;

	}
}
