/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.geocodificacao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import net.sf.json.JSONObject;

/**
 *
 * @author Aluno
 */
public class Geocodificacvao {

    private static final String URL_API = "https://nominatim.openstreetmap.org/search?q=";

    public static void main(String[] args) {

        Geocodificacvao obj = new Geocodificacvao();
        obj.lerCSV();
        ArrayList<endereco> enderecos = new ArrayList();
    }

    public class endereco {

        public Integer gid;
        public String nome;
        public String endereco;
        public String latitude;
        public String longitude;

        public String toString() {
            return "Local [id=" + gid
                    + ", nome=" + nome
                    + ", endereço=" + endereco
                    + ", latitude=" + latitude
                    + ", longitude=" + longitude + "]";
        }
    }

    public String buscaURL(String url) throws IOException {
        URL urlPagina = new URL(url);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlPagina.openStream()));

        String retornoJson;

        StringBuilder builder = new StringBuilder();
        while ((retornoJson = bufferedReader.readLine()) != null) {
            builder.append(retornoJson);
        }

        bufferedReader.close();

        return builder.toString();
    }

    public void lerCSV() {

        String arquivoCSV = "CSVData.csv";
        BufferedReader br = null;
        String linha = "";
        String csvDivisor = ";";
        try {

            Reader reader = new InputStreamReader(new FileInputStream(arquivoCSV), "UTF-8");
            br = new BufferedReader(reader);
            int i = 0;
            while ((linha = br.readLine()) != null) {
                endereco end = new endereco();

                String[] enderecoCSV = linha.split(csvDivisor);
                i++;
                end.gid = i;
                end.nome = enderecoCSV[enderecoCSV.length - 2].toString();
                end.endereco = enderecoCSV[enderecoCSV.length - 1].toString();
                System.out.println("-------------------------------------------");
                System.out.println("id: " + end.gid);
                System.out.println("nome: " + end.nome);
                System.out.println("endereço: " + end.endereco);
                // enderecos
                System.out.println("-------------------------------------------");

                try {
                    String URLConsulta = end.nome + "," + end.endereco;
                    String url = URLEncoder.encode(URLConsulta, "UTF-8");
                    System.out.println("URL: " + URL_API + url + "&format=json");

                    String retorno = buscaURL(URL_API + url + "&format=json");
                    String objetoRetornado = retorno.replace("[", "");
                    System.out.println("resposta" + retorno);
                    System.out.println("Retornado "+ objetoRetornado);
                    if (retorno.equals("[]")) {
                        System.out.println("Nao achou as cordenadas");
                    } else {
                        JSONObject objetoJson = JSONObject.fromObject(objetoRetornado);
                        String latitude = (String) objetoJson.get("lat");
                        String longitude = (String) objetoJson.get("lon");
                        System.out.println("Cordenadas: " + latitude + ": " + longitude);
                    }

                } catch (Exception e) {
                    System.out.println("Deu ruim");
                    e.printStackTrace();
                } finally {
                    System.out.println("Acabou!");
                }
                System.out.println("-------------------------------------------");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
