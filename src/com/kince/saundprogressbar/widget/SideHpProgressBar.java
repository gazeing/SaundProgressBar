package com.kince.saundprogressbar.widget;

import com.kince.saundprogressbar.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class SideHpProgressBar extends ProgressBar {
	
	protected Drawable m_indicator;
	protected int m_offset = 5;
	protected TextPaint m_textPaint;
	protected Formatter m_formatter;

	public SideHpProgressBar(Context context) {
		this(context, null);
	}

	public SideHpProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideHpProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// create a default progress bar indicator text paint used for drawing
		// the
		// text on to the canvas
		m_textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		m_textPaint.density = getResources().getDisplayMetrics().density;

		m_textPaint.setColor(Color.WHITE);
		m_textPaint.setTextAlign(Align.CENTER);
		m_textPaint.setTextSize(10);
		m_textPaint.setFakeBoldText(true);

		// get the styleable attributes as defined in the xml
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SaundProgressBar, defStyle, 0);

		if (a != null) {
			m_textPaint.setTextSize(a.getDimension(
					R.styleable.SaundProgressBar_textSize, 10));
			m_textPaint.setColor(a.getColor(
					R.styleable.SaundProgressBar_textColor, Color.WHITE));

			int alignIndex = (a.getInt(R.styleable.SaundProgressBar_textAlign,
					1));
			if (alignIndex == 0) {
				m_textPaint.setTextAlign(Align.LEFT);
			} else if (alignIndex == 1) {
				m_textPaint.setTextAlign(Align.CENTER);
			} else if (alignIndex == 2) {
				m_textPaint.setTextAlign(Align.RIGHT);
			}

			int textStyle = (a
					.getInt(R.styleable.SaundProgressBar_textStyle, 1));
			if (textStyle == 0) {
				m_textPaint.setTextSkewX(0.0f);
				m_textPaint.setFakeBoldText(false);
			} else if (textStyle == 1) {
				m_textPaint.setTextSkewX(0.0f);
				m_textPaint.setFakeBoldText(true);
			} else if (textStyle == 2) {
				m_textPaint.setTextSkewX(-0.25f);
				m_textPaint.setFakeBoldText(false);
			}

			m_indicator = a
					.getDrawable(R.styleable.SaundProgressBar_progressIndicator);

			m_offset = (int) a.getDimension(
					R.styleable.SaundProgressBar_offset, 0);

			a.recycle();
		}
		
		
	}

	/**
	 * Sets the drawable used as a progress indicator
	 * 
	 * @param indicator
	 */
	public void setProgressIndicator(Drawable indicator) {
		m_indicator = indicator;
	}

	/**
	 * The text formatter is used for customizing the presentation of the text
	 * displayed in the progress indicator. The default text format is X% where
	 * X is [0,100]. To use the formatter you must provide an object which
	 * implements the {@linkplain SaundProgressBar.Formatter} interface.
	 * 
	 * @param formatter
	 */
	public void setTextFormatter(Formatter formatter) {
		m_formatter = formatter;
	}

	/**
	 * The additional offset is for tweaking the position of the indicator.
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		m_offset = offset;
	}

	/**
	 * Set the text color
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		m_textPaint.setColor(color);
	}

	/**
	 * Set the text size.
	 * 
	 * @param size
	 */
	public void setTextSize(float size) {
		m_textPaint.setTextSize(size);
	}

	/**
	 * Set the text bold.
	 * 
	 * @param bold
	 */
	public void setTextBold(boolean bold) {
		m_textPaint.setFakeBoldText(true);
	}

	/**
	 * Set the alignment of the text.
	 * 
	 * @param align
	 */
	public void setTextAlign(Align align) {
		m_textPaint.setTextAlign(align);
	}

	/**
	 * Set the paint object used to draw the text on to the canvas.
	 * 
	 * @param paint
	 */
	public void setPaint(TextPaint paint) {
		m_textPaint = paint;
	}
	
	public void setBarHeight(int height){
		this.getLayoutParams().height = height;
		this.invalidate();
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// if we have an indicator we need to adjust the height of the view to
		// accomodate the indicator
		if (m_indicator != null) {
			final int width = getMeasuredWidth();
			final int height = getMeasuredHeight() + getIndicatorHeight();

			// make the view the original height + indicator height size
			setMeasuredDimension(width, height);
		}
	}

	protected int getIndicatorWidth() {
		if (m_indicator == null) {
			return 0;
		}

		Rect r = m_indicator.copyBounds();
		int width = r.width();

		return width;
	}

	protected int getIndicatorHeight() {
		if (m_indicator == null) {
			return 0;
		}

		Rect r = m_indicator.copyBounds();
		int height = r.height();

		return height;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		Drawable progressDrawable = getProgressDrawable();

		// If we have an indicator then we'll need to adjust the drawable bounds
		// for
		// the progress bar and its layers (if the drawable is a layer
		// drawable).
		// This will ensure the progress bar gets drawn in the correct position
		if (m_indicator != null) {
			if (progressDrawable != null
					&& progressDrawable instanceof LayerDrawable) {
				LayerDrawable d = (LayerDrawable) progressDrawable;

				for (int i = 0; i < d.getNumberOfLayers(); i++) {
					d.getDrawable(i).getBounds().top = getIndicatorHeight();

					// thanks to Dave [dave@pds-uk.com] for point out a bug
					// which eats up
					// a lot of cpu cycles. It turns out the issue was linked to
					// calling
					// getIntrinsicHeight which proved to be very cpu intensive.
					d.getDrawable(i).getBounds().bottom = d.getDrawable(i)
							.getBounds().height()
							+ getIndicatorHeight();
				}
			} else if (progressDrawable != null) {
				// It's not a layer drawable but we still need to adjust the
				// bounds
				progressDrawable.getBounds().top = m_indicator
						.getIntrinsicHeight();
				// thanks to Dave[dave@pds-uk.com] -- see note above for
				// explaination.
				progressDrawable.getBounds().bottom = progressDrawable
						.getBounds().height() + getIndicatorHeight();
			}
		}

		// update the size of the progress bar and overlay
		updateProgressBar();

		super.onDraw(canvas);

		// Draw the indicator to match the far right position of the progress
		// bar
		if (m_indicator != null) {
			canvas.save();
//			int dx = 0;
//
//			// get the position of the progress bar's right end
//			if (progressDrawable != null
//					&& progressDrawable instanceof LayerDrawable) {
//				LayerDrawable d = (LayerDrawable) progressDrawable;
//				Drawable progressBar = d.findDrawableByLayerId(R.id.progress);
//				dx = progressBar.getBounds().right;
//			} else if (progressDrawable != null) {
//				dx = progressDrawable.getBounds().right;
//			}
//
//			// adjust for any additional offset
//			dx = dx - getIndicatorWidth() / 2 - m_offset + getPaddingLeft();
//
//			// translate the canvas to the position where we should draw the
//			// indicator
			canvas.translate((progressDrawable.getBounds().right-progressDrawable.getBounds().left)/2 , /*getLayoutParams().height/2 + getIndicatorHeight()*3/4*/0);

			m_indicator.draw(canvas);

			canvas.drawText(
					m_formatter != null ? m_formatter.getText(getProgress())
							: Math.round(getScale(getProgress()) * 100.0f)
									+ "%", getIndicatorWidth() / 2,
					getIndicatorHeight() / 2 + 1, m_textPaint);

			// restore canvas to original
			canvas.restore();
		}
	}

	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);

		// the setProgress super will not change the details of the progress bar
		// anymore so we need to force an update to redraw the progress bar
		invalidate();
	}

	protected float getScale(int progress) {
		float scale = getMax() > 0 ? (float) progress / (float) getMax() : 0;

		return scale;
	}

	/**
	 * Instead of using clipping regions to uncover the progress bar as the
	 * progress increases we increase the drawable regions for the progress bar
	 * and pattern overlay. Doing this gives us greater control and allows us to
	 * show the rounded cap on the progress bar.
	 */
	protected void updateProgressBar() {
		Drawable progressDrawable = getProgressDrawable();

		if (progressDrawable != null
				&& progressDrawable instanceof LayerDrawable) {
			LayerDrawable d = (LayerDrawable) progressDrawable;

			final float scale = getScale(getProgress());

			// get the progress bar and update it's size
			Drawable progressBar = d.findDrawableByLayerId(R.id.progress);

			final int width = d.getBounds().right - d.getBounds().left;

			if (progressBar != null) {
				Rect progressBarBounds = progressBar.getBounds();
				progressBarBounds.right = progressBarBounds.left
						+ (int) (width * scale + 0.5f);
				progressBar.setBounds(progressBarBounds);
			}

			// get the pattern overlay
			Drawable patternOverlay = d.findDrawableByLayerId(R.id.pattern);

			if (patternOverlay != null) {
				if (progressBar != null) {
					// we want our pattern overlay to sit inside the bounds of
					// our progress bar
					Rect patternOverlayBounds = progressBar.copyBounds();
					final int left = patternOverlayBounds.left;
					final int right = patternOverlayBounds.right;

					patternOverlayBounds.left = (left + 1 > right) ? left
							: left + 1;
					patternOverlayBounds.right = (right > 0) ? right - 1
							: right;
					patternOverlay.setBounds(patternOverlayBounds);
				} else {
					// we don't have a progress bar so just treat this like the
					// progress bar
					Rect patternOverlayBounds = patternOverlay.getBounds();
					patternOverlayBounds.right = patternOverlayBounds.left
							+ (int) (width * scale + 0.5f);
					patternOverlay.setBounds(patternOverlayBounds);
				}
			}
		}
	}

	/**
	 * You must implement this interface if you wish to present a custom
	 * formatted text to be used by the Progress Indicator. The default format
	 * is X% where X [0,100]
	 * 
	 * @author jsaund
	 * 
	 */
	public interface Formatter {
		public String getText(int progress);
	}
	

	
}
