package at.htlkaindorf.bigbrain.server.json;

import at.htlkaindorf.bigbrain.server.beans.Category;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The JSON serializer for a Category object.
 * Used so catgories are only numbers in JSON
 * format.
 * @version BigBrain v1
 * @since 07.04.2021
 * @author m4ttm00ny
 */

public class CategorySerializer extends StdSerializer<Category> {

    public CategorySerializer() {
        super(Category.class);
    }

    @Override
    public void serialize(Category category, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(category.getCid());
    }
}
