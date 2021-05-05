package at.htlkaindorf.bigbrain.server.json;

import at.htlkaindorf.bigbrain.server.beans.Category;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

public class TDBQuestionQuestionDeserializer extends StdDeserializer<String> {

    public TDBQuestionQuestionDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return StringEscapeUtils.unescapeHtml4(deserializationContext.readValue(jsonParser, String.class));
    }
}
