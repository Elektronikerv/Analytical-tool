import java.util.*;

public class QueryTypeD extends Query {
	private Date fromDate;

	private Date toDate;

	public void setFromDate (Date date) {
		this.fromDate = date;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setToDate (Date date) {
		this.toDate = date;
	}

	public Date getToDate() {
		return toDate;
	}

}