package wallpl.example.vvvlad.walltherm.Moduly;

public class Modul {
    private String idUrzytkownika;
    private String mailUser;
    private String nazwaModul;
    private String statusModul;
    private String iloscModul;
    private String timeModul;
    private String idModul;
    private String textModul;
    private String iloscPunkt;
    private String TextForUser;

    public String getIdUrzytkownika() {
        return idUrzytkownika;
    }

    public void setIdUrzytkownika(String idUrzytkownika) {
        this.idUrzytkownika = idUrzytkownika;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getNazwaModul() {
        return nazwaModul;
    }

    public void setNazwaModul(String nazwaModul) {
        this.nazwaModul = nazwaModul;
    }

    public String getStatusModul() {
        return statusModul;
    }

    public void setStatusModul(String statusModul) {
        this.statusModul = statusModul;
    }

    public String getIloscModul() {
        return iloscModul;
    }

    public void setIloscModul(String iloscModul) {
        this.iloscModul = iloscModul;
    }

    public String getTimeModul() {
        return timeModul;
    }

    public void setTimeModul(String timeModul) {
        this.timeModul = timeModul;
    }

    public String getIdModul() {
        return idModul;
    }

    public void setIdModul(String idModul) {
        this.idModul = idModul;
    }

    public String getTextModul() {
        return textModul;
    }

    public void setTextModul(String textModul) {
        this.textModul = textModul;
    }

    public String getIloscPunkt() {
        return iloscPunkt;
    }

    public void setIloscPunkt(String iloscPunkt) {
        this.iloscPunkt = iloscPunkt;
    }

    public String getTextForUser() {
        return TextForUser;
    }

    public void setTextForUser(String textForUser) {
        this.TextForUser = textForUser;
    }

    public Modul(String idUrzytkownika, String mailUser, String nazwaModul, String statusModul, String iloscModul, String timeModul, String idModul, String textModul, String iloscPunkt, String textForUser) {
        this.idUrzytkownika = idUrzytkownika;
        this.mailUser = mailUser;
        this.nazwaModul = nazwaModul;
        this.statusModul = statusModul;
        this.iloscModul = iloscModul;
        this.timeModul = timeModul;
        this.idModul = idModul;
        this.textModul = textModul;
        this.iloscPunkt = iloscPunkt;
        this.TextForUser = textForUser;
    }

    public Modul() {
    }
}
