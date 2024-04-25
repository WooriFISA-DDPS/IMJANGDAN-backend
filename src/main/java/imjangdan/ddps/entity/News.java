package imjangdan.ddps.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @Column(name = "pDate")
    private LocalDateTime pDate;

    @Builder
    public News(Long id, String title, String description, String link, LocalDateTime pDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pDate = pDate;
    }
}
