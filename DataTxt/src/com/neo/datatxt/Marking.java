package com.neo.datatxt;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Marking extends Activity implements OnClickListener {
	public final static String URL = "/data/data/com.neo.datatxt/databases";
	public final static String DB_FILE_NAME = "info.db";
	SQLiteDatabase db = null;
	public DBHelper m_db = null;
	private TextView Name;
	private TextView Abbr;
	private TextView Code;
	private Button Chose;
	private String name;
	private String abbr;
	private String code;
	private int times;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marking);

		m_db = DBHelper.getInstance(this);

		Name = (TextView) findViewById(R.id.textname);
		Abbr = (TextView) findViewById(R.id.textabbr);
		Code = (TextView) findViewById(R.id.textcode);
		Bundle bundle = this.getIntent().getExtras();
		name = bundle.getString("data_name");
		abbr = bundle.getString("data_abbr");
		code = bundle.getString("data_code");
		Name.setText(name);
		Abbr.setText(abbr);
		Code.setText(code);
		Chose = (Button) findViewById(R.id.bt_yes);
		Chose.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bt_yes:
			File file = new File(URL, DB_FILE_NAME);
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
			String sql = "SELECT * FROM info_date WHERE name='" + name
					+ "' AND abbr='" + abbr + "' AND code='" + code + "';";
			Cursor cur = db.rawQuery(sql, null);
			Log.d("search", "finish");
			if (cur != null && cur.moveToFirst()) {
				do {
					times = cur.getInt(cur.getColumnIndex("times"));
				} while ((cur.moveToNext()));
				Log.d("Loading", "finish");
				cur.close();
				db.close();
				try {
					m_db.update(DBHelper.TABLE_NAME, name, abbr, code, times);
					Toast.makeText(getApplicationContext(), "update",
							Toast.LENGTH_SHORT).show();
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), "failed",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				cur.close();
				db.close();
				m_db.insert(DBHelper.TABLE_NAME, name, abbr, code);
				Toast.makeText(getApplicationContext(), "null",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
