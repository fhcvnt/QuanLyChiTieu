package note;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import data_function.DataArrayImage;
import naruto.hinata.quanlychitieu.R;
import sqlite.Database;

public class NoteAdd extends AppCompatActivity {
    private DataArrayImage arrayImage; // araylist image groupname
    private final String[] arraytype = {"Thu", "Chi", "Cho mượn", "Nợ"};
    private Spinner spinnerloai;
    private ImageView imgloai;
    private Spinner spinnernhom;
    private ImageView imgnhom;
    private EditText textsotien;
    private EditText textghichu;
    private TextView tvthoigian1, tvthoigian2;

    private ArrayList<String> arraylist_group_thu; // tạo dữ liệu cho spinner nhóm: sẽ có 8 mảng chứa cả dữ liệu và hình (2 thu, 2 chi, 2 cho muon, 2 no)
    private String[] array_group_thu; // arraylist_group_thu => array_group_thu
    private ArrayList<Integer> arraylist_group_image_thu;
    private ArrayList<String> arraylist_group_chi;
    private String[] array_group_chi;
    private ArrayList<Integer> arraylist_group_image_chi;
    private ArrayList<String> arraylist_group_chomuon; // tạo dữ liệu cho spinner nhóm: sẽ có 8 mảng chứa cả dữ liệu và hình (2 thu, 2 chi, 2 cho muon, 2 no)
    private String[] array_group_chomuon;
    private ArrayList<Integer> arraylist_group_image_chomuon;
    private ArrayList<String> arraylist_group_no;
    private String[] array_group_no;
    private ArrayList<Integer> arraylist_group_image_no;

    private Database database;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        // thay doi tieu de cho title bar
        getSupportActionBar().setTitle("Thêm ghi chú");
        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AnhXa();

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        String ngay = lastSelectedDayOfMonth + "";
        ngay = ngay.length() == 2 ? ngay : "0" + ngay;
        String thang = (lastSelectedMonth + 1) + "";
        thang = thang.length() == 2 ? thang : "0" + thang;
        tvthoigian1.setText(ngay + "/" + thang + "/" + lastSelectedYear);

        String gio = c.get(Calendar.HOUR_OF_DAY) + "";
        gio = gio.length() == 2 ? gio : "0" + gio;
        String phut = c.get(Calendar.MINUTE) + "";
        phut = phut.length() == 2 ? phut : "0" + phut;
        if (Integer.parseInt(gio) >= 12) {
            phut = phut + " PM";
        } else {
            phut = phut + " AM";
        }
        tvthoigian2.setText(gio + ":" + phut);

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
        ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_group_chi);
        // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
        adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnernhom.setAdapter(adapterspinnernhom);
        try {
            spinnernhom.setSelection(0);
        } catch (Exception exc) {
        }

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bat su kien thay doi nhom thu chi
        spinnernhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (spinnerloai.getSelectedItemPosition() == 0) {
                        imgnhom.setImageResource(arraylist_group_image_thu.get(i));
                    } else if (spinnerloai.getSelectedItemPosition() == 1) {
                        imgnhom.setImageResource(arraylist_group_image_chi.get(i));
                    } else if (spinnerloai.getSelectedItemPosition() == 2) {
                        imgnhom.setImageResource(arraylist_group_image_chomuon.get(i));
                    } else if (spinnerloai.getSelectedItemPosition() == 3) {
                        imgnhom.setImageResource(arraylist_group_image_no.get(i));
                    }
                } catch (Exception e) {
                    imgnhom.setImageResource(R.drawable.background_noteadd_item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Xử lý lấy dữ liệu cho spinner loại ----------------------------------
        ArrayAdapter<String> adapterspinnerloai = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraytype);
        // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
        adapterspinnerloai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerloai.setAdapter(adapterspinnerloai);
        spinnerloai.setSelection(1);

        // bat su kien thay doi loai thu chi
        spinnerloai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    imgloai.setImageResource(R.drawable.thu48);
                    // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_thu);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnernhom.setAdapter(adapterspinnernhom);

                    // nếu danh sách trong nhóm rỗng thì gán image nhóm là màu nền luôn
                    if (array_group_thu.length == 0) {
                        imgnhom.setImageResource(R.drawable.background_noteadd_item);
                    }
                } else if (i == 1) {
                    imgloai.setImageResource(R.drawable.chi48);
                    // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_chi);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnernhom.setAdapter(adapterspinnernhom);

                    // nếu danh sách trong nhóm rỗng thì gán image nhóm là màu nền luôn
                    if (array_group_chi.length == 0) {
                        imgnhom.setImageResource(R.drawable.background_noteadd_item);
                    }
                } else if (i == 2) {
                    imgloai.setImageResource(R.drawable.chomuon48);
                    // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_chomuon);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnernhom.setAdapter(adapterspinnernhom);

                    // nếu danh sách trong nhóm rỗng thì gán image nhóm là màu nền luôn
                    if (array_group_chomuon.length == 0) {
                        imgnhom.setImageResource(R.drawable.background_noteadd_item);
                    }
                } else if (i == 3) {
                    imgloai.setImageResource(R.drawable.no48);
                    // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
                    ArrayAdapter<String> adapterspinnernhom = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, array_group_no);
                    adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnernhom.setAdapter(adapterspinnernhom);

                    // nếu danh sách trong nhóm rỗng thì gán image nhóm là màu nền luôn
                    if (array_group_no.length == 0) {
                        imgnhom.setImageResource(R.drawable.background_noteadd_item);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // mở cửa sổ chọn ngày khi click vào textview date
        tvthoigian1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelectDate();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // mở cửa sổ chọn thời gian khi click vào textview time
        tvthoigian2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelectTime();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // bắt sự kiện thay đổi dữ liệu trong text tiền, đình dạng tiền
        textsotien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                textsotien.removeTextChangedListener(this);
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

                    textsotien.setText(formattedString);

                    textsotien.setSelection(textsotien.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                textsotien.addTextChangedListener(this);
            }
        });
    }

    // tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_noteadd, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // bắt sự kiện khi nhấn vào nút save trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_noteadd_save) {
            // menu save
            if (!textsotien.getText().toString().isEmpty() && !(spinnernhom.getSelectedItem() == null)) {
                // tao ID cho note
                String noteid = "0";
                String selectid = "SELECT Id FROM Note ORDER BY Id DESC LIMIT 1";
                Cursor resultid = database.GetData(selectid);
                while (resultid.moveToNext()) {
                    noteid = resultid.getString(0);
                }
                try {
                    if (noteid.equals("0")) {
                        noteid = "1";
                    } else {
                        noteid = "" + (Integer.parseInt(noteid) + 1);
                    }
                } catch (Exception ew) {
                    noteid = "1";
                }

                // kiểm tra groupid của groupname
                String groupid = "0";
                String selectgroupid = "SELECT Id FROM GroupName WHERE GroupName='" + spinnernhom.getSelectedItem().toString() + "' AND Type=" + spinnerloai.getSelectedItemPosition() + "";
                Cursor resultgroupid = database.GetData(selectgroupid);
                while (resultgroupid.moveToNext()) {
                    groupid = resultgroupid.getString(0);
                }

                String sotien=textsotien.getText().toString();
                if (sotien.contains(".")) {
                    sotien = sotien.replaceAll("\\.", "");
                }
                // insert data to table GroupName
                String insert = "INSERT INTO Note VALUES(" + noteid + "," + sotien + "," + spinnerloai.getSelectedItemPosition() + "," + groupid + ",'" + textghichu.getText().toString() + "','" + tvthoigian1.getText().toString() + "','" + tvthoigian2.getText().toString() + "')";
                try {
                    database.QueryData(insert);
                    Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    textsotien.setText("");
                    textghichu.setText("");
                } catch (Exception exception) {
                }
            } else {
                if (textsotien.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Số tiền rỗng!", Toast.LENGTH_SHORT).show();
                } else if (spinnernhom.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), "Bạn hãy tạo nhóm trước!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (item.getItemId() == android.R.id.home) {
            // sự kiện nhấn nút quay lại trên thanh tiêu đề
            Intent intentresult = new Intent();
            setResult(2, intentresult);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AnhXa() {
        arrayImage=new DataArrayImage();
        spinnerloai = (Spinner) findViewById(R.id.noteadd_spinner_loai);
        imgloai = (ImageView) findViewById(R.id.noteadd_imageView_loai);
        spinnernhom = (Spinner) findViewById(R.id.noteadd_spinner_nhom);
        imgnhom = (ImageView) findViewById(R.id.noteadd_imageView_nhom);
        textsotien = (EditText) findViewById(R.id.noteadd_editText_sotien);
        textghichu = (EditText) findViewById(R.id.noteadd_editText_ghichu);
        tvthoigian1 = (TextView) findViewById(R.id.noteadd_textView_thoigian1);
        tvthoigian2 = (TextView) findViewById(R.id.noteadd_textView_thoigian2);

        arraylist_group_thu = new ArrayList<>();
        arraylist_group_image_thu = new ArrayList<>();
        arraylist_group_chi = new ArrayList<>();
        arraylist_group_image_chi = new ArrayList<>();
        arraylist_group_chomuon = new ArrayList<>();
        arraylist_group_image_chomuon = new ArrayList<>();
        arraylist_group_no = new ArrayList<>();
        arraylist_group_image_no = new ArrayList<>();

        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        try {
            String select = "SELECT GroupName,Type,Icon FROM GroupName ORDER BY GroupName ASC";
            Cursor result = database.GetData(select);
            while (result.moveToNext()) {
                try {
                    if (Integer.parseInt(result.getString(1)) == 0) {
                        // thu
                        arraylist_group_thu.add(result.getString(0));
                        arraylist_group_image_thu.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));

                    } else if (Integer.parseInt(result.getString(1)) == 1) {
                        // chi
                        arraylist_group_chi.add(result.getString(0));
                        arraylist_group_image_chi.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));

                    } else if (Integer.parseInt(result.getString(1)) == 2) {
                        // cho mượn
                        arraylist_group_chomuon.add(result.getString(0));
                        arraylist_group_image_chomuon.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));

                    } else if (Integer.parseInt(result.getString(1)) == 3) {
                        // nợ
                        arraylist_group_no.add(result.getString(0));
                        arraylist_group_image_no.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));
                    }
                } catch (Exception ex) {
                }
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
            array_group_thu = new String[0];
            array_group_chi = new String[0];
            array_group_chomuon = new String[0];
            array_group_no = new String[0];
        }
    }

    // User click on 'Select Date' button.
    private void buttonSelectDate() {
        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String ngay = dayOfMonth + "";
                ngay = ngay.length() == 2 ? ngay : "0" + ngay;
                String thang = (monthOfYear + 1) + "";
                thang = thang.length() == 2 ? thang : "0" + thang;
                tvthoigian1.setText(ngay + "/" + thang + "/" + year);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = null;
        // Calendar Mode (Default):
        datePickerDialog = new DatePickerDialog(this,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        // Show
        datePickerDialog.show();
    }

    private void buttonSelectTime() {
        if (this.lastSelectedHour == -1) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }

        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvthoigian2.setText(hourOfDay + ":" + minute);
                String gio = hourOfDay + "";
                gio = gio.length() == 2 ? gio : "0" + gio;
                String phut = minute + "";
                phut = phut.length() == 2 ? phut : "0" + phut;
                if (Integer.parseInt(gio) >= 12) {
                    phut = phut + " PM";
                } else {
                    phut = phut + " AM";
                }
                tvthoigian2.setText(gio + ":" + phut);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = null;
        // TimePicker in Clock Mode (Default):
        timePickerDialog = new TimePickerDialog(this,
                timeSetListener, lastSelectedHour, lastSelectedMinute, false);

        // Show
        timePickerDialog.show();
    }

}