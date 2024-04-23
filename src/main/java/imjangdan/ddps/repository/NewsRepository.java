package imjangdan.ddps.repository;

import imjangdan.ddps.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long> {
    // 첫 페이징 화면("/")
    @Query(value = "SELECT n FROM News n")
    Page<News> findAllNews(Pageable pageable);
}
