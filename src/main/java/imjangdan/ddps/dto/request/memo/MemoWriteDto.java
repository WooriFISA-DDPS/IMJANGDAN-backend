package imjangdan.ddps.dto.request.memo;

import imjangdan.ddps.entity.Memo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Request-
 * 게시글 등록 정보 요청, 작성자는 Authentication 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class MemoWriteDto {

    private String title;
    private String content;
    private String latitude;
    private String longitude;
    private String category;
    private int score;

    public MemoWriteDto(String title, String content, String latitude, String longitude) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    @Builder
    public static Memo ofEntity(MemoWriteDto dto) {
        return Memo.builder()
                .title(dto.title)
                .content(dto.content)
                .latitude(dto.latitude)
                .longitude(dto.longitude)
                .category(dto.category)
                .build();
    }
}
