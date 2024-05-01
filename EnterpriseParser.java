package pkg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class EnterpriseParser {
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
		    Elements car_price=doc.getElementsByClass("pricing-details__price-total");
		    String output="";
		    int itr2=0;
		    Elements summary_containers = doc.getElementsByClass("vehicle-item_summary-container");
		   //Storing the parsed content in FrequencyCount object along with file
		   //The one stored in FrequencyCount object will be used for counting the word frequency
		    for(int itr=0; itr<summary_containers.size(); itr++) {
		    	 FrequencyCount freqCount=new FrequencyCount();
		    	 freqCount.websiteName="https://www.enterprise.ca";
		    	 output += summary_containers.get(itr).select("h2").text() + "\n";
		    	 freqCount.pickUpLocation = fileName;
		    	 output += "Pick up location: " + fileName + "\n";
		    	 freqCount.carType=summary_containers.get(itr).select("h2").text();
		    	 output += summary_containers.get(itr).getElementsByClass("vehicle-item__models").text() + "\n";
		    	 freqCount.carModel=summary_containers.get(itr).getElementsByClass("vehicle-item__models").text();
		    	 Elements unordered_list=summary_containers.get(itr).getElementsByClass("vehicle-item__attributes");
		    	 Elements list = unordered_list.select("li");
		    	 for(int itr1=0; itr1<list.size(); itr1++) {
		    		 output += list.get(itr1).text() + "\n";
		    		 String str=list.get(itr1).text();
		    		 str=str.substring(0,1);
		    		 if(itr1==1) {
		    			 freqCount.numberOfSeats=Integer.parseInt(str);
		    		 }
		    		 if(itr1==2) {
		    			 freqCount.numberOfBags=Integer.parseInt(str);
		    		 }
		    	 }
		    	 
		    	 String price=car_price.get(itr2).text();
		    	 price=price.substring(0, 5);
		    	 freqCount.price=Float.parseFloat(price);
		    	 //Putting the integer key as key and freqCount object as value in dictionary 
		    	 dict.put(key, freqCount);
		    	 key++;
		    	 output += price + "\n";;
		    	 output += "\n";
		    	 itr2++;
		    }
		    //writing the parsed details in the output file
		    bw.write(output);
		    bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	public void listFileNameForFolder(File folder, Dictionary dict, int key) throws IOException {
		//Iterating the folder and parsing files present inside the folder 
		for (File fileName : folder.listFiles()) {
	        if (fileName.isDirectory()) {
	        	continue;
	        } else {
	        	 convertFileToWebPage(folder,fileName.getName(),dict, key);
	            
	        }
	    }
		
	}
}
