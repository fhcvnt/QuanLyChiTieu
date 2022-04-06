package group;

import static naruto.hinata.quanlychitieu.R.id.imageView_icon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import naruto.hinata.quanlychitieu.R;

public class CustomGridAdapterIcons extends BaseAdapter {
    private List<Icons> listDataIcon;
    private int layout;
    private Context context;
    private int count=0;

    public CustomGridAdapterIcons(List<Icons> listDataIcon, int layout, Context context) {
        this.listDataIcon = listDataIcon;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listDataIcon.size();
    }

    @Override
    public Object getItem(int i) {
        return listDataIcon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView imgHinh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgHinh = (ImageView) view.findViewById(imageView_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            if (i == 0&& count==0) {
                count++;
                view.setBackgroundResource(R.drawable.border_gridview_image_select);
            }
        }
        Icons hinhanh = listDataIcon.get(i);
        holder.imgHinh.setImageResource(hinhanh.getIcon());

        return view;
    }
}
