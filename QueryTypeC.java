import java.util.*;

public class QueryTypeC extends Query {
	private Date date;

	private int time;

	public void setDate (Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setTime (int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

}