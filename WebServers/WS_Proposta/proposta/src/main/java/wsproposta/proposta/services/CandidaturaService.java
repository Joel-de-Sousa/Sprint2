package wsproposta.proposta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wsproposta.proposta.DTO.*;
import wsproposta.proposta.DTO.assemblers.CandidaturaDomainDTOAssembler;
import wsproposta.proposta.datamodel.REST.ProjetoRestDto;
import wsproposta.proposta.domain.entities.Candidatura;
import wsproposta.proposta.domain.factories.ICandidaturaFactory;
import wsproposta.proposta.repositories.CandidaturaRepository;
import wsproposta.proposta.repositories.ProjetoWebRepository;
import wsproposta.proposta.repositories.iRepositories.ICandidaturaRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidaturaService {
    @Autowired
    ICandidaturaFactory candidaturaFactory;
    @Autowired
    CandidaturaRepository candidaturaRepository;
    @Autowired
    CandidaturaDomainDTOAssembler candidaturaAssembler;

    @Autowired
    ProjetoWebRepository projetoWebRepository;

    public CandidaturaService() {
    }

    public CandidaturaDTO createAndSaveCandidatura(NewCandidaturaInfoDTO candidaturaInfoDTO) {

        Candidatura candidatura = candidaturaFactory.createCandidatura(candidaturaInfoDTO.getCodProposta(), candidaturaInfoDTO.getCodEstudante());

        Candidatura candidaturaSaved = candidaturaRepository.save(candidatura);

        CandidaturaDTO candidaturaDTO = candidaturaAssembler.toDTO(candidaturaSaved);

        return candidaturaDTO;
    }

    public List<CandidaturaDTO> findAllCandidaturas() {
        List<Candidatura> listCandidaturas = candidaturaRepository.findAll();

        List<CandidaturaDTO> listCandidaturasDTO = new ArrayList<>();
        for (Candidatura candidatura : listCandidaturas) {
            CandidaturaDTO candidaturaDTO = candidaturaAssembler.toDTO(candidatura);
            listCandidaturasDTO.add(candidaturaDTO);
        }
        return listCandidaturasDTO;
    }

    public List<CandidaturaDTOAllArgs> findAllCandidaturasAllArgs() {
        List<Candidatura> listCandidaturas = candidaturaRepository.findAll();

        List<CandidaturaDTOAllArgs> listCandidaturasDTO = new ArrayList<>();
        for (Candidatura candidatura : listCandidaturas) {
            CandidaturaDTOAllArgs candidaturaDTO = candidaturaAssembler.toDTOAllArgs(candidatura);
            listCandidaturasDTO.add(candidaturaDTO);
        }
        return listCandidaturasDTO;
    }

    public Optional<CandidaturaDTO> getCandidaturaByCodCandidatura (int codCandidatura) {

        Optional<Candidatura> opCandidatura = candidaturaRepository.findById(codCandidatura);

        if (opCandidatura.isPresent()) {
            Candidatura candidatura = opCandidatura.get();
            CandidaturaDTO candidaturaDTO = candidaturaAssembler.toDTO(candidatura);
            Optional<CandidaturaDTO> opCandidaturaDTO = Optional.of(candidaturaDTO);
            return opCandidaturaDTO;
        } else return Optional.empty();
    }

    public Optional<CandidaturaDTO> getCandidaturaByCodCandidaturaEstudante (int codEstudante) {

        Optional<Candidatura> opCandidatura = candidaturaRepository.findByIdEstudante(codEstudante);

        if (opCandidatura.isPresent()) {
            Candidatura candidatura = opCandidatura.get();
            CandidaturaDTO candidaturaDTO = candidaturaAssembler.toDTO(candidatura);
            Optional<CandidaturaDTO> opCandidaturaDTO = Optional.of(candidaturaDTO);
            return opCandidaturaDTO;
        } else return Optional.empty();
    }

    public Optional<CandidaturaDTOAllArgs> getCandidaturaByCodCandidaturaAllArgs (int codCandidatura) {

        Optional<Candidatura> opCandidatura = candidaturaRepository.findById(codCandidatura);

        if (opCandidatura.isPresent()) {
            Candidatura candidatura = opCandidatura.get();
            CandidaturaDTOAllArgs candidaturaDTO = candidaturaAssembler.toDTOAllArgs(candidatura);
            Optional<CandidaturaDTOAllArgs> opCandidaturaDTO = Optional.of(candidaturaDTO);
            return opCandidaturaDTO;
        } else return Optional.empty();
    }

    //MÉTODO UODATE ESTADO CANDIDATURA
    public CandidaturaDTO updateEstadoCandidatura (CandidaturaDTOParcial candidaturaUpdate, int codCandidatura) throws Exception {

        Optional<Candidatura> opCandidatura = candidaturaRepository.findById(codCandidatura);

        opCandidatura.get().setCodCandidatura(candidaturaUpdate.getCodCandidatura());
        opCandidatura.get().setEstadoEstudante(Candidatura.Estado.valueOf(candidaturaUpdate.getEstadoEstudante()));

        Candidatura candidaturaSaved = candidaturaRepository.save(opCandidatura.get());
        CandidaturaDTO candidaturaSavedDTO = candidaturaAssembler.toDTO(candidaturaSaved);

        ProjetoRestDto projetoParcial = new ProjetoRestDto(opCandidatura.get().getCodEstudante(), codCandidatura);
        boolean criado = projetoWebRepository.createAndSaveProjeto(projetoParcial);

        return candidaturaSavedDTO;
    }

}
