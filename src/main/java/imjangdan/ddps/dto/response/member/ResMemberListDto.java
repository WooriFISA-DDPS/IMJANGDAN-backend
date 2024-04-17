package imjangdan.ddps.dto.response.member;

import imjangdan.ddps.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Response-
 * list 요청에 대한 정보를 반환, 양방향 관계로 인해 JSON 직렬화가 반복되는 문제를 해결하기 위한 DTO
 */

@Getter
@Setter
@NoArgsConstructor
public class ResMemberListDto {
    // 작성일, 수정일, 작성자, 댓글 개수만 전체 목록에 대한 데이터로 받으면 됨
    // 상세한 댓글 내용 등은 상세보기에서 처리
    private Long memberId;
    private String email;
    private String phone;
    private String regionId;
    private String roles;
    private String username;
    // private String writerName;

    @Builder
    public ResMemberListDto(Long memberId, String email, String phone, String regionId, String roles, String username) {
        this.memberId = memberId;
        this.email = email;
        this.phone = phone;
        this.regionId = regionId;
        this.roles = roles;
        this.username = username;
    }

    // Entity -> DTO
    public static ResMemberListDto fromEntity(Member member) {
        return ResMemberListDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .phone(member.getPhone())
                .regionId(member.getRegionId())
                .roles(String.valueOf(member.getRoles()))
                .username(member.getUsername())
                .build();
    }
}
