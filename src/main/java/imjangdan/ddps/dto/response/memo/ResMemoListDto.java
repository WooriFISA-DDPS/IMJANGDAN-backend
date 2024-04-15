package imjangdan.ddps.dto.response.memo;

import imjangdan.ddps.dto.response.comment.ResCommentDto;
import imjangdan.ddps.dto.response.file.ResMemoDetailsFileDto;
import imjangdan.ddps.entity.Memo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * -Response-
 * list 요청에 대한 정보를 반환, 양방향 관계로 인해 JSON 직렬화가 반복되는 문제를 해결하기 위한 DTO
 */

@Getter
@Setter
@NoArgsConstructor
public class ResMemoListDto {
    // 작성일, 수정일, 작성자, 댓글 개수만 전체 목록에 대한 데이터로 받으면 됨
    // 상세한 댓글 내용 등은 상세보기에서 처리
    private Long memoId;
    private String title;
    private String content;
    private String latitude;
    private String longitude;
    private int score;
    private int viewCount;
    private String createdDate;
    private String modifiedDate;
    private String writerName;

    @Builder
    public ResMemoListDto(Long memoId, String title, String latitude, String longitude, int score, String content, int viewCount, String writerName, String createdDate, String modifiedDate, List<ResCommentDto> comments, List<ResMemoDetailsFileDto> files) {
        this.memoId = memoId;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.viewCount = viewCount;
        this.writerName = writerName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    // Entity -> DTO
    public static ResMemoListDto fromEntity(Memo memo) {
        return ResMemoListDto.builder()
                .memoId(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .latitude(memo.getLatitude())
                .longitude(memo.getLongitude())
                .score(memo.getScore())
                .viewCount(memo.getViewCount())
                .createdDate(memo.getCreatedDate())
                .modifiedDate(memo.getModifiedDate())
                .writerName(memo.getMember().getUsername())
                .build();
    }
}
