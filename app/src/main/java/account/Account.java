package account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import group.GroupList;
import naruto.hinata.quanlychitieu.R;
import note.NoteList;
import note.NoteSearch;
import report.Report;
import sqlite.Database;

public class Account extends AppCompatActivity {
    private ImageView imgnote;
    private TextView textviewnote;
    private ImageView imggroup;
    private TextView textgroup;
    private ImageView imgreport;
    private TextView textviewreport;

    private float x1, x2;

    private TextView textView_taikhoanlogin;
    private TextView textView_tentaikhoanlogin;
    private TextView textView_matkhau;
    private TextView textView_xoamatkhau;
    private TextView textView_dongbolencloud;
    private TextView textView_dongbotucloud;
    private TextView textView_xuatexcel;
    private TextView textView_nhaptuexcel;
    private TextView textView_saoluurafile;
    private TextView textView_khoiphuctufile;
    private TextView textView_xoatatcadulieu;
    private TextView textView_exit;
    private ImageView imageView_exit;

    private Database database;

    private MenuItem dangnhap_timkiem;
    private MenuItem dangxuat_dangky;

    private String text_password = ""; // l?? n???i dung c???a textview password
    private int icon_selectpass = 0; // icon password ??ang ???????c ch???n, l??u trong R.drawable.....
    private ImageView img = null; // la image dang chon

    // google signin
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    // ????a d??? li???u l??n firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference mDatabase;
    private boolean synchronized_status = false; // false: ?????ng b??? ch??a k???t th??c, true: ?????ng b??? xong
    private Handler handler = new Handler(); // d??ng cho x??? l?? ??a lu???ng hi???n th??? progressbar khi ?????ng b??? d??? li???u
    private Animation animation_alpha;
    // animation hi???u ???ng x??a
    Animation animation_translate;
    private int select_radio = 0; // radiobutton nao duoc chon, 0: ngay, 1: thang, 2: nam


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        getSupportActionBar().setTitle("T??i kho???n");
        // ?????i m??u n???n cho status bar (thanh ch???a gi??? ??i???n tho???i)
        getWindow().setStatusBarColor(Color.BLACK);

        // d??ng ????? ????ng nh???p google, l???y t??i kho???n ??ang ????ng nh???p tr??n ??i???n tho???i
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getApplicationContext(), gso);

        // animation hi???u ???ng n??t nh???n password
        animation_alpha = AnimationUtils.loadAnimation(this, R.anim.animation_alpha_button_password);
        // hi???u ???ng nh???n n??t x??a
        animation_translate = AnimationUtils.loadAnimation(this, R.anim.animation_translate_backspace);
        // animation hi???u ???ng nh???n
        Animation animation_scale = AnimationUtils.loadAnimation(this, R.anim.animation_scale_login);

        AnhXa();

        // luu du lieu le firebase
        firebase_database = FirebaseDatabase.getInstance();
        mDatabase = firebase_database.getReference();

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // b???t s??? ki???n m??? c???a s??? nh??m khi nh???n v??o h??nh ???nh nh??m
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
        // b???t s??? ki???n m??? c???a s??? nh??m khi nh???n v??o text nh??m
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
        // b???t s??? ki???n m??? c???a s??? b??o c??o khi nh???n v??o h??nh ???nh b??o c??o
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
        // b???t s??? ki???n m??? c???a s??? b??o c??o khi nh???n v??o text b??o c??o
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
        // b???t s??? ki???n m??? c???a s??? ghi ch?? khi nh???n v??o h??nh ???nh ghi ch??
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
        // b???t s??? ki???n m??? c???a s??? ghi ch?? khi nh???n v??o text ghi ch??
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
        // tho??t ph???n m???m
        textView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // tho??t ph???n m???m
        imageView_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                finish();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // ?????t m???t kh???u ho???c thay d???i m???t kh???u
        textView_matkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                displayAlertDialog();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // x??a m???t kh???u
        textView_xoamatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                try {
                    Cursor result = database.GetData("SELECT Status FROM Account");
                    while (result.moveToNext()) {
                        if (result.getString(0).equals("no")) {
                            Toast.makeText(getApplicationContext(), "B???n ch??a ?????t m???t kh???u!", Toast.LENGTH_SHORT).show();

                        } else {
                            // Hi???n th??? th??ng b??o b???n c?? mu???n x??a kh??ng?
                            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                            dialog.setTitle("X??a m???t kh???u");
                            dialog.setMessage("B???n c?? mu???n x??a m???t kh???u kh??ng?");
                            dialog.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // X??a d??? li???u
                                    database.QueryData("UPDATE Account SET Status='no'");
                                    textView_matkhau.setText("?????t m???t kh???u");
                                }

                            });
                            dialog.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            dialog.show();
                        }
                    }
                } catch (Exception ex) {
                }
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // ?????ng b??? d??? li???u l??n cloud
        textView_dongbolencloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                // luu du lieu len firebase
                // Write a message to the database
                // DatabaseReference myRef = firebase_database.getReference("message");
                // DatabaseReference myRef = firebase_database.getReference();
                // myRef.setValue("may la ai");
                // myRef.push().setValue("ansfbkjas");
                // writeNewUser("a1234", "to ngoc tri", "tongoctri@gmail.com");
                synchronized_status = false; // ?????ng b??? ch??a k???t th??c

                // m??? dialog hi???n th??? progressbar
                displayAlertDialogProgressbar();

                String email = textView_tentaikhoanlogin.getText().toString().replace(".", "@");
                // n???u ???? ????ng nh???p r???i m???i cho ?????ng b???
                if (!email.isEmpty()) {
                    DatabaseReference myRef = firebase_database.getReference();
                    // l???y d??? li???u t??? sqlite ????a v??o list
                    ArrayList<String> data_sql = new ArrayList<>();
                    String select_groupname = "SELECT Id,GroupName,Type,Icon FROM GroupName";
                    Cursor result_groupname = database.GetData(select_groupname);
                    while (result_groupname.moveToNext()) {
                        data_sql.add("INSERT INTO GroupName VALUES(" + result_groupname.getString(0) + ",'" + result_groupname.getString(1) + "'," + result_groupname.getString(2) + "," + result_groupname.getString(3) + ")");
                    }

                    String select_notelist = "SELECT Id,Money,Type,GroupId,Note,Ngay,Gio FROM Note";
                    Cursor result_note = database.GetData(select_notelist);
                    while (result_note.moveToNext()) {
                        data_sql.add("INSERT INTO Note VALUES(" + result_note.getString(0) + "," + result_note.getString(1) + "," + result_note.getString(2) + "," + result_note.getString(3) + ",'" + result_note.getString(4) + "','" + result_note.getString(5) + "','" + result_note.getString(6) + "')");
                    }

                    // x??a d??? li???u tr??n firebase tr?????c
                    myRef.child(email).removeValue();
                    // ????a d??? li???u l??n firebase
                    for (int i = 0; i < data_sql.size(); i++) {
                        myRef.child(email).push().setValue(data_sql.get(i));
                    }
                    Toast.makeText(view.getContext(), "?????ng b??? l??n Cloud th??nh c??ng!", Toast.LENGTH_SHORT).show();
                }
                synchronized_status = true; // ?????ng b??? k???t th??c
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // ?????ng b??? d??? li???u t??? cloud xu???ng ??i???n tho???i
        textView_dongbotucloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                synchronized_status = false; // ?????ng b??? ch??a k???t th??c

                // Hi???n th??? th??ng b??o b???n c?? mu???n ?????ng b??? d??? li???u xu???ng kh??ng?
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("X??c nh???n");
                dialog.setMessage("B???n c?? mu???n ?????ng b??? xu???ng kh??ng?");
                dialog.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // m??? dialog hi???n th??? progressbar
                        displayAlertDialogProgressbar();

                        String email = textView_tentaikhoanlogin.getText().toString().replace(".", "@");
                        // n???u ???? ????ng nh???p r???i m???i cho ?????ng b???
                        if (!email.isEmpty()) {
                            // l???y d??? li???u m???t l???n
                            ArrayList<String> data_firebase = new ArrayList<>();
                            mDatabase.child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(view.getContext(), "?????ng b??? th???t b???i!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        for (DataSnapshot snap : task.getResult().getChildren()) {
                                            if (snap.exists()) {
                                                data_firebase.add(snap.getValue(String.class));
                                            }
                                        }

                                        if (data_firebase.size() > 0) {
                                            // x??a t???t c??? d??? li???u tr??? m???t kh???u ????ng nh???p
                                            database.QueryData("DELETE FROM Note");
                                            database.QueryData("DELETE FROM GroupName");
                                            // insert d??? li???u t??? firebase v??o sqlite
                                            for (int i = 0; i < data_firebase.size(); i++) {
                                                database.QueryData(data_firebase.get(i));
                                                Toast.makeText(view.getContext(), "?????ng b??? th??nh c??ng!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        synchronized_status = true; // ?????ng b??? k???t th??c
                    }
                });
                dialog.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // export exel
        textView_xuatexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), BackupAndRestore.class);
                intent.putExtra("export", "export");
                startActivity(intent);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // import excel
        textView_nhaptuexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), BackupAndRestore.class);
                intent.putExtra("import", "import");
                startActivity(intent);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // sao l??u ra file
        textView_saoluurafile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), BackupAndRestore.class);
                intent.putExtra("backup", "backup");
                startActivity(intent);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // kh??i ph???c t??? file
        textView_khoiphuctufile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), BackupAndRestore.class);
                intent.putExtra("restore", "restore");
                startActivity(intent);
            }
        });

//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // x??a t???t c??? d??? li???u
        textView_xoatatcadulieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng thu nh??? khi nh???n
                view.startAnimation(animation_scale);
                try {
                    // Hi???n th??? th??ng b??o b???n c?? mu???n x??a kh??ng?
                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("X??a d??? li???u");
                    dialog.setMessage("B???n c?? mu???n x??a t???t c??? d??? li???u kh??ng?");
                    dialog.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // X??a d??? li???u
                            database.QueryData("UPDATE Account SET Status='no'");
                            database.QueryData("DELETE FROM Note");
                            database.QueryData("DELETE FROM GroupName");
                            textView_matkhau.setText("?????t m???t kh???u");
                        }

                    });
                    dialog.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
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

    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //vu???t h???t m??n h??nh th?? m??? activity kh??c
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                x1 = event.getX();
//                break;
//            case MotionEvent.ACTION_UP:
//                x2 = event.getX();
//                if (x1 >= 0 && x1 < x2 && (x2 - x1 > 400)) {
//                    Intent intent = new Intent();
//                    intent.setClass(this, Report.class);
//                    startActivity(intent);
//                    finish();
//                }
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // t???o menu
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        dangnhap_timkiem = menu.getItem(0);
        dangxuat_dangky = menu.getItem(1);
        // l???y t??n, t??n mail ng?????i d??ng n???u ???? ????ng nh???p t??? tr?????c
        getInfoAccount();
        return super.onCreateOptionsMenu(menu);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // b???t s??? ki???n tr??n menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_account_dangnhap_timkiem) {
            if (item.getTitle().toString().equalsIgnoreCase("T??m ki???m")) {
                // menu search
                Intent intent = new Intent();
                intent.setClass(this, NoteSearch.class);
                startActivity(intent);
            } else if (item.getTitle().toString().equalsIgnoreCase("????ng nh???p")) {
                // ????ng nh???p
                signIn();
            }

        } else if (item.getItemId() == R.id.menu_account_dangxuat_dangky) {
            if (item.getTitle().toString().equalsIgnoreCase("????ng k??")) {
                // ????ng k??
                signIn();
            } else if (item.getTitle().toString().equalsIgnoreCase("????ng xu???t")) {
                // ????ng xu???t
                signOut();
            }
        } else if (item.getItemId() == R.id.menu_account_caidat) {
            // Hi???n th??? dialog setting
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.account_layout_dialog_setting, null);
            RadioGroup radiogroup = (RadioGroup) alertLayout.findViewById(R.id.account_dialog_setting_radioGroup);
            RadioButton radiobutton_ngay = (RadioButton) alertLayout.findViewById(R.id.account_dialog_setting_radioButton_ngay);
            RadioButton radiobutton_thang = (RadioButton) alertLayout.findViewById(R.id.account_dialog_setting_radioButton_thang);
            RadioButton radiobutton_nam = (RadioButton) alertLayout.findViewById(R.id.account_dialog_setting_radioButton_nam);
            EditText text_ngay = (EditText) alertLayout.findViewById(R.id.account_dialog_setting_editText_ngay);
            EditText text_thang = (EditText) alertLayout.findViewById(R.id.account_dialog_setting_editText_thang);
            EditText text_nam = (EditText) alertLayout.findViewById(R.id.account_dialog_setting_editText_nam);

            String number = "", timetype = "";
            Cursor result_setting = database.GetData("SELECT Number,TimeType FROM Setting");
            while (result_setting.moveToNext()) {
                number = result_setting.getString(0);
                timetype = result_setting.getString(1);
            }

            if (timetype.equals("D")) {
                radiogroup.check(R.id.account_dialog_setting_radioButton_ngay);
                select_radio = 0;
                text_ngay.setText(number);
                text_ngay.requestFocus();
                text_thang.setText("");
                text_nam.setText("");
            } else if (timetype.equals("M")) {
                radiogroup.check(R.id.account_dialog_setting_radioButton_thang);
                select_radio = 1;
                text_ngay.setText("");
                text_thang.setText(number);
                text_thang.requestFocus();
                text_nam.setText("");
            } else if (timetype.equals("Y")) {
                radiogroup.check(R.id.account_dialog_setting_radioButton_nam);
                radiobutton_nam.setChecked(true);
                select_radio = 2;
                text_ngay.setText("");
                text_thang.setText("");
                text_nam.setText(number);
                text_nam.requestFocus();
            }

            // b???t s??? ki???n check v??o radiobutton
            radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.account_dialog_setting_radioButton_ngay) {
                        text_ngay.requestFocus();
                        select_radio = 0; // chon radio button ngay
                    } else if (i == R.id.account_dialog_setting_radioButton_thang) {
                        text_thang.requestFocus();
                        select_radio = 1; // chon radio button thang
                    } else if (i == R.id.account_dialog_setting_radioButton_nam) {
                        text_nam.requestFocus();
                        select_radio = 2; // chon radio button nam
                    }
                }
            });


            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Ch???n th???i gian hi???n th??? ghi ch??");
            alert.setView(alertLayout);
            alert.setCancelable(true);
            alert.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        if (select_radio == 0) {
                            database.QueryData("UPDATE Setting SET Number=" + text_ngay.getText() + ",TimeType='D'");
                        } else if (select_radio == 1) {
                            database.QueryData("UPDATE Setting SET Number=" + text_thang.getText() + ",TimeType='M'");
                        } else if (select_radio == 2) {
                            database.QueryData("UPDATE Setting SET Number=" + text_nam.getText() + ",TimeType='Y'");
                        }
                    } catch (Exception ex) {
                    }
                }

            });
            alert.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = alert.create();

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // nhan ket qua tra ve
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                getInfoAccount();
            } catch (Exception e) {
            }
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    void getInfoAccount() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personName = account.getDisplayName();
            String email = account.getEmail();
            textView_taikhoanlogin.setText(personName);
            textView_tentaikhoanlogin.setText(email);

            dangnhap_timkiem.setTitle("T??m ki???m");
            dangnhap_timkiem.setIcon(R.drawable.search_30);
            dangxuat_dangky.setTitle("????ng xu???t");
            // update mail v??o database QuanLyChiTieu
            String update_account = "UPDATE Account SET Email='" + email + "',Name='" + personName + "'";
            database.QueryData(update_account);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                textView_taikhoanlogin.setText("T??i kho???n:");
                textView_tentaikhoanlogin.setText("");
                dangnhap_timkiem.setTitle("????ng nh???p");
                dangnhap_timkiem.setIcon(null);
                dangxuat_dangky.setTitle("????ng k??");
                // update mail v??o database QuanLyChiTieu
                String update_account = "UPDATE FROM Account SET Email='no',Name='no'";
                database.QueryData(update_account);
            }
        });
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AnhXa() {
        imgnote = (ImageView) findViewById(R.id.account_imageView_ghichu);
        textviewnote = (TextView) findViewById(R.id.account_textView_ghichu);
        imggroup = (ImageView) findViewById(R.id.account_imageView_nhom);
        textgroup = (TextView) findViewById(R.id.account_textView_nhom);
        imgreport = (ImageView) findViewById(R.id.account_imageView_baocao);
        textviewreport = (TextView) findViewById(R.id.account_textView_baocao);

        textView_taikhoanlogin = (TextView) findViewById(R.id.account_textView_taikhoanlogin);
        textView_tentaikhoanlogin = (TextView) findViewById(R.id.account_textView_tentaikhoanlogin);
        textView_matkhau = (TextView) findViewById(R.id.account_textView_matkhau);
        textView_xoamatkhau = (TextView) findViewById(R.id.account_textView_xoamatkhau);
        textView_dongbolencloud = (TextView) findViewById(R.id.account_textView_dongbolencloud);
        textView_dongbotucloud = (TextView) findViewById(R.id.account_textView_dongbotucloud);
        textView_xuatexcel = (TextView) findViewById(R.id.account_textView_xuatexcel);
        textView_nhaptuexcel = (TextView) findViewById(R.id.account_textView_nhaptuexcel);
        textView_saoluurafile = (TextView) findViewById(R.id.account_textView_saoluurafile);
        textView_khoiphuctufile = (TextView) findViewById(R.id.account_textView_khoiphuctufile);
        textView_xoatatcadulieu = (TextView) findViewById(R.id.account_textView_xoatatcadulieu);
        textView_exit = (TextView) findViewById(R.id.account_textView_exit);
        imageView_exit = (ImageView) findViewById(R.id.account_imageView_exit);

        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        // create table Account
        // Status=yes: ph???i nh???p m???t kh???u khi m??? ???ng d???ng, status=no: kh??ng c???n nh???p m???t kh???u khi ????ng nh???p
        String sql_account = "CREATE TABLE IF NOT EXISTS Account(Password VARCHAR(100),Status VARCHAR(3) NOT NULL, Email VARCHAR(100), Name VARCHAR(100))";
        database.QueryData(sql_account);

        String select = "SELECT Status FROM Account";
        Cursor result = database.GetData(select);
        String status = "";
        while (result.moveToNext()) {
            status = result.getString(0).toString();
            if (status.equals("yes")) {
                textView_matkhau.setText("Thay ?????i m???t kh???u");
            }
        }
        if (status.isEmpty()) {
            String insert = "INSERT INTO Account VALUES('no','no','no','no')";
            database.QueryData(insert);
        }

        // n???u ???? ????ng nh???p t??? tr?????c th?? v???n gi??? ????ng nh???p
        String select_account = "SELECT Email FROM Account";
        String mail = "no";
        Cursor result_account = database.GetData(select_account);
        while (result_account.moveToNext()) {
            mail = result_account.getString(0);
        }

        if (mail != null) {
            if (!mail.equals("no")) {

                //signIn();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // hi???n th??? th??ng b??o cho ch???n ?????t m???t kh???u
    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.account_layout_custom_dialog_setpassword, null);

        TextView textView_password = (TextView) alertLayout.findViewById(R.id.account_dialog_password_textView_password);
        textView_password.setText("");
        ImageView imageView_backspace = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageview_backspace);
        ImageView imageView_num0 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num0); //a
        ImageView imageView_num1 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num1); //b
        ImageView imageView_num2 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num2); //c
        ImageView imageView_num3 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num3); //d
        ImageView imageView_num4 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num4); //e
        ImageView imageView_num5 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num5); //f
        ImageView imageView_num6 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num6); //g
        ImageView imageView_num7 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num7); //h
        ImageView imageView_num8 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num8); //i
        ImageView imageView_num9 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num9); //j
        ImageView imageView_num10 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num10); //k
        ImageView imageView_num11 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num11); //l
        ImageView imageView_num12 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num12); //m
        ImageView imageView_num13 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num13); //n
        ImageView imageView_num14 = (ImageView) alertLayout.findViewById(R.id.account_dialog_password_imageView_num14); //o

        text_password = ""; // l?? n???i dung c???a textview password
        icon_selectpass = 0; // icon password ??ang ???????c ch???n, l??u trong R.drawable.....
        img = null; // la image dang chon


        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(textView_matkhau.getText().toString());
        alert.setView(alertLayout);
        alert.setCancelable(false);

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // x??a
        imageView_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng x??a
                view.startAnimation(animation_translate);

                String pass = textView_password.getText().toString();
                if (pass.length() > 0) {
                    pass = pass.substring(0, pass.length() - 1);
                    textView_password.setText(pass);
                    text_password = text_password.substring(0, text_password.length() - 1);
                    // x??a ch???n icon password
                    try {
                        img.setImageResource(icon_selectpass);
                    } catch (Exception e) {
                    }
                }
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // x??a h???t
        imageView_backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // hi???u ???ng x??a
                view.startAnimation(animation_translate);

                String pass = textView_password.getText().toString();
                if (pass.length() > 0) {
                    pass = "";
                    textView_password.setText(pass);
                    text_password = "";

                    // x??a ch???n icon password
                    try {
                        img.setImageResource(icon_selectpass);
                    } catch (Exception e) {
                    }
                }

                return true;
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // a
        imageView_num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "a";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num0;
                icon_selectpass = R.drawable.aries128white;
                imageView_num0.setImageResource(R.drawable.aries128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // b
        imageView_num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "b";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num1;
                icon_selectpass = R.drawable.chopper128white;
                imageView_num1.setImageResource(R.drawable.chopper128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // c
        imageView_num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "c";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num2;
                icon_selectpass = R.drawable.erza128white;
                imageView_num2.setImageResource(R.drawable.erza128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // d
        imageView_num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "d";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num3;
                icon_selectpass = R.drawable.fujio128white;
                imageView_num3.setImageResource(R.drawable.fujio128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // e
        imageView_num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "e";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num4;
                icon_selectpass = R.drawable.gon128white;
                imageView_num4.setImageResource(R.drawable.gon128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // f
        imageView_num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "f";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num5;
                icon_selectpass = R.drawable.inuyasha128white;
                imageView_num5.setImageResource(R.drawable.inuyasha128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // g
        imageView_num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "g";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num6;
                icon_selectpass = R.drawable.kiriko128white;
                imageView_num6.setImageResource(R.drawable.kiriko128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // h
        imageView_num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "h";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num7;
                icon_selectpass = R.drawable.lucy128white;
                imageView_num7.setImageResource(R.drawable.lucy128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // i
        imageView_num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "i";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num8;
                icon_selectpass = R.drawable.luffy128white;
                imageView_num8.setImageResource(R.drawable.luffy128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // j
        imageView_num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "j";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num9;
                icon_selectpass = R.drawable.mirajane128white;
                imageView_num9.setImageResource(R.drawable.mirajane128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // k
        imageView_num10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "k";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num10;
                icon_selectpass = R.drawable.natsu128white;
                imageView_num10.setImageResource(R.drawable.natsu128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // l
        imageView_num11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "l";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num11;
                icon_selectpass = R.drawable.orihime128white;
                imageView_num11.setImageResource(R.drawable.orihime128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // m
        imageView_num12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "m";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num12;
                icon_selectpass = R.drawable.saitama128white;
                imageView_num12.setImageResource(R.drawable.saitama128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // n
        imageView_num13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "n";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num13;
                icon_selectpass = R.drawable.songoku128white;
                imageView_num13.setImageResource(R.drawable.songoku128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // o
        imageView_num14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hi???u ???ng animation m??? d???n
                view.startAnimation(animation_alpha);

                text_password = text_password + "o";
                textView_password.append("*");
                try {
                    img.setImageResource(icon_selectpass);
                } catch (Exception e) {
                }
                img = imageView_num14;
                icon_selectpass = R.drawable.yukino128white;
                imageView_num14.setImageResource(R.drawable.yukino128red);
            }
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        alert.setPositiveButton("X??c nh???n", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // update data to table Account
                database.QueryData("UPDATE Account SET Password='" + text_password + "',Status='yes'");
                textView_matkhau.setText("Thay ?????i m???t kh???u");
            }
        });
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        alert.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // hi???n th??? progressbar xoay tron cho dong bo ket thuc
    public void displayAlertDialogProgressbar() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.account_syn_progressbar, null);
        final ProgressBar progressBar = (ProgressBar) alertLayout.findViewById(R.id.account_dialog_progressBar_syn);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        doStartProgressBar(progressBar, dialog);
        dialog.show();

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void doStartProgressBar(ProgressBar progressBar, AlertDialog dialog) {
        progressBar.setIndeterminate(true);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Update interface
                handler.post(new Runnable() {
                    public void run() {
                    }
                });
                // Do something ... (Update database,..)
                SystemClock.sleep(500); // Sleep 0.5 seconds.

                // Update interface
                handler.post(new Runnable() {
                    public void run() {
                        // ????ng c???a s??? dialog
                        if (synchronized_status) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        thread.start();
    }
}