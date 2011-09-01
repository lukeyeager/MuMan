/*
Copyright 2011 Luke Yeager and Sam Bryan
$Id$

This file is part of MuMan.

MuMan is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

MuMan is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MuMan.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.muman.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PopupActivity extends Activity {

	public static final String TYPE = "popup_type";
	public static final int REQUEST_PAUSE = 1;
	public static final int REQUEST_WIN = 2;
	public static final int REQUEST_LOSE = 3;
	
	public static final int RESULT_ERROR = -1;
	public static final int RESULT_EXIT = 1;
	public static final int RESULT_RELOAD = 2;
	
	private Integer requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		requestCode = (extras != null) ? extras.getInt(TYPE) : null;
		if (requestCode == null) {
			finishPopup(RESULT_ERROR);
			return;
		}
		
		switch (requestCode) {
		case REQUEST_PAUSE:
			setContentView(R.layout.popup_pause);
			break;
		case REQUEST_WIN:
			setContentView(R.layout.popup_win);
			break;
		case REQUEST_LOSE:
			setContentView(R.layout.popup_lose);
			break;
		default:
			break;
		}
		

		findViewById(R.id.popup_restart).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						PopupActivity.this.finishPopup(RESULT_RELOAD);
					}
				}
			);
	}

	@Override
	public void onBackPressed() {
		finishPopup(RESULT_ERROR);
	}

	private void finishPopup(int result) {
		Intent i = new Intent(this, PopupActivity.class);
		i.putExtra(PopupActivity.TYPE, requestCode);
		setResult(result, i);
		finish();
	}

}
