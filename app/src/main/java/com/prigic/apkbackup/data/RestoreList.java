package com.prigic.apkbackup.data;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prigic.apkbackup.R;
import com.prigic.apkbackup.data.Restore;

import java.util.ArrayList;
import java.util.List;

public class RestoreList extends BaseAdapter {

    private List<Restore> original_items = new ArrayList<>();
    private List<Restore> filtered_items = new ArrayList<>();
    private Context contex;
    private ItemFilter mFilter = new ItemFilter();

    private LayoutInflater l_Inflater;

    public RestoreList(Context context, List<Restore> items) {
        this.contex = context;
        original_items = items;
        filtered_items = items;
        l_Inflater = LayoutInflater.from(context);
    }

    public void setSelected(int position, boolean value) {
        filtered_items.get(position).setChecked(value);
    }

    public List<Restore> getSelected() {
        List<Restore> selected_item = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            if (filtered_items.get(i).isChecked()) {
                selected_item.add(filtered_items.get(i));
            }
        }
        return selected_item;
    }

    public void resetSelected() {
        for (int i = 0; i < getCount(); i++) {
            filtered_items.get(i).setChecked(false);
        }
    }

    public void selectAll() {
        for (int i = 0; i < getCount(); i++) {
            filtered_items.get(i).setChecked(true);
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return filtered_items.size();
    }

    public Restore getItem(int position) {
        return filtered_items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.list_restore, null);
            holder = new ViewHolder();
            holder.txt_title = (TextView) convertView.findViewById(R.id.name);
            holder.txt_size = (TextView) convertView.findViewById(R.id.size);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_title.setText(filtered_items.get(position).getName());
        holder.txt_size.setText(Formatter.formatFileSize(contex, filtered_items.get(position).getApp_memory()));
        holder.image.setImageDrawable(filtered_items.get(position).getIcon());
        return convertView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    static class ViewHolder {
        ImageView image;
        TextView txt_title;
        TextView txt_size;
    }


    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Restore> list = original_items;
            final List<Restore> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).getName();
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<Restore>) results.values;
            notifyDataSetChanged();
        }
    }

}