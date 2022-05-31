package WSEdicao.controllers;

import WSEdicao.domain.entities.AnoLetivo;
import WSEdicao.dto.AnoLetivoDTO;
import WSEdicao.services.AnoLetivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping(path = "/anoletivo")
public class AnoLetivoController {

    @Autowired
    private AnoLetivoService service;

    public AnoLetivoController(AnoLetivoService service){this.service=service;}

    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Object> getByCode(@PathVariable int codAnoLetivo){

        Optional<AnoLetivo> opAnoLetivo = service.getAnoLetivoByCode(codAnoLetivo);

        if(opAnoLetivo.isPresent()){
            AnoLetivo anoLetivo = opAnoLetivo.get();
            return new ResponseEntity<>(anoLetivo, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*@GetMapping("")
    @ResponseBody
    public ResponseEntity<Object> findAll(){
        List<AnoLetivo> listAnoLetivo = service.getAllAnoLetivo();

        return new ResponseEntity<>(listAnoLetivo, HttpStatus.OK);
    }*/

    /*@PostMapping("")
    public ResponseEntity<Object> createAnoLetivo(@RequestBody AnoLetivoDTO info){

        AnoLetivo anoLetivo = service.createAndSaveAnoLetivo(info.getCodAnoLetivo(), info.getData());

        return new ResponseEntity<>(anoLetivo, HttpStatus.CREATED);

    }*/
}
