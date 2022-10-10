package Campus.Model;

public class HrPositionCategory {

    private String name;
    private String id;

    public HrPositionCategory(String name) {
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
        return "HrPositionCategoryTest{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
