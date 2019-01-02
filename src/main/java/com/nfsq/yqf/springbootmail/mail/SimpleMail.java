package com.nfsq.yqf.springbootmail.mail;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

/**
 * Created by qfyu
 * Date:2019/1/2
 * Time:11:21
 **/
@RestController
public class SimpleMail {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    JavaMailSender javaMailSender;

    @RequestMapping("/sendMail")
    public String sendMail(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("13588730966@163.com");
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setSubject("测试邮件");
        simpleMailMessage.setText("这是一封测试邮件，请勿回");
        simpleMailMessage.setTo("1049801106@qq.com");
        try{
            javaMailSender.send(simpleMailMessage);
            return "发送邮件成功";
        }catch (Exception e){
            return "发送邮件失败";
        }
    }
    @RequestMapping("/sendMail2")
    public String sendMail2() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom("13588730966@163.com");
        mimeMessageHelper.setSubject("静态资源邮件");
        mimeMessageHelper.setText("<html><body>带静态资源的邮件内容 <h>这是标签</h></body></html>",true);
        mimeMessageHelper.setTo("1049801106@qq.com");
        try{
            javaMailSender.send(mimeMessage);
            return "发送成功";
        }catch (Exception e){
            return "发送失败";
        }
    }

    @RequestMapping("/mailTemp")
    public String sendMail3() throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom("13588730966@163.com");
        mimeMessageHelper.setSubject("模板邮件");
        Map<String, Object> model = new HashMap<>();
        model.put("username","renxu");

        //修改 application.properties 文件中的读取路径
//            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//            configurer.setTemplateLoaderPath("classpath:templates");
        //读取 html 模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mail.html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setTo(new String[]{"953897316@qq.com","458299576@qq.com"});
        //Cc抄送，Bcc暗抄送（收件人不知道）
        mimeMessageHelper.setCc("1049801106@qq.com");
        try{
            javaMailSender.send(mimeMessage);
            return "发送成功";
        }catch (Exception e){
            return "发送失败";
        }
    }
}
