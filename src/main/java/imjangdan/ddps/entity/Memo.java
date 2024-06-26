package imjangdan.ddps.entity;

import jakarta.persistence.*;
import imjangdan.ddps.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Memo extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_ID")
    private Long id;

    @Column(nullable = false)
    private String title; // 사람용 위치 정보

    @Column(nullable = false)
    private String latitude; // 위도

    @Column(nullable = false)
    private String longitude; // 경도

    @Column
    private int score; // 평점

    private String content;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    public Member member;

    // 메모에는 코멘트 없음
//    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @BatchSize(size = 10)
//    public List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    public List<MemoFile> files = new ArrayList<>();

    @Builder
    public Memo(Long id, String title, String latitude, String longitude, String content, int viewCount, int score, Member member) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.content = content;
        this.viewCount = viewCount;
        this.score = score;
        this.member = member;
    }

    //== 조회수 증가 ==//
    public void upViewCount() {
        this.viewCount++;
    }

    //== 수정 Dirty Checking ==//
    public void update(String title, String content, String latitude, String longitude, int score) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
    }

    //== Member & Board 연관관계 편의 메소드 ==//
    public void setMappingMember(Member member) {
        this.member = member;
        member.getMemos().add(this);
    }
}
