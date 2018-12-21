package com.company;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;


//Класс для установки соединения с URL
class URLConnectClass {

    private static String URL_adress;

    public static void setURL(String URL){
        URL_adress=URL;
    }

    public static URLConnection Connect() throws IOException {
            URLConnection connection = new URL(URL_adress).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            return connection;
        }
    }

//Класс чтения в строку информации из URL
class StreamToString {

    private static URLConnection connect;

    public static void setConnect (URLConnection url) {connect=url;}

    static public String GoToStream() throws IOException {
        BufferedReader Reader  = new BufferedReader(new InputStreamReader(URLConnectClass.Connect().getInputStream(), Charset.forName("UTF-8")));
        StringBuilder Builder = new StringBuilder();
        String line;
        while ((line = Reader.readLine()) != null) {
            Builder.append(line);
        }
        return Builder.toString();
    }
}
//Класс записи распарсенной информации из Json'а в txt
class WriteToFile{

    private static String WritingText;

    public static void setWritingText (String Text){ WritingText = Text;}

    public static void Write() throws IOException {
        File FileTxt = new File("note.txt");
        FileWriter writer = new FileWriter(FileTxt);
        writer.write(WritingText);
        writer.flush();
        writer.close();
    }
}

public class Main {
    public static void main(String[] args) throws IOException {

        //Отправка URL в класс создания соединения
        URLConnectClass.setURL("http://main3.mysender.ru/persons.json");

        //Отправка соединения в класс чтения информации из URL
        StreamToString.setConnect(URLConnectClass.Connect());

        //Создание Объекта класса PersonList через библиотеку Gson, отправляя туда String строку прочитанной информации из URL
        PersonsList JsonPerson = new GsonBuilder().create().fromJson(StreamToString.GoToStream(), PersonsList.class);
        //Проверка, ну пуста ли коллекция объекта PersonList
        if (JsonPerson.GetPersons() != null) {
            String text = "";
            //Цикл перебора всех элементов коллекции объекта PersonList по конструктору класса Person (тут я не уверен, что это делается по констрктору, немного плохо понимаю этот момент) и запись в переменную String
                for (Persons p : JsonPerson.GetPersons()) {
                    text += p.GetName() + " " + p.GetAge() + " " + p.GetCity() + "\n";
                }
            //Отправка строки в класс записи в файл
            WriteToFile.setWritingText(text);
            //Выполнениие метода записи в файл.
            WriteToFile.Write();
        }
    }
}
