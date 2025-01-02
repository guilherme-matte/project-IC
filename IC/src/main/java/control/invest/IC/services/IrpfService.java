package control.invest.IC.services;

import control.invest.IC.models.IrpfModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
public class IrpfService {

    private String extension(String fileName) {
        int i = fileName.lastIndexOf('.');//pega o numero do indice do utimo ponto

        if (i > 0) {
            return fileName.substring(i + 1);//Pega a primeira palavra após o ponto e retona uma string
        } else {
            return null;
        }

    }

    public boolean extensionVerify(String filename) {
        //método criado para verificar se o arquivo recebio é um pdf ou uma imagem de diversas extensões.
        if (extension(filename).equalsIgnoreCase("pdf")) {
            return true;
        } else {
            return false;
        }
    }
}
