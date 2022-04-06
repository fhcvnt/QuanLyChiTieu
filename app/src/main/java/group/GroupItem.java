package group;

import data_function.DataArrayImage;

public class GroupItem {
    private int imageicon; // vị trí icon của nhóm trong arrayListImageGroupname
    private String groupname; // tên nhóm
    private int imagedelete; // icon delete
    private String groupid; // co dang 1000000, 1000001, 1000002,...
    private DataArrayImage arrayImage=new DataArrayImage();

    public GroupItem(int imageicon, String groupname, int imagedelete, String groupid) {
        this.imageicon = imageicon;
        this.groupname = groupname;
        this.imagedelete = imagedelete;
        this.groupid = groupid;
    }

    public int getImageicon() {
        return arrayImage.getIcon(imageicon);
    }

    public void setImageicon(int imageicon) {

        this.imageicon = imageicon;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getImagedelete() {
        return imagedelete;
    }

    public void setImagedelete(int imagedelete) {
        this.imagedelete = imagedelete;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
