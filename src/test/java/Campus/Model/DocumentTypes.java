package Campus.Model;

public class DocumentTypes {

    private String name;
    private String stage;
    private String id;

    public DocumentTypes(String name, String stage) {
        setName(name);
        setStage(stage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "DocumentTypes{" +
                "name='" + name + '\'' +
                ", stage='" + stage + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
