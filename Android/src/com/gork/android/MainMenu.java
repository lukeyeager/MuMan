package com.gork.android;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] entries = new String[] { "Play", "Exit" };
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				entries));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CharSequence entry = ((TextView) view).getText();
				
				if (entry == "Exit") {
					finish();
				} else {
					// When clicked, show a toast with the TextView text
					Toast.makeText(getApplicationContext(), entry, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}