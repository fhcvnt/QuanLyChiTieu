package account;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import data_function.ReadAndWrite;
import naruto.hinata.quanlychitieu.R;
import note.NoteList;
import sqlite.Database;

public class Login extends AppCompatActivity {
    private TextView textView_password;
    private ImageView imageView_backspace;
    private ImageView imageView_num0;
    private ImageView imageView_num1;
    private ImageView imageView_num2;
    private ImageView imageView_num3;
    private ImageView imageView_num4;
    private ImageView imageView_num5;
    private ImageView imageView_num6;
    private ImageView imageView_num7;
    private ImageView imageView_num8;
    private ImageView imageView_num9;
    private ImageView imageView_num10;
    private ImageView imageView_num11;
    private ImageView imageView_num12;
    private ImageView imageView_num13;
    private ImageView imageView_num14;
    private ImageView imageView_login;

    private Database database;
    private String status = ""; // status="yes": có mật khẩu đăng nhập, status="no": không có mật khẩu đăng nhập
    private String password = ""; // là mật khẩu lưu tron table Account, nếu status="yes" thì kiểm tra đăng nhập
    private String text_password = ""; // là nội dung của textview password
    private int icon_selectpass = 0; // icon password đang được chọn, lưu trong R.drawable.....
    private ImageView img; // la image dang chon dung de thay doi borser cho image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnhXa();

        // animation hiệu ứng xóa
        Animation animation_translate = AnimationUtils.loadAnimation(this, R.anim.animation_translate_backspace);
        // animation hiệu ứng login
        Animation animation_scale = AnimationUtils.loadAnimation(this, R.anim.animation_scale_login);

        // animation hiệu ứng nút nhấn password
        Animation animation_alpha = AnimationUtils.loadAnimation(this, R.anim.animation_alpha_button_password);


        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // xóa
        imageView_backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation_translate);
                String pass = textView_password.getText().toString();
                if (pass.length() > 0) {
                    pass = pass.substring(0, pass.length() - 1);
                    textView_password.setText(pass);
                    text_password = text_password.substring(0, text_password.length() - 1);
                    // xóa chọn icon password
                    try {
                        img.setImageResource(icon_selectpass);
                    } catch (Exception e) {
                    }
                }
            }
        });

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // xóa hết
        imageView_backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startAnimation(animation_translate);
                String pass = textView_password.getText().toString();
                if (pass.length() > 0) {
                    pass = "";
                    textView_password.setText(pass);
                    text_password = "";

                    // xóa chọn icon password
                    try {
                        img.setImageResource(icon_selectpass);
                    } catch (Exception e) {
                    }
                }

                return true;
            }
        });

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // đăng nhập nếu nhập đúng mật khẩu
        imageView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation_scale);
                if (status.equals("yes") && password.equals(text_password)) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), NoteList.class);
                    startActivity(intent);
                    finish();
                } else if (status.equals("no") || status.isEmpty()) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), NoteList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // a
        imageView_num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // b
        imageView_num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // c
        imageView_num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // d
        imageView_num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // e
        imageView_num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // f
        imageView_num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // g
        imageView_num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // h
        imageView_num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // i
        imageView_num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // j
        imageView_num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // k
        imageView_num10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // l
        imageView_num11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // m
        imageView_num12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // n
        imageView_num13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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

        // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
        // o
        imageView_num14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hiệu ứng animation mờ dần
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
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AnhXa() {
        textView_password = (TextView) findViewById(R.id.login_textView_password);
        textView_password.setText("");
        imageView_backspace = (ImageView) findViewById(R.id.login_imageView_backspace);
        imageView_num0 = (ImageView) findViewById(R.id.login_imageView_num0); //a
        imageView_num1 = (ImageView) findViewById(R.id.login_imageView_num1); //b
        imageView_num2 = (ImageView) findViewById(R.id.login_imageView_num2); //c
        imageView_num3 = (ImageView) findViewById(R.id.login_imageView_num3); //d
        imageView_num4 = (ImageView) findViewById(R.id.login_imageView_num4); //e
        imageView_num5 = (ImageView) findViewById(R.id.login_imageView_num5); //f
        imageView_num6 = (ImageView) findViewById(R.id.login_imageView_num6); //g
        imageView_num7 = (ImageView) findViewById(R.id.login_imageView_num7); //h
        imageView_num8 = (ImageView) findViewById(R.id.login_imageView_num8); //i
        imageView_num9 = (ImageView) findViewById(R.id.login_imageView_num9); //j
        imageView_num10 = (ImageView) findViewById(R.id.login_imageView_num10); //k
        imageView_num11 = (ImageView) findViewById(R.id.login_imageView_num11); //l
        imageView_num12 = (ImageView) findViewById(R.id.login_imageView_num12); //m
        imageView_num13 = (ImageView) findViewById(R.id.login_imageView_num13); //n
        imageView_num14 = (ImageView) findViewById(R.id.login_imageView_num14); //o
        imageView_login = (ImageView) findViewById(R.id.login_imageView_login);

        ImageView imageView_tamac = (ImageView) findViewById(R.id.login_imageView_hinhdaidien);
        // animation hiệu ứng Tả Mạc
        Animation animation_scale_tamac = AnimationUtils.loadAnimation(this, R.anim.animation_scale_tamac);
        imageView_tamac.startAnimation(animation_scale_tamac);

        ImageView imageView_sharingan1 = (ImageView) findViewById(R.id.login_imageView_sharingan1);
        ImageView imageView_sharingan2 = (ImageView) findViewById(R.id.login_imageView_sharingan2);
        ImageView imageView_sharingan3 = (ImageView) findViewById(R.id.login_imageView_sharingan3);
        ImageView imageView_sharingan4 = (ImageView) findViewById(R.id.login_imageView_sharingan4);
        ImageView imageView_sharingan5 = (ImageView) findViewById(R.id.login_imageView_sharingan5);
        // animation hiệu ứng sharingan
        Animation animation_rotate_sharingan = AnimationUtils.loadAnimation(this, R.anim.animation_rotate_sharingan);
        Animation animation_rotate_sharingan2 = AnimationUtils.loadAnimation(this, R.anim.animation_rotate_sharingan);
        Animation animation_rotate_sharingan3 = AnimationUtils.loadAnimation(this, R.anim.animation_rotate_sharingan);
        Animation animation_rotate_sharingan4 = AnimationUtils.loadAnimation(this, R.anim.animation_rotate_sharingan);
        Animation animation_rotate_sharingan5 = AnimationUtils.loadAnimation(this, R.anim.animation_rotate_sharingan);
        imageView_sharingan1.startAnimation(animation_rotate_sharingan);
        imageView_sharingan2.startAnimation(animation_rotate_sharingan2);
        imageView_sharingan3.startAnimation(animation_rotate_sharingan3);
        imageView_sharingan4.startAnimation(animation_rotate_sharingan4);
        imageView_sharingan5.startAnimation(animation_rotate_sharingan5);

        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        // create table Account
        // Status=yes: phải nhập mật khẩu khi mở ứng dụng, status=no: không cần nhập mật khẩu khi đăng nhập
        String sql_account = "CREATE TABLE IF NOT EXISTS Account(Password VARCHAR(100),Status VARCHAR(3) NOT NULL, Email VARCHAR(100), Name VARCHAR(100))";
        database.QueryData(sql_account);

        // tạo table quản lý cài đặt Setting
        // TimeType: "D" ngày, "M" tháng, "Y" năm
        String sql_setting = "CREATE TABLE IF NOT EXISTS Setting(Number INTEGER UNIQUE NOT NULL, TimeType VARCHAR (1))";
        database.QueryData(sql_setting);
        String number = "";
        Cursor result_setting = database.GetData("SELECT Number,TimeType FROM Setting");
        while (result_setting.moveToNext()) {
            number = result_setting.getString(0);
        }
        if (number == null || number.isEmpty()) {
            // mặc định là 1 năm
            String insert = "INSERT INTO Setting VALUES(1,'Y')";
            database.QueryData(insert);
        }

        String select = "SELECT Password,Status FROM Account";
        Cursor result = database.GetData(select);
        status = "";
        while (result.moveToNext()) {
            status = result.getString(1).toString();
            if (status.equals("yes")) {
                password = result.getString(0).toString();
            }
        }
        if (status.isEmpty()) {
            String insert = "INSERT INTO Account VALUES('no','no','no','no')";
            database.QueryData(insert);
        }

        // đăng nhập luôn nếu không có mật khẩu, status=no
        if (status.equals("no") || status.isEmpty()) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), NoteList.class);
            startActivity(intent);
            finish();
        } else {
            // Hỏi quyền đọc, ghi file trên điện thoại
            ReadAndWrite readAndWrite = new ReadAndWrite(getApplicationContext(), Login.this);
            readAndWrite.askPermissionAndWriteFile();
            readAndWrite.askPermissionAndReadFile();
        }
    }
}