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

public class NoteEdit extends AppCompatActivity {
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

    private int vitri_groupname_array = -1;  // vi tri trong array group name cu item sua
    private String groupname_sua = "";
    private int type_sua = 0;
    private int dem_vitri_start=0; // dùng cho đặt vị trí nhóm lúc đầu
    private int idnote_sua=0; // id note dung cho sua


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        // thay doi tieu de cho title bar
        getSupportActionBar().setTitle("Sửa ghi chú");
        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AnhXa();

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Xử lý lấy dữ liệu cho spinner nhóm ----------------------------------
        ArrayAdapter<String> adapterspinnernhom;
        if (type_sua == 0) {
            adapterspinnernhom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_group_thu);
            // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
            adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnernhom.setAdapter(adapterspinnernhom);
            try {
                spinnernhom.setSelection(vitri_groupname_array);

            } catch (Exception exc) {
            }
        } else if (type_sua == 1) {
            adapterspinnernhom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_group_chi);
            // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
            adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnernhom.setAdapter(adapterspinnernhom);
            try {
                spinnernhom.setSelection(vitri_groupname_array);
            } catch (Exception exc) {
            }
        } else if (type_sua == 2) {
            adapterspinnernhom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_group_chomuon);
            // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
            adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnernhom.setAdapter(adapterspinnernhom);
            try {
                spinnernhom.setSelection(vitri_groupname_array);
            } catch (Exception exc) {
            }
        } else if (type_sua == 3) {
            adapterspinnernhom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array_group_no);
            // Layout for All rows of Spinner.  (Optional for ArrayAdapter).
            adapterspinnernhom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnernhom.setAdapter(adapterspinnernhom);
            try {
                spinnernhom.setSelection(vitri_groupname_array);
            } catch (Exception exc) {
            }
        }

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
        try {
            spinnerloai.setSelection(type_sua);
        } catch (Exception exception) {
        }

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

                try {
                    if(dem_vitri_start==0) {
                        spinnernhom.setSelection(vitri_groupname_array);
                        dem_vitri_start++;
                    }
                } catch (Exception excep) {
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
        getMenuInflater().inflate(R.menu.menu_noteedit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // bắt sự kiện khi nhấn vào nút save trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_noteedit_save) {
            // menu save
            if (!textsotien.getText().toString().isEmpty() && !(spinnernhom.getSelectedItem() == null)) {

                // kiểm tra groupid của groupname
                String groupid = "0";
                String selectgroupid = "SELECT Id FROM GroupName WHERE GroupName='" + spinnernhom.getSelectedItem().toString() + "' AND Type=" + spinnerloai.getSelectedItemPosition() + "";
                Cursor resultgroupid = database.GetData(selectgroupid);
                while (resultgroupid.moveToNext()) {
                    groupid = resultgroupid.getString(0);
                }

                String sotien = textsotien.getText().toString();
                if (sotien.contains(".")) {
                    sotien = sotien.replaceAll("\\.", "");
                }
                // update data to table GroupName
                String update_note="UPDATE Note SET Money="+sotien+",Type="+spinnerloai.getSelectedItemPosition()+",GroupId="+groupid+",Note='"+textghichu.getText().toString()+"',Ngay='"+tvthoigian1.getText().toString()+"',Gio='"+tvthoigian2.getText().toString()+"' WHERE Id="+idnote_sua;
                try {
                    database.QueryData(update_note);
                    Toast.makeText(getApplicationContext(), "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    Intent intentresult = new Intent();
                    setResult(3, intentresult);

                    finish();
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
            setResult(3, intentresult);

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AnhXa() {
        arrayImage=new DataArrayImage();
        spinnerloai = (Spinner) findViewById(R.id.noteedit_spinner_loai);
        imgloai = (ImageView) findViewById(R.id.noteedit_imageView_loai);
        spinnernhom = (Spinner) findViewById(R.id.noteedit_spinner_nhom);
        imgnhom = (ImageView) findViewById(R.id.noteedit_imageView_nhom);
        textsotien = (EditText) findViewById(R.id.noteedit_editText_sotien);
        textghichu = (EditText) findViewById(R.id.noteedit_editText_ghichu);
        tvthoigian1 = (TextView) findViewById(R.id.noteedit_textView_thoigian1);
        tvthoigian2 = (TextView) findViewById(R.id.noteedit_textView_thoigian2);

        arraylist_group_thu = new ArrayList<>();
        arraylist_group_image_thu = new ArrayList<>();
        arraylist_group_chi = new ArrayList<>();
        arraylist_group_image_chi = new ArrayList<>();
        arraylist_group_chomuon = new ArrayList<>();
        arraylist_group_image_chomuon = new ArrayList<>();
        arraylist_group_no = new ArrayList<>();
        arraylist_group_image_no = new ArrayList<>();

        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);

        Intent intent = getIntent();
         idnote_sua = intent.getIntExtra("idnote", 0);

        String select_start = "SELECT Icon,GroupName,Ngay,Gio,Money,Note,GroupName.Type FROM Note,GroupName WHERE Note.GroupId=GroupName.Id AND Note.Id=" + idnote_sua;
        Cursor result_start = database.GetData(select_start);
        while (result_start.moveToNext()) {
            try {
                groupname_sua = result_start.getString(1);

                Locale localeVN = new Locale("vi", "VN");
                NumberFormat formatter = NumberFormat.getInstance(localeVN);
                String formattedString = formatter.format(Integer.parseInt(result_start.getString(4)));
                textsotien.setText(formattedString);
                textsotien.setSelection(textsotien.getText().length());

                type_sua = Integer.parseInt(result_start.getString(6));
                //spinnerloai.setSelection(type_sua);

                //spinnernhom.setSelection(result_start.getString(0));
                textghichu.setText(result_start.getString(5));
                tvthoigian1.setText(result_start.getString(2));
                tvthoigian2.setText(result_start.getString(3));
            } catch (Exception ex) {
            }
        }


        try {
            String select = "SELECT GroupName,Type,Icon FROM GroupName ORDER BY GroupName ASC";
            Cursor result = database.GetData(select);
            int count_thu = 0, count_chi = 0, count_chomuon = 0, count_no = 0;
            while (result.moveToNext()) {
                try {
                    if (Integer.parseInt(result.getString(1)) == 0) {
                        // thu
                        arraylist_group_thu.add(result.getString(0));
                        arraylist_group_image_thu.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));
                        if (groupname_sua.equals(result.getString(0)) && type_sua == 0) {
                            vitri_groupname_array = count_thu;
                        }
                        count_thu++;

                    } else if (Integer.parseInt(result.getString(1)) == 1) {
                        // chi
                        arraylist_group_chi.add(result.getString(0));
                        arraylist_group_image_chi.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));
                        if (groupname_sua.equals(result.getString(0)) && type_sua == 1) {
                            vitri_groupname_array = count_chi;
                        }
                        count_chi++;

                    } else if (Integer.parseInt(result.getString(1)) == 2) {
                        // cho mượn
                        arraylist_group_chomuon.add(result.getString(0));
                        arraylist_group_image_chomuon.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));
                        if (groupname_sua.equals(result.getString(0)) && type_sua == 2) {
                            vitri_groupname_array = count_chomuon;
                        }
                        count_chomuon++;

                    } else if (Integer.parseInt(result.getString(1)) == 3) {
                        // nợ
                        arraylist_group_no.add(result.getString(0));
                        arraylist_group_image_no.add(arrayImage.getIcon(Integer.parseInt(result.getString(2))));
                        if (groupname_sua.equals(result.getString(0)) && type_sua == 3) {
                            vitri_groupname_array = count_no;
                        }
                        count_no++;
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