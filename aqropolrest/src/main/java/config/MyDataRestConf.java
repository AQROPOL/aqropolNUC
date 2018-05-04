package config;

import hello.data.Measure;
import hello.data.Sensor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * @author VinYarD
 * created : 01/05/2018, 15:39
 */

@Configuration
public class MyDataRestConf extends RepositoryRestConfigurerAdapter {

    /*
        Classe de configuration des repository rest.
     */

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        /*
            Ordonne a spring rest d'afficher l'ID des ressources Sensor et Measure.
         */
        config.exposeIdsFor(Sensor.class);
        config.exposeIdsFor(Measure.class);
    }
}
