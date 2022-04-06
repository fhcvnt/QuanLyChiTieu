package account;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import data_function.Excel;
import data_function.ReadAndWrite;
import naruto.hinata.quanlychitieu.R;
import sqlite.Database;

public class BackupAndRestore extends AppCompatActivity {
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private static final String LOG_TAG = "ExternalStorageQuanLyChiTieu";

    private ListView listview_filename;
    private Database database;
    private ArrayList<BackupAndRestoreItem> arrayListBackupandrestore;
    private BackupAndRestoreAdapter adapterbackupandrestore;

    private String fileName = ""; // file lưu backup
    private String path_file_select = ""; // đường dẫn file đang chọn
    private int vitri_select = -1; // vị trí trong arraydatabackupandrestore đang chọn
    private View view_select = null; // view đang được chọn
    private String file_content_select = ""; // nội dung của file đang chọn
    private String status_backup_restore = "";
    private MenuItem menuitem_backup_restore;
    private MenuItem menuitem_delete_back;
    private int status_directory = 1; //0: hiển thị danh sách file trong thư mục QuanLyChiTieu, 1: hiển thị danh sách file thư mục trong thư mục gốc
    private String path_file_select_now = ""; // đường dẫn thư mục đang chọn

    private final String model_phone = Build.MODEL; // lấy tên model điện thoại
    private ReadAndWrite readAndWrite; //hỏi quyền đọc, ghi dữ liệu lên điện thoại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_and_restore);

        // set color title bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(17, 56, 99)));
        getSupportActionBar().setTitle("Sao lưu và khôi phục");
        // đổi màu nền cho status bar (thanh chứa giờ điện thoại)
        getWindow().setStatusBarColor(Color.BLACK);
        // tao nut quay lai tren thanh tieu de
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AnhXa();

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        listview_filename.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    path_file_select = arrayListBackupandrestore.get(i).getPath();
                    path_file_select_now = path_file_select;
                    vitri_select = i;
                    if (view_select != null) {
                        view_select.setBackgroundColor(Color.WHITE);
                    }
                    view_select = view;
                    view.setBackgroundColor(Color.LTGRAY);

                    File file_path = new File(path_file_select);
                    if (file_path.exists() && file_path.isDirectory()) {
                        listExternalStorages_Root(path_file_select);
                    }

                } catch (Exception exception) {
                }
            }
        });

    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_backupandrestore, menu);
        menuitem_backup_restore = menu.getItem(0);
        menuitem_delete_back = menu.getItem(2);
        if (status_backup_restore.equals("backup")) {
            menuitem_backup_restore.setIcon(R.drawable.backup48);
        } else if (status_backup_restore.equals("export")) {
            menuitem_backup_restore.setIcon(R.drawable.excel64);
        } else if (status_backup_restore.equals("import")) {
            menuitem_backup_restore.setIcon(R.drawable.importexcel64);
        }
        return super.onCreateOptionsMenu(menu);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // bắt sự kiện khi nhấn vào nút sao lưu trên menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuitem_backupandrestore_restore) {
            if (status_backup_restore.equals("restore")) {
                if (readAndWrite.askPermissionAndReadFile()) {
                    readFile();
                }

            } else if (status_backup_restore.equals("backup")) {
                // backup
                if (readAndWrite.askPermissionAndWriteFile()) {
                    writeFile();
                    listExternalStorages_QuanLyChiTieu();
                }

            } else if (status_backup_restore.equals("export")) {
                // export excel
                // Hỏi quyền đọc, ghi file trên điện thoại

                if (readAndWrite.askPermissionAndWriteFile()) {
                    try {
                        File extStore = readAndWrite.getAppExternalFilesDir();
                        String path = extStore.getAbsolutePath() + "/" + "QuanLyChiTieu";
                        Excel.export(path, getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Xuất Excel thành công!", Toast.LENGTH_SHORT).show();
                        listExternalStorages_QuanLyChiTieu();
                    } catch (Exception ex) {
                    }
                }

            } else if (status_backup_restore.equals("import")) {
                // import excel
                if (readAndWrite.askPermissionAndReadFile()) {
                    importExcel();
                }
            }
        } else if (item.getItemId() == R.id.menuitem_backupandrestore_listfolder) {
            if (status_directory == 0) {
                // lấy danh sách file trong thư mục QuanLyChiTieu
                status_directory = 1;
                listExternalStorages_QuanLyChiTieu();
                menuitem_delete_back.setIcon(R.drawable.delete48);
            } else {
                // lấy danh sách file trong thư mục gốc
                status_directory = 0;
                listExternalStorages_Root("");
                menuitem_delete_back.setIcon(R.drawable.back48);
            }
        } else if (item.getItemId() == R.id.menuitem_backupandrestore_delete) {
            try {
                if (path_file_select != null && status_directory == 1) {
                    // xóa
                    if (!path_file_select.isEmpty()) {
                        // kiểm tra xem path_file_select có là file đang tồn tại không
                        File file_delete = new File(path_file_select);
                        if (file_delete.exists()) {
                            if (file_delete.delete()) {
                                path_file_select = "";
                                Toast.makeText(getApplicationContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                arrayListBackupandrestore.remove(vitri_select);
                                adapterbackupandrestore.notifyDataSetChanged();
                                vitri_select = -1;
                                listview_filename.invalidate();
                            }
                        }
                    }
                } else if (status_directory == 0) {
                    // back về thư mục trước đó
                    try {
                        // kiểm tra nếu đã back về thư mục gốc thì không cho back nữa, nếu không sẽ bị lỗi
                        if (!path_file_select_now.equalsIgnoreCase(readAndWrite.getAppExternalFilesDir().toString())) {
                            File file_back = new File(path_file_select_now);
                            if (file_back.getParentFile().exists()) {
                                if (file_back.getParentFile().isDirectory()) {
                                    path_file_select_now = file_back.getParentFile().toString();
                                    listExternalStorages_Root(path_file_select_now);
                                }
                            }
                        }

                    } catch (Exception ex) {
                    }
                }
            } catch (Exception exc) {

            }

        } else if (item.getItemId() == android.R.id.home) {
            // sự kiện nhấn nút quay lại trên thanh tiêu đề
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AnhXa() {
        // Hỏi quyền đọc, ghi file trên điện thoại
        readAndWrite = new ReadAndWrite(getApplicationContext(), BackupAndRestore.this);
        readAndWrite.askPermissionAndWriteFile();
        readAndWrite.askPermissionAndReadFile();

        listview_filename = findViewById(R.id.backupandrestore_listview_listfilename);
        arrayListBackupandrestore = new ArrayList<>();

        // create database QuanLyChiTieu
        database = new Database(getApplicationContext(), "QuanLyChiTieu", null, 3);
        fileName = "Quanlychitieu" + LocalDate.now().toString() + "." + LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + ".qlct";

        Intent intent = getIntent();
        status_backup_restore = "";
        try {
            status_backup_restore = intent.getStringExtra("backup");
        } catch (Exception ex) {
        }
        if (status_backup_restore == null) {
            status_backup_restore = intent.getStringExtra("restore");
        }
        if (status_backup_restore == null) {
            status_backup_restore = intent.getStringExtra("export");
        }
        if (status_backup_restore == null) {
            status_backup_restore = intent.getStringExtra("import");
        }
        if (status_backup_restore == null) {
            status_backup_restore = "backup";
        }

        // sao lưu ra file (backup)
        if (status_backup_restore.equals("backup")) {
            getSupportActionBar().setTitle("Sao lưu");
            // thực hiện hỏi quyền ghi dữ liệu lên điện thoại
            readAndWrite.askPermissionAndWriteFile();
        } else if (status_backup_restore.equals("restore")) {
            // khôi phục từ file (restore)
            getSupportActionBar().setTitle("Khôi phục");
        } else if (status_backup_restore.equals("export")) {
            // xuất ra file excel
            getSupportActionBar().setTitle("Xuất Excel");
        } else if (status_backup_restore.equals("import")) {
            // khôi phục từ file excel
            getSupportActionBar().setTitle("Nhập Excel");
        }

        // hiển thị dữ liệu lên listview
        listExternalStorages_QuanLyChiTieu();

    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void writeFile() {
        try {
            File extStore = readAndWrite.getAppExternalFilesDir();
            // ==> /storage/emulated/0/note.txt  (API < 29)
            // ==> /storage/emulated/0/Android/data/org.o7planning.externalstoragedemo/files/note.txt (API >=29)
            String path = extStore.getAbsolutePath() + "/" + "QuanLyChiTieu";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            path = path + "/" + fileName;

            String data = "";
            // lấy dữ liệu ghi ra file backup
            String select_groupname = "SELECT Id,GroupName,Type,Icon FROM GroupName";
            Cursor result_groupname = database.GetData(select_groupname);
            while (result_groupname.moveToNext()) {
                data = data + "INSERT INTO GroupName VALUES(" + result_groupname.getString(0) + ",'" + result_groupname.getString(1) + "'," + result_groupname.getString(2) + "," + result_groupname.getString(3) + ");" + "\n";
            }

            String select_notelist = "SELECT Id,Money,Type,GroupId,Note,Ngay,Gio FROM Note";
            Cursor result_note = database.GetData(select_notelist);
            while (result_note.moveToNext()) {
                data = data + "INSERT INTO Note VALUES(" + result_note.getString(0) + "," + result_note.getString(1) + "," + result_note.getString(2) + "," + result_note.getString(3) + ",'" + result_note.getString(4) + "','" + result_note.getString(5) + "','" + result_note.getString(6) + "');" + "\n";
            }

            File myFile = new File(path);
            FileOutputStream fout = new FileOutputStream(myFile);
            fout.write(data.getBytes("UTF-8"));
            fout.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ghi lỗi:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void readFile() {
        //File extStore = this.getAppExternalFilesDir();
        // ==> /storage/emulated/0/note.txt  (API < 29)
        // ==> /storage/emulated/0/Android/data/org.o7planning.externalstoragedemo/note.txt (API >=29)
        // String path = extStore.getAbsolutePath() + "/QuanLyChiTieu/" + fileName;
        // Log.i(LOG_TAG, "Read file: " + path);

        try {
            String path = path_file_select;
            // lấy loại file (file type), ví dụ: .qlct,.exe, .docx,...
            String filetype = path_file_select.substring(path_file_select.lastIndexOf("."));
            if (filetype.equalsIgnoreCase(".qlct")) {
                // nếu là file backup của Quản lý chi tiêu .qlct thì mới cho khôi phục dữ liệu, nếu là loại file khác thì bỏ qua không làm gì.
                String s = "";
                file_content_select = "";

                File myFile = new File(path);
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));

                while ((s = myReader.readLine()) != null) {
                    file_content_select += s + "\n";
                }
                myReader.close();
                //  Log.e("QLCT", file_content_select);

                // backup dữ liệu trước, nếu restore lỗi thì vẫn không bị mất dữ liệu
                ArrayList<String> data = new ArrayList<>();
                // lấy dữ liệu ghi ra file backup
                String select_groupname = "SELECT Id,GroupName,Type,Icon FROM GroupName";
                Cursor result_groupname = database.GetData(select_groupname);
                while (result_groupname.moveToNext()) {
                    data.add("INSERT INTO GroupName VALUES(" + result_groupname.getString(0) + ",'" + result_groupname.getString(1) + "'," + result_groupname.getString(2) + "," + result_groupname.getString(3) + ")");
                }

                String select_notelist = "SELECT Id,Money,Type,GroupId,Note,Ngay,Gio FROM Note";
                Cursor result_note = database.GetData(select_notelist);
                while (result_note.moveToNext()) {
                    data.add("INSERT INTO Note VALUES(" + result_note.getString(0) + "," + result_note.getString(1) + "," + result_note.getString(2) + "," + result_note.getString(3) + ",'" + result_note.getString(4) + "','" + result_note.getString(5) + "','" + result_note.getString(6) + "')");
                }

                // xóa tất cả dữ liệu trừ mật khẩu đăng nhập
                database.QueryData("DELETE FROM Note");
                database.QueryData("DELETE FROM GroupName");

                // insert dữ liệu từ file backup vào database
                try {
                    String[] datas = file_content_select.split("\\n");
                    for (int i = 0; i < datas.length; i++) {
                        database.QueryData(datas[i]);
                    }
                    Toast.makeText(getApplicationContext(), "Khôi phục thành công!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Khôi phục thất bại!", Toast.LENGTH_SHORT).show();
                    // insert lại dữ liệu ban đầu khi khôi phục dữ liệu thất bại
                    for (int i = 0; i < data.size(); i++) {
                        database.QueryData(data.get(i));
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void importExcel() {
        try {
            String path = path_file_select;
            // lấy loại file (file type), ví dụ: .qlct,.exe, .docx,...
            String filetype = path_file_select.substring(path_file_select.lastIndexOf("."));
            if (filetype.equalsIgnoreCase(".xls")) {
                // đọc dữ liệu từ file excel
                ArrayList<String> arrayListdata = Excel.readExcel(path_file_select);

                // backup dữ liệu trước, nếu restore lỗi thì vẫn không bị mất dữ liệu
                ArrayList<String> data = new ArrayList<>();
                // lấy dữ liệu ghi ra file backup
                String select_groupname = "SELECT Id,GroupName,Type,Icon FROM GroupName";
                Cursor result_groupname = database.GetData(select_groupname);
                while (result_groupname.moveToNext()) {
                    data.add("INSERT INTO GroupName VALUES(" + result_groupname.getString(0) + ",'" + result_groupname.getString(1) + "'," + result_groupname.getString(2) + "," + result_groupname.getString(3) + ")");
                }

                String select_notelist = "SELECT Id,Money,Type,GroupId,Note,Ngay,Gio FROM Note";
                Cursor result_note = database.GetData(select_notelist);
                while (result_note.moveToNext()) {
                    data.add("INSERT INTO Note VALUES(" + result_note.getString(0) + "," + result_note.getString(1) + "," + result_note.getString(2) + "," + result_note.getString(3) + ",'" + result_note.getString(4) + "','" + result_note.getString(5) + "','" + result_note.getString(6) + "')");
                }

                // xóa tất cả dữ liệu trừ mật khẩu đăng nhập
                database.QueryData("DELETE FROM Note");
                database.QueryData("DELETE FROM GroupName");

                // insert dữ liệu từ file excel vào database
                try {
                    String a = "";
                    for (int i = 0; i < arrayListdata.size(); i = i + 9) {
                        String insert_group = "INSERT INTO GroupName VALUES(" + arrayListdata.get(i) + ",'" + arrayListdata.get(i + 1) + "'," + arrayListdata.get(i + 2) + "," + arrayListdata.get(i + 3) + ")";
                        try {
                            database.QueryData(insert_group);
                        } catch (Exception ex) {
                        }
                        String insert_note = "INSERT INTO Note VALUES(" + arrayListdata.get(i + 4) + "," + arrayListdata.get(i + 5) + "," + arrayListdata.get(i + 2) + "," + arrayListdata.get(i) + ",'" + arrayListdata.get(i + 6) + "','" + arrayListdata.get(i + 7) + "','" + arrayListdata.get(i + 8) + "')";
                        database.QueryData(insert_note);

                    }
                    Toast.makeText(getApplicationContext(), "Nhập excel thành công!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Nhập excel thất bại!", Toast.LENGTH_SHORT).show();
                    // insert lại dữ liệu ban đầu khi nhập excel thất bại
                    for (int i = 0; i < data.size(); i++) {
                        database.QueryData(data.get(i));
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void listExternalStorages_QuanLyChiTieu() {
        try {
            arrayListBackupandrestore.clear();
            File extStore = readAndWrite.getAppExternalFilesDir();
            String path = extStore.getAbsolutePath() + "/" + "QuanLyChiTieu";
            File file = new File(path);
            if (file.exists()) {
                // lấy danh sách file trong thư mục QuanLyChiTieu
                File directory = new File(path);
                File[] files = directory.listFiles();

                for (int i = 0; i < files.length; i++) {
                    int icon_file = files[i].isFile() == true ? 0 : 1;

                    // tính toán dung lượng file
                    String dungluong = "0 Byte";
                    float capacity = (float) files[i].length();
                    if (icon_file == 0) {
                        // là file
                        if (capacity >= 1024) {
                            capacity = capacity / 1024;
                            if (capacity >= 1024) {
                                capacity = capacity / 1024;
                                if (capacity >= 1024) {
                                    capacity = capacity / 1024;
                                    if (capacity >= 1024) {
                                        dungluong = Math.round(capacity * 100) / 100 + " TB";
                                        if (capacity == 1f) {
                                            dungluong = "1 TB";
                                        }
                                    } else {
                                        dungluong = Math.round(capacity * 100) / 100 + " GB";
                                        if (capacity == 1f) {
                                            dungluong = "1 GB";
                                        }
                                    }
                                } else {
                                    dungluong = Math.round(capacity * 100) / 100 + " MB";
                                    if (capacity == 1f) {
                                        dungluong = "1 MB";
                                    }
                                }
                            } else {
                                dungluong = Math.round(capacity * 100) / 100 + " KB";
                                if (capacity == 1f) {
                                    dungluong = "1 KB";
                                }
                            }
                        } else {
                            dungluong = files[i].length() + " Byte";
                        }
                    } else {
                        // là thư mục
                        dungluong = "";
                    }

                    // lấy ngày tạo file
                    String ngaytao = "";
                    try {
                        Path file_ngay = Paths.get(files[i].getPath());
                        BasicFileAttributes attr = Files.readAttributes(file_ngay, BasicFileAttributes.class);
                        ngaytao = attr.creationTime() + "";
                        String[] arrayngay = ngaytao.split("-");
                        ngaytao = arrayngay[2].substring(0, 2) + "/" + arrayngay[1] + "/" + arrayngay[0];
                    } catch (Exception exc) {
                    }
                    if (status_backup_restore.equals("import") || status_backup_restore.equals("export")) {
                        if (icon_file == 1 || files[i].getPath().substring(files[i].getPath().lastIndexOf(".")).equals(".xls")) {
                            arrayListBackupandrestore.add(new BackupAndRestoreItem(icon_file, files[i].getPath(), files[i].getName(), dungluong, "Ngày tạo: " + ngaytao));
                        }
                    } else if (status_backup_restore.equals("backup") || status_backup_restore.equals("restore")) {
                        if (icon_file == 1 || files[i].getPath().substring(files[i].getPath().lastIndexOf(".")).equals(".qlct")) {
                            arrayListBackupandrestore.add(new BackupAndRestoreItem(icon_file, files[i].getPath(), files[i].getName(), dungluong, "Ngày tạo: " + ngaytao));
                        }
                    }
                }
            }

            adapterbackupandrestore = new BackupAndRestoreAdapter(arrayListBackupandrestore, R.layout.listview_item_backupandrestore, this);
            listview_filename.setAdapter(adapterbackupandrestore);
        } catch (Exception exception) {
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void listExternalStorages_Root(String duongdan) {
        try {
            arrayListBackupandrestore.clear();
            File extStore = readAndWrite.getAppExternalFilesDir();
            String path = extStore.getAbsolutePath();
            if (duongdan.isEmpty()) {
                path = extStore.getAbsolutePath();
            } else {
                try {
                    path = duongdan;
                } catch (Exception ex) {
                }
            }

            File file = new File(path);
            if (file.exists()) {
                // lấy danh sách file trong thư mục gốc
                File directory = new File(path);
                File[] files = directory.listFiles();

                for (int i = 0; i < files.length; i++) {
                    int icon_file = files[i].isFile() == true ? 0 : 1;

                    // tính toán dung lượng file
                    String dungluong = "0 Byte";
                    float capacity = (float) files[i].length();
                    if (icon_file == 0) {
                        // là file
                        if (capacity >= 1024) {
                            capacity = capacity / 1024;
                            if (capacity >= 1024) {
                                capacity = capacity / 1024;
                                if (capacity >= 1024) {
                                    capacity = capacity / 1024;
                                    if (capacity >= 1024) {
                                        dungluong = Math.round(capacity * 100) / 100 + " TB";
                                        if (capacity == 1f) {
                                            dungluong = "1 TB";
                                        }
                                    } else {
                                        dungluong = Math.round(capacity * 100) / 100 + " GB";
                                        if (capacity == 1f) {
                                            dungluong = "1 GB";
                                        }
                                    }
                                } else {
                                    dungluong = Math.round(capacity * 100) / 100 + " MB";
                                    if (capacity == 1f) {
                                        dungluong = "1 MB";
                                    }
                                }
                            } else {
                                dungluong = Math.round(capacity * 100) / 100 + " KB";
                                if (capacity == 1f) {
                                    dungluong = "1 KB";
                                }
                            }
                        } else {
                            dungluong = files[i].length() + " Byte";
                        }
                    } else {
                        // là thư mục
                        dungluong = "";
                    }

                    // lấy ngày tạo file
                    String ngaytao = "";
                    try {
                        Path file_ngay = Paths.get(files[i].getPath());
                        BasicFileAttributes attr = Files.readAttributes(file_ngay, BasicFileAttributes.class);
                        ngaytao = attr.creationTime() + "";
                        String[] arrayngay = ngaytao.split("-");
                        ngaytao = arrayngay[2].substring(0, 2) + "/" + arrayngay[1] + "/" + arrayngay[0];
                    } catch (Exception exc) {
                    }
                    //arrayListBackupandrestore.add(new BackupAndRestoreItem(icon_file, files[i].getPath(), files[i].getName(), dungluong, "Ngày tạo: " + ngaytao));
                    if (status_backup_restore.equals("import") || status_backup_restore.equals("export")) {
                        if (icon_file == 1 || files[i].getPath().substring(files[i].getPath().lastIndexOf(".")).equals(".xls")) {
                            arrayListBackupandrestore.add(new BackupAndRestoreItem(icon_file, files[i].getPath(), files[i].getName(), dungluong, "Ngày tạo: " + ngaytao));
                        }
                    } else {
                        if (icon_file == 1 || files[i].getPath().substring(files[i].getPath().lastIndexOf(".")).equals(".qlct")) {
                            arrayListBackupandrestore.add(new BackupAndRestoreItem(icon_file, files[i].getPath(), files[i].getName(), dungluong, "Ngày tạo: " + ngaytao));
                        }
                    }
                }
            }

            adapterbackupandrestore = new BackupAndRestoreAdapter(arrayListBackupandrestore, R.layout.listview_item_backupandrestore, this);
            listview_filename.setAdapter(adapterbackupandrestore);
        } catch (Exception exception) {
        }
    }
}