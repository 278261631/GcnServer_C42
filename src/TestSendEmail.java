import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class TestSendEmail {

	public static void main(String[] args) {
		Email email = new SimpleEmail();
		email.setHostName("smtp.163.com");
		email.setSmtpPort(465);
		//密码是pai(10)a
		email.setAuthenticator(new DefaultAuthenticator("c42email", "xxxxxxxxxxx")); //这里是163的客户端登录授权码 不是登录163邮箱的密码
		email.setSSLOnConnect(true);
		try {
			email.setFrom("c42email@163.com");
			email.setSubject("TestMail");
			email.setMsg("This is a test mail ... :-)");
			email.addTo("mars.maya.g@gmail.com");
			email.addTo("278261631@qq.com");
			email.send();
			
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
