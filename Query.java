import java.util.*;

public class Query {
	private char queryType;

	private byte serviceId;
	private byte variationId;

	private byte questionTypeId;
	private byte categoryId;
	private byte subCategoryId;

	private char responseType;

	public void setQueryType(char type) {
		this.queryType = type;
	}

	public char getQueryType() {
		return queryType;
	}

	public void setServiceId(byte id) {
		this.serviceId = id;
	}

	public byte getServiceId() {
		return serviceId;
	}

	public void setVariationId(byte id) {
		this.variationId = id;
	}

	public byte getVariationId() {
		return variationId;
	}

	public void setQuestionTypeId(byte id) {
		this.questionTypeId = id;
	}

	public byte getQuestionTypeId() {
		return questionTypeId;
	}

	public void setCategoryId(byte id) {
		this.categoryId = id;
	}

	public byte getCategoryId() {
		return categoryId;
	}

	public void setSubCategoryId(byte id) {
		this.subCategoryId = id;
	}

	public byte getSubCategoryId() {
		return subCategoryId;
	}

	public void setResponseType(char type) {
		this.responseType = type;
	}

	public char getResponseType() {
		return responseType;
	}
}