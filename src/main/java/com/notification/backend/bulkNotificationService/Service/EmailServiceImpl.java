package com.notification.backend.bulkNotificationService.Service;

import com.notification.backend.bulkNotificationService.entity.Category;
import com.notification.backend.bulkNotificationService.entity.MailRecords;
import com.notification.backend.bulkNotificationService.model.EmailDTO;
import com.notification.backend.bulkNotificationService.repo.CategoryRepo;
import com.notification.backend.bulkNotificationService.repo.MailRecordRepo;
import com.notification.backend.bulkNotificationService.util.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService
{

    @Autowired
    MailRecordRepo mailRecordRepo;

    @Autowired
    CategoryRepo categoryRepo;



    @Override
    @Async
    public boolean send(EmailDTO emailDTO)
    {
        try
        {
            Category bycategoryName = categoryRepo.findBycategoryName(emailDTO.getCategory());
            List<MailRecords> allBycategory = mailRecordRepo.findAllBycategory(bycategoryName);
            List<String> mailList = allBycategory.stream().map(mailRecords -> mailRecords.getEmail()).collect(
                    Collectors.toList());
            EmailSender sender=new EmailSender();
            for(String mail:mailList)
            {
                    sender.send(mail,emailDTO.getSubject(),emailDTO.getMessage(),emailDTO.isHtmlContent());
            }
            return true;
        }
        catch (Exception e)
        {
            log.error("Failed  message :"+e.getLocalizedMessage());
            return false;
        }


    }
}
