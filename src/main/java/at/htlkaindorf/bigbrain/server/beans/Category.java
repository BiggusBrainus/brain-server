package at.htlkaindorf.bigbrain.server.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<Question> questions;
}
