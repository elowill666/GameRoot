package tw.eeit175groupone.finalproject.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticlesDto {

    private Integer articlesId;
    private Integer userId;
    private String articleGameType;
    private String articleHead;
    private String articleText;
    private Date updateAt;
    private String articleType;

}
