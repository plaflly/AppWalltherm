package wallpl.example.vvvlad.walltherm;

public class ReglaminUpload {
    public String url;
    public String name;

    public ReglaminUpload(String downloadUrl) {
    }


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ReglaminUpload(String url, String name) {
        if (name==null){
            name = "no name";
        }
        this.url = url;
        this.name = name;
    }

}
