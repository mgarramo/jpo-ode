package us.dot.its.jpo.ode;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.management.ManagementFactory;

import javax.annotation.PreDestroy;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import us.dot.its.jpo.ode.storage.StorageException;

@SpringBootApplication
@EnableConfigurationProperties(OdeProperties.class)
public class OdeSvcsApplication {

   private Logger logger = LoggerFactory.getLogger(this.getClass());
   private static final int DEFAULT_NO_THREADS=10;
   private static final String DEFAULT_SCHEMA="default";

   public static void main(String[] args) throws MalformedObjectNameException, InterruptedException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException{
      SpringApplication.run(OdeSvcsApplication.class, args);
      //Get the MBean server
      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      //register the MBean
      SystemConfig mBean = new SystemConfig(DEFAULT_NO_THREADS, DEFAULT_SCHEMA);
      ObjectName name = new ObjectName("com.journaldev.jmx:type=SystemConfig");
      mbs.registerMBean(mBean, name);
      do{
          Thread.sleep(3000);
          System.out.println("Thread Count="+mBean.getThreadCount()+":::Schema Name="+mBean.getSchemaName());
      }while(mBean.getThreadCount() !=0);
   }

   @Bean
   CommandLineRunner init(OdeProperties odeProperties) {
      return (args) -> {
         try {
            Files.createDirectory(Paths.get(odeProperties.getUploadLocation()));
         } catch (FileAlreadyExistsException fae) {
            logger.info("Upload directory already exisits");
         } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
         }
      };
   }

   @PreDestroy
   public void clanup() {
   }

}
