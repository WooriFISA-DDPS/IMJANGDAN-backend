package imjangdan.ddps.service;

import jakarta.transaction.Transactional;
import imjangdan.ddps.entity.Memo;
import imjangdan.ddps.entity.MemoFile;
import imjangdan.ddps.repository.MemoRepository;
import imjangdan.ddps.common.exception.ResourceNotFoundException;
import imjangdan.ddps.repository.MemoFileRepository;
import imjangdan.ddps.dto.response.file.ResMemoFileDownloadDto;
import imjangdan.ddps.dto.response.file.ResMemoFileUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoFileService {

	private final MemoRepository memoRepository;
	private final MemoFileRepository memoFileRepository;

	@Value("${project.folderPath}")
	private String FOLDER_PATH;

	public List<ResMemoFileUploadDto> upload(Long memoId, List<MultipartFile> multipartFiles) throws IOException {
		// 메모글 찾기
		Memo memo = memoRepository.findById(memoId).orElseThrow(
			() -> new ResourceNotFoundException("Memo", "Memo Id", String.valueOf(memoId))
		);
		List<MemoFile> memoFiles = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			// get origin file name
			String fileName = multipartFile.getOriginalFilename();

			// random name generation of image while uploading to store in folder
			String randomId = UUID.randomUUID().toString();

			// create save File name : ex) POST_memoID_randomID.확장자
			String filePath =
				"POST_" + memo.getId() + "_" + randomId.concat(fileName.substring(fileName.indexOf(".")));

			// File.separator : OS에 따른 구분자 //////////////////////////////////////////???????????
			String fileResourcePath = FOLDER_PATH + File.separator + filePath;

			// create folder if not created
			File f = new File(FOLDER_PATH);
			if (!f.exists()) {
				f.mkdir();
			}

			// file copy in folder
			Files.copy(multipartFile.getInputStream(), Paths.get(fileResourcePath));

			// create File Entity & 연관관게 매핑
			MemoFile saveMemoFile = MemoFile.builder()
				.originFileName(multipartFile.getOriginalFilename())
				.filePath(filePath)
				.fileType(multipartFile.getContentType())
				.build();
			saveMemoFile.setMappingMemo(memo);
			// MemoFile Entity 저장 및 DTO로 변환 전송

			memoFiles.add(memoFileRepository.save(saveMemoFile));
		}
		List<ResMemoFileUploadDto> dtos = memoFiles.stream()
			.map(ResMemoFileUploadDto::fromEntity)
			.collect(Collectors.toList());

		return dtos;
	}

	public ResMemoFileDownloadDto download(Long memoFileId) throws IOException {
		MemoFile memoFile = memoFileRepository.findById(memoFileId).orElseThrow(
			() -> new FileNotFoundException()
		);
		String memoFilePath = FOLDER_PATH + memoFile.getFilePath();
		String contentType = determineContentType(memoFile.getFileType());
		byte[] content = Files.readAllBytes(new File(memoFilePath).toPath());
		return ResMemoFileDownloadDto.fromMemoFileResource(memoFile, contentType, content);
	}

	public void delete(Long fileId) {
		MemoFile file = memoFileRepository.findById(fileId).orElseThrow(
			() -> new ResourceNotFoundException("MemoFile", "File Id", String.valueOf(fileId))
		);

		// local 파일을 삭제
		String filePath = FOLDER_PATH + File.separator + file.getFilePath();
		File physicalFile = new File(filePath);
		if (physicalFile.exists()) {
			physicalFile.delete();
		}
		memoFileRepository.delete(file);
	}

	private String determineContentType(String contentType) {
		// ContentType에 따라 MediaType 결정
		switch (contentType) {
			case "image/png":
				return MediaType.IMAGE_PNG_VALUE;
			case "image/jpeg":
				return MediaType.IMAGE_JPEG_VALUE;
			case "text/plain":
				return MediaType.TEXT_PLAIN_VALUE;
			default:
				return MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
	}
}
