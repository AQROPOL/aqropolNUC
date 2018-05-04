package hello.data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author VinYarD
 * created : 13/03/2018, 15:55
 */

@Entity
@Table(name = "measure")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, targetEntity = Nuc.class)
    @JoinColumn(name = "id_nuc")
    private Nuc nuc;

    @ManyToOne(optional = false, targetEntity = Sensor.class)
    @JoinColumn(name = "id_sensor")
    private Sensor sensor;

    @Column(name = "value", nullable = false)
    private byte[] value;

    public Nuc getNuc() {
        return nuc;
    }

    public void setNuc(Nuc nuc) {
        this.nuc = nuc;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    @Column(name = "hash", nullable = true)
    private byte[] hash;

    @Column(name = "datetime", nullable = false)
    private Timestamp timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Measure() {
    }
}
