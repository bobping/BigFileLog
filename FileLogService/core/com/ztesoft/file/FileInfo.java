package com.ztesoft.file;

public class FileInfo {

	private String fileName;

	private long position;
	
	private Long log_id;
	
	private int logLength;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public Long getLog_id() {
		return log_id;
	}

	public void setLog_id(Long long1) {
		this.log_id = long1;
	}

	@Override
	public String toString() {
		return fileName + "|"+ position +"."+logLength;
	}

	public int getLogLength() {
		return logLength;
	}

	public void setLogLength(int logLength) {
		this.logLength = logLength;
	}


	
	
	
}
