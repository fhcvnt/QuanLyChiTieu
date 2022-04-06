package note;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

import naruto.hinata.quanlychitieu.R;
import sqlite.Database;

public class NoteSearch extends AppCompatActivity {
    private ImageView imageView_back;
    private Spinner spinner_loai;
    private Spinner spinner_nhom;
    private EditText editText_ghichu;
    private EditText editText_sotientoithieu;
    private Button button_tungay;
    private Button button_denngay;
    private TextView textView_ketquatimkiem2;
    private TextView textView_tongthu2;
    private TextView textView_tongchi2;
    private TextView textView_tongchomuon2;
    private TextView textView_tongno2;
    private TextView textView_morong_thunho;
    private ImageView imageView_morong_thunho;
    private Button button_timkiem;
    private ConstraintLayout contraintlayout_hide_show;
    private ListView listview_notelist;

    private ArrayList<String> arraylist_group_thu; // tạo dữ liệu cho spinner nhóm
    private String[] array_group_thu; // arraylist_group_thu => array_group_thu
    private ArrayList<String> arraylist_group_chi;
    private String[] array_group_chi;
    private ArrayList<String> arraylist_group_chomuon;
    private String[] array_group_chomuon;
    private ArrayList<String> arraylist_group_no;
    private String[] array_group_no;
    private ArrayList<String> arraylist_group_tatca; // tất cả
    private String[] array_group_tatca;

    private Database database;
    private final String[] arraytype = {"-Tất cả-", "Thu", "Chi", "Cho mượn", "Nợ"};

    private ArrayList<NoteItem> arraynoteitem;
    private NoteItemAdapter adapternoteitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_search);

        AnhXa();

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

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Quay lại màn hình trước (Ghi chú)
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Chọn từ ngày
        button_tungay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelectDate(button_tungay);
            }
        });

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Chọn đến ngày
        button_denngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelectDate(button_denngay);
            }
        });

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở rộng, thu nhỏ layout
        imageView_morong_thunho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morong_thunho();
            }
        });

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện mở rộng, thu nhỏ layout
        textView_morong_thunho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morong_thunho();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện thay đổi dữ liệu trong text tiền, đình dạng tiền
        editText_sotientoithieu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editText_sotientoithieu.removeTextChangedListener(this);
                try {
                    String originalString = editable.toString();

                    Long longval;
                    if (originalString.contains(".")) {
                        originalString = originalString.replaceAll("\\.", "");
                    }
                    longval = Long.parseLong(originalString);

                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat formatter = NumberFormat.getInstance(localeVN);
                    String formattedString = formatter.format(longval);

                    editText_sotientoithieu.setText(formattedString);

                    editText_sotientoithieu.setSelection(editText_sotientoithieu.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                editText_sotientoithieu.addTextChangedListener(this);
            }
        });

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        button_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner_nhom.getSelectedItemPosition() == 0) {
                    setDataListViewFromSQL(spinner_loai.getSelectedItemPosition(), "");
                } else {
                    setDataListViewFromSQL(spinner_loai.getSelectedItemPosition(), " AND GroupName.GroupName='" + spinner_nhom.getSelectedItem().toString() + "'");
                }
            }
        });

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // hàm này để nhận lại dữ liệu từ cửa sổ đã mở
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                if (spinner_nhom.getSelectedItemPosition() == 0) {
                    setDataListViewFromSQL(spinner_loai.getSelectedItemPosition(), "");
                } else {
                    setDataListViewFromSQL(spinner_loai.getSelectedItemPosition(), " AND GroupName.GroupName='" + spinner_nhom.getSelectedItem().toString() + "'");
                }
                break;
            default:
                break;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AnhXa() {
        imageView_back = findViewById(R.id.notesearch_imageView_back);
        spinner_loai = findViewById(R.id.notesearch_spinner_loai);
        spinner_loai.setBackgroundColor(Color.BLACK);
        spinner_nhom = findViewById(R.id.notesearch_spinner_nhom);
        spinner_nhom.setBackgroundColor(Color.WHITE);

        editText_ghichu = findViewById(R.id.notesearch_editText_ghichu);
        editText_sotientoithieu = findViewById(R.id.notesearch_editText_sotientoithieu);
        button_tungay = findViewById(R.id.notesearch_button_tungay);
        button_denngay = findViewById(R.id.notesearch_button_denngay);
        textView_ketquatimkiem2 = findViewById(R.id.notesearch_textView_ketquatimkiem2);
        textView_tongthu2 = findViewById(R.id.notesearch_textView_tongthu2);
        textView_tongchi2 = findViewById(R.id.notesearch_textView_tongchi2);
        textView_tongchomuon2 = findViewById(R.id.notesearch_textView_tongchomuon2);
        textView_tongno2 = findViewById(R.id.notesearch_textView_tongno2);
        textView_morong_thunho = findViewById(R.id.notesearch_textView_morong_thunho);
        imageView_morong_thunho = findViewById(R.id.notesearch_imageView_morong_thunho);
        button_timkiem = findViewById(R.id.notesearch_button_timkiem);
        contraintlayout_hide_show = findViewById(R.id.notesearch_contraintlayout_hide_show);
        listview_notelist = findViewById(R.id.notesearch_listview_notelist);

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

        arraynoteitem = new ArrayList<>();
        adapternoteitem = new NoteItemAdapter(arraynoteitem, R.layout.listview_item_notelist, this);

        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);

        // lấy dữ liệu cho các arraylist: tất cả, thu, chi, cho mượn, nợ
        try {
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
        } catch (Exception e) {
        }

    }


    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // lay du lieu tu sqlite xuong
    public void setDataListViewFromSQL(int type, String where_groupname) {
        int loai = type - 1;
        String select = "";

        // tiền tối thiểu
        String sotien = editText_sotientoithieu.getText().toString();
        String where_sotien = "";
        Long longval;
        if (sotien.contains(".")) {
            sotien = sotien.replaceAll("\\.", "");
        }
        try {
            longval = Long.parseLong(sotien);
            where_sotien = " AND Money>=" + longval;
        } catch (Exception ex) {
            where_sotien = "";
        }

        // tìm kiếm từ ngày đến ngày
        String where_ngay = "";
        try {
            String[] ngaybatdau = button_tungay.getText().toString().split("/");
            String[] ngayketthuc = button_denngay.getText().toString().split("/");

            where_ngay = " AND (substr(Ngay,7)||substr(Ngay,4,2)||substr(Ngay,1,2) BETWEEN '" + ngaybatdau[2]  + ngaybatdau[1]  + ngaybatdau[0] + "' AND '" + ngayketthuc[2] + ngayketthuc[1] + ngayketthuc[0] + "')";
        } catch (Exception exception) {
            where_ngay = "";
        }

        if (loai == -1) {

            select = "SELECT Note.Id,Icon,GroupName,Ngay,Gio,Money,Note,Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id  AND Note LIKE '%" + editText_ghichu.getText().toString() + "%'" + where_sotien + where_ngay + where_groupname + " ORDER BY substr(Ngay,7,11)||substr(Ngay,4,6)||substr(Ngay,1,2) DESC, Gio DESC ";

        } else {
            select = "SELECT Note.Id,Icon,GroupName,Ngay,Gio,Money,Note,Note.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id AND Note LIKE '%" + editText_ghichu.getText().toString() + "%'" + where_sotien + where_ngay + " AND Note.Type=" + loai + where_groupname + " ORDER BY substr(Ngay,7,11)||substr(Ngay,4,6)||substr(Ngay,1,2) DESC, Gio DESC";
        }
        // xoa du lieu trong listview cu
        arraynoteitem.clear();
        listview_notelist.removeAllViewsInLayout();

        //Tính tổng thu, chi, cho mượn, nợ
        int sum_thu = 0;
        int sum_chi = 0;
        int sum_chomuon = 0;
        int sum_no = 0;
        int count_rows = 0;

        Cursor result = database.GetData(select);
        while (result.moveToNext()) {
            count_rows++;
            try {
                arraynoteitem.add(new NoteItem(Integer.parseInt(result.getString(0)), Integer.parseInt(result.getString(1)), result.getString(2), result.getString(3) + " " + result.getString(4), formatNumber(result.getString(5)) + " đ", result.getString(6), countDate(result.getString(3))));
                if (Integer.parseInt(result.getString(7)) == 0) {
                    sum_thu = sum_thu + Integer.parseInt(result.getString(5));
                } else if (Integer.parseInt(result.getString(7)) == 1) {
                    sum_chi = sum_chi + Integer.parseInt(result.getString(5));
                } else if (Integer.parseInt(result.getString(7)) == 2) {
                    sum_chomuon = sum_chomuon + Integer.parseInt(result.getString(5));
                } else if (Integer.parseInt(result.getString(7)) == 3) {
                    sum_no = sum_no + Integer.parseInt(result.getString(5));
                }
            } catch (Exception ex) {

            }
        }
        textView_ketquatimkiem2.setText(count_rows + " bản ghi");
        textView_tongthu2.setText(formatNumber(sum_thu + "") + " đ");
        textView_tongchi2.setText(formatNumber(sum_chi + "") + " đ");
        textView_tongchomuon2.setText(formatNumber(sum_chomuon + "") + " đ");
        textView_tongno2.setText(formatNumber(sum_no + "") + " đ");

        listview_notelist.setAdapter(adapternoteitem);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void buttonSelectDate(Button button) {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String ngay = dayOfMonth + "";
                ngay = ngay.length() == 2 ? ngay : "0" + ngay;
                String thang = (monthOfYear + 1) + "";
                thang = thang.length() == 2 ? thang : "0" + thang;
                button.setText(ngay + "/" + thang + "/" + year);
            }
        };

        DatePickerDialog datePickerDialog = null;
        // Calendar Mode (Default):
        datePickerDialog = new DatePickerDialog(this,
                dateSetListener, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
        // Show
        datePickerDialog.show();
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

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void morong_thunho() {
        if (textView_morong_thunho.getText().equals("Thu nhỏ")) {
            contraintlayout_hide_show.setMinHeight(0);
            contraintlayout_hide_show.setMaxHeight(0);
            textView_morong_thunho.setText("Mở rộng");
            imageView_morong_thunho.setImageResource(R.drawable.mo_rong_30);

        } else {
            int height_show_hide = (int) convertDpToPx(getApplicationContext(), 325);
            contraintlayout_hide_show.setMinHeight(height_show_hide);
            contraintlayout_hide_show.setMaxHeight(height_show_hide);

            textView_morong_thunho.setText("Thu nhỏ");
            imageView_morong_thunho.setImageResource(R.drawable.thu_hep_30);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // chuyển từ dp to px
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}