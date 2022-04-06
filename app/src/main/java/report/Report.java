package report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import account.Account;
import group.GroupList;
import naruto.hinata.quanlychitieu.R;
import note.NoteList;
import note.NoteSearch;
import sqlite.Database;

public class Report extends AppCompatActivity {
    private ImageView imgnote;
    private TextView textviewnote;
    private ImageView imggroup;
    private TextView textgroup;
    private ImageView imgaccount;
    private TextView textviewaccount;

    private TextView tongthu; // tổng thu
    private TextView tongchi; // tổng chi
    private TextView sodu; // số dư

    private TextView dangchomuon; // đang cho mượn
    private TextView tongchomuon; // tổng cho mượn
    private TextView chomuondatra; // đã trả (cho mượn)

    private TextView conno; // còn nợ
    private TextView tongno; // tổng nợ
    private TextView nodatra; // nợ đã trả

    private ImageView morong_thunho; // imageview cho phép mở rộng, thu nhở giao diện
    private ConstraintLayout constraintLayout_hide_show;
    private boolean mo_rong = true;
    private boolean mo_rong_thu_nho_menu = true; //

    private ArrayList<ReportItem> arrayreportitem;
    private ReportItemAdapter adapterreporttem;
    private ListView listviewreportlist;
    private Database database;

    private final String[] arraytype = {"-Tất cả-", "Thu", "Chi", "Cho mượn", "Nợ"};
    private float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        getSupportActionBar().setTitle("Báo cáo");
        // đổi màu nền cho status bar (thanh chứa giờ điện thoại)
        getWindow().setStatusBarColor(Color.BLACK);

        AnhXa();

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
        // bắt sự kiện mở cửa sổ ghi chú khi nhấn vào hình ảnh ghi chú
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
        // bắt sự kiện mở cửa sổ ghi chú khi nhấn vào text ghi chú
        textviewnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), NoteList.class);
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

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện phóng to thu nhỏ
        morong_thunho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morong_thunho();
            }
        });

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // tạo menu
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // bắt sự kiện trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_report_search) {
            // menu search
            Intent intent = new Intent();
            intent.setClass(this, NoteSearch.class);
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_report_type_groupname) {
            displayAlertDialog();

        } else if (item.getItemId() == R.id.menu_report_thunho_phongto) {
            if (mo_rong_thu_nho_menu) {
                int height_show_hide = (int) convertDpToPx(getApplicationContext(), 0);
                constraintLayout_hide_show.setMinHeight(height_show_hide);
                constraintLayout_hide_show.setMaxHeight(height_show_hide);
                item.setIcon(R.drawable.mo_rong_30);

            } else {
                int height_show_hide = (int) convertDpToPx(getApplicationContext(), 240);
                constraintLayout_hide_show.setMinHeight(height_show_hide);
                constraintLayout_hide_show.setMaxHeight(height_show_hide);
                item.setIcon(R.drawable.thu_hep_30);
            }
            mo_rong_thu_nho_menu = !mo_rong_thu_nho_menu;

        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AnhXa() {
        imggroup = (ImageView) findViewById(R.id.report_imageView_nhom);
        textgroup = (TextView) findViewById(R.id.report_textView_nhom);
        imgnote = (ImageView) findViewById(R.id.report_imageView_ghichu);
        textviewnote = (TextView) findViewById(R.id.report_textView_ghichu);
        imgaccount = (ImageView) findViewById(R.id.report_imageView_taikhoan);
        textviewaccount = (TextView) findViewById(R.id.report_textView_taikhoan);

        tongthu = (TextView) findViewById(R.id.report_textView_tongthu2);
        tongchi = (TextView) findViewById(R.id.report_textView_tongchi2);
        sodu = (TextView) findViewById(R.id.report_textView_sodu2);

        dangchomuon = (TextView) findViewById(R.id.report_textView_dangchomuon2);
        tongchomuon = (TextView) findViewById(R.id.report_textView_tongchomuon2);
        chomuondatra = (TextView) findViewById(R.id.report_textView_chomuon_datra2);

        conno = (TextView) findViewById(R.id.report_textView_conno2);
        tongno = (TextView) findViewById(R.id.report_textView_tongno2);
        nodatra = (TextView) findViewById(R.id.report_textView_no_datra2);

        morong_thunho = (ImageView) findViewById(R.id.report_imageView_morong_thunho);
        constraintLayout_hide_show = (ConstraintLayout) findViewById(R.id.report_constraintLayout_hide_show);

        arrayreportitem = new ArrayList<>();
        listviewreportlist = (ListView) findViewById(R.id.report_listview);
        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);


        adapterreporttem = new ReportItemAdapter(arrayreportitem, R.layout.listview_item_report, this);
        setDataListViewFromSQL("", "", "", "", "");
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lay du lieu tu sqlite xuong
    public void setDataListViewFromSQL(String where_type, String where_groupname, String where_date, String ngaybatdau, String ngayketthuc) {
        if (where_date.isEmpty()) {
            String select = "SELECT Note.Id,GroupName,Ngay,Money,Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_type + where_groupname + " ORDER BY Ngay DESC, Gio DESC";
            // xoa du lieu trong listview cu
            arrayreportitem.clear();
            listviewreportlist.removeAllViewsInLayout();

            int tong_thu = 0;
            int tong_chi = 0;
            int so_du = 0;
            int dang_chomuon = 0;
            int tong_chomuon = 0;
            int datra_chomuon = 0;
            int con_no = 0;
            int tong_no = 0;
            int datra_no = 0;

            Cursor result = database.GetData(select);
            // lấy dữ liệu cho tổng thu, chi, cho mượn, nợ
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            while (result.moveToNext()) {
                try {
                    if (result.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result.getString(3));

                    } else if (result.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result.getString(3));

                    } else if (result.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result.getString(3));
                        if (Integer.parseInt(result.getString(3)) > 0) {
                            tong_chomuon = tong_chomuon + Integer.parseInt(result.getString(3));
                        } else {
                            datra_chomuon = datra_chomuon + Integer.parseInt(result.getString(3));
                        }

                    } else if (result.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result.getString(3));
                        if (Integer.parseInt(result.getString(3)) > 0) {
                            tong_no = tong_no + Integer.parseInt(result.getString(3));
                        } else {
                            datra_no = datra_no + Integer.parseInt(result.getString(3));
                        }

                    }
                } catch (Exception ex) {

                }
            }
            so_du = tong_thu - tong_chi;

            tongthu.setText(formatNumber(tong_thu + "") + " đ");
            tongchi.setText(formatNumber(tong_chi + "") + " đ");
            sodu.setText(formatNumber(so_du + "") + " đ");

            dangchomuon.setText(formatNumber(dang_chomuon + "") + " đ");
            tongchomuon.setText(formatNumber(tong_chomuon + "") + " đ");
            chomuondatra.setText((-datra_chomuon) + " đ");

            conno.setText(formatNumber(con_no + "") + " đ");
            tongno.setText(formatNumber(tong_no + "") + " đ");
            nodatra.setText(formatNumber((-datra_no) + "") + " đ");

            // lấy dữ liệu cho ngày hôm nay
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //String where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '"+ LocalDate.now().getYear()+(LocalDate.now().getMonthValue()+1)+LocalDate.now().getDayOfMonth() +"' AND '20220314')";
            String where_ngay = " AND Ngay='" + getDateDDMMYYYY() + "'";

            //String select_homnay = "SELECT Note.Id,GroupName,Ngay,Money,Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " ORDER BY Ngay DESC, Gio DESC";
            String select_homnay = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";

            String data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_homnay = database.GetData(select_homnay);
            while (result_homnay.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_homnay.getString(1) + " (" + formatNumber(result_homnay.getString(3)) + " đ)" + "\n";
                    if (result_homnay.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_homnay.getString(3));

                    } else if (result_homnay.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_homnay.getString(3));

                    } else if (result_homnay.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_homnay.getString(3));

                    } else if (result_homnay.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_homnay.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Hôm nay (" + getDateDDMMYYYY() + ")", formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu cho ngày hôm qua
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            where_ngay = " AND Ngay='" + getDateYesterdayDDMMYYYY() + "'";

            String select_homqua = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_homqua = database.GetData(select_homqua);
            while (result_homqua.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_homqua.getString(1) + " (" + formatNumber(result_homqua.getString(3)) + " đ)" + "\n";
                    if (result_homqua.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_homqua.getString(3));

                    } else if (result_homqua.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_homqua.getString(3));

                    } else if (result_homqua.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_homqua.getString(3));

                    } else if (result_homqua.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_homqua.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Hôm qua (" + getDateYesterdayDDMMYYYY() + ")", formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu trong tuần này
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //String where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '"+ LocalDate.now().getYear()+(LocalDate.now().getMonthValue()+1)+LocalDate.now().getDayOfMonth() +"' AND '20220314')";

            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + getStartWeekYYYYMMDD(0) + "' AND '" + getLastWeekYYYYMMDD(0) + "')";

            String select_trongtuan = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_trongtuan = database.GetData(select_trongtuan);
            while (result_trongtuan.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_trongtuan.getString(1) + " (" + formatNumber(result_trongtuan.getString(3)) + " đ)" + "\n";
                    if (result_trongtuan.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_trongtuan.getString(3));

                    } else if (result_trongtuan.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_trongtuan.getString(3));

                    } else if (result_trongtuan.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_trongtuan.getString(3));

                    } else if (result_trongtuan.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_trongtuan.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Trong tuần (" + getStartWeekYYYYMMDD(0).substring(6, 8) + "/" + getStartWeekYYYYMMDD(0).substring(4, 6) + " - " + getLastWeekYYYYMMDD(0).substring(6, 8) + "/" + getLastWeekYYYYMMDD(0).substring(4, 6) + ")", formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu trong tuần trước
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + getStartWeekYYYYMMDD(7) + "' AND '" + getLastWeekYYYYMMDD(7) + "')";

            String select_trongtuantruoc = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_trongtuantruoc = database.GetData(select_trongtuantruoc);
            while (result_trongtuantruoc.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_trongtuantruoc.getString(1) + " (" + formatNumber(result_trongtuantruoc.getString(3)) + " đ)" + "\n";
                    if (result_trongtuantruoc.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_trongtuantruoc.getString(3));

                    } else if (result_trongtuantruoc.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_trongtuantruoc.getString(3));

                    } else if (result_trongtuantruoc.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_trongtuantruoc.getString(3));

                    } else if (result_trongtuantruoc.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_trongtuantruoc.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Tuần trước (" + getStartWeekYYYYMMDD(7).substring(6, 8) + "/" + getStartWeekYYYYMMDD(7).substring(4, 6) + " - " + getLastWeekYYYYMMDD(7).substring(6, 8) + "/" + getLastWeekYYYYMMDD(7).substring(4, 6) + ")", formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu trong tháng
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + getMonthYYYYMMDD(0) + "' AND '" + getMonthYYYYMMDD(1) + "')";

            String select_trongthang = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_trongthang = database.GetData(select_trongthang);
            while (result_trongthang.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_trongthang.getString(1) + " (" + formatNumber(result_trongthang.getString(3)) + " đ)" + "\n";
                    if (result_trongthang.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_trongthang.getString(3));

                    } else if (result_trongthang.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_trongthang.getString(3));

                    } else if (result_trongthang.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_trongthang.getString(3));

                    } else if (result_trongthang.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_trongthang.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Tháng " + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear(), formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu trong tháng trước
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + getMonthYYYYMMDD(2) + "' AND '" + getMonthYYYYMMDD(3) + "')";

            String select_trongthangtruoc = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_trongthangtruoc = database.GetData(select_trongthangtruoc);
            while (result_trongthangtruoc.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_trongthangtruoc.getString(1) + " (" + formatNumber(result_trongthangtruoc.getString(3)) + " đ)" + "\n";
                    if (result_trongthangtruoc.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_trongthangtruoc.getString(3));

                    } else if (result_trongthangtruoc.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_trongthangtruoc.getString(3));

                    } else if (result_trongthangtruoc.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_trongthangtruoc.getString(3));

                    } else if (result_trongthangtruoc.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_trongthangtruoc.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Tháng " + LocalDate.now().minusMonths(1).getMonthValue() + "/" + LocalDate.now().minusMonths(1).getYear(), formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu trong năm
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + getMonthYYYYMMDD(4) + "' AND '" + getMonthYYYYMMDD(5) + "')";

            String select_trongnam = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_trongnam = database.GetData(select_trongnam);
            while (result_trongnam.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_trongnam.getString(1) + " (" + formatNumber(result_trongnam.getString(3)) + " đ)" + "\n";
                    if (result_trongnam.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_trongnam.getString(3));

                    } else if (result_trongnam.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_trongnam.getString(3));

                    } else if (result_trongnam.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_trongnam.getString(3));

                    } else if (result_trongnam.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_trongnam.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Năm " + LocalDate.now().getYear(), formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            // lấy dữ liệu trong năm trước
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + getMonthYYYYMMDD(6) + "' AND '" + getMonthYYYYMMDD(7) + "')";

            String select_trongnamtruoc = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_ngay + where_type + where_groupname + " GROUP BY GroupName";
            data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_trongnamtruoc = database.GetData(select_trongnamtruoc);
            while (result_trongnamtruoc.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_trongnamtruoc.getString(1) + " (" + formatNumber(result_trongnamtruoc.getString(3)) + " đ)" + "\n";
                    if (result_trongnamtruoc.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_trongnamtruoc.getString(3));

                    } else if (result_trongnamtruoc.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_trongnamtruoc.getString(3));

                    } else if (result_trongnamtruoc.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_trongnamtruoc.getString(3));

                    } else if (result_trongnamtruoc.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_trongnamtruoc.getString(3));

                    }
                } catch (Exception ex) {

                }
            }
            arrayreportitem.add(new ReportItem("Năm " + (LocalDate.now().getYear() - 1), formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));

            listviewreportlist.setAdapter(adapterreporttem);
        } else {
            // xoa du lieu trong listview cu
            arrayreportitem.clear();
            listviewreportlist.removeAllViewsInLayout();

            int tong_thu = 0;
            int tong_chi = 0;
            int dang_chomuon = 0;
            int con_no = 0;

            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            String select_homnay = "SELECT Note.Id,GroupName,Ngay,SUM(Money),Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id " + where_date + where_type + where_groupname + " GROUP BY GroupName";

            String data_list_groupname = "";
            tong_thu = 0;
            tong_chi = 0;
            dang_chomuon = 0;
            con_no = 0;
            Cursor result_homnay = database.GetData(select_homnay);
            while (result_homnay.moveToNext()) {
                try {
                    data_list_groupname = data_list_groupname + result_homnay.getString(1) + " (" + formatNumber(result_homnay.getString(3)) + " đ)" + "\n";
                    if (result_homnay.getString(4).equals("0")) {
                        tong_thu = tong_thu + Integer.parseInt(result_homnay.getString(3));

                    } else if (result_homnay.getString(4).equals("1")) {
                        tong_chi = tong_chi + Integer.parseInt(result_homnay.getString(3));

                    } else if (result_homnay.getString(4).equals("2")) {
                        dang_chomuon = dang_chomuon + Integer.parseInt(result_homnay.getString(3));

                    } else if (result_homnay.getString(4).equals("3")) {
                        con_no = con_no + Integer.parseInt(result_homnay.getString(3));

                    }
                } catch (Exception ex) {

                }
            }

            String tieude = "";
            if (ngaybatdau.equals("Chọn ngày")) {
                if (ngayketthuc.equals("Chọn ngày")) {
                    tieude = "";
                } else {
                    tieude = "Đến ngày " + ngayketthuc;
                }
            } else {
                if (ngayketthuc.equals("Chọn ngày")) {
                    tieude = "Từ ngày " + ngaybatdau;
                } else {
                    tieude = "Từ ngày (" + ngaybatdau + " - " + ngayketthuc + ")";
                }
            }

            arrayreportitem.add(new ReportItem(tieude, formatNumber(tong_thu + "") + " đ", formatNumber(tong_chi + "") + " đ", formatNumber(dang_chomuon + "") + " đ", formatNumber(con_no + "") + " đ", data_list_groupname));


            listviewreportlist.setAdapter(adapterreporttem);
        }
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
    public void morong_thunho() {
        if (mo_rong) {
            int height_show_hide = (int) convertDpToPx(getApplicationContext(), 80);
            constraintLayout_hide_show.setMinHeight(height_show_hide);
            constraintLayout_hide_show.setMaxHeight(height_show_hide);
            morong_thunho.setImageResource(R.drawable.mo_rong_30);

        } else {
            int height_show_hide = (int) convertDpToPx(getApplicationContext(), 240);
            constraintLayout_hide_show.setMinHeight(height_show_hide);
            constraintLayout_hide_show.setMaxHeight(height_show_hide);
            morong_thunho.setImageResource(R.drawable.thu_hep_30);
        }
        mo_rong = !mo_rong;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // chuyển từ dp to px
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lấy ngày tháng năm hiện tại dạng YYYYMMDD, ví dụ: 20220315
    public String getDateYYYYMMDD() {
        String ngay = LocalDate.now().getDayOfMonth() + "";
        ngay = ngay.length() == 2 ? ngay : "0" + ngay;
        String thang = (LocalDate.now().getMonthValue()) + "";
        thang = thang.length() == 2 ? thang : "0" + thang;
        String result = LocalDate.now().getYear() + thang + ngay;
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lấy ngày tháng năm hiện tại dạng dd/MM/yyyy, ví dụ: 15/03/2022
    public String getDateDDMMYYYY() {
        String ngay = LocalDate.now().getDayOfMonth() + "";
        ngay = ngay.length() == 2 ? ngay : "0" + ngay;
        String thang = (LocalDate.now().getMonthValue()) + "";
        thang = thang.length() == 2 ? thang : "0" + thang;
        String result = ngay + "/" + thang + "/" + LocalDate.now().getYear();
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lấy ngày tháng năm của hôm qua dạng dd/MM/yyyy, ví dụ: 15/03/2022
    public String getDateYesterdayDDMMYYYY() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        String ngay = yesterday.getDayOfMonth() + "";
        ngay = ngay.length() == 2 ? ngay : "0" + ngay;
        String thang = (yesterday.getMonthValue()) + "";
        thang = thang.length() == 2 ? thang : "0" + thang;
        String result = ngay + "/" + thang + "/" + yesterday.getYear();
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lấy ngày tháng năm của ngày đầu tuần dạng yyyy/MM/dd, ví dụ: 20220315
    public String getStartWeekYYYYMMDD(int themtuan) {
        // themtuan=0 tuần này, themtuan=7 tuần trước
        String result = "";
        LocalDate today = LocalDate.now();
        LocalDate ngay_dau_tuan = null;

        DayOfWeek day = today.getDayOfWeek();
        if (day.getValue() == DayOfWeek.MONDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(themtuan);

        } else if (day.getValue() == DayOfWeek.TUESDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(1 + themtuan);

        } else if (day.getValue() == DayOfWeek.WEDNESDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(2 + themtuan);

        } else if (day.getValue() == DayOfWeek.THURSDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(3 + themtuan);

        } else if (day.getValue() == DayOfWeek.FRIDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(4 + themtuan);

        } else if (day.getValue() == DayOfWeek.SATURDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(5 + themtuan);

        } else if (day.getValue() == DayOfWeek.SUNDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(6 + themtuan);

        }
        String ngay = ngay_dau_tuan.getDayOfMonth() + "";
        ngay = ngay.length() == 2 ? ngay : "0" + ngay;
        String thang = (ngay_dau_tuan.getMonthValue()) + "";
        thang = thang.length() == 2 ? thang : "0" + thang;
        result = ngay_dau_tuan.getYear() + thang + ngay;
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lấy ngày tháng năm của ngày cuối tuần dạng yyyy/MM/dd, ví dụ: 20220315
    public String getLastWeekYYYYMMDD(int themtuan) {
        // themtuan=0 tuần này, themtuan=7 tuần trước
        String result = "";
        LocalDate today = LocalDate.now();
        LocalDate ngay_dau_tuan = null;

        DayOfWeek day = today.getDayOfWeek();
        if (day.getValue() == DayOfWeek.MONDAY.getValue()) {
            ngay_dau_tuan = today.plusDays(6 - themtuan);


        } else if (day.getValue() == DayOfWeek.TUESDAY.getValue()) {
            ngay_dau_tuan = today.plusDays(5 - themtuan);

        } else if (day.getValue() == DayOfWeek.WEDNESDAY.getValue()) {
            ngay_dau_tuan = today.plusDays(4 - themtuan);

        } else if (day.getValue() == DayOfWeek.THURSDAY.getValue()) {
            ngay_dau_tuan = today.plusDays(3 - themtuan);

        } else if (day.getValue() == DayOfWeek.FRIDAY.getValue()) {
            ngay_dau_tuan = today.plusDays(2 - themtuan);

        } else if (day.getValue() == DayOfWeek.SATURDAY.getValue()) {
            ngay_dau_tuan = today.plusDays(1 - themtuan);

        } else if (day.getValue() == DayOfWeek.SUNDAY.getValue()) {
            ngay_dau_tuan = today.minusDays(0 - themtuan);

        }
        String ngay = ngay_dau_tuan.getDayOfMonth() + "";
        ngay = ngay.length() == 2 ? ngay : "0" + ngay;
        String thang = (ngay_dau_tuan.getMonthValue()) + "";
        thang = thang.length() == 2 ? thang : "0" + thang;
        result = ngay_dau_tuan.getYear() + thang + ngay;

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lấy ngày tháng năm trong tháng (đầu tháng, cuối tháng) dạng yyyy/MM/dd, ví dụ: ngày hiện tại 15/03/2022=>20220301 hoặc 20220331
    public String getMonthYYYYMMDD(int start_or_end) {
        // start_or_end=0 đầu tháng, start_or_end=1 cuối tháng
        // start_or_end=2 đầu tháng trước, start_or_end=3 cuối tháng trước
        // start_or_end=4 đầu năm (01/12/yyyy), start_or_end=5 cuối năm (31/12/yyyy)
        // start_or_end=6 đầu năm trước (01/12/yyyy), start_or_end=7 cuối năm trước (31/12/yyyy)
        String result = "";

        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfMonth(1); // ngày đầu tháng
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth()); // ngày cuối tháng

        LocalDate month_pre = initial.minusMonths(1); // tháng trước
        LocalDate start_month_pre = month_pre.withDayOfMonth(1); // ngày đầu tháng
        LocalDate end_month_pre = month_pre.withDayOfMonth(month_pre.lengthOfMonth()); // ngày cuối tháng


        if (start_or_end == 0) {
            // đầu tháng
            String ngay = start.getDayOfMonth() + "";
            ngay = ngay.length() == 2 ? ngay : "0" + ngay;
            String thang = (start.getMonthValue()) + "";
            thang = thang.length() == 2 ? thang : "0" + thang;
            result = start.getYear() + thang + ngay;

        } else if (start_or_end == 1) {
            // cuối tháng
            String ngay = end.getDayOfMonth() + "";
            ngay = ngay.length() == 2 ? ngay : "0" + ngay;
            String thang = (end.getMonthValue()) + "";
            thang = thang.length() == 2 ? thang : "0" + thang;
            result = end.getYear() + thang + ngay;

        } else if (start_or_end == 2) {
            // đầu tháng trước
            String ngay = start_month_pre.getDayOfMonth() + "";
            ngay = ngay.length() == 2 ? ngay : "0" + ngay;
            String thang = (start_month_pre.getMonthValue()) + "";
            thang = thang.length() == 2 ? thang : "0" + thang;
            result = start_month_pre.getYear() + thang + ngay;

        } else if (start_or_end == 3) {
            // cuối tháng trước
            String ngay = end_month_pre.getDayOfMonth() + "";
            ngay = ngay.length() == 2 ? ngay : "0" + ngay;
            String thang = (end_month_pre.getMonthValue()) + "";
            thang = thang.length() == 2 ? thang : "0" + thang;
            result = end_month_pre.getYear() + thang + ngay;

        } else if (start_or_end == 4) {
            // đầu năm
            result = initial.getYear() + "0101";

        } else if (start_or_end == 5) {
            // cuối năm
            result = initial.getYear() + "1231";

        } else if (start_or_end == 6) {
            // đầu năm trước
            result = (initial.getYear() - 1) + "0101";

        } else if (start_or_end == 7) {
            // cuối năm trước
            result = (initial.getYear() - 1) + "1231";

        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // hiển thị thông báo cho chọn loại, nhóm và ngày bắt đầu, kết thúc để lọc báo cáo
    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.report_layout_custom_dialog, null);
        final Spinner spinner_loai = (Spinner) alertLayout.findViewById(R.id.report_dialog_spinner_loai);
        final Spinner spinner_nhom = (Spinner) alertLayout.findViewById(R.id.report_dialog_spinner_nhom);
        final TextView textView_ngaybatdau = (TextView) alertLayout.findViewById(R.id.report_dialog_textView_ngaybatdau2);
        final TextView textView_ngayketthuc = (TextView) alertLayout.findViewById(R.id.report_dialog_textView_ngayketthuc2);
        final Button btnXacnhan = (Button) alertLayout.findViewById(R.id.report_dialog_button_xacnhan);

        ArrayList<String> arraylist_group_thu; // tạo dữ liệu cho spinner nhóm
        final String[] array_group_thu; // arraylist_group_thu => array_group_thu
        ArrayList<String> arraylist_group_chi;
        final String[] array_group_chi;
        ArrayList<String> arraylist_group_chomuon;
        final String[] array_group_chomuon;
        ArrayList<String> arraylist_group_no;
        final String[] array_group_no;
        ArrayList<String> arraylist_group_tatca; // tất cả
        final String[] array_group_tatca;

        arraylist_group_thu = new ArrayList<>();
        arraylist_group_chi = new ArrayList<>();
        arraylist_group_chomuon = new ArrayList<>();
        arraylist_group_no = new ArrayList<>();
        arraylist_group_tatca = new ArrayList<>();

        arraylist_group_tatca.add("-Tất cả-");
        arraylist_group_thu.add("-Tất cả-");
        arraylist_group_chi.add("-Tất cả-");
        arraylist_group_chomuon.add("-Tất cả-");
        arraylist_group_no.add("-Tất cả-");

        // lấy dữ liệu cho các arraylist: tất cả, thu, chi, cho mượn, nợ
        String select = "SELECT GroupName,Type,Icon FROM GroupName ORDER BY lower(GroupName) ASC";
        Cursor result = database.GetData(select);
        while (result.moveToNext()) {
            try {
                // Tất cả
                arraylist_group_tatca.add(result.getString(0));
                if (Integer.parseInt(result.getString(1)) == 0) {
                    // thu
                    arraylist_group_thu.add(result.getString(0));

                } else if (Integer.parseInt(result.getString(1)) == 1) {
                    // chi
                    arraylist_group_chi.add(result.getString(0));

                } else if (Integer.parseInt(result.getString(1)) == 2) {
                    // cho mượn
                    arraylist_group_chomuon.add(result.getString(0));

                } else if (Integer.parseInt(result.getString(1)) == 3) {
                    // nợ
                    arraylist_group_no.add(result.getString(0));
                }
            } catch (Exception ex) {
            }
        }


        array_group_tatca = new String[arraylist_group_tatca.size()];
        for (int i = 0; i < arraylist_group_tatca.size(); i++) {
            array_group_tatca[i] = arraylist_group_tatca.get(i).toString();
        }
        array_group_thu = new String[arraylist_group_thu.size()];
        for (int i = 0; i < arraylist_group_thu.size(); i++) {
            array_group_thu[i] = arraylist_group_thu.get(i).toString();
        }
        array_group_chi = new String[arraylist_group_chi.size()];
        for (int i = 0; i < arraylist_group_chi.size(); i++) {
            array_group_chi[i] = arraylist_group_chi.get(i).toString();
        }
        array_group_chomuon = new String[arraylist_group_chomuon.size()];
        for (int i = 0; i < arraylist_group_chomuon.size(); i++) {
            array_group_chomuon[i] = arraylist_group_chomuon.get(i).toString();
        }
        array_group_no = new String[arraylist_group_no.size()];
        for (int i = 0; i < arraylist_group_no.size(); i++) {
            array_group_no[i] = arraylist_group_no.get(i).toString();
        }


        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Xử lý lấy dữ liệu cho spinner nhóm
        ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_group_tatca);
        adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nhom.setAdapter(adapterspinnernhom);
        spinner_nhom.setSelection(0);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Xử lý lấy dữ liệu cho spinner loại
        ArrayAdapter<String> adapterspinnerloai = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraytype);
        // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
        adapterspinnerloai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_loai.setAdapter(adapterspinnerloai);
        spinner_loai.setSelection(0);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bat su kien thay doi loai thu chi
        spinner_loai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_tatca);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_nhom.setAdapter(adapterspinnernhom);

                } else if (i == 1) {
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_thu);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_nhom.setAdapter(adapterspinnernhom);

                } else if (i == 2) {
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_chi);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_nhom.setAdapter(adapterspinnernhom);

                } else if (i == 3) {
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_chomuon);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_nhom.setAdapter(adapterspinnernhom);

                } else if (i == 4) {

                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_no);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_nhom.setAdapter(adapterspinnernhom);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // chọn ngày bắt đầu
        textView_ngaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textviewSelectDate(textView_ngaybatdau);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // chọn ngày kết thúc
        textView_ngayketthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textviewSelectDate(textView_ngayketthuc);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Chọn loại báo cáo");
        alert.setView(alertLayout);
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String where_loai = spinner_loai.getSelectedItemPosition() == 0 ? "" : " AND Note.Type=" + (spinner_loai.getSelectedItemPosition() - 1);
                String where_nhom = spinner_nhom.getSelectedItemPosition() == 0 ? "" : " AND GroupName.GroupName='" + spinner_nhom.getSelectedItem().toString() + "'";

                String where_ngay = "";
                String ngaybatdau = textView_ngaybatdau.getText().toString();
                String ngayketthuc = textView_ngayketthuc.getText().toString();

                // xử lý xem có chọn ngày hay không?
                if (ngaybatdau.equals("Chọn ngày")) {
                    if (ngayketthuc.equals("Chọn ngày")) {
                        where_ngay = "";
                    } else {
                        where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2)<='" + ngayketthuc.substring(6) + ngayketthuc.substring(3, 5) + ngayketthuc.substring(0, 2) + "')";
                    }
                } else {
                    if (ngayketthuc.equals("Chọn ngày")) {
                        where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2)>='" + ngaybatdau.substring(6) + ngaybatdau.substring(3, 5) + ngaybatdau.substring(0, 2) + "')";
                    } else {
                        where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + ngaybatdau.substring(6) + ngaybatdau.substring(3, 5) + ngaybatdau.substring(0, 2) + "' AND '" + ngayketthuc.substring(6) + ngayketthuc.substring(3, 5) + ngayketthuc.substring(0, 2) + "')";
                    }
                }

                setDataListViewFromSQL(where_loai, where_nhom, where_ngay, ngaybatdau, ngayketthuc);

                // đóng cửa sổ dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void textviewSelectDate(TextView textView) {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String ngay = dayOfMonth + "";
                ngay = ngay.length() == 2 ? ngay : "0" + ngay;
                String thang = (monthOfYear + 1) + "";
                thang = thang.length() == 2 ? thang : "0" + thang;
                textView.setText(ngay + "/" + thang + "/" + year);
            }
        };

        DatePickerDialog datePickerDialog = null;
        // Calendar Mode (Default):
        datePickerDialog = new DatePickerDialog(this,
                dateSetListener, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
        // Show
        datePickerDialog.show();
    }
}