package com.example.tasklist.service;

import com.example.tasklist.model.entity.Task;
import com.example.tasklist.model.entity.User;
import com.example.tasklist.service.impl.MailServiceImpl;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {
    @Mock
    Configuration configuration;

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    MailServiceImpl mailService;

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void sendMail() throws IOException {
        User user = User.builder()
                .id(10L)
                .username("username" + UUID.randomUUID())
                .name("name")
                .build();

        Task task = Task.builder()
                .id(20L)
                .title("title")
                .description("description")
                .owner(user)
                .build();

        Properties properties = new Properties();
        properties.setProperty("task.title", task.getTitle());
        properties.setProperty("task.description", task.getDescription());

        Configuration newConfiguration = new Configuration(Configuration.VERSION_2_3_30);
        newConfiguration.setTemplateLoader(
                new ClassTemplateLoader(MailServiceImplTest.class, "/templates")
        );
        newConfiguration.setDefaultEncoding("UTF-8");

        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        when(configuration.getTemplate("reminder.ftlh"))
                .thenReturn(newConfiguration.getTemplate("reminder.ftlh"));

        mailService.sendMail(user, properties);

        verify(javaMailSender, times(1)).send((MimeMessage) any());
        verify(configuration, times(1)).getTemplate("reminder.ftlh");
    }
}
