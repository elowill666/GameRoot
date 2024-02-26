package tw.eeit175groupone.finalproject.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import tw.eeit175groupone.finalproject.domain.ArticlesBean;
import tw.eeit175groupone.finalproject.domain.CommentsBean;
import tw.eeit175groupone.finalproject.domain.ImagesBean;
import tw.eeit175groupone.finalproject.domain.UserBean;

@Data
@NoArgsConstructor
public class ArticleDetailDto {
    //文章的資料
    private ArticlesBean article;
    private UserBean articleuser;
    private List<ImagesBean> images;
    private Integer commentsnumber;
    private Integer likesnumber;
    //留言的資料
    private List<Object[]> commentsdetail;
    private List<ImagesBean> commentsimages;
    private List<Object[]> commentlikesdetail;
    private Map<Integer, Integer> commentLikesMap;
}
