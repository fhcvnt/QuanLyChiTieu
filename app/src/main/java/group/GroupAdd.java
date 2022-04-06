package group;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import data_function.DataArrayImage;
import naruto.hinata.quanlychitieu.R;
import sqlite.Database;

public class GroupAdd extends AppCompatActivity {
    private EditText group; // tên  nhóm
    private Spinner type; // loại - 0: thu, 1: chi, 2: cho mượn, 3 nợ
    private GridView iconsgrid;// danh sach cac icons
    private Database database;

    private DataArrayImage arrayImage;
    private CustomGridAdapterIcons adaptericon;
    private final String[] arraytype = {"Thu", "Chi", "Cho mượn", "Nợ"};
    private int positionicon = 0;// vị trí ảnh được chọn trong gridview, từ đó lấy giá trị trong arrayList DataArrayImage
    private View viewselect = null; // view dang chon, neu chon view khac thi xoa background cai da chon truoc


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        // thay doi tieu de cho title bar
        getSupportActionBar().setTitle("Thêm nhóm");
        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AnhXa();

        // Xử lý lấy dữ liệu cho spinner loại ----------------------------------------------------------------------------------------------------------------------------
        ArrayAdapter<String> adapterspinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraytype);
        // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
        adapterspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapterspinner);

        // Xử lý lấy dữ liệu cho GridView ----------------------------------------------------------------------------------------------------------------------------
        adaptericon = new CustomGridAdapterIcons(arrayImage.getArrayListImageGroupname(), R.layout.grid_item_layout_group, getApplicationContext());
        iconsgrid.setAdapter(adaptericon);
        //adaptericon.notifyDataSetChanged();
        //iconsgrid.invalidateViews();
        try {
            positionicon = 0;
        } catch (Exception e) {
        }

        // bắt sự kiện khi click vào hình ảnh trong GridView
        iconsgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                positionicon = i;
                try {
                    iconsgrid.getChildAt(0).setBackgroundResource(R.drawable.border_gridview_image);
                    viewselect.setBackgroundResource(R.drawable.border_gridview_image);

                } catch (Exception ee) {
                }
                viewselect = view;
                view.setBackgroundResource(R.drawable.border_gridview_image_select);
            }
        });

    }

    // tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_groupadd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // bắt sự kiện khi nhấn vào nút save trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_groupadd_save) {
            // menu save
            if (!group.getText().toString().trim().isEmpty()) {

                // tao ID cho groupname
                int groupid = 1000000;
                String selectid = "SELECT Id FROM GroupName ORDER BY Id DESC LIMIT 1"; // chỉ lấy 1 dòng duy nhất, SQLite chỉ sắp xếp theo ABC không sắp xếp theo số, (VD: 2>10)
                Cursor resultid = database.GetData(selectid);
                while (resultid.moveToNext()) {
                    try {
                        groupid = Integer.parseInt(resultid.getString(0));
                    } catch (Exception exc) {
                    }
                }
                groupid++;

                // insert data to table GroupName
                String insert = "INSERT INTO GroupName VALUES (" + groupid + ",'" + group.getText().toString() + "'," + type.getSelectedItemPosition() + "," + positionicon + ")";
                try {
                    database.QueryData(insert);
                    Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    group.setText("");
                } catch (Exception exception) {
                }
            }
        } else if (item.getItemId() == android.R.id.home) {
            // sự kiện nhấn nút quay lại trên thanh tiêu đề
            Intent intentresult = new Intent();
            setResult(1, intentresult);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AnhXa() {
        group = (EditText) findViewById(R.id.groupadd_editText_tennhom);
        type = (Spinner) findViewById(R.id.groupadd_spinner_loai);
        iconsgrid = (GridView) findViewById(R.id.groupadd_gridview_icons);

        // create database QuanLyChiTieu
        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);

        arrayImage = new DataArrayImage();

    }
}