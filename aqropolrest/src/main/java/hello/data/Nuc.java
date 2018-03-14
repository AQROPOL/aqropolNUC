package hello.data;

import javax.persistence.*;

/**
 * @author VinYarD
 * created : 13/03/2018, 23:48
 */

@Entity
@Table(name = "nuc")
public class Nuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token", nullable = false)
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Nuc() {}


}
