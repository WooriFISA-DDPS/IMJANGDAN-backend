package imjangdan.ddps.controller;

import imjangdan.ddps.dto.response.news.ResNewsDto;
import imjangdan.ddps.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    // 페이징 목록
    @GetMapping("/list")
    public ResponseEntity<Page<ResNewsDto>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResNewsDto> listDTO = newsService.getAllNewss(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }
}
