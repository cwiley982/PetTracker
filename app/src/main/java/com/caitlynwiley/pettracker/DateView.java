package com.caitlynwiley.pettracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class DateView extends FrameLayout {

    Paint mPaint;

    public DateView(Context context, AttributeSet attribs) {
        super(context, attribs);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.primaryColor, null));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.backgroundLight));

        int offset = 20;
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());

        // Define the corners radius of rounded rectangle
        int cornersRadius = 25;

        // Finally, draw the rounded corners rectangle object on the canvas
        canvas.drawRoundRect(
                rectF, // rect
                cornersRadius, // rx
                cornersRadius, // ry
                mPaint // Paint
        );
    }
}
