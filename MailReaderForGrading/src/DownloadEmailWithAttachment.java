import java.io.*;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import com.csvreader.CsvWriter;

public class DownloadEmailWithAttachment {
	
	static String outputFile = "grading.csv";
	static CsvWriter csvOutput;
	static File directory = new File("HW6");
	
	
	public static void writeRecord(String from, String time)
	{
		 File f = new File(directory,outputFile);
		boolean alreadyExists =  f.exists();
		
		try {
			// use FileWriter constructor that specifies open for appending
			if(csvOutput ==  null)
				csvOutput = new CsvWriter(new FileWriter(f), ',');
			
			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)
			{
				csvOutput.write("From");
				csvOutput.write("time");
				csvOutput.write("marks");				
				csvOutput.endRecord();
			}			
			csvOutput.write(from);
			csvOutput.write(time);
			csvOutput.write("0");
			csvOutput.endRecord();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String args[]) throws Exception {

		directory.mkdir();
		String host = "mail.cs.uoregon.edu";
		String username = "----"; 
		String password = "--------";

		Session session = Session.getInstance(new Properties(), null);
		Store store = session.getStore("imaps");
		store.connect(host, username, password);

		Folder folder = store.getFolder("------");

		System.out.println(folder.getFullName() + ": "
				+ folder.getMessageCount());
		folder.open(Folder.READ_ONLY);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		Message message[] = folder.getMessages();
		
		int i, n;
		for (i = 0; i < message.length; i++) {
			System.out.println(i + ": " + message[i].getFrom()[0] + "\t"
					+ message[i].getSubject() + " -- " +(message[i].getReceivedDate()));
			writeRecord(message[i].getFrom()[0].toString(), message[i].getReceivedDate().toString());
			String from  = parseEmail(message[i].getFrom()[0].toString());
			Object content = message[i].getContent();
			if (content instanceof Multipart) {
				//handleMultipart(from,(Multipart) content);
			} else {
				//handlePart(from,message[i]);
			}
		}
		folder.close(false);
		store.close();
		csvOutput.close();
		
	}
	
	public static String parseEmail(String from)
	{
		StringBuilder s = new StringBuilder();
		
		int start = from.indexOf('<');
		int end = from.indexOf('>');
		for(int i =start +1 ;  i<end ; i++)
		{
			s.append(from.charAt(i));
		}	
		
		return s.toString();
		
	}
	

	public static void handleMultipart(String from, Multipart multipart)
			throws MessagingException, IOException {
		for (int i = 0; i < multipart.getCount(); i++) {
			handlePart(from,multipart.getBodyPart(i));
		}
	}

	public static void handlePart(String from, Part part) throws MessagingException,
			IOException {
		String dposition = part.getDisposition();
		String cType = part.getContentType();		
		
		if (dposition == null) {
			//System.out.println("Null+ cType");
		} else if (dposition.equalsIgnoreCase(Part.ATTACHMENT)) {
			//System.out.println("Attachment part.getFileName()  " + " : "+ cType);
			saveFile(from,part.getFileName(), part.getInputStream());
		} else if (dposition.equalsIgnoreCase(Part.INLINE)) {
			//System.out.println("Inline part.getFileName() " + " : " + cType);			
			saveFile(from,part.getFileName(), part.getInputStream());
		} else {
			System.out.println("Other dposition");
		}
	}

	public static void saveFile(String from, String filename,  InputStream input)
			 {
		
		try{
		if (filename == null) {
			filename = File.createTempFile("MailAttacheFile", ".out").getName();
		}
		System.out.println("downloadingchment..." + from);
		
		File file = new File(directory,from+filename);
		for (int i = 0; file.exists(); i++) {
			file = new File(file.getName()+ i);
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		BufferedInputStream bis = new BufferedInputStream(input);
		int fByte;
		while ((fByte = bis.read()) != -1) {
			bos.write(fByte);
		}
		bos.flush();
		bos.close();
		bis.close();
		System.out.println("donechment...");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
