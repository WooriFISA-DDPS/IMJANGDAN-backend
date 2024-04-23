package imjangdan.ddps.service;

import imjangdan.ddps.dto.response.news.ResNewsDto;
import imjangdan.ddps.entity.News;
import imjangdan.ddps.repository.NewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;

    // 페이징 리스트
    public Page<ResNewsDto> getAllNewss(Pageable pageable) {
        Page<News> newss = newsRepository.findAllNews(pageable);
        List<ResNewsDto> list = newss.getContent().stream()
                .map(ResNewsDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, newss.getTotalElements());
    }
}
