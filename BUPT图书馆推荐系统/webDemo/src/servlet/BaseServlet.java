package servlet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(urlPatterns="/qt/library")
public class BaseServlet extends HttpServlet{
	public void service(HttpServletRequest request,HttpServletResponse response) throws 
	ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");  
	    response.setCharacterEncoding("utf-8"); 
	    response.setContentType("application/application/x-www-form-urlencoded; charset=UTF-8");
	    
	    /*(Enumeration<String> headerNames = request.getHeaderNames();
	    while(headerNames.hasMoreElements())
	    {
	    	String hearderName = headerNames.nextElement();
	    	System.out.println(hearderName+"-->"+request.getHeader(hearderName));
	    }*/
	    
	    BufferedReader in=new BufferedReader(new InputStreamReader(request.getInputStream()));
	    StringBuilder sb = new StringBuilder();   
	           String line = null;  
	           while ((line = in.readLine()) != null) {   
	           sb.append(line);   
	           } 
	    String[] result = sb.toString().split("\\|");
	    if(result != null&&result.length > 1){
	     if(!result[1].equals("-----") && !result[1].equals("")){
	    	 System.out.println(readMsg(result[1]));
			     response.getWriter().println(readMsg(result[1]));
	       }
	     else
	     {
	    	 response.getWriter().println(readPopular());
	     }
	    }
	}
	public static String readPopular() throws IOException
	{
		String fileName = "/Users/wangyuhui/Desktop/item/most_popular.txt";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf-8"));
			String temp = "最受欢迎的十本书为：\n\n";
			String data = null;
			// 一次读入一行，直到读入null为文件结束
			while ((data = br.readLine()) != null) {
			temp+=data+"\n";
            }
			br.close();
			return temp;
	}
	public static String readMsg(String studentName)
	{   
		BufferedReader br;
		String fileName = "/Users/wangyuhui/Desktop/item/"+studentName+".txt";
		String temp1 = "已借阅的书：\n\n";
		String temp2 = "推荐的书：\n\n";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf-8"));
			String data = null;
			// 一次读入一行，直到读入null为文件结束
			while ((data = br.readLine()) != null) {
			if(data.charAt(0) == '#')
			{
				temp1+=data.substring(1)+"\n";
			}
			else
			{
				temp2+=data.substring(1)+"\n";
			}
            }
			br.close();
		} 
		//未找到该同学就把最热的十本书推荐给他
		catch (java.io.FileNotFoundException e0) {
			fileName = "/Users/wangyuhui/Desktop/item/most_popular.txt";
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf-8"));
				String data = null;
				temp1 = "该同学尚未借阅过图书，推荐最流行的十本书：\n\n";
				// 一次读入一行，直到读入null为文件结束
				while ((data = br.readLine()) != null) {
				temp1+=data+"\n";
	            }
				br.close();
				return temp1;
			} catch (FileNotFoundException e1) {
				return "";
			} catch (Exception e2) {
				return "Something Error.";
			}
		}
		catch (IOException e3) {
			return "Something Error.";
		}
		return temp1+"\n"+temp2;
		//return "该同学借阅过图书的图书：\n"+temp1+"为该同学推荐的图书：\n"+temp2; 
	}
}
