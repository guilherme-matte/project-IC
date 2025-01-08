package control.invest.IC.services;

import control.invest.IC.models.IrpfModel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StringExtractService {


    private String extractValue(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String extractCpf(String Text) {
        return extractValue(Text, "\\s*(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})\\s+(.+)");
    }

    public String extractCnpj(String Text) {
        return extractValue(Text, "\\s*(\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2})\\s+(.+)");
    }

    public String extractValor(String Text) {
        String regex = "(\\d{1,3}(?:\\.\\d{3})*,\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Text);

        if (matcher.find()) {
            String valor = matcher.group();

            valor = valor.replace(".", "").replace(",", ".");


            try {
                return matcher.group();

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String[] splitLines(String text) {
        return text.split("\\n");
    }

    public IrpfModel extrairCabecalho(String text) {
        try {
            IrpfModel irpfModel = new IrpfModel();

            String[] lines = splitLines(text);

            Pattern patternCpf = Pattern.compile("\\s*(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})\\s+(.+)");
            Pattern patternCnpj = Pattern.compile("\\s*(\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2})\\s+(.+)");

            for (String line : lines) {
                Matcher matcherCpf = patternCpf.matcher(line);

                Matcher matcherCpnj = patternCnpj.matcher(line);

                if (matcherCpf.find()) {
                    irpfModel.setCpf(matcherCpf.group(1));
                    irpfModel.setNomePessoaFisica(matcherCpf.group(2));
                }

                if (matcherCpnj.find()) {
                    irpfModel.setFontePagadoraCnpj(matcherCpnj.group(1));
                    irpfModel.setFontePagadoraNomeEmpresa(matcherCpnj.group(2));
                }
            }
            return irpfModel;
        } catch (Exception e) {

        }
        return null;
    }

    public ArrayList<Double> extrairPagamentosValores(String text) {
        try {
            ArrayList<Double> retorno = new ArrayList<>();
            String[] lines = splitLines(text);
            Pattern patternValor = Pattern.compile("(\\d{1,3}(?:\\.\\d{3})*,\\d{2})");

            for (String line : lines) {
                Matcher matcherValor = patternValor.matcher(line);
                if (matcherValor.find()) {
                    retorno.add(Double.parseDouble(matcherValor.group().replace(".", "").replace(",", ".")));
                }
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<String> extrairPagamentos(String text) {
        try {
            ArrayList<String> retorno = new ArrayList<>();
            String[] lines = splitLines(text);

            Pattern patternCpf = Pattern.compile("\\s*(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})\\s+(.+)");
            Pattern patternCnpj = Pattern.compile("\\s*(\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2})\\s+(.+)");

            for (String line : lines) {
                Matcher matcherCpf = patternCpf.matcher(line);
                Matcher matcherCnpj = patternCnpj.matcher(line);
                if (matcherCpf.find()) {
                    retorno.add(matcherCpf.group(1) + " - " + matcherCpf.group(2));
                } else if (matcherCnpj.find()) {
                    retorno.add(matcherCnpj.group(1) + " - " + matcherCnpj.group(2));
                }
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IrpfModel extrairValores(String text) {
        try {
            IrpfModel irpfModel = new IrpfModel();

            String[] lines = splitLines(text);

            Pattern pattern = Pattern.compile("(\\d{1,3}(?:\\.\\d{3})*,\\d{2})");

            List<Double> extractedValues = new ArrayList<>();


            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    double value = Double.parseDouble(matcher.group().replace(".", "").replace(",", "."));

                    extractedValues.add(value);


                }
            }

            Field[] fields = IrpfModel.class.getDeclaredFields();
            int index = 0;

            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType().equals(double.class) && index < extractedValues.size()) {
                    field.set(irpfModel, extractedValues.get(index));
                    index++;
                }
            }

            return irpfModel;

        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
