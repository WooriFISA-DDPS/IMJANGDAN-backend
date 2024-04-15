package imjangdan.ddps.dto.response.memo;

import imjangdan.ddps.entity.Memo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Response-
 * 게시글 등록 반환 정보
 */

@Getter
@Setter
@NoArgsConstructor
public class ResMemoWriteDto {

    private Long memoId;
    private String title;
    private String content;
    private String latitude;
    private String longitude;
    private String category;
    private String writerName;
    private String createdDate;

    @Builder
    public ResMemoWriteDto(Long memoId, String title, String content, String latitude, String longitude, String category, String writerName, String createdDate) {
        this.memoId = memoId;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.writerName = writerName;
        this.createdDate = createdDate;
    }

    public static ResMemoWriteDto fromEntity(Memo memo, String writerName) {
        return ResMemoWriteDto.builder()
                .memoId(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .latitude(memo.getLatitude())
                .longitude(memo.getLongitude())
                .category(memo.getCategory())
                .writerName(writerName)
                .createdDate(memo.getCreatedDate())
                .build();
    }
}
