package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.beans.Category;
import at.htlkaindorf.bigbrain.server.rest.res.errors.GetCategoriesError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version BigBrain v1
 * @since 10.06.2021
 * @author m4ttm00ny
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoriesResponse extends RESTResponse {
    private List<Category> categories;
    private GetCategoriesError error;

    public GetCategoriesResponse(List<Category> categories) {
        this.success = true;
        this.categories = categories;
    }

    public GetCategoriesResponse(GetCategoriesError error) {
        this.success = false;
        this.error = error;
    }
}
