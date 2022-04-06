package report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import naruto.hinata.quanlychitieu.R;

public class ReportItemAdapter extends BaseAdapter {
    private List<ReportItem> datareportlist;
    private int layout;
    private Context context;

    public ReportItemAdapter(List<ReportItem> datareportlist, int layout, Context context) {
        this.datareportlist = datareportlist;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datareportlist.size();
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
        private TextView tieude;
        private TextView thu;
        private TextView chi;
        private TextView chomuon;
        private TextView no;
        private TextView danhsach;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ReportItemAdapter.viewHolder holder;
        if (view == null) {
            holder = new ReportItemAdapter.viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder.tieude = (TextView) view.findViewById(R.id.listview_item_report_textView_tieude);
            holder.thu = (TextView) view.findViewById(R.id.listview_item_report_textView_thu2);
            holder.chi = (TextView) view.findViewById(R.id.listview_item_report_textView_chi2);
            holder.chomuon = (TextView) view.findViewById(R.id.listview_item_report_textView_chomuon2);
            holder.no = (TextView) view.findViewById(R.id.listview_item_report_textView_no2);
            holder.danhsach = (TextView) view.findViewById(R.id.listview_item_report_textView_danhsach);
            view.setTag(holder);
        } else {
            holder = (ReportItemAdapter.viewHolder) view.getTag();
        }

        ReportItem item = datareportlist.get(i);
        holder.tieude.setText(item.getTieude());
        holder.thu.setText(item.getThu());
        holder.chi.setText(item.getChi());
        holder.chomuon.setText(item.getChomuon());
        holder.no.setText(item.getNo());
        holder.danhsach.setText(item.getGroupnamelist());

        return view;
    }
}
