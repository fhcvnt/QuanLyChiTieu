package account;

import naruto.hinata.quanlychitieu.R;

public class BackupAndRestoreItem {
    private int icon; // 0: file, 1: thư mục
    private String path; // đường dẫn lưu trữ file
    private String filename;
    private String capacity; // dung lượng
    private String date; // ngày tạo

    public BackupAndRestoreItem(int icon, String path, String filename, String capacity, String date) {
        this.icon = icon;
        this.path = path;
        this.filename = filename;
        this.capacity = capacity;
        this.date = date;
    }

    public int getIcon() {
        if (icon == 0) {
            return R.drawable.file_24;
        } else if (icon == 1) {
            return R.drawable.folder_24;
        }
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
