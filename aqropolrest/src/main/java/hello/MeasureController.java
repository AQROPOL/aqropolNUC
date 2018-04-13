package hello;

import hello.data.repository.MeasureRepository;
import hello.data.repository.NucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author VinYarD
 * created : 10/04/2018, 21:21
 */

@RestController
public class MeasureController {

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private NucRepository nucRepository;


    @RequestMapping(value = "/LastHashs", method = RequestMethod.POST)
    public ResponseEntity<?> postGreeting(@RequestBody HashMap<String, byte[]> hmTkNuc_HshMeasure) {

        nucRepository.findFirstByOrderByIdDesc().ifPresent(n ->
                measureRepository.findMeasureByHash(hmTkNuc_HshMeasure.get(n.getToken())).ifPresent(m ->
                        measureRepository.removeAllByIdBefore(m.getId())
                )
        );

        return ResponseEntity.accepted().build();
    }
}
