package core.trst.com.coretoyobopda.util;

/**
 * Created by Kuncoro on 22/12/2016.
 */

public class ModelData {
    private String id, name, address;

    public ModelData() {
    }

    public ModelData(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
