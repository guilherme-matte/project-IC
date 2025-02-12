package control.invest.IC.irfp.controller;

import control.invest.IC.irfp.models.ContribuinteModel;
import control.invest.IC.irfp.models.DependenteModel;
import control.invest.IC.irfp.repositories.ContribuinteRepository;
import control.invest.IC.irfp.repositories.DependenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CadastroController {
    @Autowired
    ContribuinteController contribuinteController;
    @Autowired
    ContribuinteRepository contribuinteRepository;
    @Autowired
    DependenteRepository dependenteRepository;

    @PostMapping("/cad/contribuinte")
    public ResponseEntity<String> cadastrarContribuinte(@RequestBody ContribuinteModel contribuinteModel) {
        try {

            ContribuinteModel result = contribuinteRepository.findByCpf(contribuinteModel.getCpf());

            if (result != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF já cadastrado");
            }

            contribuinteRepository.save(contribuinteModel);

            return ResponseEntity.ok("Cadastrado com sucesso");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar contribuinte" + e.getMessage());

        }
    }

    @PostMapping("/cad/dependente/{cpfContribuinte}")
    public ResponseEntity<String> cadastrarDependente(@RequestBody DependenteModel dependenteModel, @PathVariable String cpfContribuinte) {

        DependenteModel resultDependenteModel = dependenteRepository.findByCpf(dependenteModel.getCpf());

        if (resultDependenteModel != null) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF " + dependenteModel.getCpf() + " já cadastrado na base de dados");

        }

        ContribuinteModel resultContribuinteModel = contribuinteRepository.findByCpf(cpfContribuinte);

        if (resultContribuinteModel == null) { //verifica se o CPF fornecido pela URL existe no banco, caso não tenha, retorna um status 404

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível vincular dependente ao CPF do contribuinte (" + cpfContribuinte + ") - CPF NÃO ENCONTRADO");

        }

        if (cpfContribuinte.equals(dependenteModel.getCpf())) {//verifica se o cpf do contribuinte e do dependentes são iguais

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF do dependente não pode ser o mesmo que o do contribuinte");

        }

        dependenteModel.setContribuinte(resultContribuinteModel);

        dependenteRepository.save(dependenteModel);

        return ResponseEntity.ok("Cadastrado com sucesso");


    }

    @DeleteMapping("/delete/contribuinte/{cpfContribuinte}")
    public ResponseEntity<String> deleteContribuinte(@PathVariable String cpfContribuinte) {
        try {
            ContribuinteModel result = contribuinteRepository.findByCpf(cpfContribuinte);

            if (result == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível deletar contribuinte! CPF " + cpfContribuinte + " Não encontrado");

            }

            contribuinteRepository.deleteById(result.getId());

            return ResponseEntity.status(HttpStatus.OK).body("CPF " + cpfContribuinte + " deletado com sucesso!");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar contribuinte - " + e.getMessage());

        }
    }

    @DeleteMapping("/delete/dependente/{cpfDependente}")
    public ResponseEntity<String> deleteDependente(@PathVariable String cpfDependente) {
        try {
            DependenteModel result = dependenteRepository.findByCpf(cpfDependente);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrado o dependente CPF: " + cpfDependente);
            }

            dependenteRepository.deleteById(result.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Dependente " + result.getNome() + " | " + result.getCpf() + " deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar Dependente - " + e.getMessage());
        }
    }

}
