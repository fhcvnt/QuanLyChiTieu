package report;

public class ReportItem {
    private String tieude; // tiêu đề
    private String thu;
    private String chi;
    private String chomuon;
    private String no;
    private String groupnamelist;

    public ReportItem(String tieude, String thu, String chi, String chomuon, String no, String groupnamelist) {
        this.tieude = tieude;
        this.thu = thu;
        this.chi = chi;
        this.chomuon = chomuon;
        this.no = no;
        this.groupnamelist = groupnamelist;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getChi() {
        return chi;
    }

    public void setChi(String chi) {
        this.chi = chi;
    }

    public String getChomuon() {
        return chomuon;
    }

    public void setChomuon(String chomuon) {
        this.chomuon = chomuon;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getGroupnamelist() {
        return groupnamelist;
    }

    public void setGroupnamelist(String groupnamelist) {
        this.groupnamelist = groupnamelist;
    }
}

