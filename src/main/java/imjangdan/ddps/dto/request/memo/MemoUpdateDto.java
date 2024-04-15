package imjangdan.ddps.dto.request.memo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Request-
 * 게시글 수정 정보 요청, 작성자는 Authentication 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class MemoUpdateDto {

    private String title;
    private String content;
    private String latitude;
    private String longitude;
    private String category;

    @Builder
    public MemoUpdateDto(String title, String content, String latitude, String longitude, String category) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }
}
