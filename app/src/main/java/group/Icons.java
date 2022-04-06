package group;

public class Icons {
    private String iconname;
    private int icon;

    public Icons(String iconname,int icon) {
        this.iconname= iconname;
        this.icon=icon;
    }

    public String getIconName() {
        return iconname;
    }

    public void setIconName(String iconname) {
        this.iconname = iconname;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
