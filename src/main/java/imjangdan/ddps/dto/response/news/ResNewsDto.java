package imjangdan.ddps.dto.response.news;

import imjangdan.ddps.entity.News;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ResNewsDto {
    private Long id;
    private String title;
    private String description;
    private String link;
    private LocalDateTime pDate;

    public static ResNewsDto fromEntity(News news) {
        return ResNewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .description(news.getDescription())
                .link(news.getLink())
                .pDate(news.getPDate())
                .build();
    }
}
