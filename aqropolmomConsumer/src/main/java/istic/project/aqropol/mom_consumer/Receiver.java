package istic.project.aqropol.mom_consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import istic.project.aqropol.mom_consumer.data.Measure;
import istic.project.aqropol.mom_consumer.data.Nuc;
import istic.project.aqropol.mom_consumer.data.Sensor;
import istic.project.aqropol.mom_consumer.data.StringByteArrayAdapter;
import istic.project.aqropol.mom_consumer.data.repository.MeasureRepository;
import istic.project.aqropol.mom_consumer.data.repository.NucRepository;
import istic.project.aqropol.mom_consumer.data.repository.SensorRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author VinYarD
 * created : 15/03/2018, 00:47
 */

public class Receiver {

    private static final Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class,
            new StringByteArrayAdapter()).create();

    private final MeasureRepository measureRepository;
    private final SensorRepository sensorRepository;
    private final NucRepository nucRepository;

    private final Nuc nuc;

    public Receiver(MeasureRepository measureRepository, SensorRepository sensorRepository, NucRepository nucRepository) {
        this.measureRepository = measureRepository;
        this.sensorRepository = sensorRepository;
        this.nucRepository = nucRepository;

        this.nuc = this.nucRepository.findByToken("token_de_mon_nuc_dev_001").get();

        if(this.nuc == null) {
            System.err.println("Cannot found nuc named token_de_mon_nuc_dev_001");
            System.exit(1);
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue.name}")
    public void receive(byte[] in) {
        receive(new String(in), 1);
    }

    public void receive(String in, int receiver) {
        /*
            Les donnees recues sont au format Json, on les deserialise donc grace a la librairie de google : gson
         */

        Measure m = gson.fromJson(in, Measure.class);
        if(m != null) {
            Sensor s = m.getSensor();
            Optional<Sensor> newSensor = sensorRepository.findByNameAndTypeAndUnity(s.getName(), s.getType(), s.getUnity());
            if(newSensor.isPresent()) {
                m.setSensor(newSensor.get());
            }
            m.setNuc(nuc);
            m.setTimestamp(new Timestamp(System.currentTimeMillis()));

            Measure mSaved = this.measureRepository.save(m); // on fait persister en base
            System.out.println("Saved : " + mSaved);

        } else {
            System.out.println("Echec JPA");
            System.out.println("instance " + receiver + " [x] Received '" + in + "'");
        }
    }
}