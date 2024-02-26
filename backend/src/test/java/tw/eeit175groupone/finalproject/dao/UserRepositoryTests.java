package tw.eeit175groupone.finalproject.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tw.eeit175groupone.finalproject.domain.UserBean;


@SpringBootTest
public class UserRepositoryTests {
    
    @Autowired
    private UserRepository userRepository;

    @Test
	public void testfindAllid(){
    List<Integer> Allid = userRepository.findAllId();
        System.err.println(Allid);
    }
}
