package imjangdan.ddps.service;

import imjangdan.ddps.dto.response.news.ResNewsDto;
import imjangdan.ddps.entity.News;
import imjangdan.ddps.repository.NewsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NewsServiceTest {

    @Mock
    private NewsRepository newsRepositoryMock;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // 목 객체 초기화
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllNewss() {
        // Arrange
        News news = new News(); // 필요에 따라 뉴스 객체를 생성할 수 있습니다.

        // Mocking
        Pageable pageable = mock(Pageable.class);
        when(newsRepositoryMock.findAllNews(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(news)));

        // Act
        Page<ResNewsDto> result = newsService.getAllNewss(pageable);

        // Assert
        assertEquals(1, result.getTotalElements()); // 결과의 총 요소 수가 예상대로인지 확인
    }
}
