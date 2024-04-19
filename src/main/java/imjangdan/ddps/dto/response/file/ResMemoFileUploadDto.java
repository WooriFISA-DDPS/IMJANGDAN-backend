package imjangdan.ddps.dto.response.file;

import imjangdan.ddps.entity.MemoFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Response-
 * 메모 파일 업로드 후 응답 dto
 */

@Getter
@Setter
@NoArgsConstructor
public class ResMemoFileUploadDto {

	private Long fileId;
	private String originFileName;
	private String filePath;
	private String fileType;

	@Builder
	public ResMemoFileUploadDto(Long fileId, String originFileName, String filePath, String fileType) {
		this.fileId = fileId;
		this.originFileName = originFileName;
		this.filePath = filePath;
		this.fileType = fileType;
	}

	public static ResMemoFileUploadDto fromEntity(MemoFile file) {
		return ResMemoFileUploadDto.builder()
			.fileId(file.getId())
			.originFileName(file.getOriginFileName())
			.filePath(file.getFilePath())
			.fileType(file.getFileType())
			.build();
	}
}
