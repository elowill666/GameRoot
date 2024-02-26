package tw.eeit175groupone.finalproject.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleListDto {

    private List<Object[]> articleList;
    private Map<Integer, Integer> commentsnumberMap;
    private Map<Integer, Integer> likesnumberMap;

}
