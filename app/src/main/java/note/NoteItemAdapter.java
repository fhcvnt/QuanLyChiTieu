package note;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import naruto.hinata.quanlychitieu.R;
import sqlite.Database;

public class NoteItemAdapter extends BaseAdapter {
    private List<NoteItem> datanotelist;
    private int layout;
    private Context context;

    public NoteItemAdapter(List<NoteItem> datanotelist, int layout, Context context) {
        this.datanotelist = datanotelist;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datanotelist.size();
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
        private ImageView imagegroupname;
        private TextView groupname;
        private TextView datetime;
        private TextView money;
        private TextView note;
        private TextView countday;
        private ImageView dau3cham;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder holder;
        if (view == null) {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imagegroupname = (ImageView) view.findViewById(R.id.listview_item_notelist_imageView_groupname);
            holder.groupname = (TextView) view.findViewById(R.id.listview_item_notelist_textView_groupname);
            holder.datetime = (TextView) view.findViewById(R.id.listview_item_notelist_textView_datetime);
            holder.money = (TextView) view.findViewById(R.id.listview_item_notelist_textView_money);
            holder.note = (TextView) view.findViewById(R.id.listview_item_notelist_textView_note);
            holder.countday = (TextView) view.findViewById(R.id.listview_item_notelist_textView_countday);
            holder.dau3cham = (ImageView) view.findViewById(R.id.listview_item_notelist_imageView_dau3cham);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }

        NoteItem item = datanotelist.get(i);
        holder.imagegroupname.setImageResource(item.getImagegroupname());
        holder.groupname.setText(item.getGroupname());
        holder.datetime.setText(item.getDatetime());
        holder.money.setText(item.getMoney());
        holder.note.setText(item.getNote());
        holder.countday.setText(item.getCountday());

        // create database QuanLyChiTieu
        Database database = new Database(context.getApplicationContext(), "QuanLyChiTieu", null, 3);

        // bắt sự kiện nhấn vào imageview dấu 3 chấm
        holder.dau3cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupMenu(context, view, item, viewGroup, view, database);
            }
        });

        return view;
    }

    // Create a PopupMenu using Java.
    public void createPopupMenu(Context activity, View anchorView, NoteItem item, ViewGroup viewGroup, View view, Database database) {
        // Create a PopupMenu and anchor it on a View.
        PopupMenu popupMenu = new PopupMenu(activity, anchorView);
        Menu menu = popupMenu.getMenu();

        // groupId, itemId, order, title
        MenuItem menuItemEdit = menu.add(1, 1, 1, "Sửa");
        MenuItem menuItemDelete = menu.add(2, 2, 2, "Xóa");

        // Edit
        menuItemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.setClass(context, NoteEdit.class);
                intent.putExtra("idnote", item.getId()); // truyen id qua NoteEdit thi ket noi sql de lay du lieu
                ((Activity)context).startActivityForResult(intent,3);

                return false;
            }

        });

        // Delete
        menuItemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                try {
                    // Hiển thị thông báo bạn có muốn xóa không?
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Xác nhận");
                    dialog.setMessage("Bạn có muốn xóa ghi chú không?");
                    dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Xóa dữ liệu
                            String delete = "DELETE FROM Note WHERE Id=" + item.getId();
                            database.QueryData(delete);
                            viewGroup.removeViewInLayout(view);
                            datanotelist.remove(item);
                            notifyDataSetChanged();
                        }
                    });
                    dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    dialog.show();
                } catch (Exception ex) {

                }
                return true;
            }
        });
        popupMenu.show();
    }
}
