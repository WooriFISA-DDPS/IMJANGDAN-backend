package imjangdan.ddps.dto.response.memo;

import imjangdan.ddps.dto.response.comment.ResCommentDto;
import imjangdan.ddps.entity.Memo;
import imjangdan.ddps.dto.response.file.ResMemoDetailsFileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * -Response-
 * 게시글 상세, 수정 요청에 대한 정보를 반환
 */

@Getter
@Setter
@NoArgsConstructor
public class ResMemoDetailsDto {

    // memo info
    private Long memoId;
    private String title;
    private String content;
    private String latitude;
    private String longitude;
    private String category;
    private String writerName;
    private String createdDate;
    private String modifiedDate;


    // file
    private List<ResMemoDetailsFileDto> files;

    @Builder
    public ResMemoDetailsDto(Long memoId, String title, String latitude, String longitude, String category, String content, String writerName, String createdDate, String modifiedDate, List<ResCommentDto> comments, List<ResMemoDetailsFileDto> files) {
        this.memoId = memoId;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.writerName = writerName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.files = files;
    }

    public static ResMemoDetailsDto fromEntity(Memo memo) {
        return ResMemoDetailsDto.builder()
                .memoId(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .writerName(memo.getMember().getUsername())
                .createdDate(memo.getCreatedDate())
                .modifiedDate(memo.getModifiedDate())
                .latitude(memo.getLatitude())
                .longitude(memo.getLongitude())
                .category(memo.getCategory())
                .files(memo.getFiles().stream()
                        .map(ResMemoDetailsFileDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
