package at.htlkaindorf.bigbrain.server.json;

import at.htlkaindorf.bigbrain.server.beans.Category;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

/**
 * The JSON Deserializer for a TDBQuestion's
 * question string, because it contains HTML
 * character escape codes.
 * @version BigBrain v1
 * @since 05.05.2021
 * @author m4ttm00ny
 */

public class TDBQuestionQuestionDeserializer extends StdDeserializer<String> {

    public TDBQuestionQuestionDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return StringEscapeUtils.unescapeHtml4(deserializationContext.readValue(jsonParser, String.class));
    }
}
