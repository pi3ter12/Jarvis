package elearning.wiacekp.pl.jarvis.social;

public class ContactList {
    private int id;
    private String fName;
    private String sName;
    private String phone;
    private String email;

    public ContactList(int id, String fName, String sName, String phone, String email) {
        this.id = id;
        this.fName = fName;
        this.sName = sName;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
