package group;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import naruto.hinata.quanlychitieu.R;
import sqlite.Database;

public class GroupItemAdapter extends BaseAdapter {
    private List<GroupItem> listdataGroupitem;
    private int layout;
    private Context context;

    public GroupItemAdapter(List<GroupItem> listdataGroupitem, int layout, Context context) {
        this.listdataGroupitem = listdataGroupitem;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listdataGroupitem.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgHinhanh;
        TextView tennhom;
        ImageView imgHinhanhxoa;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgHinhanh = (ImageView) view.findViewById(R.id.listview_item_groupname_hinhanh);
            holder.tennhom = (TextView) view.findViewById(R.id.listview_item_groupname_tennhom);
            holder.imgHinhanhxoa = (ImageView) view.findViewById(R.id.listview_item_groupname_hinhxoa);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GroupItem item = listdataGroupitem.get(i);
        holder.imgHinhanh.setImageResource(item.getImageicon());
        holder.tennhom.setText(item.getGroupname());
        holder.imgHinhanhxoa.setImageResource(item.getImagedelete());

        // create database QuanLyChiTieu
        Database database = new Database(context.getApplicationContext(), "QuanLyChiTieu", null, 3);

        // su kien nhan nut xoa
        //--------------------------------------------------------------------------------------------------------------------------------------------------------
        holder.imgHinhanhxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Hiển thị thông báo bạn có muốn xóa không?
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Xác nhận");
                    dialog.setMessage("Bạn có muốn xóa nhóm: " + item.getGroupname() + " không?");
                    dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Xóa dữ liệu
                            database.QueryData("DELETE FROM Note WHERE GroupId=" + item.getGroupid());
                            database.QueryData("DELETE FROM GroupName WHERE Id=" + item.getGroupid());
                            viewGroup.removeViewInLayout(view);
                            listdataGroupitem.remove(item);
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
            }
        });

        //--------------------------------------------------------------------------------------------------------------------------------------------------------
        // sự kiện sửa khi long click vào icon của group name
        holder.imgHinhanh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, GroupEdit.class);
                intent.putExtra("groupid", item.getGroupid());
                ((Activity) context).startActivityForResult(intent, 4);

                return true;
            }
        });

        return view;
    }
}
