package wallpl.example.vvvlad.walltherm.Constructors;

public class ConstructorForRuch {
    private String id;
    private String lastEnter;
    private String lastTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastEnter() {
        return lastEnter;
    }

    public void setLastEnter(String lastEnter) {
        this.lastEnter = lastEnter;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public ConstructorForRuch(String id, String lastEnter, String lastTime) {
        this.id = id;
        this.lastEnter = lastEnter;
        this.lastTime = lastTime;
    }

    public ConstructorForRuch() {
    }
}
