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

public class GroupEdit extends AppCompatActivity {
    private EditText group; // tên  nhóm
    private Spinner type; // loại - 0: thu, 1: chi, 2: cho mượn, 3 nợ
    private GridView iconsgrid;// danh sach cac icons
    private Database database;

    private DataArrayImage arrayImage;
    private CustomGridAdapterIcons adaptericon;
    private final String[] arraytype = {"Thu", "Chi", "Cho mượn", "Nợ"};
    private int positionicon = 0;// ảnh được chọn trong gridview
    private View viewselect = null; // view dang chon, neu chon view khac thi xoa background cai da chon truoc

    private String groupid_edit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        // thay doi tieu de cho title bar
        getSupportActionBar().setTitle("Thêm nhóm");
        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AnhXa();

        // Xử lý lấy dữ liệu cho spinner loại ---------------------------------------------------------------------------------------------------------------------------------
        ArrayAdapter<String> adapterspinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraytype);
        // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
        adapterspinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapterspinner);

        // Xử lý lấy dữ liệu cho GridView ---------------------------------------------------------------------------------------------------------------------------------
        adaptericon = new CustomGridAdapterIcons(arrayImage.getArrayListImageGroupname(), R.layout.grid_item_layout_group, this);
        iconsgrid.setAdapter(adaptericon);
        //  adaptericon.notifyDataSetChanged();
        // iconsgrid.invalidateViews();
        try {
            positionicon = 0;
        } catch (Exception e) {
        }


        //-----------------------------------------------------------------------------------------------------------------------------------------------------------------
        // lấy giá trị từ grouplist truyền qua, group sửa
        Intent intent_edit = getIntent();
        groupid_edit = intent_edit.getStringExtra("groupid");

        String select_edit = "SELECT GroupName,Type FROM GroupName WHERE Id=" + groupid_edit;
        Cursor result_edit = database.GetData(select_edit);
        while (result_edit.moveToNext()) {
            try {
                group.setText(result_edit.getString(0));
                type.setSelection(Integer.parseInt(result_edit.getString(1)));
            } catch (Exception ex) {
            }
        }

        // bắt sự kiện khi click vào hình ảnh trong GridView ---------------------------------------------------------------------------------------------------------------------------------
        iconsgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                positionicon = i;
                try {
                    //viewselect.setBackgroundColor(0x0277BD);
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
        getMenuInflater().inflate(R.menu.menu_groupedit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // bắt sự kiện khi nhấn vào nút save trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_groupedit_save) {
            // menu save
            if (!group.getText().toString().trim().isEmpty()) {

                // update data to table GroupName
                String update="UPDATE GroupName SET GroupName='"+group.getText().toString()+"',Type="+type.getSelectedItemPosition()+",Icon="+positionicon+" WHERE Id="+groupid_edit;
                try {
                    database.QueryData(update);
                    Toast.makeText(getApplicationContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();

                    Intent intentresult = new Intent();
                    setResult(4, intentresult);
                    finish();
                } catch (Exception exception) {
                }
            }
        } else if (item.getItemId() == android.R.id.home) {
            // sự kiện nhấn nút quay lại trên thanh tiêu đề
            Intent intentresult = new Intent();
            setResult(4, intentresult);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AnhXa() {
        group = (EditText) findViewById(R.id.groupedit_editText_tennhom);
        type = (Spinner) findViewById(R.id.groupedit_spinner_loai);
        iconsgrid = (GridView) findViewById(R.id.groupedit_gridview_icons);

        // create database QuanLyChiTieu
        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        arrayImage = new DataArrayImage();

    }
}