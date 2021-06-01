package at.htlkaindorf.bigbrain.server.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Represents a certain category a card belogns to.
 * E.g.: "What's the name of the highest mountain?" would
 * probably belong into a category like "Geography".
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int cid;
    private String title;
    private String lang;
    @JsonIgnore
    private List<Question> questions;

    public Category(String title, String lang) {
        this.title = title;
        this.lang = lang;
    }

    public Category(int cid, String title, String lang) {
        this.cid = cid;
        this.title = title;
        this.lang = lang;
    }

    public static Category fromResultSet(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("cid"), rs.getString("title"), rs.getString("lang"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return cid == category.cid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid);
    }
}
