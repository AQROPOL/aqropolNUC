package istic.project.aqropol.mom_consumer.data;

import javax.persistence.*;

/**
 * @author VinYarD
 * created : 13/03/2018, 15:55
 */

@Entity
@Table(name = "sensor", uniqueConstraints = @UniqueConstraint(columnNames={"name", "type", "unity"}))
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Override
    public String toString() {
        return "istic.project.aqropol.mom_consumer.data.Sensor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", unity='" + unity + '\'' +
                '}';
    }

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "unity", nullable = false)
    private String unity;

    public Sensor() {
        // TODO
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }
}
