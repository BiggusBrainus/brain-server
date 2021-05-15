package at.htlkaindorf.bigbrain.server.rest.req;

import at.htlkaindorf.bigbrain.server.beans.Category;
import lombok.Data;

import java.util.List;

@Data
public class GetLobbiesRequest {
    private String cids;
}
