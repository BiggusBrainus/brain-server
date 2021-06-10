package at.htlkaindorf.bigbrain.server.rest.res;

import at.htlkaindorf.bigbrain.server.beans.Rank;
import at.htlkaindorf.bigbrain.server.rest.res.errors.GetRankingResponseError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRankingResponse {
    private boolean success;
    private List<Rank> ranking;
    private GetRankingResponseError error;

    public GetRankingResponse(List<Rank> ranking) {
        this.success = true;
        this.ranking = ranking;
    }

    public GetRankingResponse(GetRankingResponseError error) {
        this.success = false;
        this.error = error;
    }
}
