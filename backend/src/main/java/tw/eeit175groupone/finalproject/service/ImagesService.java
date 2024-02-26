package tw.eeit175groupone.finalproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.eeit175groupone.finalproject.dao.ImagesRepository;
import tw.eeit175groupone.finalproject.domain.ImagesBean;

@Service
@Transactional
public class ImagesService {
    @Autowired
    private ImagesRepository imagesRepository;

    public List<ImagesBean> findAllCommentsImagesByArticlesId(Integer articleId) {
        List<ImagesBean> commentsImages = imagesRepository.findAllCommentsImagesByArticlesId(articleId);
        if (commentsImages != null && !commentsImages.isEmpty()) {
            return commentsImages;
        } else {
            return null;
        }
    }

}
