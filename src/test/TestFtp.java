package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;



public class TestFtp {

	@Test
	public void testFtp(){
			try {
				FTPClient ftp=new FTPClient();
				 ftp.connect("192.168.1.126", 21);
				 ftp.login("mayong", "mayong");
				 String ftpSbuPath="swift";
				 ftp.enterLocalPassiveMode();
				 String ftpFileName ="d:/test.txt";
				 String destFileName ="";
				 String ftpDestFileName="dest.txt";
				 InputStream  in = new BufferedInputStream(new FileInputStream(ftpFileName));
				
					boolean isSubDirectoryExsit = false;
					FTPFile[] dirs = ftp.listDirectories();
					if (dirs != null && dirs.length > 0) {
					    for (int i = 0; i < dirs.length; i++) {
					        if (dirs[i].getName().equals(ftpSbuPath)) {
					            isSubDirectoryExsit = true;
					        }
					        break;
					    }
					}
					dirs = null;
					if (!isSubDirectoryExsit && !ftpSbuPath.equals("")) {
				ftp.makeDirectory(ftpSbuPath);
				destFileName = ftpSbuPath + "/" + ftpDestFileName;
				}
				if (isSubDirectoryExsit && !ftpSbuPath.equals("")) {
					destFileName = ftpSbuPath + "/" + ftpDestFileName;
				}
				 
				if (!ftp.storeFile(destFileName, in)) {
					throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
				}
				
				new File(ftpFileName).renameTo(new File("D:/testx.txt"));
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	}
	
	
	public static void main(String[] args) {
		InputStream  in = null ;
		try {
			FTPClient ftp=new FTPClient();
			 ftp.connect("192.168.1.126", 21);
			 ftp.login("mayong", "mayong");
			 String ftpSbuPath="swift1";
			 ftp.enterLocalPassiveMode();
			 String ftpFileName ="d:/testx.txt";
			 String destFileName ="";
			 String ftpDestFileName="dest.txt";
			 in = new BufferedInputStream(new FileInputStream(ftpFileName));
			
				boolean isSubDirectoryExsit = false;
				FTPFile[] dirs = ftp.listDirectories();
				if (dirs != null && dirs.length > 0) {
				    for (int i = 0; i < dirs.length; i++) {
				        if (dirs[i].getName().equals(ftpSbuPath)) {
				            isSubDirectoryExsit = true;
				        }
				        break;
				    }
				}
				dirs = null;
				if (!isSubDirectoryExsit && !ftpSbuPath.equals("")) {
			ftp.makeDirectory(ftpSbuPath);
			destFileName = ftpSbuPath + "/" + ftpDestFileName;
			}
			if (isSubDirectoryExsit && !ftpSbuPath.equals("")) {
				destFileName = ftpSbuPath + "/" + ftpDestFileName;
			}
			 
			if (!ftp.storeFile(destFileName, in)) {
				throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
			}
			in.close();
			
			new File(ftpFileName).renameTo(new File("D:/test1.txt"));
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 

	}
}
