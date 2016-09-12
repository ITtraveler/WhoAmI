package com.tc.defineview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;

import com.tc.whoami.R;

public class CircleView extends View{
	private ShapeDrawable mDrawable;
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mDrawable = new ShapeDrawable(new OvalShape());
		Paint paint = mDrawable.getPaint();
		paint.setColor(context.getResources().getColor(R.color.bn_blue));
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(4);
		mDrawable.setBounds(0, 0, 150, 150);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mDrawable.draw(canvas);
	}
}
