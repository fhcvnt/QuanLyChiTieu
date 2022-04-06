package note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

import account.Account;
import data_function.ReadAndWrite;
import group.GroupList;
import naruto.hinata.quanlychitieu.R;
import report.Report;
import sqlite.Database;

public class NoteList extends AppCompatActivity {
    private ImageView imggroup;
    private TextView textgroup;
    private ImageView imgreport;
    private TextView textviewreport;
    private ImageView imgaccount;
    private TextView textviewaccount;

    private ArrayList<NoteItem> arraynoteitem;
    private NoteItemAdapter adapternoteitem;
    private ListView listviewNotelist;
    private FloatingActionButton btncreate;
    private Database database;

    private MenuItem menuitem_grouplist;
    private MenuItem menuitem_thu;
    private MenuItem menuitem_chi;
    private MenuItem menuitem_chomuon;
    private MenuItem menuitem_no;
    private int check_item = 1; // đang chọn menu nào? 0: thu, 1: chi, 2: cho mượn, 3: nợ
    ArrayList<String> arraygroupdata; // danh sách các tên nhóm trong thu, chi, cho mượn, nợ
    private float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        getSupportActionBar().setTitle("Ghi chú");
        // đổi màu nền cho status bar (thanh chứa giờ điện thoại)
        getWindow().setStatusBarColor(Color.BLACK);

        AnhXa();

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện khi nhấn FloatingActionButton tạo
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NoteAdd.class);
                startActivityForResult(intent, 2);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // su kien long click FloatingActionButton tao
        btncreate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // ẩn đi FloatingActionButton tạo
                btncreate.hide();
                return true;
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở cửa sổ nhóm khi nhấn vào hình ảnh nhóm
        imggroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), GroupList.class);
                startActivity(intent);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở cửa sổ nhóm khi nhấn vào text nhóm
        textgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), GroupList.class);
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
    // tạo menu
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notelist, menu);
        menuitem_grouplist = menu.getItem(1);
        menuitem_thu = menu.getItem(2);
        menuitem_chi = menu.getItem(3);
        menuitem_chomuon = menu.getItem(4);
        menuitem_no = menu.getItem(5);

        return super.onCreateOptionsMenu(menu);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // bắt sự kiện trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_notelist_search) {
            // menu search
            Intent intent = new Intent();
            intent.setClass(this, NoteSearch.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_notelist_grouplist) {
            // select data
            String sqlselect = "SELECT GroupName FROM GroupName WHERE Type=" + check_item + " ORDER BY lower(GroupName) ASC";
            arraygroupdata.clear();
            arraygroupdata.add("-Tất cả-");
            Cursor dataGroupName = database.GetData(sqlselect);
            while (dataGroupName.moveToNext()) {
                try {
                    arraygroupdata.add(dataGroupName.getString(0));
                } catch (Exception ex) {

                }
            }
            String[] grouplist = new String[arraygroupdata.size()];

            for (int i = 0; i < arraygroupdata.size(); i++) {
                grouplist[i] = arraygroupdata.get(i);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Danh sách nhóm");
            builder.setItems(grouplist, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                    if (which == 0) {
                        setDataListViewFromSQL(check_item, "");
                    } else {
                        setDataListViewFromSQL(check_item, " AND GroupName.GroupName='" + grouplist[which] + "'");
                    }
                }
            });

            // hiển thị dialog ngay vị trí menu vừa nhấn
            Window window = builder.show().getWindow();
            WindowManager.LayoutParams param = window.getAttributes();
            param.gravity = Gravity.TOP;
            param.y = 40;
            // param.x=100;
            // param.width = 300;
            param.width = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(param);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            //builder.show();

        } else if (item.getItemId() == R.id.menu_notelist_thu) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuitem_chi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuitem_chomuon.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuitem_no.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            check_item = 0;
            setDataListViewFromSQL(0, "");

        } else if (item.getItemId() == R.id.menu_notelist_chi) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuitem_thu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuitem_chomuon.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuitem_no.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            check_item = 1;
            setDataListViewFromSQL(1, "");

        } else if (item.getItemId() == R.id.menu_notelist_chomuon) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuitem_thu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuitem_chi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menuitem_no.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

            check_item = 2;
            setDataListViewFromSQL(2, "");

        } else if (item.getItemId() == R.id.menu_notelist_no) {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menuitem_thu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menuitem_chi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menuitem_chomuon.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

            check_item = 3;
            setDataListViewFromSQL(3, "");

        }
        btncreate.show();
        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // hàm này để nhận lại dữ liệu từ cửa sổ đã mở
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                setDataListViewFromSQL(check_item, "");
                break;
            case 3:
                setDataListViewFromSQL(check_item, "");
                break;
            default:
                setDataListViewFromSQL(check_item, "");
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void AnhXa() {
        // Hỏi quyền đọc, ghi file trên điện thoại
        ReadAndWrite readAndWrite = new ReadAndWrite(getApplicationContext(), NoteList.this);
        readAndWrite.askPermissionAndWriteFile();
        readAndWrite.askPermissionAndReadFile();

        imggroup = (ImageView) findViewById(R.id.notelist_imageView_nhom);
        textgroup = (TextView) findViewById(R.id.notelist_textView_nhom);
        imgreport = (ImageView) findViewById(R.id.notelist_imageView_baocao);
        textviewreport = (TextView) findViewById(R.id.notelist_textView_baocao);
        imgaccount = (ImageView) findViewById(R.id.notelist_imageView_taikhoan);
        textviewaccount = (TextView) findViewById(R.id.notelist_textView_taikhoan);
        btncreate = (FloatingActionButton) findViewById(R.id.notelist_floatingActionButton_tao);
        arraygroupdata = new ArrayList<>();

        arraynoteitem = new ArrayList<>();
        listviewNotelist = (ListView) findViewById(R.id.notelist_listview_danhsachghichu);

        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        // create table GroupName
        // Type - loại - 0: thu, 1: chi, 2: cho mượn, 3 nợ
        String sql_group = "CREATE TABLE IF NOT EXISTS GroupName(Id INTEGER PRIMARY KEY NOT NULL, GroupName NVARCHAR (50) UNIQUE NOT NULL,Type INTEGER NOT NULL,Icon INTEGER)";
        database.QueryData(sql_group);

        // Tạo table Note nếu chưa tồn tại
        // Type - loại - 0: thu, 1: chi, 2: cho mượn, 3 nợ
        String sql = "CREATE TABLE IF NOT EXISTS Note(Id INTEGER PRIMARY KEY NOT NULL,Money INTEGER NOT NULL,Type INTEGER NOT NULL,GroupId INTEGER NOT NULL, Note NVARCHAR (100) NULL, Ngay VARCHAR (10) NOT NULL, Gio VARCHAR(10) NOT NULL,FOREIGN KEY(GroupId) REFERENCES GroupName(Id))";
        database.QueryData(sql);

        adapternoteitem = new NoteItemAdapter(arraynoteitem, R.layout.listview_item_notelist, this);
        // bat dau thi ta se seclect du lieu cua chi
        setDataListViewFromSQL(1, "");

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lay du lieu tu sqlite xuong
    public void setDataListViewFromSQL(int type, String where_groupname) {
        // String select = "SELECT Note.Id,Icon,GroupName,Ngay,Gio,Money,Note FROM Note,GroupName WHERE Note.GroupId=GroupName.Id AND Note.Type=" + type + where_groupname + " ORDER BY substr(Ngay,7,11)||substr(Ngay,4,6)||substr(Ngay,1,2) DESC, Gio DESC";

        // lấy điều kiện hiển thị theo số ngày được cấu hình trong cài đặt
        String where_ngay = "";
        //where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + ngaybatdau.substring(6) + ngaybatdau.substring(3, 5) + ngaybatdau.substring(0, 2) + "' AND '" + ngayketthuc.substring(6) + ngayketthuc.substring(3, 5) + ngayketthuc.substring(0, 2) + "')";
        String number = "";
        String timetype = "";
        try {
            Cursor result_setting = database.GetData("SELECT Number,TimeType FROM Setting");
            while (result_setting.moveToNext()) {
                number = result_setting.getString(0);
                timetype = result_setting.getString(1);
            }
        } catch (Exception ex) {
        }

        if (timetype.equals("D")) {
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN strftime('%Y%m%d','now','-" + number + " day') AND strftime('%Y%m%d','now'))";

        } else if (timetype.equals("M")) {
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN strftime('%Y%m%d','now','-" + number + " month') AND strftime('%Y%m%d','now'))";
        } else if (timetype.equals("Y")) {
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN strftime('%Y%m%d','now','-" + number + " year') AND strftime('%Y%m%d','now'))";
        }
        String select = "SELECT Note.Id,Icon,GroupName,Ngay,Gio,Money,Note FROM Note,GroupName WHERE Note.GroupId=GroupName.Id AND Note.Type=" + type + where_groupname + where_ngay + " ORDER BY substr(Ngay,7,11)||substr(Ngay,4,6)||substr(Ngay,1,2) DESC, Gio DESC";

        // xoa du lieu trong listview cu
        arraynoteitem.clear();
        listviewNotelist.removeAllViewsInLayout();
        Cursor result = database.GetData(select);
        while (result.moveToNext()) {
            try {
                arraynoteitem.add(new NoteItem(Integer.parseInt(result.getString(0)), Integer.parseInt(result.getString(1)), result.getString(2), result.getString(3) + " " + result.getString(4), formatNumber(result.getString(5)) + " đ", result.getString(6), countDate(result.getString(3))));
            } catch (Exception ex) {

            }
        }
        listviewNotelist.setAdapter(adapternoteitem);
        //Toast.makeText(getApplicationContext(), "Number="+number+", TimeType="+timetype, Toast.LENGTH_SHORT).show();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // định dạng tiền tệ
    public String formatMoney(String tien) {

        long vnd = 0L;
        try {
            vnd = Long.parseLong(tien);
        } catch (Exception ex) {
        }

        // tạo 1 NumberFormat để định dạng tiền tệ theo tiêu chuẩn của Việt Nam
        // đơn vị tiền tệ của Việt Nam là đồng
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String money = currencyVN.format(vnd);

        return money;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // định dạng số
    public String formatNumber(String tien) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);

        long vnd = 0L;
        try {
            vnd = Long.parseLong(tien);
        } catch (Exception ex) {
        }
        String money = vn.format(vnd);

        return money;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // tính số ngày từ ngày lập ghi chú tới hiện tại
    public String countDate(String date_note) {
        String result = "";
        try {
            String[] datenote = date_note.split("/");
            int yearnote = Integer.parseInt(datenote[2]);
            int monthnote = Integer.parseInt(datenote[1]);
            int daynote = Integer.parseInt(datenote[0]);

            LocalDate dateBefore = LocalDate.of(yearnote, monthnote, daynote);
            //29-July-2017, change this to your desired End Date
            LocalDate dateAfter = LocalDate.of(2017, Month.JULY, 29);
            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, LocalDate.now());
            if (noOfDaysBetween == 0) {
                result = "Hôm nay";
            } else if (noOfDaysBetween == 1) {
                result = "Hôm qua";
            } else if (noOfDaysBetween > 1) {
                result = noOfDaysBetween + " ngày trước";
            } else if (noOfDaysBetween == -1) {
                result = "Ngày mai";
            } else {
                result = (-noOfDaysBetween) + " ngày tới";
            }

        } catch (Exception ex) {
        }

        return result;
    }

}