package org.lsi.producers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lsi.dto.Diabete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    @Value("${topic.name.producer}")
    private String topicName;



    private final KafkaTemplate<Integer, Diabete> template;

    String line = "";
    String splitBy = ",";
    Diabete diabete=new Diabete();

    public void send() throws FileNotFoundException {

        File file = new File("diabetes.txt");
        Scanner sc = new Scanner(file);

        int i=0;
        while (sc.hasNextLine()) {

            line = sc.nextLine();
            String[] d=line.split(splitBy);
            diabete.setPreg(d[0]);
            diabete.setPlas(d[1]);
            diabete.setPres(d[2]);
            diabete.setSkin(d[3]);
            diabete.setInsu(d[4]);
            diabete.setMass(d[5]);
            diabete.setPedi(d[6]);
            diabete.setAge(d[7]);
            diabete.setClasse(d[8]);

            log.info("Payload : {}", diabete);
            template.send(topicName, i,diabete);
            i++;
            try{
            Thread.sleep(100);}
            catch (Exception e){
                System.out.println("error");
            }


        }

    }
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://172.18.0.2:3000/");
        config.addAllowedOrigin("http://localhost:3000/");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
