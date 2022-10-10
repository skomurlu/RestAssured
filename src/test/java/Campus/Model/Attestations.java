package Campus.Model;

public class Attestations {

    private String name;
    private String id;

    public Attestations(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Attestations{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
