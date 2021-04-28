package com.dev.phim_pro.services;

import com.dev.phim_pro.exceptions.SpringPhimException;
import com.dev.phim_pro.models.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            messageHelper.setFrom("nhocrong98@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText("<div style=\"max-width: 700px; margin:auto; border: 10px solid #ddd; padding: 50px 20px; font-size: 110%;text-align:center;\">\n" +
                    "    <h2 style=\"text-align: center; text-transform: uppercase;color: teal;\">Chào mừng bạn đến với\n" +
                    "        Phim Pro.</h2>\n" +
                    "    <p>Chúc mừng bạn đã đăng ký thành công tài khoản tại Phim Pro.\n" +
                    "        Chỉ còn 1 bước cuối cùng để kích hoạt tài khoản, Click nút phía bên dưới để xác nhận tài khoản.\n" +
                    "    </p>\n" +
                    "\n" +
                    "    <a href=\"" + notificationEmail.getBody() + "\"\n" +
                    "       style=\"background: crimson; text-decoration: none; color: white; padding: 10px 20px; margin: 10px 0; display: inline-block;border-radius:0.5rem;font-weight: bold;box-shadow:0 3px 6px crimson;text-transform: uppercase\">Xác nhận</a>\n" +
                    "\n" +
                    "    <p>Nếu click vào nút phía trên mà không được thì bạn có thể đi đến địa chỉ trang web phía dưới:</p>\n" +
                    "\n" +
                    "    <span >" + notificationEmail.getBody() + "</span>\n" +
                    "</div>", true);

        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");

        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringPhimException("Exception occured when sending mail to" + notificationEmail.getRecipient(), e);
        }
    }
}
