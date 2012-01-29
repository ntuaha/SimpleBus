package com.simplebus;



import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import android.widget.TextView;

public class InformationAdapter extends BaseAdapter{
	private LayoutInflater _mInflater;
	private Data[] data;
	private Context context;
	public InformationAdapter(Context context,Data[] data)
	{
		this.context = context;
		_mInflater =LayoutInflater.from(context);
		this.data = data;
	}

	public View getView(int position, View rowView, ViewGroup arg2)
	{
		ViewHolder holder;

		rowView = _mInflater.inflate(R.layout.result_item,null);
		holder = new ViewHolder();
		holder.title =  (TextView) rowView.findViewById(R.id.title);
		holder.content =  (TextView) rowView.findViewById(R.id.content);
		rowView.setTag(holder);

		if(data[position].status==1){
			holder.title.setAutoLinkMask(Linkify.ALL);
			holder.content.setAutoLinkMask(Linkify.ALL);
		}
		holder.title.setText(data[position].title);
		holder.content.setText(data[position].content);

		return rowView;
	}

	private class ViewHolder
	{
		TextView content;
		TextView title;	
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int arg0) {
		return data[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}
}
