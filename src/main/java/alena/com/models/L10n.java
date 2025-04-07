package alena.com.models;
import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(
        name = "l10n"
)
@NamedQueries({ @NamedQuery(
        name = "GetMenu",
        query = "select a from L10n a where a.language=:language and a.stringid=:stringid"
)})
public class L10n implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int id;
    @Column(
            name = "stringid"
    )
    private String stringid;
    public String getStringid() {
        return stringid;
    }

    public void setStringid(String stringid) {
        this.stringid = stringid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    @Column(
            name = "language"
    )
    private String language;
    @Column(
            name = "content"
    )
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }


}
