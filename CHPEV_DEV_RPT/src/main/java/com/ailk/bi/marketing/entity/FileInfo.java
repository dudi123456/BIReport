package com.ailk.bi.marketing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 【实体类】文件实体类   对应  表【MK_PL_FILE_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_FILE_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileInfo {


	private int fileId;//	NUMBER	N
	private String fileCode;//VARCHAR2(100)	N
	private String fileName;//	VARCHAR2(100)	N
	private long fileSize;//	NUMBER	N
	private int relationshipId;//	NUMBER	N
	private String fileUrl;//	VARCHAR2(500)	N
	@Id
	@Column(name = "FILE_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_FILE_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FILE_ID")
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	@Column(name = "FILE_CODE", nullable = true)
	public String getFileCode() {
		return fileCode;
	}
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
	@Column(name = "FILE_NAME", nullable = true)
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Column(name = "FILE_SIZE", nullable = true)
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	@Column(name = "RELATIONSHIP_ID", nullable = true)
	public int getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
	}
	@Column(name = "FILE_URL", nullable = true)
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

}
