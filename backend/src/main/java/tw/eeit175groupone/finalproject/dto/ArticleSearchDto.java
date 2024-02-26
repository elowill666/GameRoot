package tw.eeit175groupone.finalproject.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleSearchDto {

    private List<Object[]> searchResult;
    private Map<Integer, Integer> searchcommentsnumberMap;
    private Map<Integer, Integer> searchlikesnumberMap;

}
