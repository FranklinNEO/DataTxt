package com.neo.datatxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.neo.datatxt.SideBar.OnTouchingLetterChangedListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		OnTouchingLetterChangedListener, OnItemClickListener {
	private ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private MyAdapter adapter = null;
	private ListView list = null;
	private String Search = null;
	private Long start, finish;
	private SideBar sideBar;
	private TextView letterTv;
	private static final int MENU_LIST_RECENT = Menu.FIRST;
	private static final int MENU_LIST_ALL = Menu.FIRST + 1;
	public final static String URL = "/data/data/com.neo.datatxt/databases";
	public final static String DB_FILE_NAME = "info.db";
	SQLiteDatabase db = null;
	private boolean isList = false;
	private boolean isexist = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView) findViewById(R.id.listView1);
		list.setOnItemClickListener(this);
		letterTv = (TextView) findViewById(R.id.letterTv);
		new AsyncTask<Integer, Integer, String[]>() {

			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}

			protected String[] doInBackground(Integer... params) {
				start = System.currentTimeMillis();
				loadLocationFile();
				return null;
			}

			protected void onPostExecute(String[] result) {
				finish = System.currentTimeMillis();
				Toast.makeText(getApplicationContext(),
						"载入数据库完成" + (finish - start), Toast.LENGTH_SHORT)
						.show();
				Collections.sort(data, new AbbrComparator());
				ListView list = (ListView) findViewById(R.id.listView1);
				adapter = new MyAdapter(MainActivity.this);
				list.setAdapter(adapter);
				super.onPostExecute(result);
			}
		}.execute(0);

		sideBar = (SideBar) findViewById(R.id.mySideBar);
		sideBar.setOnTouchingLetterChangedListener(this);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText edit = (EditText) findViewById(R.id.editText1);
				Search = edit.getText().toString().replaceAll(",", "");
				if (Search == null || Search.equals("")) {
					Toast.makeText(MainActivity.this,
							"please enter the key words", Toast.LENGTH_LONG)
							.show();
				} else {
					new AsyncTask<Integer, Integer, String[]>() {

						protected void onPreExecute() {
							super.onPreExecute();
						}

						@Override
						protected void onCancelled() {
							super.onCancelled();
						}

						protected String[] doInBackground(Integer... params) {
							start = System.currentTimeMillis();
							Searchfile(Search);
							return null;
						}

						protected void onPostExecute(String[] result) {
							finish = System.currentTimeMillis();
							Toast.makeText(getApplicationContext(),
									"载入数据库完成" + (finish - start),
									Toast.LENGTH_SHORT).show();
							Collections.sort(data, new AbbrComparator());
							ListView list = (ListView) findViewById(R.id.listView1);
							adapter = new MyAdapter(MainActivity.this);
							list.setAdapter(adapter);
							super.onPostExecute(result);
						}
					}.execute(0);
				}
			}
		});
	}

	protected void Searchfile(String sear) {
		// TODO Auto-generated method stub
		Resources res = this.getResources();
		InputStream in = null;
		BufferedReader br = null;
		data = new ArrayList<HashMap<String, String>>();
		try {
			in = res.openRawResource(R.raw.customers);
			String str;
			br = new BufferedReader(new InputStreamReader(in, "GBK"));
			try {

				while ((str = br.readLine()) != null) {
					if (str.contains(sear.toUpperCase())) {
						String names = str.split(",")[0];
						String abbr = str.split(",")[1];
						String code = str.split(",")[2];
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("name", names);
						map.put("abbr", abbr);
						map.put("code", code);
						data.add(map);
					}
				}
			} finally {
			}

		} catch (NotFoundException e) {
			Toast.makeText(this, "文本文件不存在", 100).show();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(this, "文本编码出现异常", 100).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "文件读取错误", 100).show();
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	protected void loadLocationFile() {
		// TODO Auto-generated method stub
		Resources res = this.getResources();
		InputStream in = null;
		BufferedReader br = null;
		data = new ArrayList<HashMap<String, String>>();
		try {
			in = res.openRawResource(R.raw.customers);
			String str;
			br = new BufferedReader(new InputStreamReader(in, "GBK"));
			try {
				while ((str = br.readLine()) != null) {
					String names = str.split(",")[0];
					String abbr = str.split(",")[1];
					String code = str.split(",")[2];
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("name", names);
					map.put("abbr", abbr);
					map.put("code", code);
					data.add(map);
				}
			} finally {
			}

		} catch (NotFoundException e) {
			Toast.makeText(this, "文本文件不存在", 100).show();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(this, "文本编码出现异常", 100).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(this, "文件读取错误", 100).show();
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public final class ViewHolder {
		public TextView nameTextView;
		public TextView abbrTextView;
		public TextView codeTextView;
		public TextView catalog;
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listitem, null);
				holder = new ViewHolder();
				holder.nameTextView = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.abbrTextView = (TextView) convertView
						.findViewById(R.id.textView2);
				holder.codeTextView = (TextView) convertView
						.findViewById(R.id.textView3);
				holder.catalog = (TextView) convertView
						.findViewById(R.id.catalogTv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String catalog = (data.get(position).get("abbr"));
			if (catalog == null || catalog.equals("")) {
				catalog = "#";
			} else {
				catalog = catalog.substring(0, 1);
			}
			if (isList) {
				holder.catalog.setVisibility(View.GONE);
			} else {
				if (position == 0) {
					holder.catalog.setVisibility(View.VISIBLE);
					holder.catalog.setText(catalog);
				} else {
					String lastCatalog = data.get(position - 1).get("abbr");
					if (lastCatalog == null || lastCatalog.equals("")) {
						lastCatalog = "#";
					} else {
						lastCatalog = lastCatalog.substring(0, 1);
					}
					if (catalog.equals(lastCatalog)) {
						holder.catalog.setVisibility(View.GONE);
					} else {
						holder.catalog.setVisibility(View.VISIBLE);
						holder.catalog.setText(catalog);
					}
				}
			}
			holder.nameTextView.setText(data.get(position).get("name"));
			holder.abbrTextView.setText(data.get(position).get("abbr"));
			holder.codeTextView.setText(data.get(position).get("code"));
			return convertView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_LIST_RECENT, 0, "显示最多查询");
		menu.add(0, MENU_LIST_ALL, 0, "显示所有信息");
		return super.onCreateOptionsMenu(menu);
	}

	private Handler _handler = new Handler();
	private Runnable letterThread = new Runnable() {
		public void run() {
			letterTv.setVisibility(View.GONE);
		}
	};

	@Override
	public void onTouchingLetterChanged(String s) {
		// TODO Auto-generated method stub
		letterTv.setText(s);
		letterTv.setVisibility(View.VISIBLE);
		_handler.removeCallbacks(letterThread);
		_handler.postDelayed(letterThread, 1000);
		if (alphaIndexer(s) > 0) {
			int position = alphaIndexer(s);
			list.setSelection(position);
		}
	}

	private int alphaIndexer(String s) {
		// TODO Auto-generated method stub
		int position = 0;
		for (int i = 0; i < data.size(); i++) {

			String py = (String) data.get(i).get("abbr");
			if (py.startsWith(s)) {
				position = i;
				break;
			}
		}
		return position;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		String Names = data.get(position).get("name");
		String Abbrs = data.get(position).get("abbr");
		String Codes = data.get(position).get("code");
		Intent dataIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("data_name", Names);
		bundle.putString("data_abbr", Abbrs);
		bundle.putString("data_code", Codes);
		dataIntent.putExtras(bundle);
		dataIntent.setClass(MainActivity.this, Marking.class);
		startActivity(dataIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case MENU_LIST_RECENT:
			new AsyncTask<Integer, Integer, String[]>() {

				protected void onPreExecute() {
					super.onPreExecute();
				}

				@Override
				protected void onCancelled() {
					super.onCancelled();
				}

				protected String[] doInBackground(Integer... params) {
					File file = new File(URL, DB_FILE_NAME);
					if (file.exists()) {
						db = SQLiteDatabase.openOrCreateDatabase(file, null);
						String sql = "SELECT * FROM info_date WHERE times>0 ORDER BY times DESC LIMIT 0,20";
						Cursor cur = db.rawQuery(sql, null);
						Log.d("search", "finish");
						data = new ArrayList<HashMap<String, String>>();
						if (cur != null && cur.moveToFirst()) {
							do {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("name", cur.getString(cur
										.getColumnIndex("name")));
								map.put("abbr", cur.getString(cur
										.getColumnIndex("abbr")));
								map.put("code", cur.getString(cur
										.getColumnIndex("code")));
								data.add(map);
							} while ((cur.moveToNext()));
							Log.d("Loading", "finish");
							cur.close();
							db.close();
						} else {
							cur.close();
							db.close();
							Toast.makeText(MainActivity.this, "null",
									Toast.LENGTH_SHORT).show();
						}
						isexist = true;
					} else {
						isexist = false;
					}

					return null;
				}

				protected void onPostExecute(String[] result) {
					if (isexist) {
						// Collections.sort(data, new AbbrComparator());
						ListView list = (ListView) findViewById(R.id.listView1);
						adapter = new MyAdapter(MainActivity.this);
						isList = true;
						list.setAdapter(adapter);
					}

					super.onPostExecute(result);
				}
			}.execute(0);

			return true;
		case MENU_LIST_ALL:
			new AsyncTask<Integer, Integer, String[]>() {

				protected void onPreExecute() {
					super.onPreExecute();
				}

				@Override
				protected void onCancelled() {
					super.onCancelled();
				}

				protected String[] doInBackground(Integer... params) {
					start = System.currentTimeMillis();
					loadLocationFile();
					return null;
				}

				protected void onPostExecute(String[] result) {
					finish = System.currentTimeMillis();
					Toast.makeText(getApplicationContext(),
							"载入数据库完成" + (finish - start),
							Toast.LENGTH_SHORT).show();
					Collections.sort(data, new AbbrComparator());
					ListView list = (ListView) findViewById(R.id.listView1);
					adapter = new MyAdapter(MainActivity.this);
					isList = false;
					list.setAdapter(adapter);
					super.onPostExecute(result);
				}
			}.execute(0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
