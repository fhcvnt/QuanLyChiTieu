package note;

import data_function.DataArrayImage;

public class NoteItem {
    private int id; // mã của ghi chú
    private int imagegroupname; // vị trí của icon trong arrayImage
    private String groupname;
    private String datetime;
    private String money;
    private String note;
    private String countday;
    private DataArrayImage arrayImage=new DataArrayImage();

    public NoteItem(int id, int imagegroupname, String groupname, String datetime, String money, String note, String countday) {
        this.id = id;
        this.imagegroupname = imagegroupname;
        this.groupname = groupname;
        this.datetime = datetime;
        this.money = money;
        this.note = note;
        this.countday = countday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagegroupname() {

        return arrayImage.getIcon(imagegroupname);
    }

    public void setImagegroupname(int imagegroupname) {
        this.imagegroupname = imagegroupname;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCountday() {
        return countday;
    }

    public void setCountday(String countday) {
        this.countday = countday;
    }
}
