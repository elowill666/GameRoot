package tw.eeit175groupone.finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private Integer firendId;
    private String username;
    private String userphoto;

}
