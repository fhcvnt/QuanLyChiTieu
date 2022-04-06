package group;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import account.Account;
import naruto.hinata.quanlychitieu.R;
import note.NoteList;
import report.Report;
import sqlite.Database;

public class GroupList extends AppCompatActivity {

    ArrayList<GroupItem> arraygroupitem;
    GroupItemAdapter adaptergroupitem;
    ListView listviewGroupname;
    private Database database;

    private FloatingActionButton fbtntao;
    private int selectmenu = 0; // loại - 0: thu, 1: chi, 2: cho mượn, 3 nợ

    private ImageView imgnote;
    private TextView textnote;
    private ImageView imgreport;
    private TextView textviewreport;
    private ImageView imgaccount;
    private TextView textviewaccount;

    private MenuItem menuitem_thu;
    private MenuItem menuitem_chi;
    private MenuItem menuitem_chomuon;
    private MenuItem menuitem_no;

    private float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        getSupportActionBar().setTitle("Nhóm");
        // đổi màu nền cho status bar (thanh chứa giờ điện thoại)
        getWindow().setStatusBarColor(Color.BLACK);

        AnhXa();

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // su kien click FloatingActionButton tao
        fbtntao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mo cua so GroupAdd
                Intent intent = new Intent();
                intent.setClass(view.getContext(), GroupAdd.class);
                startActivityForResult(intent, 1);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // su kien long click FloatingActionButton tao
        fbtntao.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // ẩn đi FloatingActionButton tạo
                fbtntao.hide();
                return true;
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // sự kiện khi click vào image ghi chú
        imgnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), NoteList.class);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // sự kiện khi click vào text ghi chú
        textnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), NoteList.class);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở cửa sổ báo cáo khi nhấn vào hình ảnh báo cáo
        imgreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Report.class);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở cửa sổ báo cáo khi nhấn vào text báo cáo
        textviewreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Report.class);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở cửa sổ tài khoản khi nhấn vào hình ảnh tài khoản
        imgaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Account.class);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở cửa sổ tài khoản khi nhấn vào text tài khoản
        textviewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Account.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // hàm này để nhận lại dữ liệu từ cửa sổ đã mở
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                setDataListViewFromSQL(selectmenu);
                break;
            case 4:
                setDataListViewFromSQL(selectmenu);
                break;
            default:
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // tạo menu
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grouplist, menu);
        menuitem_thu = menu.getItem(0);
        menuitem_chi = menu.getItem(1);
        menuitem_chomuon = menu.getItem(2);
        menuitem_no = menu.getItem(3);

        return super.onCreateOptionsMenu(menu);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // bắt sự kiện trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_grouplist_thu) {
            item.setIcon(R.drawable.thu48select);
            menuitem_chi.setIcon(R.drawable.chi48);
            menuitem_chomuon.setIcon(R.drawable.chomuon48);
            menuitem_no.setIcon(R.drawable.no48);

            selectmenu = 0;
            setDataListViewFromSQL(selectmenu);

        } else if (item.getItemId() == R.id.menu_grouplist_chi) {
            item.setIcon(R.drawable.chi48select);
            menuitem_thu.setIcon(R.drawable.thu48);
            menuitem_chomuon.setIcon(R.drawable.chomuon48);
            menuitem_no.setIcon(R.drawable.no48);

            selectmenu = 1;
            setDataListViewFromSQL(selectmenu);

        } else if (item.getItemId() == R.id.menu_grouplist_chomuon) {
            item.setIcon(R.drawable.chomuon48select);
            menuitem_thu.setIcon(R.drawable.thu48);
            menuitem_chi.setIcon(R.drawable.chi48);
            menuitem_no.setIcon(R.drawable.no48);

            selectmenu = 2;
            setDataListViewFromSQL(selectmenu);

        } else if (item.getItemId() == R.id.menu_grouplist_no) {
            item.setIcon(R.drawable.no48select);
            menuitem_thu.setIcon(R.drawable.thu48);
            menuitem_chi.setIcon(R.drawable.chi48);
            menuitem_chomuon.setIcon(R.drawable.chomuon48);

            selectmenu = 3;
            setDataListViewFromSQL(selectmenu);

        }

        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void AnhXa() {
        listviewGroupname = (ListView) findViewById(R.id.grouplist_listview_danhsachnhom);
        fbtntao = (FloatingActionButton) findViewById(R.id.grouplist_floatingActionButton_tao);

        imgnote = (ImageView) findViewById(R.id.grouplist_imageView_ghichu);
        textnote = (TextView) findViewById(R.id.grouplist_textView_ghichu);
        imgreport = (ImageView) findViewById(R.id.grouplist_imageView_baocao);
        textviewreport = (TextView) findViewById(R.id.grouplist_textView_baocao);
        imgaccount = (ImageView) findViewById(R.id.grouplist_imageView_taikhoan);
        textviewaccount = (TextView) findViewById(R.id.grouplist_textView_taikhoan);

        arraygroupitem = new ArrayList<>();

        // create database QuanLyChiTieu
        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        // create table GroupName
        // Type - loại - 0: thu, 1: chi, 2: cho mượn, 3 nợ
        String sql = "CREATE TABLE IF NOT EXISTS GroupName(Id INTEGER PRIMARY KEY NOT NULL, GroupName NVARCHAR (50) UNIQUE NOT NULL,Type INTEGER NOT NULL,Icon INTEGER)";
        database.QueryData(sql);
        // select data
        String sqlselect = "SELECT GroupName,Icon,Id FROM GroupName WHERE Type=0 ORDER BY lower(GroupName) ASC";
        Cursor dataGroupName = database.GetData(sqlselect);
        while (dataGroupName.moveToNext()) {
            try {
                arraygroupitem.add(new GroupItem(Integer.parseInt(dataGroupName.getString(1)), dataGroupName.getString(0), R.drawable.delete_24, dataGroupName.getString(2)));
            } catch (Exception ex) {

            }
        }

        adaptergroupitem = new GroupItemAdapter(arraygroupitem, R.layout.listview_item_groupname, this);
        listviewGroupname.setAdapter(adaptergroupitem);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lay du lieu tu sqlite xuong
    public void setDataListViewFromSQL(int type) {
        //database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        // select data
        String sqlselect = "SELECT GroupName,Icon,Id FROM GroupName WHERE Type=" + type + " ORDER BY lower(GroupName) ASC";
        Cursor dataGroupName = database.GetData(sqlselect);
        // xoa du lieu trong listview cu
        arraygroupitem.clear();
        listviewGroupname.removeAllViewsInLayout();
        while (dataGroupName.moveToNext()) {
            try {
                arraygroupitem.add(new GroupItem(Integer.parseInt(dataGroupName.getString(1)), dataGroupName.getString(0), R.drawable.delete_24, dataGroupName.getString(2)));
            } catch (Exception e) {
            }
            // phai gan lai de cap nhat du lieu moi
            listviewGroupname.setAdapter(adaptergroupitem);
        }
    }
}