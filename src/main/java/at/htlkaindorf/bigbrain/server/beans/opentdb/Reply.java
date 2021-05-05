package at.htlkaindorf.bigbrain.server.beans.opentdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    private int response_code;
    private List<TDBQuestion> results;
}
