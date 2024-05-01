package pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AvisParser {
	
    public void parseAvisWebsite (File folder, String filename, Dictionary dict, int key) {
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
			String outFile=folder+"\\txt\\"+fileName+ ".txt";
			//Creating BufferedWriter object for writing the html file
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write(fileContents);
		    writer.close();
		    File input = new File(file);
		    Document doc = Jsoup.parse(input, "UTF-8", "");
		    BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		    //From the converted html file extracting the Elements which are required for further analysis
		    Elements car_option = doc.getElementsByClass("col-sm-5 col-xs-12 avilcardtl dualPayOption");
		    Elements price = doc.getElementsByClass("paybtndtl col-lg-12 col-xs-12");
		    Elements facilities = doc.getElementsByClass("available-car-fac hidden");
		    String output="";
		    int itr2=0;
		    int itr1=0;
		    //Storing the parsed content in FrequencyCount object along with file
		    //The one stored in FrequencyCount object will be used for counting the word frequency
		    for(int itr=0; itr<car_option.size()&&itr1<facilities.size()&&itr2<price.size(); itr++) {
		    	FrequencyCount freqCount=new FrequencyCount();
		    	freqCount.websiteName="https://www.avis.ca";
		    	output += "Pick up location: "+ fileName + "\n";
		    	freqCount.pickUpLocation = fileName;
			    output += car_option.get(itr).select("h3").text() + "\n";
			    freqCount.carType=car_option.get(itr).select("h3").text();
		    	output += car_option.get(itr).getElementsByClass("featurecartxt similar-car").text() + "\n";
		    	freqCount.carModel=car_option.get(itr).getElementsByClass("featurecartxt similar-car").text();
		    	output += facilities.get(itr1).getElementsByClass("four-door-feat").text() + "\n";
		    	output += facilities.get(itr1).getElementsByClass("four-seats-feat").text() + "\n";
		    	String str=facilities.get(itr1).getElementsByClass("four-seats-feat").text();
		    	if(str.indexOf(" ") > 0) {
			    	str=str.substring(0, str.indexOf(" "));
			    	freqCount.numberOfSeats=Integer.parseInt(str);
		    	}
		    	else {
		    		freqCount.numberOfSeats=0;
		    	}
		    	output += facilities.get(itr1).getElementsByClass("ac-seats-feat").text() + "\n";
		    	output += facilities.get(itr1).getElementsByClass("four-automatic-feat").text() + "\n";
		    	output += facilities.get(itr1).getElementsByClass("four-bags-feat").text() + "\n";
		    	str = facilities.get(itr1).getElementsByClass("four-bags-feat").text();
		    	int number1=0, number2=0;
		    	if(str.indexOf(" ") > 0) {
		    		str=str.substring(0, str.indexOf(" "));
		    		number1=Integer.parseInt(str);
		    	}
		    	output += facilities.get(itr1).getElementsByClass("four-bags-feat-small").text() + "\n";
		    	str = facilities.get(itr1).getElementsByClass("four-bags-feat-small").text();
		    	if(str.indexOf(" ") > 0) {
		    		str=str.substring(0, str.indexOf(" "));
		    		number2=Integer.parseInt(str);
		    	}
		    	freqCount.numberOfBags=number1+number2;
		    	itr1++;
		    	output += price.get(itr2).getElementsByClass("payamntp").text()+ "\n";
		    	str=price.get(itr2).getElementsByClass("payamntp").text();
		    	str=str.substring(str.indexOf("$")+1);
		    	freqCount.price=Float.parseFloat(str);
		    	itr2++;
		    	output += "\n";
		    	//Putting the integer key as key and freqCount object as value in dictionary 
		    	dict.put(key, freqCount);
		    	key++;
		    }
		    //writing the parsed details in the output file
		    bw.write(output);
		    bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
    //This method is iterating all the files present inside the folder and calling parse method for parsing the information which is required
	public void listFileNameForFolder(File folder, Dictionary dictObject, int key) throws IOException {
		//Iterating the folder and parsing files present inside the folder 
		for (File fileName : folder.listFiles()) {
	        if (fileName.isDirectory()) {
	        	//if folder is present ignoring
	        	continue;
	        } else {
	        	//for each file parsing
	        	parseAvisWebsite(folder,fileName.getName(), dictObject, key);
	            
	        }
	    }
	}
	
}
