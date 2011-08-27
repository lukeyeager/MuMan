package com.muman.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopupView extends RelativeLayout {

	public PopupView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void createLoseGamePopup() {
		removeAllViews();
		
		TextView text = new TextView(getContext());
		text.setGravity(ALIGN_PARENT_TOP);
		text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		text.setText("LOSER!");
		addView(text);
		
		setVisibility(VISIBLE);
	}
	
	public void createWinGamePopup() {
		removeAllViews();
		
		TextView text = new TextView(getContext());
		text.setGravity(ALIGN_PARENT_TOP);
		text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		text.setText("WINNER!");
		addView(text);
		
		setVisibility(VISIBLE);
	}

	public void createPauseGamePopup() {
		removeAllViews();
		
		TextView text = new TextView(getContext());
		text.setGravity(ALIGN_PARENT_TOP);
		text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		text.setText("PAUSE!");
		addView(text);
		
		setVisibility(VISIBLE);
	}
}
