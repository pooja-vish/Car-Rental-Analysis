package pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BudgetParser {
	public void convertFileToWebPage (File folder, String filename, Dictionary dict, int key) {
		try {
			//location from where we need to access the file
			String loc=folder+"\\"+filename;
			//Creating BufferedReader object for reading the content of the file 
			BufferedReader bfReader = new BufferedReader(new FileReader(loc));
			String readFromFile;
			String fileContents = "";
			//Extracting the name of the file by removing the extension and storing in a variable
			String fileName = filename.substring(0,filename.lastIndexOf("."));
			//Reading content of file line by line
			while((readFromFile = bfReader.readLine()) != null) {
				fileContents += "\n" + readFromFile;
			}
			//Creating html folder where we will convert txt file to html and store in the location
			String file=folder+"\\html\\"+fileName+".html";
			//Creating txt folder where we will store the parsed information which is required for further analysis
			String outFile=folder + "\\txt\\"+fileName+ ".txt";
			//Creating BufferedWriter object for writing the html file
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write(fileContents);
		    writer.close();
		    File input = new File(file);
		    Document doc = Jsoup.parse(input, "UTF-8", "");
		    BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		    //From the converted html file extracting the Elements which are required for further analysis
		    Elements price = doc.getElementsByClass("payamntr");
		    int itr1=0;
		    int itr2=0;
		    String output="";
		    Elements car_option = doc.getElementsByClass("featured-car-fac-for-mob");
		    Elements facilities = doc.getElementsByClass("available-car-fac hidden");
		    //Storing the parsed content in FrequencyCount object along with file
		    //The one stored in FrequencyCount object will be used for counting the word frequency
		    for(int itr=0; itr<car_option.size()&&itr1<price.size()&&itr2<facilities.size(); itr++) {
		    	FrequencyCount freqCount = new FrequencyCount();
		    	freqCount.websiteName="https://www.budget.ca";
		    	output += "Pick up location: "+ fileName + "\n";
		    	freqCount.pickUpLocation = fileName;
		    	output += car_option.get(itr).select("h4").text() + "\n";
		    	freqCount.carType = car_option.get(itr).select("h4").text();
		    	output += car_option.get(itr).getElementsByClass("feat-car-text-mob").text() + "\n";
		    	freqCount.carModel = car_option.get(itr).getElementsByClass("feat-car-text-mob").text();
		    	output += price.get(itr1).select("p").text();
		    	try {
		    		String value = price.get(itr1).select("p").text();
			    	value=value.substring(value.indexOf("$")+1, value.indexOf(" "));
		    	    freqCount.price = Float.parseFloat(value);
		    	}
		    	catch(Exception ex) {
		    		freqCount.price=0;
		    	}
		    	output += facilities.get(itr2).getElementsByClass("icon-smoke-free").text() + "\n";
		    	output += facilities.get(itr2).getElementsByClass("four-door-feat").text() + "\n";
		    	output += facilities.get(itr2).getElementsByClass("four-seats-feat").text()+ "\n";
		    	String str=facilities.get(itr2).getElementsByClass("four-seats-feat").text();
		    	int number1=0;
		    	if(str.indexOf(" ") > 0) {
		    		str=str.substring(0, str.indexOf(" "));
		    		number1=Integer.parseInt(str);
		    	}
		    	freqCount.numberOfSeats = number1;
		    	output += facilities.get(itr2).getElementsByClass("four-automatic-feat").text()+ "\n";
		    	output += facilities.get(itr2).getElementsByClass("ac-seats-feat").text()+ "\n";
		    	output += facilities.get(itr2).getElementsByClass("four-bags-feat").text()+ "\n";
                str = facilities.get(itr2).getElementsByClass("four-bags-feat").text();
                number1=0;
                if(str.indexOf(" ") > 0) {
		    		str=str.substring(0, str.indexOf(" "));
		    		number1=Integer.parseInt(str);
		    	}
                int number2=0;
		    	output += facilities.get(itr2).getElementsByClass("four-bags-feat-small").text()+ "\n";
		    	str = facilities.get(itr2).getElementsByClass("four-bags-feat-small").text();
		    	if(str.indexOf(" ") > 0) {
		    		str=str.substring(0, str.indexOf(" "));
		    		number2=Integer.parseInt(str);
		    	}
		    	freqCount.numberOfBags=number1+number2;
		    	output += facilities.get(itr2).getElementsByClass("mpg-seats-feat").text()+ "\n";
		    	output += "\n";
		    	itr2++;
		    	itr1++;
		    	dict.put(key, freqCount);
		    	key+=1;
		    	}
		    bw.write(output);
		    bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	public void listFileNameForFolder(File folder, Dictionary dict, int key) throws IOException {
		//Iterating the folder and parsing files present inside the folder 
		for (File fileName : folder.listFiles()) {
	        if (fileName.isDirectory()) {
	        	continue;
	        } else {
	        	convertFileToWebPage(folder,fileName.getName(), dict, key);
	            
	        }
	    }
	}
	
}
