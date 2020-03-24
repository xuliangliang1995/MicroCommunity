package com.java110.dto.file;

import java.io.Serializable;

public class FileDto  implements Serializable {
    private String fileId;

    private String fileName;

    private String communityId;

    private String context;

    private String statusCd;

    private String suffix;

    private String fileSaveName;

    private boolean upload2Oss;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }

    public boolean isUpload2Oss() {
        return upload2Oss;
    }

    public void setUpload2Oss(boolean upload2Oss) {
        this.upload2Oss = upload2Oss;
    }

    public static final class FileDtoBuilder {
        private String fileId;
        private String fileName;
        private String communityId;
        private String context;
        private String statusCd;
        private String suffix;
        private String fileSaveName;

        private FileDtoBuilder() {
        }

        public static FileDtoBuilder aFileDto() {
            return new FileDtoBuilder();
        }

        public FileDtoBuilder withFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public FileDtoBuilder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileDtoBuilder withCommunityId(String communityId) {
            this.communityId = communityId;
            return this;
        }

        public FileDtoBuilder withContext(String context) {
            this.context = context;
            return this;
        }

        public FileDtoBuilder withStatusCd(String statusCd) {
            this.statusCd = statusCd;
            return this;
        }

        public FileDtoBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public FileDtoBuilder withFileSaveName(String fileSaveName) {
            this.fileSaveName = fileSaveName;
            return this;
        }

        public FileDto build() {
            FileDto fileDto = new FileDto();
            fileDto.setFileId(fileId);
            fileDto.setFileName(fileName);
            fileDto.setCommunityId(communityId);
            fileDto.setContext(context);
            fileDto.setStatusCd(statusCd);
            fileDto.setSuffix(suffix);
            fileDto.setFileSaveName(fileSaveName);
            return fileDto;
        }
    }


    public static final class AliOssBuilder {
        private String context;

        private AliOssBuilder() {
        }

        public static AliOssBuilder aFileDto() {
            return new AliOssBuilder();
        }

        public AliOssBuilder withContext(String context) {
            this.context = context;
            return this;
        }

        public FileDto build() {
            FileDto fileDto = new FileDto();
            fileDto.setContext(context);
            fileDto.setUpload2Oss(true);
            return fileDto;
        }
    }


}
