package account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import naruto.hinata.quanlychitieu.R;

public class BackupAndRestoreAdapter extends BaseAdapter {
    private List<BackupAndRestoreItem> databackupandrestore;
    private int layout;
    private Context context;

    public BackupAndRestoreAdapter(List<BackupAndRestoreItem> databackupandrestore, int layout, Context context) {
        this.databackupandrestore = databackupandrestore;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return databackupandrestore.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class viewHolder {
        private ImageView icon;
        private TextView filename;
        private TextView capacity;
        private TextView date;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BackupAndRestoreAdapter.viewHolder holder;
        if (view == null) {
            holder = new BackupAndRestoreAdapter.viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder.icon = (ImageView) view.findViewById(R.id.listview_item_backupandrestore_icon);
            holder.filename = (TextView) view.findViewById(R.id.listview_item_backupandrestore_filename);
            holder.capacity = (TextView) view.findViewById(R.id.listview_item_backupandrestore_capacity);
            holder.date = (TextView) view.findViewById(R.id.listview_item_backupandrestore_date);

            view.setTag(holder);
        } else {
            holder = (BackupAndRestoreAdapter.viewHolder) view.getTag();
        }

        BackupAndRestoreItem item = databackupandrestore.get(i);
        holder.icon.setImageResource(item.getIcon());
        holder.filename.setText(item.getFilename());
        holder.capacity.setText(item.getCapacity());
        holder.date.setText(item.getDate());

        return view;
    }
}
