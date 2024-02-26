package tw.eeit175groupone.finalproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tw.eeit175groupone.finalproject.dao.UserDashboardRepository;
import tw.eeit175groupone.finalproject.dto.DashboardUser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DashboardUserServiceTest{

    @Autowired
    private DashboardUserService dashboardUserService;

    @Test
    public void findAllUserTest(){
        DashboardUser temp=new DashboardUser();
        temp.setStart(1);
        temp.setRows(10);
        temp.setId(1);
        Map<String, Object> allInfo=dashboardUserService.findAllUser(temp);
        Object tempallUser=allInfo.get("data");
        System.out.println(allInfo);

    }


}
