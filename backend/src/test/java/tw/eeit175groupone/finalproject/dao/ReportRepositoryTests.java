package tw.eeit175groupone.finalproject.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.domain.ReportBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ReportRepositoryTests{

    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void countReportsNumberByIdTest(){
        System.out.println(reportRepository.countReportsNumberById(1));
    }

    @Test
    public void countReportsNumberTest(){
        List<Object[]> temp=reportRepository.countReportsNumberForArticle();
        Map<Integer, Integer> map=new HashMap<>();
        if(temp!=null){
            for(Object[] o : temp){
                map.put((Integer) o[0],((Long) o[1]).intValue());
            }
            System.out.println(map);
        }
    }

    @Test
    public void countReportsNumberForCommentTest(){
        List<Object[]> temp=reportRepository.countReportsNumberForComment();
        if(temp!=null){
            Map<Integer, Integer> map=new HashMap<>();
            for(Object[] otemp : temp){
                map.put((Integer) otemp[0],((Long) otemp[1]).intValue());
            }
            System.out.println(map);

        }
    }


    @Test
    public void countByReportedArticlesIdTest(){
        Integer temp=reportRepository.countByReportedArticlesId(1);
        if(temp!=null){

            System.out.println(temp);

        }
    }

    @Test
    public void countByReportedCommentsIdTest(){
        Integer temp=reportRepository.countByReportedCommentsId(2);
        if(temp!=null){

            System.out.println(temp);

        }
    }

    @Test
    public void findByReportedArticlesIdTest(){
        List<ReportBean> temp=reportRepository.findByReportedArticlesId(71);
        System.out.println(temp);
        if(temp!=null){
            System.out.println("???");
            for(ReportBean r : temp){
                System.out.println(r);
            }
        }
    }


    @Test
    public void findByReportedCommentsIdTest(){
        List<ReportBean> temp=reportRepository.findByReportedCommentsId(1);
        if(temp!=null){
            for(ReportBean r : temp){
                System.out.println(r);
            }
        }
    }

    @Test
    public void findByReporterUserIdTest(){
        List<ReportBean> temp=reportRepository.findByReporterUserId(1);
        if(temp!=null){
            for(ReportBean r : temp){
                System.out.println(r);
            }
        }
    }
}
