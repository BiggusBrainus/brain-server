package at.htlkaindorf.bigbrain.server.rest.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRequest {
    private String token;
    private String username;
}
