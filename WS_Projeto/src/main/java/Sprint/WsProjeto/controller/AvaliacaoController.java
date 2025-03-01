package Sprint.WsProjeto.controller;

import Sprint.WsProjeto.DTO.*;
import Sprint.WsProjeto.domain.entities.Avaliacao;
import Sprint.WsProjeto.service.AvaliacaoService;
import Sprint.WsProjeto.service.JuriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(path = "/avaliacoes")
public class AvaliacaoController {

        @Autowired
        private AvaliacaoService avaliacaoService;

        public AvaliacaoController(AvaliacaoService avaliacaoService) {
            this.avaliacaoService = avaliacaoService;
        }

        @GetMapping("/{codAvaliacao}")
        @ResponseBody
        //public ResponseEntity<Object> findAvalicaoByCode(@PathVariable int codAvaliacao) {
            public ResponseEntity<Object> findAvalicaoByCode(@PathVariable int codAvaliacao) throws Exception {

          AvaliacaoDTO oAvaliacao = avaliacaoService.findAvaliacaoByCode(codAvaliacao);


            if (oAvaliacao!=null) {
                return new ResponseEntity<>(oAvaliacao, HttpStatus.OK);
            } else
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    @GetMapping("/codProjeto/{codProjeto}")
    @ResponseBody
    public ResponseEntity<Object> findAvaliacoesByCodProjeto (@PathVariable int codProjeto){
        try {
            List<AvaliacaoDTO> listAvaliacoes = avaliacaoService.findAvaliacoesByCodProjeto(codProjeto);

            return new ResponseEntity<>(listAvaliacoes, HttpStatus.OK);}

        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


        @PostMapping("")
        @ResponseBody
        public ResponseEntity<Object> createAndSaveAvaliacao(@RequestBody NewAvaliacaoInfoDTO avaliacaoInfoDto) {

            try {
                Avaliacao avaliacaoDTO = avaliacaoService.createAndSaveAvaliacao(avaliacaoInfoDto.getCodprojeto(), avaliacaoInfoDto.getCodMA());
                return new ResponseEntity<>(avaliacaoDTO, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }


    @PatchMapping("/updatePresidente/{codAvaliacao}") //PRESIDENTE Preenche a nota e justificação de avaliação
    public ResponseEntity<Object> updateAvaliacao (@RequestBody AvaliacaoPartialDTO avaliacaoUpdate, @PathVariable int codAvaliacao) throws Exception {
        try {
            AvaliacaoDTO updatedAvaliacao = avaliacaoService.updateAvaliacao (avaliacaoUpdate);
            return new ResponseEntity<>(updatedAvaliacao, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/updatePresidenteRevisao/{codAvaliacao}") //PRESIDENTE Editar a nota e justificação de avaliação solicitada pelo RUC
    public ResponseEntity<Object> updateAvaliacaoRevisao (@RequestBody AvaliacaoPartialDTO avaliacaoUpdate, @PathVariable int codAvaliacao) throws Exception {
        try {
            AvaliacaoDTO updatedAvaliacao = avaliacaoService.updateAvaliacao (avaliacaoUpdate);
            return new ResponseEntity<>(updatedAvaliacao, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/updateRUC/{codAvaliacao}") //RUC Reve a avaliação, dando-a como concluída ou solicitando revisão
    public ResponseEntity<Object> updateEstadoAvaliacao (@RequestBody AvaliacaoPartialDTO avaliacaoUpdate, @PathVariable int codAvaliacao) throws Exception {
        try {
            AvaliacaoDTO updatedAvaliacao = avaliacaoService.updateEstadoAvaliacao (avaliacaoUpdate);
            return new ResponseEntity<>(updatedAvaliacao, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    }






