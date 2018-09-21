import java.util.*;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
import java.text.*;
import java.io.*;
   
public class AnalyticalTool {

	private final static int MAX_LINES = 100000;
	private final static byte MAX_SERVICE = 10;
	private final static byte MAX_VARIATION = 3;
	private final static byte MAX_QUESTION_TYPE = 10; 
	private final static byte MAX_CATEGORY = 20; 
	private final static byte MAX_SUBCATEGORY = 5; 


	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java AnalyticalTool FILE");
			System.exit(1);
		}

		List<String> input = readFromFile(args[0]);
		List<Query> parsedList =  parseQueries(input);
		processQueries(parsedList);
	}

	public static List<String> readFromFile(String filename) {
        List<String> lines = new ArrayList<String>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
        	String line = null;
        	line = bufferedReader.readLine();
        	int count = 0;
        	try {
        		count = Integer.parseInt(line);
        	}
        	catch(NumberFormatException e) {
        		System.out.println("Error: Invalid count value");
        		System.exit(1);
        	}

        	if (count > MAX_LINES) {
        		System.out.println("Error: Count is bigger than " + MAX_LINES);
        		System.exit(1);
        	}
        	while (((line = bufferedReader.readLine()) != null) && (count > 0)) {
            	lines.add(line);
            	count--;
        	}
    	}
     	catch (IOException e) {
         	e.printStackTrace();
        }
        return lines;
	}

	public static boolean validateString(String str) {  
        Pattern patternC = Pattern.compile("^C (\\d+(\\.\\d+)?) (\\d+(\\.\\d+(\\.\\d+)?)?) [P|N] \\d{2}\\.\\d{2}\\.\\d{4} \\d+$");  
        Pattern patternD = Pattern.compile("^D ((\\d+(\\.\\d+)?)|\\*) ((\\d+(\\.\\d+(\\.\\d+)?)?)|\\*) [P|N] (\\d{0,2}\\.\\d{0,2}\\.\\d{4})(-\\d{0,2}\\.\\d{0,2}\\.\\d{4})?$");
        Matcher matcherTypeC = patternC.matcher(str);
        Matcher matcherTypeD = patternD.matcher(str);
        return matcherTypeD.matches() || matcherTypeC.matches();  
    }

    public static List<Query> parseQueries(List<String> inputData) {
    	List<Query> parsedQueries = new ArrayList<>();
    	for(String s : inputData) {
    		if(!validateString(s))
    			continue;

    		String splittedString[] = s.split(" ");
    		Query query = null;
    		if (splittedString[0].equals("C")) {
    			query = parseQueryTypeC(splittedString);
    		}
    		else if(splittedString[0].equals("D")) {
    			query = parseQueryTypeD(splittedString);
    		}
    		if(query != null) {
    			parsedQueries.add(query);
    		}
    	}

    	if (inputData.size() != parsedQueries.size()) {
    		System.out.println("File contains not valid records!");
    	}

    	return parsedQueries;
    }

    public static QueryTypeC parseQueryTypeC(String[] splittedString) {
    	QueryTypeC query = new QueryTypeC(); 
		query.setQueryType('C');

		String service[] = splittedString[1].split("\\.");
		byte serviceId = Byte.parseByte(service[0]);
		if (serviceId > MAX_SERVICE) {
			return null;
		}
		query.setServiceId(serviceId);
		

		if(service.length == 2) {
			byte variationId = Byte.parseByte(service[1]);
			if (variationId > MAX_VARIATION) {
				return null;
			}
			query.setVariationId(variationId);
		}

		String question[] = splittedString[2].split("\\.");
		byte questionTypeId = Byte.parseByte(question[0]);

		if (questionTypeId > MAX_QUESTION_TYPE) {
			return null;
		}
		query.setQuestionTypeId(questionTypeId);
		
		if (question.length >= 2) {
			byte category = Byte.parseByte(question[1]);
			if (category > MAX_CATEGORY) {
				return null;
			}
			query.setCategoryId(category);
			
			if (question.length == 3) {
				byte subCategory = Byte.parseByte(question[2]);
				if (subCategory > MAX_SUBCATEGORY) {
					return null;
				}
				query.setSubCategoryId(subCategory);
			}
		}

		// set N or P
		query.setResponseType(splittedString[3].charAt(0));

    	DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    	try {
    		query.setDate(df.parse(splittedString[4]));
    	}
    	catch (ParseException e) {
    		e.printStackTrace();
    	}

    	query.setTime(Integer.parseInt(splittedString[5]));		

    return query;
    }

    public static QueryTypeD parseQueryTypeD(String[] splittedString) {
    	QueryTypeD query = new QueryTypeD(); 
		query.setQueryType('D');

		String service[] = splittedString[1].split("\\.");

		if(!service[0].equals("*")) {
			byte serviceId = Byte.parseByte(service[0]);
			if (serviceId > MAX_SERVICE) {
				return null;
			}
			query.setServiceId(serviceId);
		
			if(service.length == 2) {
				byte variationId = Byte.parseByte(service[1]);
				if (variationId > MAX_VARIATION) {
					return null;
				}
				query.setVariationId(variationId);
			}
		}

		String question[] = splittedString[2].split("\\.");

		if(!question[0].equals("*")) {
			byte questionTypeId = Byte.parseByte(question[0]);

			if (questionTypeId > MAX_QUESTION_TYPE) {
				return null;
			}
			query.setQuestionTypeId(questionTypeId);
			
			if (question.length >= 2) {
				byte category = Byte.parseByte(question[1]);
				if (category > MAX_CATEGORY) {
					return null;
				}
				query.setCategoryId(category);
				
				if (question.length == 3) {
					byte subCategory = Byte.parseByte(question[2]);
					if (subCategory > MAX_SUBCATEGORY) {
						return null;
					}
					query.setSubCategoryId(subCategory);
				}
			}
		}

		query.setResponseType(splittedString[3].charAt(0));

    	DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    	String dates[] = splittedString[4].split("-");
    	try {
    		query.setFromDate(df.parse(dates[0]));
    		if (dates.length == 2) {
	    		query.setToDate(df.parse(dates[1]));			
    		}
    	}
    	catch (ParseException e) {
    		e.printStackTrace();
    	}

    return query;
    }


    public static void processQueries(List<Query> queries) {
    	for (int i = 0; i < queries.size(); i++) {
    		Query query = queries.get(i);

    		List<Query> fittedQueries = new ArrayList<>();

    		// look for D type query
    		if (query.getQueryType() == 'D') {
    			int j = 0;
    			Query currentQuery = queries.get(0);

    			//if it is founded, look through previous records  
    			while(query != currentQuery) {
    				currentQuery = queries.get(j++);
   					
   					// to skip D type queries
    				if (currentQuery.getQueryType() == 'D') {
    					continue;
    				}

    				// if C type query fitted all parameters, add it to list
    				if (compareServices(query, currentQuery) && compareQuestions(query, currentQuery) 
    					&& compareDates(query, currentQuery) && compareResponseTypes(query, currentQuery)) {
    					fittedQueries.add(currentQuery);
    				}
    			}

    			// if query does not match any data line, print '-' 
    			if (fittedQueries.size() == 0) {
    				System.out.println("-");
    			}
    			// else count average time
    			else {
    				int sum = 0;
    				for(Query q : fittedQueries) {
    					QueryTypeC queryTypeC = (QueryTypeC)q;
    					sum += queryTypeC.getTime();
    				}
    				int average = sum / fittedQueries.size();
    				System.out.println(average);
    			}
    		}
    	}
    } 	

    public static boolean compareServices(Query queryD, Query queryC) {
		if(queryD.getServiceId() == 0) {
			return true;
		}

		if (queryD.getServiceId() == queryC.getServiceId()) {
			if (queryD.getVariationId() == 0) {
				return true;
			}
			else if(queryD.getVariationId() == queryC.getVariationId()) {
				return true;
			}
		}
		return false;    	
    }

    public static boolean compareQuestions(Query queryD, Query queryC) {
    	if (queryD.getQuestionTypeId() == 0) {
    		return true;
    	}

    	if (queryD.getQuestionTypeId() == queryC.getQuestionTypeId()) {
    		if (queryD.getCategoryId() == 0) {
    			return true;
    		}
    		else if(queryD.getCategoryId() == queryC.getCategoryId()) {
    			if(queryD.getSubCategoryId() == 0) {
    				return true;
    			}
    			else if(queryD.getSubCategoryId() == queryC.getSubCategoryId()) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    public static boolean compareDates(Query queryD, Query queryC) {
    	QueryTypeD queryTypeD = (QueryTypeD)queryD;
    	QueryTypeC queryTypeC = (QueryTypeC)queryC;
    	if (queryTypeD.getToDate() == null) {
    		if (queryTypeD.getFromDate().compareTo(queryTypeC.getDate()) == 0) {
    			return true;
    		}
    	}
    	else {
    		if ((queryTypeD.getFromDate().compareTo(queryTypeC.getDate()) <= 0) 
    		   && (queryTypeD.getToDate().compareTo(queryTypeC.getDate()) >= 0)) {
    			return true;			
    		}
    	}
    	return false;
    }	

    public static boolean compareResponseTypes(Query queryD, Query queryC) {
    	return queryD.getResponseType() == queryC.getResponseType();
    }
}