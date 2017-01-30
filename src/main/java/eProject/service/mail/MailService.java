package eProject.service.mail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailService {

	private static Logger LOG = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	public String sendLoginDetailsEmail(Map<String, Object> map) {
		String from = map.get("from").toString();
		String to = map.get("to").toString();
		String subject = map.get("subject").toString();
		return sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"UserDetails.vm", map);
	}

	public String sendForgotPasswordEmail(Map<String, Object> map) {
		return this.sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"ForgotPassword.vm", map);
	}

	public String changePasswordEmail(Map<String, Object> map) {
		return this.sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"ChangePassword.vm", map);
	}

	public String leaveApplicationMail(Map<String, Object> map) {
		return this.sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"LeaveApplication.vm", map);
	}

	public String leaveApprovalMail(Map<String, Object> map) {
		return this.sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"LeaveApproval.vm", map);
	}

	public String leaveApplicationStatusMail(Map<String, Object> map) {
		return this.sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"LeaveApplicationStatus.vm", map);
	}

	public String sendAddOrganization(Map<String, Object> map) {
		return sendMail(map.get("from").toString(), map.get("to").toString(), map.get("subject").toString(),
				"AddOrganization.vm", map);
	}

	private String sendMail(final String from, final String to, final String emailSubject, final String velocityModel,
			final Map<String, Object> map) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
						MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
				messageHelper.setTo(to);
				messageHelper.setFrom(from);
				messageHelper.setSubject(emailSubject);

				// To display logo in email.
				String logoName = "hrms_logo.png";
				String uploadDirectory = File.separator + "webapps" + File.separator + "hrms" + File.separator
						+ "resources" + File.separator + "images" + File.separator;
				String uploadDirectoryBase = System.getProperty("catalina.base");
				String appPath = uploadDirectoryBase + uploadDirectory + logoName;
				File file = new File(appPath);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, velocityModel, "UTF-8", map);

				File file_Pdf = null;
				messageHelper.setText(new String(text.getBytes(), "UTF-8"), true);
				if (file.isFile()) {
					messageHelper.addInline("hrmslogo", file);
				}

			}
		};
		LOG.debug("Sending {} token to : {}", to);

		this.javaMailSenderImpl.send(preparator);

		return "success";
	}

}
